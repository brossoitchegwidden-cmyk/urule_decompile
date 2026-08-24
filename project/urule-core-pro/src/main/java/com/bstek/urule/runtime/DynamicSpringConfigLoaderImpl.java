package com.bstek.urule.runtime;

import com.bstek.urule.ClassUtils;
import com.bstek.urule.SystemUtils;
import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

/** Loads hot-deployed JARs and initializes the runtime license state. */
public class DynamicSpringConfigLoaderImpl implements DynamicSpringConfigLoader, ApplicationContextAware {
   private Logger logger = Logger.getGlobal();
   private static final long STARTUP_TIMESTAMP = new Date().getTime();
   private static long trialExpired;
   private String licenseDecryptionKey;
   private static String limitDate;
   private static long limit;
   private static String licenseKey;
   private static String authInfo;
   private static String productVersion;
   private String dynamicJarsPath;
   private String dynamicJarsRootPath;
   private String dynamicJarsStoreDirectPath;
   private String dynamicJarsIdDigest;
   private ApplicationContext applicationContext;
   private ClassLoader parentClassLoader;
   private URLClassLoader dynamicJarClassLoader;
   private DynamicJarCreator dynamicJarCreator;
   private BuiltInActionLibraryBuilder builtInActionLibraryBuilder;
   private RemoteDynamicJarsBuilder remoteDynamicJarsBuilder;

   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      this.parentClassLoader = applicationContext.getClassLoader();
      this.applicationContext = applicationContext;
      Collection dynamicJarCreators = applicationContext.getBeansOfType(DynamicJarCreator.class).values();
      if (dynamicJarCreators.size() > 0) {
         this.dynamicJarCreator = (DynamicJarCreator)dynamicJarCreators.iterator().next();
      }

