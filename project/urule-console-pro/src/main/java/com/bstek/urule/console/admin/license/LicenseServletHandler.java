package com.bstek.urule.console.admin.license;

import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.config.HomeLocator;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.security.URuleAuthAnonymous;
import com.bstek.urule.runtime.DynamicSpringConfigLoaderImpl;
import com.bstek.urule.runtime.LicenseValidationResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LicenseServletHandler extends ApiServletHandler {
   private static final int MAX_LICENSE_BYTES = 65536;
   private static final String LICENSE_FILE = "urule-license.txt";
   private static final String CSRF_HEADER = "X-URule-License-CSRF";
   private static final String CSRF_SESSION_ATTRIBUTE = LicenseServletHandler.class.getName() + ".csrf";
   private static final SecureRandom RANDOM = new SecureRandom();
   private static final Object INSTALL_LOCK = new Object();

   @Override
   public boolean useApiPrefix() {
      return false;
   }

   @Override
   protected boolean handleUnauthenticated(HttpServletRequest request, HttpServletResponse response) throws Exception {
      noStore(response);
      error(response, 401, "authentication_required", "Login is required for this endpoint.", false);
      return true;
   }

   @Override
   public String url() {
      return "/license";
   }

   public void status(HttpServletRequest request, HttpServletResponse response) throws Exception {
      noStore(response);
      if (!requireMethod(request, response, "GET") || !requireAdmin(request, response)) {
         return;
      }

      Map<String, Object> installed = installedStatus();
      Map<String, Object> issuer = new LinkedHashMap<String, Object>();
      issuer.put("enabled", LicenseIssuer.isEnabled());
      issuer.put("configured", LicenseIssuer.isConfigured());
      Map<String, Object> result = new LinkedHashMap<String, Object>();
      result.put("ok", true);
      result.put("active", activeStatus());
      result.put("installed", installed);
      result.put("issuer", issuer);
      result.put("restartRequired", requiresRestart(installed));
      result.put("csrfToken", csrfToken(request.getSession(true)));
      this.writeObjectToJson(response, result);
   }

   public void install(HttpServletRequest request, HttpServletResponse response) throws Exception {
      noStore(response);
      if (!requireMethod(request, response, "POST") || !requireAdmin(request, response) || !requireCsrf(request, response)) {
         return;
      }

      String contentType = request.getContentType();
      if (!isTextPlain(contentType)) {
         error(response, 415, "unsupported_media_type", "Content-Type must be text/plain.", false);
         return;
      }

      if (request.getContentLength() > MAX_LICENSE_BYTES) {
         error(response, 413, "license_too_large", "License exceeds 65536 bytes.", false);
         return;
      }

      byte[] bytes;
      try {
         bytes = readBounded(request.getInputStream(), MAX_LICENSE_BYTES);
      } catch (IllegalArgumentException illegalArgumentException) {
         error(response, 413, "license_too_large", "License exceeds 65536 bytes.", false);
         return;
      }

      installLicense(bytes, false, request, response);
   }

   public void issueInstall(HttpServletRequest request, HttpServletResponse response) throws Exception {
      noStore(response);
      if (!requireMethod(request, response, "POST") || !requireAdmin(request, response) || !requireCsrf(request, response)) {
         return;
      }
      if (!LicenseIssuer.isEnabled()) {
         error(response, 403, "issuer_disabled", "Local license issuance is disabled.", false);
         return;
      }
      if (!LicenseIssuer.isConfigured()) {
         error(response, 503, "issuer_not_configured", "Local license issuer key files are not configured.", false);
         return;
      }
      if (!isJson(request.getContentType())) {
         error(response, 415, "unsupported_media_type", "Content-Type must be application/json.", false);
         return;
      }
      if (request.getContentLength() > 4096) {
         error(response, 413, "issue_request_too_large", "Issue request exceeds 4096 bytes.", false);
         return;
      }

      byte[] requestBytes;
      try {
         requestBytes = readBounded(request.getInputStream(), 4096);
      } catch (IllegalArgumentException illegalArgumentException) {
         error(response, 413, "issue_request_too_large", "Issue request exceeds 4096 bytes.", false);
         return;
      }

      Map requestData;
      try {
         requestData = (Map)this.createObjectMapper().readValue(requestBytes, Map.class);
      } catch (Exception exception) {
         error(response, 400, "issue_request_invalid", "Issue request must be a JSON object.", false);
         return;
      }

      Object licenseeValue = requestData.get("licensee");
      Object limitValue = requestData.get("limit");
      if (!(licenseeValue instanceof String) || limitValue == null) {
         error(response, 400, "issue_request_invalid", "licensee and limit are required.", false);
         return;
      }

      String licensee = ((String)licenseeValue).trim();
      if (licensee.length() < 1 || licensee.length() > 256) {
         error(response, 400, "licensee_invalid", "licensee must contain 1 to 256 characters.", false);
         return;
      }

      long limit;
      try {
         limit = Long.parseLong(limitValue.toString());
      } catch (NumberFormatException numberFormatException) {
         error(response, 400, "limit_invalid", "limit must be -1 or a non-negative epoch millisecond value.", false);
         return;
      }
      if (limit < -1L) {
         error(response, 400, "limit_invalid", "limit must be -1 or a non-negative epoch millisecond value.", false);
         return;
      }
      if (limit >= 0L && limit <= System.currentTimeMillis()) {
         error(response, 422, "license_expired", "The requested license expiration must be in the future.", false);
         return;
      }

      byte[] licenseBytes;
      try {
         licenseBytes = LicenseIssuer.issue(licensee, limit);
      } catch (Exception exception2) {
         error(response, 500, "issuer_failed", "The local issuer could not generate a license.", false);
         return;
      }

      installLicense(licenseBytes, true, request, response);
   }

   @URuleAuthAnonymous
   public void issueDefault(HttpServletRequest request, HttpServletResponse response) throws Exception {
      noStore(response);
      if (!requireMethod(request, response, "GET")) {
         return;
      }
      if (!isLoopback(request.getRemoteAddr())) {
         error(response, 403, "loopback_required", "Default issuance is restricted to the local machine.", false);
         return;
      }
      if (!LicenseIssuer.isEnabled()) {
         error(response, 403, "issuer_disabled", "Local license issuance is disabled.", false);
         return;
      }
      if (!LicenseIssuer.isConfigured()) {
         error(response, 503, "issuer_not_configured", "Local license issuer key files are not configured or do not match.", false);
         return;
      }

      String licensee = System.getProperty("urule.license.issuer.default-licensee", "Portable License").trim();
      String limitText = System.getProperty("urule.license.issuer.default-limit", "-1").trim();
      boolean portable = Boolean.parseBoolean(System.getProperty("urule.license.issuer.default-portable", "true"));
      if (licensee.length() < 1 || licensee.length() > 256) {
         error(response, 503, "issuer_default_invalid", "Configured default licensee is invalid.", false);
         return;
      }

      long limit;
      try {
         limit = Long.parseLong(limitText);
      } catch (NumberFormatException numberFormatException) {
         error(response, 503, "issuer_default_invalid", "Configured default limit is invalid.", false);
         return;
      }
      if (limit < -1L || limit >= 0L && limit <= System.currentTimeMillis()) {
         error(response, 503, "issuer_default_invalid", "Configured default limit must be -1 or a future epoch millisecond value.", false);
         return;
      }

      byte[] licenseBytes;
      try {
         licenseBytes = LicenseIssuer.issue(licensee, limit, portable);
      } catch (Exception exception) {
         error(response, 500, "issuer_failed", "The local issuer could not generate a license.", false);
         return;
      }
      installLicense(licenseBytes, true, request, response);
   }

   private void installLicense(byte[] bytes, boolean issued, HttpServletRequest request, HttpServletResponse response) throws Exception {
      String license = new String(bytes, StandardCharsets.UTF_8);
      LicenseValidationResult validation = DynamicSpringConfigLoaderImpl.validateLicense(license);
      if (!validation.isValid()) {
         error(response, issued ? 500 : 422, issued ? "issuer_validation_failed" : "license_invalid", issued
            ? "The generated license did not pass configured trust validation."
            : "The vendor signature, format, or environment binding is invalid.", false);
         return;
      }
      if (validation.isExpired()) {
         error(response, 422, "license_expired", "The signed license is expired.", false);
         return;
      }

      String home = HomeLocator.getHomePath();
      if (home == null || home.trim().length() == 0) {
         error(response, 503, "license_home_unavailable", "URULE_HOME is not configured.", false);
         return;
      }

      Path temporary = null;
      synchronized (INSTALL_LOCK) {
         try {
            Path directory = Paths.get(home).toAbsolutePath().normalize();
            Files.createDirectories(directory);
            Path target = directory.resolve(LICENSE_FILE);
            temporary = Files.createTempFile(directory, ".urule-license-", ".tmp");
            writeAndSync(temporary, bytes);

            String persisted = new String(Files.readAllBytes(temporary), StandardCharsets.UTF_8);
            LicenseValidationResult persistedValidation = DynamicSpringConfigLoaderImpl.validateLicense(persisted);
            if (!persistedValidation.isValid() || persistedValidation.isExpired()) {
               error(response, 422, "license_revalidation_failed", "License failed validation after staging.", false);
               return;
            }

            try {
               Files.move(temporary, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
               temporary = null;
            } catch (AtomicMoveNotSupportedException atomicMoveNotSupportedException) {
               error(response, 500, "atomic_move_unsupported", "The filesystem does not support atomic license installation.", false);
               return;
            }
         } catch (IOException iOException) {
            error(response, 500, "license_install_failed", "License could not be persisted safely.", false);
            return;
         } finally {
            if (temporary != null) {
               try {
                  Files.deleteIfExists(temporary);
               } catch (IOException iOException2) {
               }
            }
         }
      }

      HttpSession session = request.getSession(false);
      if (session != null) {
         session.removeAttribute(CSRF_SESSION_ATTRIBUTE);
      }
      Map<String, Object> result = new LinkedHashMap<String, Object>();
      result.put("ok", true);
      result.put("issued", issued);
      result.put("installed", true);
      result.put("valid", true);
      result.put("expired", false);
      result.put("licensee", validation.getLicensee());
      result.put("limit", validation.getLimit());
      result.put("unlimited", validation.isUnlimited());
      result.put("portable", validation.isPortable());
      result.put("sha256", sha256(bytes));
      result.put("restartRequired", true);
      response.setStatus(201);
      this.writeObjectToJson(response, result);
   }

   public void reload(HttpServletRequest request, HttpServletResponse response) throws Exception {
      noStore(response);
      if (!requireMethod(request, response, "POST") || !requireAdmin(request, response) || !requireCsrf(request, response)) {
         return;
      }

      Map<String, Object> installed = installedStatus();
      if (!Boolean.TRUE.equals(installed.get("exists"))) {
         error(response, 404, "license_not_installed", "No installed license was found.", false);
         return;
      }
      if (!Boolean.TRUE.equals(installed.get("valid"))) {
         error(response, 422, "license_invalid", "The installed license is invalid.", false);
         return;
      }
      if (Boolean.TRUE.equals(installed.get("expired"))) {
         error(response, 422, "license_expired", "The installed license is expired.", false);
         return;
      }

      Map<String, Object> result = new LinkedHashMap<String, Object>();
      result.put("ok", false);
      result.put("code", "restart_required");
      result.put("message", "The installed license is valid. Restart the service to activate it safely.");
      result.put("installed", installed);
      result.put("restartRequired", true);
      response.setStatus(409);
      this.writeObjectToJson(response, result);
   }

   private Map<String, Object> activeStatus() {
      Map<String, Object> active = new LinkedHashMap<String, Object>();
      String licensee = DynamicSpringConfigLoaderImpl.getAuthInfo();
      boolean licensed = licensee != null && licensee.trim().length() > 0;
      long limit = DynamicSpringConfigLoaderImpl.getLimit();
      active.put("licensed", licensed);
      active.put("licensee", licensed ? licensee : null);
      active.put("limit", licensed ? Long.valueOf(limit) : null);
      active.put("unlimited", licensed && limit == -1L);
      active.put("expired", licensed && limit >= 0L && System.currentTimeMillis() > limit);
      active.put("portable", licensed && DynamicSpringConfigLoaderImpl.isLicensePortable());
      active.put("productVersion", DynamicSpringConfigLoaderImpl.getProductVersion());
      active.put("source", DynamicSpringConfigLoaderImpl.getLicenseSource());
      return active;
   }

   private Map<String, Object> installedStatus() {
      Map<String, Object> installed = new LinkedHashMap<String, Object>();
      String home = HomeLocator.getHomePath();
      if (home == null || home.trim().length() == 0) {
         installed.put("exists", false);
         installed.put("valid", false);
         installed.put("error", "license_home_unavailable");
         return installed;
      }

      Path path = Paths.get(home).toAbsolutePath().normalize().resolve(LICENSE_FILE);
      if (!Files.isRegularFile(path)) {
         installed.put("exists", false);
         installed.put("valid", false);
         return installed;
      }

      installed.put("exists", true);
      try {
         if (Files.size(path) > MAX_LICENSE_BYTES) {
            installed.put("valid", false);
            installed.put("error", "license_too_large");
            return installed;
         }

         byte[] bytes = Files.readAllBytes(path);
         LicenseValidationResult validation = DynamicSpringConfigLoaderImpl.validateLicense(new String(bytes, StandardCharsets.UTF_8));
         installed.put("valid", validation.isValid());
         installed.put("expired", validation.isExpired());
         installed.put("sha256", sha256(bytes));
         if (validation.isValid()) {
            installed.put("licensee", validation.getLicensee());
            installed.put("limit", validation.getLimit());
            installed.put("unlimited", validation.isUnlimited());
            installed.put("portable", validation.isPortable());
         } else {
            installed.put("error", "license_invalid");
         }
      } catch (IOException iOException) {
         installed.put("valid", false);
         installed.put("error", "license_unreadable");
      }
      return installed;
   }

   private boolean requiresRestart(Map<String, Object> installed) {
      if (!Boolean.TRUE.equals(installed.get("valid")) || Boolean.TRUE.equals(installed.get("expired"))) {
         return false;
      }

      String source = DynamicSpringConfigLoaderImpl.getLicenseSource();
      String activeLicensee = DynamicSpringConfigLoaderImpl.getAuthInfo();
      Object installedLicensee = installed.get("licensee");
      Object installedLimit = installed.get("limit");
      return !"home".equals(source)
         || activeLicensee == null
         || !activeLicensee.equals(installedLicensee)
         || !(installedLimit instanceof Number)
         || DynamicSpringConfigLoaderImpl.getLimit() != ((Number)installedLimit).longValue()
         || DynamicSpringConfigLoaderImpl.isLicensePortable() != Boolean.TRUE.equals(installed.get("portable"));
   }

   private boolean isTextPlain(String contentType) {
      return isMediaType(contentType, "text/plain");
   }

   private boolean isJson(String contentType) {
      return isMediaType(contentType, "application/json");
   }

   private boolean isMediaType(String contentType, String expected) {
      if (contentType == null) {
         return false;
      }

      String[] parts = contentType.split(";", -1);
      if (!expected.equalsIgnoreCase(parts[0].trim())) {
         return false;
      }

      for (int index = 1; index < parts.length; index++) {
         String parameter = parts[index].trim();
         int separator = parameter.indexOf('=');
         if (separator < 1 || separator == parameter.length() - 1) {
            return false;
         }
      }
      return true;
   }

   private boolean isLoopback(String address) {
      try {
         return address != null && InetAddress.getByName(address).isLoopbackAddress();
      } catch (Exception exception) {
         return false;
      }
   }

   private boolean requireMethod(HttpServletRequest request, HttpServletResponse response, String expected) throws Exception {
      if (expected.equals(request.getMethod())) {
         return true;
      }
      response.setHeader("Allow", expected);
      error(response, 405, "method_not_allowed", "Use " + expected + " for this endpoint.", false);
      return false;
   }

   private boolean requireAdmin(HttpServletRequest request, HttpServletResponse response) throws Exception {
      String username = SecurityUtils.getLoginUsername(request);
      String configured = System.getProperty("urule.license.admins", "admin");
      if (username != null) {
         for (String candidate : configured.split(",")) {
            if (username.equals(candidate.trim())) {
               return true;
            }
         }
      }
      error(response, 403, "license_admin_required", "This endpoint requires a configured license administrator.", false);
      return false;
   }

   private boolean requireCsrf(HttpServletRequest request, HttpServletResponse response) throws Exception {
      HttpSession session = request.getSession(false);
      Object expected = session == null ? null : session.getAttribute(CSRF_SESSION_ATTRIBUTE);
      String supplied = request.getHeader(CSRF_HEADER);
      if (expected instanceof String && supplied != null && MessageDigest.isEqual(((String)expected).getBytes(StandardCharsets.UTF_8), supplied.getBytes(StandardCharsets.UTF_8))) {
         return true;
      }
      error(response, 403, "csrf_invalid", "Fetch status and send its token in " + CSRF_HEADER + ".", false);
      return false;
   }

   private String csrfToken(HttpSession session) {
      Object existing = session.getAttribute(CSRF_SESSION_ATTRIBUTE);
      if (existing instanceof String) {
         return (String)existing;
      }
      byte[] bytes = new byte[32];
      RANDOM.nextBytes(bytes);
      String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
      session.setAttribute(CSRF_SESSION_ATTRIBUTE, token);
      return token;
   }

   private byte[] readBounded(InputStream input, int maximumBytes) throws IOException {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      byte[] buffer = new byte[4096];
      int total = 0;
      int read;
      while ((read = input.read(buffer)) != -1) {
         total += read;
         if (total > maximumBytes) {
            throw new IllegalArgumentException("request_too_large");
         }
         output.write(buffer, 0, read);
      }
      return output.toByteArray();
   }

   private void writeAndSync(Path path, byte[] bytes) throws IOException {
      FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
      try {
         ByteBuffer buffer = ByteBuffer.wrap(bytes);
         while (buffer.hasRemaining()) {
            channel.write(buffer);
         }
         channel.force(true);
      } finally {
         channel.close();
      }
   }

   private String sha256(byte[] bytes) {
      try {
         byte[] digest = MessageDigest.getInstance("SHA-256").digest(bytes);
         StringBuilder result = new StringBuilder(digest.length * 2);
         for (byte value : digest) {
            result.append(String.format("%02x", value & 255));
         }
         return result.toString();
      } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
         throw new IllegalStateException(noSuchAlgorithmException);
      }
   }

   private void error(HttpServletResponse response, int status, String code, String message, boolean restartRequired) throws Exception {
      Map<String, Object> result = new LinkedHashMap<String, Object>();
      result.put("ok", false);
      result.put("code", code);
      result.put("message", message);
      result.put("restartRequired", restartRequired);
      response.setStatus(status);
      this.writeObjectToJson(response, result);
   }

   private void noStore(HttpServletResponse response) {
      response.setHeader("Cache-Control", "no-store");
      response.setHeader("Pragma", "no-cache");
   }
}
