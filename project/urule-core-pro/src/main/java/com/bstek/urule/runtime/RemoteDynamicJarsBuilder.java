package com.bstek.urule.runtime;

import com.bstek.urule.exception.RuleException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/** Downloads and unpacks dynamically supplied rule-library JARs. */
public class RemoteDynamicJarsBuilder implements ApplicationContextAware {
   private String user;
   private String pwd;
   private String dynamicJarsLoadUrl;
   private String latestJarsCheckUrl;
   private int remoteLoadInterval;
   private String lastJarsDigest;
   private Timer timer;
   public static final String NONE = "n";
   private Logger logger = Logger.getGlobal();
   private ArgumentsProvider argumentsProvider;
   public static final String BEAN_ID = "urule.remoteDynamicJarsBuilder";

   public boolean startIntervalLoadRemoteJars(DynamicSpringConfigLoader loader) throws Exception {
      if (this.remoteLoadInterval == 0) {
         return false;
      }

      if (this.timer == null) {
         this.timer = new Timer();
      }

      this.logger.info("Start loading dynamic jars from server [" + this.getResporityServerUrl() + "] every " + this.remoteLoadInterval + " minutes.");
      int number = this.remoteLoadInterval * 1000 * 60;
      this.timer.schedule(new IntervalTask(loader, this), number, number);
      return true;
   }

   public boolean requestRemoteJars(String storePath) throws Exception {
      String latestDigest = this.checkLatestJarsDigest();
      if (latestDigest.equals(NONE)) {
         this.lastJarsDigest = latestDigest;
         return false;
      }

      if (this.lastJarsDigest != null && this.lastJarsDigest.equals(latestDigest)) {
         return false;
      }

      this.logger.info("Start pull dynamic jars from server side.");
      this.lastJarsDigest = latestDigest;
      HttpURLConnection httpURLConnection = null;
      OutputStreamWriter outputStreamWriter = null;
      InputStream inputStream = null;
      java.io.Closeable closeable = null;
      java.io.Closeable closeable2 = null;

      try {
         String text = "_u=" + URLEncoder.encode(this.user, "utf-8") + "&_p=" + URLEncoder.encode(this.pwd, "utf-8") + "";
         URL uRL = new URL(this.getResporityServerUrl());
         httpURLConnection = (HttpURLConnection)uRL.openConnection();
         httpURLConnection.setRequestMethod("POST");
         httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
         httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
         httpURLConnection.setRequestProperty("Content-Length", String.valueOf(text.length()));
         httpURLConnection.setUseCaches(false);
         httpURLConnection.setDoOutput(true);
         httpURLConnection.connect();
         outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
         outputStreamWriter.write(text);
         outputStreamWriter.flush();
         if (httpURLConnection.getResponseCode() != 200) {
            throw new RuleException("Server request was failed, Response message : " + httpURLConnection.getResponseMessage());
         }

         inputStream = httpURLConnection.getInputStream();
         this.unzipDynamicJars(inputStream, storePath);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         try {
            if (outputStreamWriter != null) {
               outputStreamWriter.close();
            }

            if (closeable2 != null) {
               closeable2.close();
            }

            if (closeable != null) {
               closeable.close();
            }

            if (inputStream != null) {
               inputStream.close();
            }
         } catch (IOException iOException) {
            java.util.logging.Logger.getLogger(RemoteDynamicJarsBuilder.class.getName()).log(java.util.logging.Level.SEVERE, iOException.getMessage(), iOException);
         }

         if (httpURLConnection != null) {
            httpURLConnection.disconnect();
         }
      }

      return true;
   }

   private String checkLatestJarsDigest() {
      try {
         String currentDigest = this.lastJarsDigest == null ? "" : this.lastJarsDigest;
         String requestBody = "_u=" + this.user + "&_p=" + this.pwd + "&jarsId=" + currentDigest + "";
         URL url = new URL(this.resolveLatestJarsCheckUrl());
         HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
         httpURLConnection.setRequestMethod("POST");
         httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
         httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
         httpURLConnection.setRequestProperty("Content-Length", String.valueOf(requestBody.length()));
         httpURLConnection.setUseCaches(false);
         httpURLConnection.setDoOutput(true);
         httpURLConnection.connect();
         OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
         outputStreamWriter.write(requestBody);
         outputStreamWriter.flush();
         if (httpURLConnection.getResponseCode() != 200) {
            throw new RuleException("Server request was failed, Response message : " + httpURLConnection.getResponseMessage());
         }

         InputStream inputStream = httpURLConnection.getInputStream();
         ObjectMapper objectMapper = JsonMapper.builder().build();
         Map valuesByKey = (Map)objectMapper.readValue(inputStream, HashMap.class);
         IOUtils.closeQuietly(inputStream);
         httpURLConnection.disconnect();
         outputStreamWriter.close();
         boolean match = (Boolean)valuesByKey.get("match");
         return match ? "n" : (String)valuesByKey.get("digest");
      } catch (Exception exception) {
         throw new RuleException("当前环境配置了【" + this.resolveLatestJarsCheckUrl() + "】参数，但访问对应的服务器时出现错误", exception);
      }
   }