      try {
         if (Utils.getApplicationContext() == null) {
            Utils.resetApplicationContext(applicationContext);
         }

         this.initialize();
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   private void initialize() throws Exception {
      String dynamicJarsStoreDirectPath = this.buildDynamicJarsStoreDirectPath();
      File file = new File(this.dynamicJarsRootPath);
      this.deleteRecursively(file, true);
      boolean flag = false;
      if (StringUtils.isNotBlank(this.remoteDynamicJarsBuilder.getResporityServerUrl())) {
         try {
            flag = this.remoteDynamicJarsBuilder.requestRemoteJars(dynamicJarsStoreDirectPath);
            this.remoteDynamicJarsBuilder.startIntervalLoadRemoteJars(this);
            System.out.println("Load hot deployed jars successfully from server : " + this.remoteDynamicJarsBuilder.getResporityServerUrl());
         } catch (Exception exception) {
            java.util.logging.Logger.getLogger(DynamicSpringConfigLoaderImpl.class.getName()).log(java.util.logging.Level.SEVERE, exception.getMessage(), exception);
            System.out.println("Load hot deployed jars was fail from server : " + this.remoteDynamicJarsBuilder.getResporityServerUrl());
         }
      } else if (this.dynamicJarCreator != null) {
         flag = this.dynamicJarCreator.doCreate(dynamicJarsStoreDirectPath);
      }

      if (flag) {
         this.loadDynamicJars(dynamicJarsStoreDirectPath);
      }

      DynamicSpringConfigLoaderImpl.productVersion = Secret.INSTANCE.getProductVersion();
      this.initializeLicenseKey();
      this.loadLicense();
   }

   private void initializeLicenseKey() {
      String text = Secret.INSTANCE.generateAesKey();
      String machineFingerprint = this.buildMachineFingerprint();
      this.licenseDecryptionKey = Secret.INSTANCE.md5Hex(machineFingerprint);
      String text2 = Secret.INSTANCE.encryptWithRsaPublicKey(text);
      String text3 = Secret.INSTANCE.encryptWithAes(text, machineFingerprint);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("{");
      stringBuilder.append("\"" + Secret.INSTANCE.decodeEncodedText("a2V5", true) + "\":\"" + text2 + "\",");
      stringBuilder.append("\"" + Secret.INSTANCE.decodeEncodedText("ZGF0YQ==", true) + "\":\"" + text3 + "\"");
      stringBuilder.append("}");

      try {
         DynamicSpringConfigLoaderImpl.licenseKey = Base64.getEncoder().encodeToString(stringBuilder.toString().getBytes("UTF-8"));
         Secret.INSTANCE.printLicenseKey(DynamicSpringConfigLoaderImpl.licenseKey);
      } catch (UnsupportedEncodingException unsupportedEncodingException) {
         throw new RuleException(unsupportedEncodingException);
      }
   }

   private String buildMachineFingerprint() {
      String text = SystemUtils.OS_NAME;
      String text2 = SystemUtils.OS_VERSION;
      String text3 = SystemUtils.JAVA_VENDOR;
      String text4 = SystemUtils.JAVA_VERSION;
      return Secret.INSTANCE.buildMachineFingerprintJson(text, text2, text3, text4);
   }

   private static String licenseSource;
   private static boolean licensePortable;

   private void loadLicense() throws Exception {
      boolean flag = false;
      ObjectMapper objectMapper = JsonMapper.builder().build();
      String text = Secret.INSTANCE.decodeEncodedText("dXJ1bGUtbGljZW5zZQ==", true);
      String text2 = text + Secret.INSTANCE.decodeEncodedText("LnR4dA==", true);
      DynamicSpringConfigLoaderImpl.licenseSource = null;

      String text3 = resolveLicenseHome();
      if (StringUtils.isNotBlank(text3)) {
         File file = new File(text3, text2);
         if (file.isFile()) {
            String file2 = this.readFile(file);
            if (file2 != null && this.activateLicense(file2, objectMapper, text2)) {
               flag = true;
               DynamicSpringConfigLoaderImpl.licenseSource = "home";
            }
         }
      }

      if (!flag) {
         for (int index = 0; index <= 10; index++) {
            String text4 = index == 0 ? text2 : text + index + Secret.INSTANCE.decodeEncodedText("LnR4dA==", true);
            String classpathResource = this.readClasspathResource(text4);
            if (classpathResource != null && this.activateLicense(classpathResource, objectMapper, text4)) {
               flag = true;
               DynamicSpringConfigLoaderImpl.licenseSource = "classpath:" + text4;
               break;
            }
         }
      }

      if (!flag) {
         Secret.INSTANCE.printTrialBanner();
      }
   }

   private static String resolveLicenseHome() {
      String licenseHome = null;
      InputStream inputStream = null;

      try {
         try {
            inputStream = new FileInputStream("urule-init.properties");
         } catch (FileNotFoundException fileNotFoundException) {
            inputStream = DynamicSpringConfigLoaderImpl.class.getClassLoader().getResourceAsStream("urule-init.properties");
         }

         if (inputStream != null) {
            java.util.Properties properties = new java.util.Properties();
            properties.load(inputStream);
            licenseHome = properties.getProperty("urule.home");
         }
      } catch (IOException iOException) {
      } finally {
         IOUtils.closeQuietly(inputStream);
      }

      if (StringUtils.isBlank(licenseHome)) {
         licenseHome = System.getProperty("urule.home");
      }

      if (StringUtils.isBlank(licenseHome)) {
         licenseHome = System.getProperty("uruleHome");
      }

      if (StringUtils.isBlank(licenseHome)) {
         licenseHome = System.getenv("URULE_HOME");
      }

      return licenseHome;
   }

   private String readFile(File file) {
      try {
         FileInputStream fileInputStream = new FileInputStream(file);

         try {
            return IOUtils.toString(fileInputStream, "UTF-8");
         } finally {
            fileInputStream.close();
         }
      } catch (Exception exception) {
         return null;
      }
   }

   private String readClasspathResource(String text) {
      String classpathResource = null;

      try {
         Resource resource = this.applicationContext.getResource("classpath:" + text);
         if (resource != null) {
            InputStream inputStream = resource.getInputStream();
            classpathResource = IOUtils.toString(inputStream);
            inputStream.close();
         }
      } catch (Exception exception) {
      }

      return classpathResource;
   }

   private boolean activateLicense(String text, ObjectMapper objectMapper, String text2) throws Exception {
      HashMap valuesByKey = this.parseLicenseSafely(text, objectMapper, text2);
      if (valuesByKey == null) {
         return false;
      }

      Object objectValue = valuesByKey.get(Secret.INSTANCE.decodeEncodedText("dG8=", true));
      Object objectValue2 = valuesByKey.get(Secret.INSTANCE.decodeEncodedText("bGltaXQ=", true));
      if (!(objectValue instanceof String) || StringUtils.isBlank((String)objectValue) || objectValue2 == null) {
         return false;
      }

      long parsedLimit = Long.parseLong(objectValue2.toString());
      if (parsedLimit < -1L) {
         return false;
      }

      KnowledgeSessionFactory.resetReg(true);
      DynamicSpringConfigLoaderImpl.authInfo = (String)objectValue;
      DynamicSpringConfigLoaderImpl.licensePortable = "portable".equals(valuesByKey.get("binding"));
      KnowledgeSessionFactory.resetLimit(valuesByKey);
      DynamicSpringConfigLoaderImpl.limit = parsedLimit;
      if (DynamicSpringConfigLoaderImpl.limit == -1L) {
         DynamicSpringConfigLoaderImpl.limitDate = Secret.INSTANCE.decodeEncodedText("VW5saW1pdGVk", true);
      } else {
         Calendar calendar = Calendar.getInstance();
         calendar.setTimeInMillis(DynamicSpringConfigLoaderImpl.limit);
         Date time = calendar.getTime();
         DynamicSpringConfigLoaderImpl.limitDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
      }

      Secret.INSTANCE.printLicensedBanner(DynamicSpringConfigLoaderImpl.authInfo, DynamicSpringConfigLoaderImpl.limitDate);
      String text3 = "TGljZW5zZSBmaWxlWw==";
      String text4 = "XSBpcyB2YWxpZA==";
      System.out.println(Secret.INSTANCE.decodeEncodedText(text3, true) + text2 + Secret.INSTANCE.decodeEncodedText(text4, true));
      return true;
   }

   private HashMap<?, ?> parseLicenseSafely(String text, ObjectMapper objectMapper, String text2) {
      try {
         return decodeLicense(text, objectMapper, this.licenseDecryptionKey);
      } catch (Exception exception) {
         System.err.println("License file[" + text2 + "] is broken.");
         return null;
      }
   }

   private static HashMap<?, ?> decodeLicense(String text, ObjectMapper objectMapper, String text2) throws Exception {
      Exception exception3 = null;
      try {
         HashMap<?, ?> valuesByKey = decryptLicensePayload(text, objectMapper, text2, null);
         if (valuesByKey != null) {
            return valuesByKey;
         }
      } catch (Exception exception) {
         exception3 = exception;
      }

      String text3 = supplementalPublicKey();
      if (StringUtils.isBlank(text3)) {
         if (exception3 != null) {
            throw exception3;
         }
         return null;
      }

      try {
         return decryptLicensePayload(text, objectMapper, text2, text3);
      } catch (Exception exception2) {
         throw exception2;
      }
   }

   private static HashMap<?, ?> decryptLicensePayload(String text, ObjectMapper objectMapper, String text2, String text3) throws Exception {
      byte[] bytes = Base64.getDecoder().decode(text);
      HashMap valuesByKey = (HashMap)objectMapper.readValue(bytes, HashMap.class);
      byte[] bytes2 = Base64.getDecoder().decode((String)valuesByKey.get(Secret.INSTANCE.decodeEncodedText("a2V5", true)));
      byte[] bytes3 = text3 == null ? Secret.INSTANCE.decryptWithRsaPublicKey(bytes2) : Secret.INSTANCE.decryptWithRsaPublicKey(bytes2, text3);
      byte[] bytes4 = Base64.getDecoder().decode((String)valuesByKey.get(Secret.INSTANCE.decodeEncodedText("ZGF0YQ==", true)));
      byte[] bytes5 = Secret.INSTANCE.decryptWithAes(bytes3, bytes4);
      HashMap valuesByKey2 = (HashMap)objectMapper.readValue(bytes5, HashMap.class);
      if ("portable".equals(valuesByKey2.get("binding"))) {
         if (text3 == null) {
            return null;
         }
         Object licenseId = valuesByKey2.get("licenseId");
         Object issuedAt = valuesByKey2.get("issuedAt");
         Object productVersion = valuesByKey2.get("productVersion");
         if (!(licenseId instanceof String) || StringUtils.isBlank((String)licenseId) || issuedAt == null || !Secret.INSTANCE.getProductVersion().equals(productVersion)) {
            return null;
         }
         long issuedAtMillis = Long.parseLong(issuedAt.toString());
         return issuedAtMillis > 0L && issuedAtMillis <= System.currentTimeMillis() + 300000L ? valuesByKey2 : null;
      }
      String text4 = Secret.INSTANCE.calculateMachineFingerprintHash(valuesByKey2);
      return text2.contentEquals(text4) ? valuesByKey2 : null;
   }

   private static String supplementalPublicKey() {
      String property = System.getProperty("urule.license.issuer.public-key");
      if (StringUtils.isBlank(property)) {
         return null;
      }

      File file = new File(property);
      if (!file.isFile() || file.length() < 1L || file.length() > 4096L) {
         return null;
      }

      FileInputStream fileInputStream = null;
      try {
         fileInputStream = new FileInputStream(file);
         String supplementalPublicKeyResult = IOUtils.toString(fileInputStream, "UTF-8").trim();
         Base64.getDecoder().decode(supplementalPublicKeyResult);
         return supplementalPublicKeyResult;
      } catch (Exception exception) {
         return null;
      } finally {
         IOUtils.closeQuietly(fileInputStream);
      }
   }

   /**
    * Validates a license without changing the active license or session state.
    */
   public static LicenseValidationResult validateLicense(String text2) {
      if (StringUtils.isBlank(text2)) {
         return LicenseValidationResult.invalid("empty_license");
      }

      try {
         ObjectMapper objectMapper = JsonMapper.builder().build();
         String text = Secret.INSTANCE.buildMachineFingerprintJson(SystemUtils.OS_NAME, SystemUtils.OS_VERSION, SystemUtils.JAVA_VENDOR, SystemUtils.JAVA_VERSION);
         String text3 = Secret.INSTANCE.md5Hex(text);
         HashMap<?, ?> valuesByKey = decodeLicense(text2, objectMapper, text3);
         if (valuesByKey == null) {
            return LicenseValidationResult.invalid("signature_or_environment_invalid");
         }

         Object objectValue = valuesByKey.get(Secret.INSTANCE.decodeEncodedText("dG8=", true));
         Object objectValue2 = valuesByKey.get(Secret.INSTANCE.decodeEncodedText("bGltaXQ=", true));
         if (!(objectValue instanceof String) || StringUtils.isBlank((String)objectValue) || objectValue2 == null) {
            return LicenseValidationResult.invalid("required_fields_missing");
         }

         long parsedLimit = Long.parseLong(objectValue2.toString());
         if (parsedLimit < -1L) {
            return LicenseValidationResult.invalid("invalid_limit");
         }
         return LicenseValidationResult.valid((String)objectValue, parsedLimit, "portable".equals(valuesByKey.get("binding")));
      } catch (Exception exception) {
         return LicenseValidationResult.invalid("signature_or_format_invalid");
      }
   }

   public static String getLicenseSource() {
      return DynamicSpringConfigLoaderImpl.licenseSource;
   }

   public static boolean isLicensePortable() {
      return DynamicSpringConfigLoaderImpl.licensePortable;
   }

   private void deleteRecursively(File file, boolean keepRoot) {
      for (File file2 : file.listFiles()) {
         if (file2.isFile()) {
            file2.delete();
         } else {
            this.deleteRecursively(file2, false);
         }
      }

      if (!keepRoot) {
         file.delete();
      }
   }

   @Override
   public void loadDynamicJars(String storePath) throws Exception {
      if (storePath == null) {
         this.logger.warning("Dynamic jars store path not specify,so do not load jars...");
      } else {
         this.dynamicJarsStoreDirectPath = storePath;
         AutowireCapableBeanFactory autowireCapableBeanFactory = this.applicationContext.getAutowireCapableBeanFactory();
         if (!(autowireCapableBeanFactory instanceof DefaultListableBeanFactory)) {
            this.logger.warning("Current \"" + autowireCapableBeanFactory + "\" is not DefaultListableBeanFactory type,so can not loading dynamic jars.");
         } else {
            System.out.println("Start loading dynamic jars,this will take a faw seconds...");
            File file = new File(this.dynamicJarsStoreDirectPath);
            File[] file2 = file.listFiles();
            if (file2 == null) {
               this.logger.warning("Dynamic dir [" + this.dynamicJarsStoreDirectPath + "] has no files.");
            } else {
               DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory)autowireCapableBeanFactory;
               ArrayList<URL> items = new ArrayList();
               ArrayList<UrlResource> items2 = new ArrayList();

               for (File file3 : file2) {
                  String name = file3.getName();
                  if (name.toLowerCase().endsWith(".jar")) {
                     URL uRL = file3.toURI().toURL();
                     items.add(uRL);
                     String substring = file3.getAbsolutePath() + "!/urule-spring-context.xml";
                     if (substring.startsWith("/")) {
                        substring = substring.substring(1, substring.length());
                     }

                     String text = "jar:file:/" + substring;
                     UrlResource urlResource = new UrlResource(new URL(text));
                     if (urlResource.exists()) {
                        items2.add(urlResource);
                     }
                  }
               }

               URL[] uRL2 = items.toArray(new URL[items.size()]);
               URLClassLoader uRLClassLoader = new URLClassLoader(uRL2, this.parentClassLoader);

               try {
                  defaultListableBeanFactory.setBeanClassLoader(uRLClassLoader);

                  for (UrlResource urlResource2 : (Iterable<UrlResource>)(Iterable<?>)(items2)) {
                     XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(defaultListableBeanFactory);
                     xmlBeanDefinitionReader.loadBeanDefinitions(urlResource2);
                  }
               } catch (Exception exception) {
                  defaultListableBeanFactory.setBeanClassLoader(this.parentClassLoader);
                  throw new RuleException(exception);
               }

               if (this.dynamicJarClassLoader != null) {
                  this.dynamicJarClassLoader.close();
               }

               this.dynamicJarClassLoader = uRLClassLoader;
               System.out.println("Loading dynamic jars successfully...");
               this.builtInActionLibraryBuilder.buildActions(this.applicationContext);
               this.cleanOldDynamicJarDirectories(file.getName());
               ClassUtils.cleanClassesCache();
            }
         }
      }
   }

