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
   private static final Log a = LogFactory.getLog(DefaultClusterPacketCacheAdapter.class);
   private ClientPacketCacheAdapter b = null;

   private ClientPacketCacheAdapter a() {
      if (this.b == null) {
         try {
            this.b = (ClientPacketCacheAdapter)Utils.getApplicationContext().getBean("urule.clientPacketCacheAdapter");
         } catch (NoSuchBeanDefinitionException var2) {
            this.b = new DefaultClientPacketCacheAdapter();
         }
      }

      return this.b;
   }

   public List removeProject(String var1, long var2, List var4) {
      ArrayList var5 = new ArrayList();

      for(PacketConfig var7 : (Iterable<PacketConfig>)(Iterable<?>)(var4)) {
         this.a().disableClientsPacket(var1, var7.getId());
      }

      for(UrlConfig var8 : (Iterable<UrlConfig>)(Iterable<?>)(UrlService.ins.load(UrlType.cluster, var1).getList())) {
         String var9 = var8.getUrl() + "/urule" + "/dynamic" + "/syncPacketForRemoveProject";
         HashMap var10 = new HashMap();
         var10.put("name", var8.getName());
         var10.put("url", var8.getUrl());

         try {
            var9 = var9 + "?" + HttpUtils.buildRequestValidator() + "&systemId=" + URLEncoder.encode(Utils.SystemId, "utf-8") + "&projectId=" + var2;
            a.debug("Sync project packet package to cluster node, groupId:" + var1 + ", url:" + var9);
            String var11 = HttpUtils.sendPostRequest(var9, (byte[])null);
            if (!"ok".equals(var11)) {
               throw new RuleException("Sync project packet package to cluster node error, url:" + var9);
            }

            a.debug("Sync success!");
            var10.put("result", true);
         } catch (Exception var12) {
            var10.put("result", false);
            var10.put("error", ExceptionUtils.buildExceptionStack(var12));
         }

         var5.add(var10);
      }

      return var5;
   }

   public List recacheAllPackets(String var1) {
      List var2 = UrlService.ins.load(UrlType.cluster, var1).getList();
      ArrayList var3 = new ArrayList();

      try {
         for(UrlConfig var5 : (Iterable<UrlConfig>)(Iterable<?>)(var2)) {
            String var6 = var5.getUrl() + "/urule" + "/dynamic" + "/recacheAllPackets";
            var6 = var6 + "?" + HttpUtils.buildRequestValidator() + "&systemId=" + URLEncoder.encode(Utils.SystemId, "utf-8");
            HashMap var7 = new HashMap();
            var7.put("name", var5.getName());
            var7.put("url", var5.getUrl());

            try {
               HttpUtils.sendPostRequest(var6, (byte[])null);
               var7.put("result", true);
            } catch (Exception var9) {
               var7.put("result", false);
               var7.put("error", this.a(var9));
            }

            var3.add(var7);
         }

         return var3;
      } catch (Exception var10) {
         var10.printStackTrace();
         throw new InfoException("集群知识包同步recacheAllPackets出错:" + var10.getMessage());
      }
   }

   public List refreshPacket(String var1, long var2) {
      List var4 = UrlService.ins.load(UrlType.cluster, var1).getList();
      ArrayList var5 = new ArrayList();

      for(UrlConfig var7 : (Iterable<UrlConfig>)(Iterable<?>)(var4)) {
         String var8 = var7.getUrl() + "/urule" + "/dynamic" + "/syncPacket";
         HashMap var9 = new HashMap();
         var9.put("name", var7.getName());
         var9.put("url", var7.getUrl());

         try {
            var8 = var8 + "?" + HttpUtils.buildRequestValidator() + "&systemId=" + URLEncoder.encode(Utils.SystemId, "utf-8") + "&id=" + var2;
            a.debug("Sync packet package to cluster node, groupId:" + var1 + ", url:" + var8);
            String var10 = HttpUtils.sendPostRequest(var8, (byte[])null);
            if (!"ok".equals(var10)) {
               throw new RuleException("Sync packet package to cluster node error, url:" + var8);
            }

            a.debug("Sync success!");
            var9.put("result", true);
         } catch (Exception var11) {
            var9.put("result", false);
            var9.put("error", ExceptionUtils.buildExceptionStack(var11));
         }

         var5.add(var9);
      }

      return var5;
   }

   protected String a(Throwable var1) {
      StringBuilder var2 = new StringBuilder();
      ByteArrayOutputStream var3 = new ByteArrayOutputStream();
      PrintStream var4 = new PrintStream(var3);
      var1.printStackTrace(var4);
      String var5 = new String(var3.toByteArray());
      IOUtils.closeQuietly(var4);
      IOUtils.closeQuietly(var3);
      var5 = var5.replaceAll("\n", "<br>");
      if (var2.length() > 0) {
         var2.append("<br>");
      }

      var2.append(var5);
      return var2.toString();
   }

   public void putPacket(long var1, PacketData var3) {
   }

   public void putPacket(String var1, PacketData var2) {
   }

   public void remove(long var1) {
   }

   public void remove(String var1) {
   }
}