   public void unzipDynamicJars(InputStream inputStream, String storePath) throws Exception {
      File file = new File(storePath);
      if (!file.exists()) {
         file.mkdirs();
      }

      ZipInputStream zipInputStream = new ZipInputStream(inputStream);

      for (ZipEntry nextEntry = zipInputStream.getNextEntry(); nextEntry != null; nextEntry = zipInputStream.getNextEntry()) {
         String text = storePath + "/" + nextEntry.getName();
         File file2 = new File(text);
         FileOutputStream fileOutputStream = new FileOutputStream(file2);
         int number = 0;
         byte[] bytes = new byte[1024];

         while ((number = zipInputStream.read(bytes)) != -1) {
            fileOutputStream.write(bytes, 0, number);
         }

         IOUtils.closeQuietly(fileOutputStream);
      }

      zipInputStream.closeEntry();
      zipInputStream.close();
   }

   public void destroy() {
      if (this.timer != null) {
         this.timer.cancel();
      }
   }

   private String resolveLatestJarsCheckUrl() {
      if (this.argumentsProvider != null && this.argumentsProvider.resporityServerUrl() != null) {
         String repositoryUrl = this.argumentsProvider.resporityServerUrl();
         if (repositoryUrl.endsWith("/")) {
            repositoryUrl = repositoryUrl + "urule/dynamic/checkLatestJarsDir";
         } else {
            repositoryUrl = repositoryUrl + "/urule/dynamic/checkLatestJarsDir";
         }

         return repositoryUrl;
      } else {
         return this.latestJarsCheckUrl;
      }
   }

   public String getResporityServerUrl() {
      if (this.argumentsProvider != null && this.argumentsProvider.resporityServerUrl() != null) {
         String resporityServerUrl = this.argumentsProvider.resporityServerUrl();
         if (resporityServerUrl.endsWith("/")) {
            resporityServerUrl = resporityServerUrl + "urule/dynamic/loadDynamicJars";
         } else {
            resporityServerUrl = resporityServerUrl + "/urule/dynamic/loadDynamicJars";
         }

         return resporityServerUrl;
      } else {
         return this.dynamicJarsLoadUrl;
      }
   }

   public void setRemoteLoadInterval(int remoteLoadInterval) {
      this.remoteLoadInterval = remoteLoadInterval;
   }

   public void setResporityServerUrl(String resporityServerUrl) {
      if (!StringUtils.isEmpty(resporityServerUrl) && !resporityServerUrl.equals("urule.resporityServerUrl")) {
         this.configureRepositoryUrls(resporityServerUrl);
      }
   }

   private void configureRepositoryUrls(String repositoryUrl) {
      if (repositoryUrl.endsWith("/")) {
         this.latestJarsCheckUrl = repositoryUrl + "urule/dynamic/checkLatestJarsDir";
         repositoryUrl = repositoryUrl + "urule/dynamic/loadDynamicJars";
      } else {
         this.latestJarsCheckUrl = repositoryUrl + "/urule/dynamic/checkLatestJarsDir";
         repositoryUrl = repositoryUrl + "/urule/dynamic/loadDynamicJars";
      }

      this.dynamicJarsLoadUrl = repositoryUrl;
   }

   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      Collection argumentsProviders = applicationContext.getBeansOfType(ArgumentsProvider.class).values();
      if (argumentsProviders.size() != 0) {
         this.argumentsProvider = (ArgumentsProvider)argumentsProviders.iterator().next();
      }
   }

   public String getUser() {
      return this.user;
   }

   public String getPwd() {
      return this.pwd;
   }

   public void setUser(String user) {
      this.user = user;
   }

   public void setPwd(String pwd) {
      this.pwd = pwd;
   }
}
