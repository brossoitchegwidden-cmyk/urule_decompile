package com.bstek.urule.console.cache.packet;

import com.bstek.urule.Utils;
import com.bstek.urule.console.database.model.UrlConfig;
import com.bstek.urule.console.database.model.UrlType;
import com.bstek.urule.console.database.service.url.UrlService;
import com.bstek.urule.console.util.HttpUtils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.runtime.KnowledgePackageImpl;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultClientPacketCacheAdapter implements ClientPacketCacheAdapter {
   private static final Log logger = LogFactory.getLog(DefaultClientPacketCacheAdapter.class);
   public List enableClientsPacket(String groupId, long packetId) {
      return this.setPacketEnabledOnClients(groupId, packetId, true);
   }
   public List disableClientsPacket(String groupId, long packetId) {
      return this.setPacketEnabledOnClients(groupId, packetId, false);
   }

   private List setPacketEnabledOnClients(String text, long longValue, boolean flag) {
      ArrayList items = new ArrayList();

      for(UrlConfig urlConfig : (Iterable<UrlConfig>)(Iterable<?>)(UrlService.ins.load(UrlType.client, text).getList())) {
         HashMap valuesByKey = new HashMap();
         valuesByKey.put("name", urlConfig.getName());
         valuesByKey.put("url", urlConfig.getUrl());

         try {
            this.setPacketEnabledOnClient(longValue, flag, urlConfig.getUrl());
            valuesByKey.put("result", true);
         } catch (Exception exception) {
            valuesByKey.put("result", false);
            valuesByKey.put("error", ExceptionUtils.buildExceptionStack(exception));
         }

         items.add(valuesByKey);
      }

      return items;
   }

   private void setPacketEnabledOnClient(long longValue, boolean flag, String text) {
      if (text.endsWith("/")) {
         text = text.substring(0, text.length() - 1);
      }

      try {
         String text2 = "packageId=" + longValue + "&enable=" + flag + "&" + HttpUtils.buildRequestValidator();
         String text3 = text + "/knowledgepackagereceiver" + "?" + text2;
         DefaultClientPacketCacheAdapter.logger.debug("Sync packet package to client, clientUrl:" + text3);
         String text4 = HttpUtils.sendPostRequest(text3, (byte[])null);
         if (!"ok".equals(text4)) {
            throw new RuleException("Sync packet package to client error, clientUrl:" + text3);
         } else {
            DefaultClientPacketCacheAdapter.logger.debug("Sync success!");
         }
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }
   public List pushPacketToClients(String groupId, PacketData packetData) {
      KnowledgePackageWrapper knowledgePackageWrapper = packetData.getKnowledgePackageWrapper();
      KnowledgePackageImpl knowledgePackage = (KnowledgePackageImpl)knowledgePackageWrapper.getKnowledgePackage();
      String text = Utils.knowledgePackageToString(knowledgePackage);
      byte[] bytes = Utils.compress(text);
      List list = UrlService.ins.load(UrlType.client, groupId).getList();
      ArrayList pushPacketToClientsResult = new ArrayList();

      for(UrlConfig urlConfig : (Iterable<UrlConfig>)(Iterable<?>)(list)) {
         Map valuesByKey = this.pushPacketToClient(packetData.getPacket(), bytes, urlConfig);
         pushPacketToClientsResult.add(valuesByKey);
      }

      return pushPacketToClientsResult;
   }

   private Map pushPacketToClient(PacketConfig packetConfig, byte[] bytes, UrlConfig urlConfig) {
      String url = urlConfig.getUrl();
      String text = this.sendPacket(packetConfig, bytes, url);
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

   private String sendPacket(PacketConfig packetConfig, byte[] bytes, String text) {
      try {
         if (text.endsWith("/")) {
            text = text.substring(0, text.length() - 1);
         }

         long id = packetConfig.getId();
         String code = packetConfig.getCode();
         String text2 = "packageId=" + id + "&code=" + code + "&" + HttpUtils.buildRequestValidator();
         String text3 = text + "/knowledgepackagereceiver" + "?" + text2;
         String text4 = HttpUtils.sendPostRequest(text3, bytes);
         return text4.equals("ok") ? null : "<strong>推送操作成功到达客户端，但客户端出错错误：</strong><br>" + text4;
      } catch (Exception exception) {
         return "<strong>服务端推送操作出现错误：</strong><br>" + ExceptionUtils.buildExceptionStack(exception);
      }
   }
}
