package com.bstek.urule.runtime;

import com.bstek.urule.exception.RuleException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;
import java.util.Base64.Decoder;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang.StringUtils;

/**
 * Internal codec used to read and validate URule Pro license data.
 *
 * <p>The descriptive names in this class document the existing protocol; they
 * do not change the cryptographic behavior recovered from the original JAR.</p>
 */
class Secret {
   protected static final String UTF_8 = "UTF-8";
   protected static final Secret INSTANCE = new Secret();
   private static final String RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCOTVYtYbbBYWsC6BQJRcoKx62FQnAeFoI9R3/7ZhRT+g46sgJxZLVGvaHp6ZX7mwdDuGcF9QJT9hsAe713PN/9QrVuNfEokKaE4+eQhFjDnHPNRcyTZDmNDmWWfyONbLXfv/hJjlfdc3PsWFg99/U3519biUpsvm+34MoKHBkQ/wIDAQAB";
   private static final String BASE64_MARKER = ";base64,";
   private static final Decoder BASE64_DECODER = Base64.getDecoder();
   private String productVersion;
   private String rsaAlgorithm;
   private String aesTransformation;

   private Secret() {
      this.rsaAlgorithm = this.decodeEncodedText("UlNB", true);
      this.aesTransformation = this.decodeEncodedText("QUVTL0VDQi9QS0NTNVBhZGRpbmc=", true);
   }