   private void cleanOldDynamicJarDirectories(String text) {
      try {
         File file = new File(this.dynamicJarsRootPath);
         File[] file2 = file.listFiles();
         if (file2 == null) {
            return;
         }

         for (File file3 : file2) {
            if (!file3.getName().equals(text)) {
               this.deleteRecursively(file3, false);
            }
         }
      } catch (Exception exception) {
         String message = exception.getMessage();
         if (message == null) {
            message = NullPointerException.class.getName();
         }

         this.logger.warning("Clean dynamic jars store path was fail:" + message);
      }
   }

   @Override
   public String buildDynamicJarsStoreDirectPath() {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
      return this.getDynamicJarsRootPath() + "/" + simpleDateFormat.format(new Date());
   }

   private final String getDynamicJarsRootPath() {
      if (this.dynamicJarsRootPath != null) {
         return this.dynamicJarsRootPath;
      }

      String dynamicJarsRootPath = this.dynamicJarsPath;
      if (StringUtils.isBlank(dynamicJarsRootPath)) {
         dynamicJarsRootPath = System.getProperty("java.io.tmpdir");
         if (!dynamicJarsRootPath.endsWith("/")) {
            dynamicJarsRootPath = dynamicJarsRootPath + "/";
         }

         dynamicJarsRootPath = dynamicJarsRootPath + "urule-jars";
      }

      String property = System.getProperty("urule.instance.id");
      if (StringUtils.isNotBlank(property)) {
         dynamicJarsRootPath = dynamicJarsRootPath + "/" + property;
      }

      File file = new File(dynamicJarsRootPath);
      if (!file.exists()) {
         file.mkdirs();
      }

      this.dynamicJarsRootPath = dynamicJarsRootPath;
      return dynamicJarsRootPath;
   }

