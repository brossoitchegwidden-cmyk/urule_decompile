package com.bstek.urule.runtime.service;

import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.model.rete.JsonUtils;
import com.bstek.urule.runtime.ArgumentsProvider;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgePackageImpl;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.bstek.urule.runtime.RemoteDynamicJarsBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Loads serialized knowledge packages from the configured remote repository.
 */
public class RemoteServiceImpl implements RemoteService, ApplicationContextAware {
   private String knowledgeLoadUrl;
   private RemoteDynamicJarsBuilder remoteDynamicJarsBuilder;
   private ArgumentsProvider argumentsProvider;
   private Logger logger = Logger.getGlobal();

   @Override
   public KnowledgePackage getKnowledge(String packageId, String timestamp) {
      if (StringUtils.isEmpty(this.resolveKnowledgeLoadUrl())) {
         return null;
      }

      this.logger.info("Load knowledgepackage [" + packageId + "] from remote server 【" + this.resolveKnowledgeLoadUrl() + "】...");
      String responseJson = this.fetchKnowledgePackage(packageId, timestamp);
      if (StringUtils.isEmpty(responseJson)) {
         return null;
      }

      KnowledgePackageWrapper knowledgePackageWrapper = JsonUtils.parseKnowledgePackageWrapper(responseJson);
      KnowledgePackageImpl knowledgePackage = (KnowledgePackageImpl)knowledgePackageWrapper.getKnowledgePackage();
      Map flowMap = knowledgePackage.getFlowMap();
      if (flowMap != null && flowMap.size() > 0) {
         for (FlowDefinition flowDefinition : (Iterable<FlowDefinition>)(Iterable<?>)(flowMap.values())) {
            flowDefinition.buildConnectionToNode();
         }
      }

      knowledgePackage.resetTimestamp();
      knowledgePackage.setPackageInfo(packageId);
      return knowledgePackage;
   }

   private String fetchKnowledgePackage(String packageId, String timestamp) {
      HttpURLConnection httpURLConnection = null;
      OutputStreamWriter outputStreamWriter = null;
      InputStream inputStream = null;

      try {
         String encodedPackageId = Utils.encodeURL(packageId);
         encodedPackageId = Utils.encodeURL(encodedPackageId);
         String requestBody = "packageId=" + encodedPackageId + "";
         if (StringUtils.isNotEmpty(timestamp)) {
            requestBody = requestBody + "&timestamp=" + timestamp + "";
         }

         requestBody = requestBody + "&_u=" + Utils.encodeURL(this.remoteDynamicJarsBuilder.getUser()) + "";
         requestBody = requestBody + "&_p=" + Utils.encodeURL(this.remoteDynamicJarsBuilder.getPwd()) + "";
         URL url = new URL(this.resolveKnowledgeLoadUrl());
         httpURLConnection = (HttpURLConnection)url.openConnection();
         httpURLConnection.setRequestMethod("POST");
         httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
         httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
         httpURLConnection.setRequestProperty("Content-Length", String.valueOf(requestBody.length()));
         httpURLConnection.setUseCaches(false);
         httpURLConnection.setDoOutput(true);
         httpURLConnection.connect();
         outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
         outputStreamWriter.write(requestBody);
         outputStreamWriter.flush();
         if (httpURLConnection.getResponseCode() != 200) {
            String headerField = httpURLConnection.getHeaderField("errorMsg");
            if (headerField != null) {
               headerField = URLDecoder.decode(headerField, "utf-8");
            }

            String errorStack = this.readErrorStack(httpURLConnection.getErrorStream());
            if (errorStack != null) {
               headerField = errorStack;
            }

            throw new RuleException("Server request was failed : " + headerField);
         } else {
            inputStream = httpURLConnection.getInputStream();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            return Utils.uncompress(bytes);
         }
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         IOUtils.closeQuietly(outputStreamWriter);
         IOUtils.closeQuietly(inputStream);
         if (httpURLConnection != null) {
            httpURLConnection.disconnect();
         }
      }
   }

   private String readErrorStack(InputStream inputStream) {
      try {
         if (inputStream != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map valuesByKey = (Map)objectMapper.readValue(inputStream, HashMap.class);
            return (String)valuesByKey.get("stack");
         } else {
            return null;
         }
      } catch (Exception exception) {
         return null;
      } finally {
         IOUtils.closeQuietly(inputStream);
      }
   }

   private String resolveKnowledgeLoadUrl() {
      if (this.argumentsProvider != null && this.argumentsProvider.resporityServerUrl() != null) {
         String repositoryUrl = this.argumentsProvider.resporityServerUrl();
         if (repositoryUrl.endsWith("/")) {
            repositoryUrl = repositoryUrl + "urule/loadknowledge";
         } else {
            repositoryUrl = repositoryUrl + "/urule/loadknowledge";
         }

         return repositoryUrl;
      } else {
         return this.knowledgeLoadUrl;
      }
   }

   public void setResporityServerUrl(String resporityServerUrl) {
      this.logger.info("urule.resporityServerUrl:" + resporityServerUrl);
      if (!StringUtils.isEmpty(resporityServerUrl) && !resporityServerUrl.equals("urule.resporityServerUrl")) {
         this.configureRepositoryUrl(resporityServerUrl);
      }
   }

   private void configureRepositoryUrl(String repositoryUrl) {
      if (repositoryUrl.endsWith("/")) {
         repositoryUrl = repositoryUrl + "urule/loadknowledge";
      } else {
         repositoryUrl = repositoryUrl + "/urule/loadknowledge";
      }

      this.knowledgeLoadUrl = repositoryUrl;
   }

   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      Collection argumentsProviders = applicationContext.getBeansOfType(ArgumentsProvider.class).values();
      if (argumentsProviders.size() != 0) {
         this.argumentsProvider = (ArgumentsProvider)argumentsProviders.iterator().next();
      }
   }

   public void setRemoteDynamicJarsBuilder(RemoteDynamicJarsBuilder remoteDynamicJarsBuilder) {
      this.remoteDynamicJarsBuilder = remoteDynamicJarsBuilder;
   }
}
