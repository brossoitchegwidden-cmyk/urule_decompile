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

class Secret {
   private String c;
   protected final String a = "UTF-8";
   private String d;
   private String e;
   private final String f = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCOTVYtYbbBYWsC6BQJRcoKx62FQnAeFoI9R3/7ZhRT+g46sgJxZLVGvaHp6ZX7mwdDuGcF9QJT9hsAe713PN/9QrVuNfEokKaE4+eQhFjDnHPNRcyTZDmNDmWWfyONbLXfv/hJjlfdc3PsWFg99/U3519biUpsvm+34MoKHBkQ/wIDAQAB";
   private String g = ";base64,";
   private Decoder h = Base64.getDecoder();
   protected static final Secret b = new Secret();

   private Secret() {
      this.d = this.a("UlNB", true);
      this.e = this.a("QUVTL0VDQi9QS0NTNVBhZGRpbmc=", true);
   }

   protected String a(String var1) {
      try {
         KeyFactory var2 = KeyFactory.getInstance(this.d);
         byte[] var3 = Base64.getDecoder()
            .decode(
               "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCOTVYtYbbBYWsC6BQJRcoKx62FQnAeFoI9R3/7ZhRT+g46sgJxZLVGvaHp6ZX7mwdDuGcF9QJT9hsAe713PN/9QrVuNfEokKaE4+eQhFjDnHPNRcyTZDmNDmWWfyONbLXfv/hJjlfdc3PsWFg99/U3519biUpsvm+34MoKHBkQ/wIDAQAB"
                  .getBytes("UTF-8")
            );
         PublicKey var4 = var2.generatePublic(new X509EncodedKeySpec(var3));
         Cipher var5 = Cipher.getInstance(this.d);
         var5.init(1, var4);
         byte[] var6 = var5.doFinal(var1.getBytes("UTF-8"));
         return new String(Base64.getEncoder().encode(var6));
      } catch (Exception var7) {
         throw new RuleException(var7);
      }
   }

   protected byte[] a(byte[] var1) {
      return this.a(var1, this.f);
   }

   protected byte[] a(byte[] var1, String var2) {
      try {
         return this.b(var1, var2);
      } catch (Exception var3) {
         throw new RuleException(var3);
      }
   }

   protected byte[] b(byte[] var1) throws Exception {
      return this.b(var1, this.f);
   }

   protected byte[] b(byte[] var1, String var2) throws Exception {
      KeyFactory var3 = KeyFactory.getInstance(this.d);
      byte[] var4 = Base64.getDecoder().decode(var2.getBytes("UTF-8"));
      PublicKey var5 = var3.generatePublic(new X509EncodedKeySpec(var4));
      if (!(var5 instanceof RSAPublicKey)) {
         throw new IllegalArgumentException("Configured license public key is not RSA.");
      }
      RSAPublicKey var6 = (RSAPublicKey)var5;
      if (var6.getModulus().bitLength() != 1024 || !BigInteger.valueOf(65537L).equals(var6.getPublicExponent())) {
         throw new IllegalArgumentException("Configured license public key parameters do not match the original trust strength.");
      }
      Cipher var7 = Cipher.getInstance(this.d);
      var7.init(2, var5);
      return var7.doFinal(var1);
   }

   protected String a(String var1, String var2) {
      try {
         Cipher var3 = Cipher.getInstance(this.e);
         var3.init(1, new SecretKeySpec(var1.getBytes(), this.a("QUVT", true)));
         byte[] var4 = var3.doFinal(var2.getBytes("UTF-8"));
         return new String(Base64.getEncoder().encode(var4));
      } catch (Exception var5) {
         throw new RuleException(var5);
      }
   }

   protected byte[] a(byte[] var1, byte[] var2) {
      try {
         Cipher var3 = Cipher.getInstance(this.e);
         var3.init(2, new SecretKeySpec(var1, this.a("QUVT", true)));
         return var3.doFinal(var2);
      } catch (Exception var4) {
         throw new RuleException(var4);
      }
   }