   @Override
   public byte[] zipDynamicJars() throws IOException, FileNotFoundException {
      String dynamicJarsStoreDirectPath = this.getDynamicJarsStoreDirectPath();
      if (dynamicJarsStoreDirectPath == null) {
         throw new RuleException("Current jars dir not exist.");
      }

      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
      File file = new File(dynamicJarsStoreDirectPath);

      for (File file2 : file.listFiles()) {
         zipOutputStream.putNextEntry(new ZipEntry(file2.getName()));
         FileInputStream fileInputStream = new FileInputStream(file2);
         IOUtils.copy(fileInputStream, zipOutputStream);
         IOUtils.closeQuietly(fileInputStream);
      }

      zipOutputStream.finish();
      zipOutputStream.flush();
      zipOutputStream.closeEntry();
      zipOutputStream.close();
      byte[] zipDynamicJarsResult = byteArrayOutputStream.toByteArray();
      IOUtils.closeQuietly(byteArrayOutputStream);
      return zipDynamicJarsResult;
   }

   public void setRemoteDynamicJarsBuilder(RemoteDynamicJarsBuilder remoteDynamicJarsBuilder) {
      this.remoteDynamicJarsBuilder = remoteDynamicJarsBuilder;
   }

   @Override
   public String getDynamicJarsStoreDirectPath() {
      return this.dynamicJarsStoreDirectPath;
   }

