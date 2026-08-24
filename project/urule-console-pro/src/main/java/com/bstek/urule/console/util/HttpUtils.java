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
   public static final String sendPostRequest(String targetUrl, byte[] bytes) throws Exception {
      HttpURLConnection httpURLConnection = null;

      String sendPostRequestResult;
      try {
         URL uRL = new URL(targetUrl);
         httpURLConnection = (HttpURLConnection)uRL.openConnection();
         httpURLConnection.setRequestMethod("POST");
         httpURLConnection.setRequestProperty("Charset", "UTF-8");
         httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
         httpURLConnection.setRequestProperty("Content-Type", "text/json");
         httpURLConnection.setUseCaches(false);
         httpURLConnection.setDoOutput(true);
         httpURLConnection.setDoInput(true);
         httpURLConnection.connect();
         OutputStream outputStream = httpURLConnection.getOutputStream();
         DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
         if (bytes != null) {
            dataOutputStream.write(bytes);
         }

         dataOutputStream.flush();
         dataOutputStream.close();
         String text = null;
         InputStream inputStream = httpURLConnection.getInputStream();
         if (inputStream != null) {
            text = IOUtils.toString(inputStream, "UTF-8");
         }

         outputStream.close();
         if (inputStream != null) {
            inputStream.close();
         }

         sendPostRequestResult = text;
      } catch (Exception exception) {
         throw exception;
      } finally {
         if (httpURLConnection != null) {
            httpURLConnection.disconnect();
         }

      }

      return sendPostRequestResult;
   }

   public static final String buildRequestValidator() throws UnsupportedEncodingException {
      RemoteDynamicJarsBuilder remoteDynamicJarsBuilder = ServiceUtils.getRemoteDynamicJarsBuilder();
      String user = remoteDynamicJarsBuilder.getUser();
      String pwd = remoteDynamicJarsBuilder.getPwd();
      String requestValidator = "_u=" + URLEncoder.encode(user, "utf-8");
      requestValidator = requestValidator + "&_p=" + URLEncoder.encode(pwd, "utf-8");
      return requestValidator;
   }
}
