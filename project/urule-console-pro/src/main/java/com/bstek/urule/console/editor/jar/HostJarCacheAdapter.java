package com.bstek.urule.console.editor.jar;

import com.bstek.urule.Utils;
import com.bstek.urule.console.database.model.UrlConfig;
import com.bstek.urule.console.database.model.UrlType;
import com.bstek.urule.console.database.service.url.UrlService;
import com.bstek.urule.console.util.HttpUtils;
import com.bstek.urule.console.util.ServiceUtils;
import com.bstek.urule.runtime.DynamicSpringConfigLoader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HostJarCacheAdapter extends JarCacheAdapter {
   public List loadDynamicJars(String groupId, UrlType urlType) throws Exception {
      ArrayList items = new ArrayList();
      return urlType == UrlType.cluster ? this.notifyClusterNodes((String)groupId, (List)items) : this.pushJarsToClients(groupId, items);
   }

   private List notifyClusterNodes(String text, List items) throws Exception {
      for(UrlConfig urlConfig : (Iterable<UrlConfig>)(Iterable<?>)(UrlService.ins.load(UrlType.cluster, text).getList())) {
         String text2 = urlConfig.getUrl() + "/urule" + "/dynamic";
         text2 = text2 + "?" + HttpUtils.buildRequestValidator() + "&systemId=" + URLEncoder.encode(Utils.SystemId, "utf-8");
         HashMap valuesByKey = new HashMap();
         valuesByKey.put("name", urlConfig.getName());
         valuesByKey.put("url", urlConfig.getUrl());

         try {
            HttpUtils.sendPostRequest(text2, (byte[])null);
            valuesByKey.put("result", true);
         } catch (Exception exception) {
            valuesByKey.put("result", false);
            valuesByKey.put("error", this.buildExceptionStack(exception));
         }

         items.add(valuesByKey);
      }

      return items;
   }

   private List pushJarsToClients(String text, List items) throws Exception {
      DynamicSpringConfigLoader dynamicSpringConfigLoader = ServiceUtils.getDynamicSpringConfigLoader();
      byte[] bytes = dynamicSpringConfigLoader.zipDynamicJars();

      for(UrlConfig urlConfig : (Iterable<UrlConfig>)(Iterable<?>)(UrlService.ins.load(UrlType.client, text).getList())) {
         Map valuesByKey = this.pushJarsToClient(bytes, urlConfig);
         items.add(valuesByKey);
      }

      return items;
   }

   private Map pushJarsToClient(byte[] bytes, UrlConfig urlConfig) {
      String url = urlConfig.getUrl();
      String text = this.sendDynamicJars(bytes, url);
      HashMap valuesByKey = new HashMap();
      if (text != null) {
         valuesByKey.put("error", "<div style='color:red;word-wrap:break-word'>" + text + "</div>");
         valuesByKey.put("result", false);
      } else {
         valuesByKey.put("result", true);
      }

      valuesByKey.put("url", urlConfig.getUrl());
      valuesByKey.put("name", urlConfig.getName());
      return valuesByKey;
   }

   private String sendDynamicJars(byte[] bytes, String text) {
      Object objectValue = null;

      String text2;
      try {
         if (text.endsWith("/")) {
            text = text.substring(0, text.length() - 1);
         }

         String requestValidator = HttpUtils.buildRequestValidator();
         requestValidator = requestValidator + "&dynamicjars=true";
         String text3 = text + "/knowledgepackagereceiver" + "?" + requestValidator;
         String text4 = HttpUtils.sendPostRequest(text3, bytes);
         if (!text4.equals("ok")) {
            text2 = "<strong>推送操作成功到达客户端，但客户端出错错误：</strong><br>" + text4;
            return text2;
         }

         text2 = null;
      } catch (Exception exception) {
         String text5 = "<strong>服务端推送操作出现错误：</strong><br>" + this.buildExceptionStack(exception);
         return text5;
      } finally {
         if (objectValue != null) {
            ((HttpURLConnection)objectValue).disconnect();
         }

      }

      return text2;
   }
}