   @Override
   public String getDynamicJarsIdDigest() {
      return this.dynamicJarsIdDigest;
   }

   @Override
   public void resetDynamicJarsIdDigest(String jarsId) {
      this.dynamicJarsIdDigest = jarsId;
   }

   public void setDynamicJarsPath(String dynamicJarsPath) {
      this.dynamicJarsPath = dynamicJarsPath;
   }

   public void setBuiltInActionLibraryBuilder(BuiltInActionLibraryBuilder builtInActionLibraryBuilder) {
      this.builtInActionLibraryBuilder = builtInActionLibraryBuilder;
   }

   public static String getAuthInfo() {
      return DynamicSpringConfigLoaderImpl.authInfo;
   }

   public static long getLimit() {
      return DynamicSpringConfigLoaderImpl.limit;
   }

   public static String getLimitDate() {
      return DynamicSpringConfigLoaderImpl.limitDate;
   }

   public static String getLicenseKey() {
      return DynamicSpringConfigLoaderImpl.licenseKey;
   }

   public static String getProductVersion() {
      return DynamicSpringConfigLoaderImpl.productVersion;
   }

   public static long getTrialExpired() {
      return DynamicSpringConfigLoaderImpl.trialExpired;
   }

   static {
      Random random = new Random();
      int trialDays = random.nextInt(20);
      long trialDurationMillis = 86400000L * trialDays;
      if (trialDays < 1) {
         trialDurationMillis = 43200000L;
      }

      DynamicSpringConfigLoaderImpl.trialExpired = STARTUP_TIMESTAMP + trialDurationMillis;
   }
}
