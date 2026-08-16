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
   public List loadDynamicJars(String var1, UrlType var2) throws Exception {
      ArrayList var3 = new ArrayList();
      return var2 == UrlType.cluster ? this.a((String)var1, (List)var3) : this.b(var1, var3);
   }

   private List a(String var1, List var2) throws Exception {
      for(UrlConfig var5 : (Iterable<UrlConfig>)(Iterable<?>)(UrlService.ins.load(UrlType.cluster, var1).getList())) {
         String var6 = var5.getUrl() + "/urule" + "/dynamic";
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

         var2.add(var7);
      }

      return var2;
   }

   private List b(String var1, List var2) throws Exception {
      DynamicSpringConfigLoader var3 = ServiceUtils.getDynamicSpringConfigLoader();
      byte[] var4 = var3.zipDynamicJars();

      for(UrlConfig var7 : (Iterable<UrlConfig>)(Iterable<?>)(UrlService.ins.load(UrlType.client, var1).getList())) {
         Map var8 = this.a(var4, var7);
         var2.add(var8);
      }

      return var2;
   }

   private Map a(byte[] var1, UrlConfig var2) {
      String var3 = var2.getUrl();
      String var4 = this.a(var1, var3);
      HashMap var5 = new HashMap();
      if (var4 != null) {
         var5.put("error", "<div style='color:red;word-wrap:break-word'>" + var4 + "</div>");
         var5.put("result", false);
      } else {
         var5.put("result", true);
      }

      var5.put("url", var2.getUrl());
      var5.put("name", var2.getName());
      return var5;
   }

   private String a(byte[] var1, String var2) {
      Object var3 = null;

      String var7;
      try {
         if (var2.endsWith("/")) {
            var2 = var2.substring(0, var2.length() - 1);
         }

         String var4 = HttpUtils.buildRequestValidator();
         var4 = var4 + "&dynamicjars=true";
         String var14 = var2 + "/knowledgepackagereceiver" + "?" + var4;
         String var6 = HttpUtils.sendPostRequest(var14, var1);
         if (!var6.equals("ok")) {
            var7 = "<strong>推送操作成功到达客户端，但客户端出错错误：</strong><br>" + var6;
            return var7;
         }

         var7 = null;
      } catch (Exception var11) {
         String var5 = "<strong>服务端推送操作出现错误：</strong><br>" + this.a(var11);
         return var5;
      } finally {
         if (var3 != null) {
            ((HttpURLConnection)var3).disconnect();
         }

      }

      return var7;
   }
}
