package com.bstek.urule.console.admin.license;

import com.bstek.urule.SystemUtils;
import com.bstek.urule.runtime.DynamicSpringConfigLoaderImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

final class LicenseIssuer {
   private static final int MAX_PRIVATE_KEY_BYTES = 16384;
   private static final SecureRandom RANDOM = new SecureRandom();

   private LicenseIssuer() {
   }

   static boolean isEnabled() {
      return Boolean.parseBoolean(System.getProperty("urule.license.issuer.enabled", "false"));
   }

   static boolean isConfigured() {
      try {
         RSAPrivateCrtKey privateKey = (RSAPrivateCrtKey)readPrivateKey();
         RSAPublicKey publicKey = readPublicKey();
         return privateKey.getModulus().equals(publicKey.getModulus())
            && privateKey.getPublicExponent().equals(publicKey.getPublicExponent());
      } catch (Exception var2) {
         return false;
      }
   }

   static byte[] issue(String licensee, long limit) throws Exception {
      return issue(licensee, limit, false);
   }

   static byte[] issue(String licensee, long limit, boolean portable) throws Exception {
      if (!isEnabled() || !isConfigured()) {
         throw new IllegalStateException("License issuer is not enabled and configured.");
      }

      PrivateKey privateKey = readPrivateKey();
      ObjectMapper mapper = JsonMapper.builder().build();
      Map<String, Object> payload = new LinkedHashMap<String, Object>();
      payload.put("osName", SystemUtils.OS_NAME);
      payload.put("osVersion", SystemUtils.OS_VERSION);
      payload.put("javaVender", SystemUtils.JAVA_VENDOR);
      payload.put("javaVersion", SystemUtils.JAVA_VERSION);
      payload.put("to", licensee);
      payload.put("limit", Long.toString(limit));
      if (portable) {
         payload.put("binding", "portable");
         payload.put("licenseId", UUID.randomUUID().toString());
         payload.put("issuedAt", Long.toString(System.currentTimeMillis()));
         payload.put("productVersion", DynamicSpringConfigLoaderImpl.getProductVersion());
      }

      byte[] aesKey = new byte[16];
      RANDOM.nextBytes(aesKey);
      try {
         Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
         aes.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(aesKey, "AES"));
         byte[] encryptedPayload = aes.doFinal(mapper.writeValueAsBytes(payload));

         Cipher rsa = Cipher.getInstance("RSA");
         rsa.init(Cipher.ENCRYPT_MODE, privateKey);
         byte[] encryptedKey = rsa.doFinal(aesKey);

         Map<String, String> envelope = new LinkedHashMap<String, String>();
         envelope.put("key", Base64.getEncoder().encodeToString(encryptedKey));
         envelope.put("data", Base64.getEncoder().encodeToString(encryptedPayload));
         return Base64.getEncoder().encode(mapper.writeValueAsBytes(envelope));
      } finally {
         Arrays.fill(aesKey, (byte)0);
      }
   }

   private static PrivateKey readPrivateKey() throws Exception {
      Path path = configuredPath("urule.license.issuer.private-key");
      long size = Files.size(path);
      if (size < 1L || size > MAX_PRIVATE_KEY_BYTES) {
         throw new IllegalStateException("Private key size is invalid.");
      }

      byte[] encodedText = Files.readAllBytes(path);
      byte[] encodedKey = null;
      try {
         String base64 = new String(encodedText, StandardCharsets.UTF_8).trim();
         encodedKey = Base64.getDecoder().decode(base64);
         PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
         if (!(privateKey instanceof RSAPrivateCrtKey)) {
            throw new IllegalArgumentException("Configured license private key is not an RSA CRT key.");
         }
         RSAPrivateCrtKey rsaKey = (RSAPrivateCrtKey)privateKey;
         if (rsaKey.getModulus().bitLength() != 1024 || !BigInteger.valueOf(65537L).equals(rsaKey.getPublicExponent())) {
            throw new IllegalArgumentException("Configured license private key parameters do not match the original trust strength.");
         }
         return privateKey;
      } finally {
         Arrays.fill(encodedText, (byte)0);
         if (encodedKey != null) {
            Arrays.fill(encodedKey, (byte)0);
         }
      }
   }

   private static RSAPublicKey readPublicKey() throws Exception {
      Path path = configuredPath("urule.license.issuer.public-key");
      long size = Files.size(path);
      if (size < 1L || size > 4096L) {
         throw new IllegalStateException("Public key size is invalid.");
      }

      byte[] encodedText = Files.readAllBytes(path);
      byte[] encodedKey = null;
      try {
         String base64 = new String(encodedText, StandardCharsets.UTF_8).trim();
         encodedKey = Base64.getDecoder().decode(base64);
         PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(encodedKey));
         if (!(publicKey instanceof RSAPublicKey)) {
            throw new IllegalArgumentException("Configured license public key is not RSA.");
         }
         RSAPublicKey rsaKey = (RSAPublicKey)publicKey;
         if (rsaKey.getModulus().bitLength() != 1024 || !BigInteger.valueOf(65537L).equals(rsaKey.getPublicExponent())) {
            throw new IllegalArgumentException("Configured license public key parameters do not match the original trust strength.");
         }
         return rsaKey;
      } finally {
         Arrays.fill(encodedText, (byte)0);
         if (encodedKey != null) {
            Arrays.fill(encodedKey, (byte)0);
         }
      }
   }

   private static Path configuredPath(String property) {
      String configured = System.getProperty(property);
      if (configured == null || configured.trim().length() == 0) {
         throw new IllegalStateException(property + " is not configured.");
      }

      Path path = Paths.get(configured).normalize();
      if (!path.isAbsolute() || !Files.isRegularFile(path)) {
         throw new IllegalStateException(property + " must reference an absolute regular file.");
      }
      return path;
   }
}
