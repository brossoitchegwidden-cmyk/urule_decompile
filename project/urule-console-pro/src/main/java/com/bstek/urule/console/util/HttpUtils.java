package com.bstek.urule.console.util;

import com.bstek.urule.runtime.RemoteDynamicJarsBuilder;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.apache.commons.io.IOUtils;

public class HttpUtils {
   public static final String sendPostRequest(String var0, byte[] var1) throws Exception {
      HttpURLConnection var2 = null;

      String var8;
      try {
         URL var3 = new URL(var0);
         var2 = (HttpURLConnection)var3.openConnection();
         var2.setRequestMethod("POST");
         var2.setRequestProperty("Charset", "UTF-8");
         var2.setRequestProperty("Accept-Charset", "utf-8");
         var2.setRequestProperty("Content-Type", "text/json");
         var2.setUseCaches(false);
         var2.setDoOutput(true);
         var2.setDoInput(true);
         var2.connect();
         OutputStream var4 = var2.getOutputStream();
         DataOutputStream var5 = new DataOutputStream(var2.getOutputStream());
         if (var1 != null) {
            var5.write(var1);
         }

         var5.flush();
         var5.close();
         String var6 = null;
         InputStream var7 = var2.getInputStream();
         if (var7 != null) {
            var6 = IOUtils.toString(var7, "UTF-8");
         }

         var4.close();
         if (var7 != null) {
            var7.close();
         }

         var8 = var6;
      } catch (Exception var12) {
         throw var12;
      } finally {
         if (var2 != null) {
            var2.disconnect();
         }

      }

      return var8;
   }

   public static final String buildRequestValidator() throws UnsupportedEncodingException {
      RemoteDynamicJarsBuilder var0 = ServiceUtils.getRemoteDynamicJarsBuilder();
      String var1 = var0.getUser();
      String var2 = var0.getPwd();
      String var3 = "_u=" + URLEncoder.encode(var1, "utf-8");
      var3 = var3 + "&_p=" + URLEncoder.encode(var2, "utf-8");
      return var3;
   }
}