   protected String encryptWithRsaPublicKey(String plainText) {
      try {
         KeyFactory keyFactory = KeyFactory.getInstance(this.rsaAlgorithm);
         byte[] bytes = Base64.getDecoder()
            .decode(
               "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCOTVYtYbbBYWsC6BQJRcoKx62FQnAeFoI9R3/7ZhRT+g46sgJxZLVGvaHp6ZX7mwdDuGcF9QJT9hsAe713PN/9QrVuNfEokKaE4+eQhFjDnHPNRcyTZDmNDmWWfyONbLXfv/hJjlfdc3PsWFg99/U3519biUpsvm+34MoKHBkQ/wIDAQAB"
                  .getBytes("UTF-8")
            );
         PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(bytes));
         Cipher cipher = Cipher.getInstance(this.rsaAlgorithm);
         cipher.init(1, publicKey);
         byte[] bytes2 = cipher.doFinal(plainText.getBytes("UTF-8"));
         return new String(Base64.getEncoder().encode(bytes2));
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   protected byte[] decryptRsaPayload(byte[] encryptedBytes) {
      return this.decryptRsaPayload(encryptedBytes, RSA_PUBLIC_KEY);
   }

   protected byte[] decryptRsaPayload(byte[] encryptedBytes, String publicKeyText) {
      try {
         return this.decryptWithRsaPublicKey(encryptedBytes, publicKeyText);
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   protected byte[] decryptWithRsaPublicKey(byte[] encryptedBytes) throws Exception {
      return this.decryptWithRsaPublicKey(encryptedBytes, RSA_PUBLIC_KEY);
   }

   protected byte[] decryptWithRsaPublicKey(byte[] encryptedBytes, String publicKeyText) throws Exception {
      KeyFactory keyFactory = KeyFactory.getInstance(this.rsaAlgorithm);
      byte[] bytes = Base64.getDecoder().decode(publicKeyText.getBytes("UTF-8"));
      PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(bytes));
      if (!(publicKey instanceof RSAPublicKey)) {
         throw new IllegalArgumentException("Configured license public key is not RSA.");
      }
      RSAPublicKey rSAPublicKey = (RSAPublicKey)publicKey;
      if (rSAPublicKey.getModulus().bitLength() != 1024 || !BigInteger.valueOf(65537L).equals(rSAPublicKey.getPublicExponent())) {
         throw new IllegalArgumentException("Configured license public key parameters do not match the original trust strength.");
      }
      Cipher cipher = Cipher.getInstance(this.rsaAlgorithm);
      cipher.init(2, publicKey);
      return cipher.doFinal(encryptedBytes);
   }

   protected String encryptWithAes(String secretKey, String plainText) {
      try {
         Cipher cipher = Cipher.getInstance(this.aesTransformation);
         cipher.init(1, new SecretKeySpec(secretKey.getBytes(), this.decodeEncodedText("QUVT", true)));
         byte[] bytes = cipher.doFinal(plainText.getBytes("UTF-8"));
         return new String(Base64.getEncoder().encode(bytes));
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   protected byte[] decryptWithAes(byte[] secretKey, byte[] encryptedBytes) {
      try {
         Cipher cipher = Cipher.getInstance(this.aesTransformation);
         cipher.init(2, new SecretKeySpec(secretKey, this.decodeEncodedText("QUVT", true)));
         return cipher.doFinal(encryptedBytes);
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   protected String getProductVersion() {
      if (this.productVersion != null) {
         return this.productVersion;
      }

      try {
         String text = this.decodeEncodedText("L01FVEEtSU5GL21hdmVuL2NvbS5ic3Rlay51cnVsZS91cnVsZS1jb3JlLXByby9wb20ucHJvcGVydGllcw==", true);
         InputStream resourceAsStream = this.getClass().getResourceAsStream(text);
         if (resourceAsStream == null) {
            throw new RuleException("Can not read version");
         }

         Properties properties = new Properties();
         properties.load(resourceAsStream);
         resourceAsStream.close();
         this.productVersion = properties.getProperty(this.decodeEncodedText("dmVyc2lvbg==", true));
         return this.productVersion;
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   protected void printTrialBanner() {
      String text = this.getProductVersion();
      StringBuilder stringBuilder = this.buildLicenseBannerHeader();
      String text2 = ".%20%E6%82%A8%E5%BD%93%E5%89%8D%E4%BD%BF%E7%94%A8%E7%9A%84%E6%98%AFURule%20Pro%E8%AF%95%E7%94%A8%E7%89%88%20%3A%20urule-pro-";
      String text3 = "%2C%E8%AF%B7%E8%B4%AD%E4%B9%B0%E5%95%86%E4%B8%9A%E8%AE%B8%E5%8F%AF%E8%AF%81";
      stringBuilder.append(this.decodeEncodedText(text2, false) + text + this.decodeEncodedText(text3, false));
      stringBuilder.append("\n");
      String text4 = "LiBZb3UgYXJlIHVzaW5nIGEgdHJpYWwgdmVyc2lvbiA6IHVydWxlLXByby0=";
      String text5 = "LHBsZWFzZSBwdXJjaGFzZSB0aGUgY29tbWVyY2lhbCBsaWNlbnNlLg==";
      stringBuilder.append(this.decodeEncodedText(text4, true) + text + this.decodeEncodedText(text5, true));
      System.out.println(stringBuilder.toString());
   }

   private StringBuilder buildLicenseBannerHeader() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("..............................................................................................................");
      stringBuilder.append("\n");
      stringBuilder.append(
         this.decodeEncodedText(
            ".%20URule%20Pro%E8%BD%AF%E4%BB%B6%E4%BD%9C%E5%93%81%E7%9A%84%E8%91%97%E4%BD%9C%E6%9D%83%E3%80%81%E5%95%86%E6%A0%87%E6%9D%83%E7%AD%89%E7%9F%A5%E8%AF%86%E4%BA%A7%E6%9D%83%E5%B1%9E%E4%BA%8E%E4%B8%8A%E6%B5%B7%E9%94%90%E9%81%93%E4%BF%A1%E6%81%AF%E6%8A%80%E6%9C%AF%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8%EF%BC%88http%3A%2F%2Fwww.bstek.com%EF%BC%89%E6%89%80%E6%9C%89%2C%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20",
            false
         )
      );
      stringBuilder.append("\n");
      stringBuilder.append(
         this.decodeEncodedText(
            ".%20%E4%BB%BB%E4%BD%95%E5%8D%95%E4%BD%8D%E5%92%8C%E4%B8%AA%E4%BA%BA%E6%9C%AA%E7%BB%8F%E4%B8%8A%E6%B5%B7%E9%94%90%E9%81%93%E4%BF%A1%E6%81%AF%E6%8A%80%E6%9C%AF%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8%E4%B9%A6%E9%9D%A2%E6%8E%88%E6%9D%83%2C%E4%B8%8D%E5%BE%97%E4%BB%A5%E4%BB%BB%E4%BD%95%E7%9B%AE%E7%9A%84%E3%80%81%E4%BB%BB%E4%BD%95%E6%96%B9%E5%BC%8F%E5%A4%8D%E5%88%B6%E3%80%81%E4%BC%A0%E6%92%AD%E6%9C%AC%E8%BD%AF%E4%BB%B6%E7%9A%84%E4%BB%BB%E4%BD%95%E9%83%A8%E5%88%86%2C%20%20%20%20%20%20%20%20%20%20%20",
            false
         )
      );
      stringBuilder.append("\n");
      stringBuilder.append(
         this.decodeEncodedText(
            ".%20%E5%90%A6%E5%88%99%E5%B0%86%E8%A7%86%E4%B8%BA%E4%BE%B5%E6%9D%83%2C%E4%B8%8A%E6%B5%B7%E9%94%90%E9%81%93%E4%BF%A1%E6%81%AF%E6%8A%80%E6%9C%AF%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8%E4%BF%9D%E7%95%99%E4%BE%9D%E6%B3%95%E8%BF%BD%E7%A9%B6%E5%85%B6%E6%B3%95%E5%BE%8B%E8%B4%A3%E4%BB%BB%E7%9A%84%E6%9D%83%E5%88%A9%E3%80%82%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20",
            false
         )
      );
      stringBuilder.append("\n");
      stringBuilder.append("..............................................................................................................");
      stringBuilder.append("\n");
      return stringBuilder;
   }

   protected String calculateMachineFingerprintHash(HashMap<?, ?> systemAttributes) {
      String osName = (String)systemAttributes.get(this.decodeEncodedText("b3NOYW1l", true));
      String osVersion = (String)systemAttributes.get(this.decodeEncodedText("b3NWZXJzaW9u", true));
      String javaVendor = (String)systemAttributes.get(this.decodeEncodedText("amF2YVZlbmRlcg==", true));
      String javaVersion = (String)systemAttributes.get(this.decodeEncodedText("amF2YVZlcnNpb24=", true));
      String fingerprintJson = this.buildMachineFingerprintJson(osName, osVersion, javaVendor, javaVersion);
      return this.md5Hex(fingerprintJson);
   }

   protected String md5Hex(String value) {
      byte[] bytes = null;

      try {
         bytes = MessageDigest.getInstance(this.decodeEncodedText("bWQ1", true)).digest(value.getBytes());
      } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
         throw new RuntimeException("No Such Algorithm");
      }

      String text = new BigInteger(1, bytes).toString(16);

      for (int index = 0; index < 32 - text.length(); index++) {
         text = "0" + text;
      }

      return text;
   }

   protected String buildMachineFingerprintJson(String osName, String osVersion, String javaVendor, String javaVersion) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("{");
      stringBuilder.append("\"" + this.decodeEncodedText("b3NOYW1l", true) + "\":\"" + osName + "\",");
      stringBuilder.append("\"" + this.decodeEncodedText("b3NWZXJzaW9u", true) + "\":\"" + osVersion + "\",");
      stringBuilder.append("\"" + this.decodeEncodedText("amF2YVZlbmRlcg==", true) + "\":\"" + javaVendor + "\",");
      stringBuilder.append("\"" + this.decodeEncodedText("amF2YVZlcnNpb24=", true) + "\":\"" + javaVersion + "\"");
      stringBuilder.append("}");
      return stringBuilder.toString();
   }

   protected void printLicensedBanner(String licensee, String expiration) {
      StringBuilder stringBuilder = this.buildLicenseBannerHeader();
      String text = this.getProductVersion();
      String text4 = "VVJ1bGUgUHJvWw==";
      String text5 = "XSBMaWNlbnNlZCB0byA=";
      stringBuilder.append(this.decodeEncodedText(text4, true) + text + this.decodeEncodedText(text5, true) + licensee);
      stringBuilder.append(this.decodeEncodedText("LCBFeHBpcmUgOiA=", true) + expiration);
      System.out.println(stringBuilder.toString());
   }

   protected String decodeEncodedText(String encodedText, boolean base64Encoded) {
      if (StringUtils.isBlank(encodedText)) {
         return encodedText;
      }

      if (base64Encoded) {
         String substring = "";
         int number = encodedText.indexOf(BASE64_MARKER);
         if (number > -1) {
            substring = encodedText.substring(number + BASE64_MARKER.length());
         } else {
            substring = encodedText;
         }

         byte[] bytes = BASE64_DECODER.decode(substring);

         try {
            return new String(bytes, "UTF-8");
         } catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new RuleException(unsupportedEncodingException);
         }
      } else {
         try {
            return URLDecoder.decode(encodedText, "UTF-8");
         } catch (UnsupportedEncodingException unsupportedEncodingException2) {
            throw new RuleException(unsupportedEncodingException2);
         }
      }
   }

   protected String generateAesKey() {
      String text = UUID.randomUUID().toString();
      return text.replace("-", "").substring(0, 16);
   }

   protected void printLicenseKey(String licenseKey) {
      System.out.println("License Key:" + licenseKey);
   }
}