   protected String a() {
      if (this.c != null) {
         return this.c;
      }

      try {
         String var1 = this.a("L01FVEEtSU5GL21hdmVuL2NvbS5ic3Rlay51cnVsZS91cnVsZS1jb3JlLXByby9wb20ucHJvcGVydGllcw==", true);
         InputStream var2 = this.getClass().getResourceAsStream(var1);
         if (var2 == null) {
            throw new RuleException("Can not read version");
         }

         Properties var3 = new Properties();
         var3.load(var2);
         var2.close();
         this.c = var3.getProperty(this.a("dmVyc2lvbg==", true));
         return this.c;
      } catch (Exception var4) {
         throw new RuleException(var4);
      }
   }

   protected void b() {
      String var1 = this.a();
      StringBuilder var2 = this.d();
      String var3 = ".%20%E6%82%A8%E5%BD%93%E5%89%8D%E4%BD%BF%E7%94%A8%E7%9A%84%E6%98%AFURule%20Pro%E8%AF%95%E7%94%A8%E7%89%88%20%3A%20urule-pro-";
      String var4 = "%2C%E8%AF%B7%E8%B4%AD%E4%B9%B0%E5%95%86%E4%B8%9A%E8%AE%B8%E5%8F%AF%E8%AF%81";
      var2.append(this.a(var3, false) + var1 + this.a(var4, false));
      var2.append("\n");
      String var5 = "LiBZb3UgYXJlIHVzaW5nIGEgdHJpYWwgdmVyc2lvbiA6IHVydWxlLXByby0=";
      String var6 = "LHBsZWFzZSBwdXJjaGFzZSB0aGUgY29tbWVyY2lhbCBsaWNlbnNlLg==";
      var2.append(this.a(var5, true) + var1 + this.a(var6, true));
      System.out.println(var2.toString());
   }

   private StringBuilder d() {
      StringBuilder var1 = new StringBuilder();
      var1.append("..............................................................................................................");
      var1.append("\n");
      var1.append(
         this.a(
            ".%20URule%20Pro%E8%BD%AF%E4%BB%B6%E4%BD%9C%E5%93%81%E7%9A%84%E8%91%97%E4%BD%9C%E6%9D%83%E3%80%81%E5%95%86%E6%A0%87%E6%9D%83%E7%AD%89%E7%9F%A5%E8%AF%86%E4%BA%A7%E6%9D%83%E5%B1%9E%E4%BA%8E%E4%B8%8A%E6%B5%B7%E9%94%90%E9%81%93%E4%BF%A1%E6%81%AF%E6%8A%80%E6%9C%AF%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8%EF%BC%88http%3A%2F%2Fwww.bstek.com%EF%BC%89%E6%89%80%E6%9C%89%2C%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20",
            false
         )
      );
      var1.append("\n");
      var1.append(
         this.a(
            ".%20%E4%BB%BB%E4%BD%95%E5%8D%95%E4%BD%8D%E5%92%8C%E4%B8%AA%E4%BA%BA%E6%9C%AA%E7%BB%8F%E4%B8%8A%E6%B5%B7%E9%94%90%E9%81%93%E4%BF%A1%E6%81%AF%E6%8A%80%E6%9C%AF%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8%E4%B9%A6%E9%9D%A2%E6%8E%88%E6%9D%83%2C%E4%B8%8D%E5%BE%97%E4%BB%A5%E4%BB%BB%E4%BD%95%E7%9B%AE%E7%9A%84%E3%80%81%E4%BB%BB%E4%BD%95%E6%96%B9%E5%BC%8F%E5%A4%8D%E5%88%B6%E3%80%81%E4%BC%A0%E6%92%AD%E6%9C%AC%E8%BD%AF%E4%BB%B6%E7%9A%84%E4%BB%BB%E4%BD%95%E9%83%A8%E5%88%86%2C%20%20%20%20%20%20%20%20%20%20%20",
            false
         )
      );
      var1.append("\n");
      var1.append(
         this.a(
            ".%20%E5%90%A6%E5%88%99%E5%B0%86%E8%A7%86%E4%B8%BA%E4%BE%B5%E6%9D%83%2C%E4%B8%8A%E6%B5%B7%E9%94%90%E9%81%93%E4%BF%A1%E6%81%AF%E6%8A%80%E6%9C%AF%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8%E4%BF%9D%E7%95%99%E4%BE%9D%E6%B3%95%E8%BF%BD%E7%A9%B6%E5%85%B6%E6%B3%95%E5%BE%8B%E8%B4%A3%E4%BB%BB%E7%9A%84%E6%9D%83%E5%88%A9%E3%80%82%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20%20",
            false
         )
      );
      var1.append("\n");
      var1.append("..............................................................................................................");
      var1.append("\n");
      return var1;
   }

