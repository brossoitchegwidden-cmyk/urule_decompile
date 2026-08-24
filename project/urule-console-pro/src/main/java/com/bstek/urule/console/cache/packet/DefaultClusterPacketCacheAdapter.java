package com.bstek.urule.console.cache.packet;

import com.bstek.urule.Utils;
import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.database.model.UrlConfig;
import com.bstek.urule.console.database.model.UrlType;
import com.bstek.urule.console.database.service.url.UrlService;
import com.bstek.urule.console.util.HttpUtils;
import com.bstek.urule.exception.RuleException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public class DefaultClusterPacketCacheAdapter implements ClusterPacketCacheAdapter {
   private static final Log logger = LogFactory.getLog(DefaultClusterPacketCacheAdapter.class);
   private ClientPacketCacheAdapter defaultClientPacketCacheAdapter = null;

   private ClientPacketCacheAdapter resolveClientPacketCacheAdapter() {
      if (this.defaultClientPacketCacheAdapter == null) {
         try {
            this.defaultClientPacketCacheAdapter = (ClientPacketCacheAdapter)Utils.getApplicationContext().getBean("urule.clientPacketCacheAdapter");
         } catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {
            this.defaultClientPacketCacheAdapter = new DefaultClientPacketCacheAdapter();
         }
      }

      return this.defaultClientPacketCacheAdapter;
   }
   public List removeProject(String groupId, long projectId, List list) {
      ArrayList removeProjectResult = new ArrayList();

      for(PacketConfig packetConfig : (Iterable<PacketConfig>)(Iterable<?>)(list)) {
         this.resolveClientPacketCacheAdapter().disableClientsPacket(groupId, packetConfig.getId());
      }

      for(UrlConfig urlConfig : (Iterable<UrlConfig>)(Iterable<?>)(UrlService.ins.load(UrlType.cluster, groupId).getList())) {
         String text = urlConfig.getUrl() + "/urule" + "/dynamic" + "/syncPacketForRemoveProject";
         HashMap valuesByKey = new HashMap();
         valuesByKey.put("name", urlConfig.getName());
         valuesByKey.put("url", urlConfig.getUrl());

         try {
            text = text + "?" + HttpUtils.buildRequestValidator() + "&systemId=" + URLEncoder.encode(Utils.SystemId, "utf-8") + "&projectId=" + projectId;
            DefaultClusterPacketCacheAdapter.logger.debug("Sync project packet package to cluster node, groupId:" + groupId + ", url:" + text);
            String text2 = HttpUtils.sendPostRequest(text, (byte[])null);
            if (!"ok".equals(text2)) {
               throw new RuleException("Sync project packet package to cluster node error, url:" + text);
            }

            DefaultClusterPacketCacheAdapter.logger.debug("Sync success!");
            valuesByKey.put("result", true);
         } catch (Exception exception) {
            valuesByKey.put("result", false);
            valuesByKey.put("error", ExceptionUtils.buildExceptionStack(exception));
         }

         removeProjectResult.add(valuesByKey);
      }

      return removeProjectResult;
   }
   public List recacheAllPackets(String groupId) {
      List list = UrlService.ins.load(UrlType.cluster, groupId).getList();
      ArrayList recacheAllPacketsResult = new ArrayList();

      try {
         for(UrlConfig urlConfig : (Iterable<UrlConfig>)(Iterable<?>)(list)) {
            String text = urlConfig.getUrl() + "/urule" + "/dynamic" + "/recacheAllPackets";
            text = text + "?" + HttpUtils.buildRequestValidator() + "&systemId=" + URLEncoder.encode(Utils.SystemId, "utf-8");
            HashMap valuesByKey = new HashMap();
            valuesByKey.put("name", urlConfig.getName());
            valuesByKey.put("url", urlConfig.getUrl());

            try {
               HttpUtils.sendPostRequest(text, (byte[])null);
               valuesByKey.put("result", true);
            } catch (Exception exception) {
               valuesByKey.put("result", false);
               valuesByKey.put("error", this.buildExceptionStack(exception));
            }

            recacheAllPacketsResult.add(valuesByKey);
         }

         return recacheAllPacketsResult;
      } catch (Exception exception2) {
         java.util.logging.Logger.getLogger(DefaultClusterPacketCacheAdapter.class.getName()).log(java.util.logging.Level.SEVERE, exception2.getMessage(), exception2);
         throw new InfoException("集群知识包同步recacheAllPackets出错:" + exception2.getMessage());
      }
   }
   public List refreshPacket(String groupId, long packetId) {
      List list = UrlService.ins.load(UrlType.cluster, groupId).getList();
      ArrayList refreshPacketResult = new ArrayList();

      for(UrlConfig urlConfig : (Iterable<UrlConfig>)(Iterable<?>)(list)) {
         String text = urlConfig.getUrl() + "/urule" + "/dynamic" + "/syncPacket";
         HashMap valuesByKey = new HashMap();
         valuesByKey.put("name", urlConfig.getName());
         valuesByKey.put("url", urlConfig.getUrl());

         try {
            text = text + "?" + HttpUtils.buildRequestValidator() + "&systemId=" + URLEncoder.encode(Utils.SystemId, "utf-8") + "&id=" + packetId;
            DefaultClusterPacketCacheAdapter.logger.debug("Sync packet package to cluster node, groupId:" + groupId + ", url:" + text);
            String text2 = HttpUtils.sendPostRequest(text, (byte[])null);
            if (!"ok".equals(text2)) {
               throw new RuleException("Sync packet package to cluster node error, url:" + text);
            }

            DefaultClusterPacketCacheAdapter.logger.debug("Sync success!");
            valuesByKey.put("result", true);
         } catch (Exception exception) {
            valuesByKey.put("result", false);
            valuesByKey.put("error", ExceptionUtils.buildExceptionStack(exception));
         }

         refreshPacketResult.add(valuesByKey);
      }

      return refreshPacketResult;
   }

   protected String buildExceptionStack(Throwable throwable) {
      StringBuilder stringBuilder = new StringBuilder();
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      PrintStream printStream = new PrintStream(byteArrayOutputStream);
      throwable.printStackTrace(printStream);
      String string = new String(byteArrayOutputStream.toByteArray());
      IOUtils.closeQuietly(printStream);
      IOUtils.closeQuietly(byteArrayOutputStream);
      string = string.replaceAll("\n", "<br>");
      if (stringBuilder.length() > 0) {
         stringBuilder.append("<br>");
      }

      stringBuilder.append(string);
      return stringBuilder.toString();
   }
   public void putPacket(long id, PacketData pd) {
   }
   public void putPacket(String code, PacketData pd) {
   }
   public void remove(long id) {
   }
   public void remove(String code) {
   }
}
