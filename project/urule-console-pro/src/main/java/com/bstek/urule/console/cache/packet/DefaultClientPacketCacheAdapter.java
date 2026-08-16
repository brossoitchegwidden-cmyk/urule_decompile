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
   private static final Log a = LogFactory.getLog(DefaultClientPacketCacheAdapter.class);

   public List enableClientsPacket(String var1, long var2) {
      return this.a(var1, var2, true);
   }

   public List disableClientsPacket(String var1, long var2) {
      return this.a(var1, var2, false);
   }

   private List a(String var1, long var2, boolean var4) {
      ArrayList var5 = new ArrayList();

      for(UrlConfig var8 : (Iterable<UrlConfig>)(Iterable<?>)(UrlService.ins.load(UrlType.client, var1).getList())) {
         HashMap var9 = new HashMap();
         var9.put("name", var8.getName());
         var9.put("url", var8.getUrl());

         try {
            this.a(var2, var4, var8.getUrl());
            var9.put("result", true);
         } catch (Exception var11) {
            var9.put("result", false);
            var9.put("error", ExceptionUtils.buildExceptionStack(var11));
         }

         var5.add(var9);
      }

      return var5;
   }

   private void a(long var1, boolean var3, String var4) {
      if (var4.endsWith("/")) {
         var4 = var4.substring(0, var4.length() - 1);
      }

      try {
         String var5 = "packageId=" + var1 + "&enable=" + var3 + "&" + HttpUtils.buildRequestValidator();
         String var6 = var4 + "/knowledgepackagereceiver" + "?" + var5;
         a.debug("Sync packet package to client, clientUrl:" + var6);
         String var7 = HttpUtils.sendPostRequest(var6, (byte[])null);
         if (!"ok".equals(var7)) {
            throw new RuleException("Sync packet package to client error, clientUrl:" + var6);
         } else {
            a.debug("Sync success!");
         }
      } catch (Exception var8) {
         throw new RuleException(var8);
      }
   }

   public List pushPacketToClients(String var1, PacketData var2) {
      KnowledgePackageWrapper var3 = var2.getKnowledgePackageWrapper();
      KnowledgePackageImpl var4 = (KnowledgePackageImpl)var3.getKnowledgePackage();
      String var5 = Utils.knowledgePackageToString(var4);
      byte[] var6 = Utils.compress(var5);
      List var7 = UrlService.ins.load(UrlType.client, var1).getList();
      ArrayList var8 = new ArrayList();

      for(UrlConfig var10 : (Iterable<UrlConfig>)(Iterable<?>)(var7)) {
         Map var11 = this.a(var2.getPacket(), var6, var10);
         var8.add(var11);
      }

      return var8;
   }

   private Map a(PacketConfig var1, byte[] var2, UrlConfig var3) {
      String var4 = var3.getUrl();
      String var5 = this.a(var1, var2, var4);
      HashMap var6 = new HashMap();
      if (var5 != null) {
         var6.put("error", "<div style='color:red;word-wrap:break-word'>" + var5 + "</div>");
         var6.put("result", false);
      } else {
         var6.put("result", true);
      }

      var6.put("url", var3.getUrl());
      var6.put("name", var3.getName());
      return var6;
   }

   private String a(PacketConfig var1, byte[] var2, String var3) {
      try {
         if (var3.endsWith("/")) {
            var3 = var3.substring(0, var3.length() - 1);
         }

         long var4 = var1.getId();
         String var6 = var1.getCode();
         String var7 = "packageId=" + var4 + "&code=" + var6 + "&" + HttpUtils.buildRequestValidator();
         String var8 = var3 + "/knowledgepackagereceiver" + "?" + var7;
         String var9 = HttpUtils.sendPostRequest(var8, var2);
         return var9.equals("ok") ? null : "<strong>推送操作成功到达客户端，但客户端出错错误：</strong><br>" + var9;
      } catch (Exception var10) {
         return "<strong>服务端推送操作出现错误：</strong><br>" + ExceptionUtils.buildExceptionStack(var10);
      }
   }
}