   protected String a(HashMap<?, ?> var1) {
      String var2 = (String)var1.get(this.a("b3NOYW1l", true));
      String var3 = (String)var1.get(this.a("b3NWZXJzaW9u", true));
      String var4 = (String)var1.get(this.a("amF2YVZlbmRlcg==", true));
      String var5 = (String)var1.get(this.a("amF2YVZlcnNpb24=", true));
      String var6 = this.a(var2, var3, var4, var5);
      return this.b(var6);
   }

   protected String b(String var1) {
      byte[] var2 = null;

      try {
         var2 = MessageDigest.getInstance(this.a("bWQ1", true)).digest(var1.getBytes());
      } catch (NoSuchAlgorithmException var5) {
         throw new RuntimeException("No Such Algorithm");
      }

      String var3 = new BigInteger(1, var2).toString(16);

      for (int var4 = 0; var4 < 32 - var3.length(); var4++) {
         var3 = "0" + var3;
      }

      return var3;
   }

   protected String a(String var1, String var2, String var3, String var4) {
      StringBuilder var5 = new StringBuilder();
      var5.append("{");
      var5.append("\"" + this.a("b3NOYW1l", true) + "\":\"" + var1 + "\",");
      var5.append("\"" + this.a("b3NWZXJzaW9u", true) + "\":\"" + var2 + "\",");
      var5.append("\"" + this.a("amF2YVZlbmRlcg==", true) + "\":\"" + var3 + "\",");
      var5.append("\"" + this.a("amF2YVZlcnNpb24=", true) + "\":\"" + var4 + "\"");
      var5.append("}");
      return var5.toString();
   }

   protected void b(String var1, String var2) {
      StringBuilder var3 = this.d();
      String var4 = this.a();
      String var5 = "VVJ1bGUgUHJvWw==";
      String var6 = "XSBMaWNlbnNlZCB0byA=";
      var3.append(this.a(var5, true) + var4 + this.a(var6, true) + var1);
      var3.append(this.a("LCBFeHBpcmUgOiA=", true) + var2);
      System.out.println(var3.toString());
   }

   protected String a(String var1, boolean var2) {
      if (StringUtils.isBlank(var1)) {
         return var1;
      }

      if (var2) {
         String var3 = "";
         int var4 = var1.indexOf(this.g);
         if (var4 > -1) {
            var3 = var1.substring(var4 + this.g.length());
         } else {
            var3 = var1;
         }

         byte[] var5 = this.h.decode(var3);

         try {
            return new String(var5, "UTF-8");
         } catch (UnsupportedEncodingException var7) {
            throw new RuleException(var7);
         }
      } else {
         try {
            return URLDecoder.decode(var1, "UTF-8");
         } catch (UnsupportedEncodingException var8) {
            throw new RuleException(var8);
         }
      }
   }

   protected String c() {
      String var1 = UUID.randomUUID().toString();
      return var1.replace("-", "").substring(0, 16);
   }

   protected void c(String var1) {
      System.out.println("License Key:" + var1);
   }
}
