package com.bstek.urule.console.editor.json;

import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.exception.RuleException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

public class JsonServletHandler extends ApiServletHandler {
   public void convert(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("json");
      if (StringUtils.isBlank(var3)) {
         throw new RuleException("JSON不能为空！");
      } else {
         var3 = var3.trim();
         StringBuilder var4 = new StringBuilder();

         try {
            ObjectMapper var5 = JsonMapper.builder().build();
            if (var3.startsWith("[") && var3.endsWith("]")) {
               List var10 = (List)var5.readValue(var3, ArrayList.class);
               var4.append(this.a(var10));
            } else {
               if (!var3.startsWith("{") || !var3.endsWith("}")) {
                  throw new RuleException("不支持的JSON格式");
               }

               Map var6 = (Map)var5.readValue(var3, HashMap.class);
               var4.append(this.a(var6));
            }
         } catch (Exception var7) {
            throw new RuleException("不支持的JSON格式");
         }

         HashMap var9 = new HashMap();
         var9.put("json", var4.toString());
         this.a(var2, var9);
      }
   }

   public void gzip(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("json");
      if (StringUtils.isBlank(var3)) {
         throw new RuleException("JSON不能为空！");
      } else {
         var3 = var3.trim();
         HashMap var4 = new HashMap();
         var4.put("data", this.doGzip(var3));
         this.a(var2, var4);
      }
   }

   public String doGzip(String var1) throws Exception {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      GZIPOutputStream var3 = new GZIPOutputStream(var2);
      var3.write(var1.getBytes("utf-8"));
      var3.close();
      Base64 var4 = new Base64();
      return var4.encodeAsString(var2.toByteArray());
   }

   private String a(Map var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("{");
      var2.append("\"name\"");
      var2.append(":");
      var2.append("\"待填写的变量分类名\"");
      var2.append(",");
      var2.append("\"fields\"");
      var2.append(":");
      var2.append("{");
      int var3 = 0;

      for(String var5 : (Iterable<String>)(Iterable<?>)(var1.keySet())) {
         Object var6 = var1.get(var5);
         if (var6 != null) {
            if (var3 > 0) {
               var2.append(",");
            }

            var2.append("\"" + var5 + "\"");
            var2.append(":");
            if (var6 instanceof Map) {
               var2.append(this.a((Map)var6));
            } else if (var6 instanceof List) {
               var2.append(this.a((List)var6));
            } else if (var6 instanceof Number) {
               var2.append(var6);
            } else {
               var2.append("\"" + var6 + "\"");
            }

            ++var3;
         }
      }

      var2.append("}");
      var2.append("}");
      return var2.toString();
   }

   private String a(List var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("[");
      int var3 = 0;

      for(Map var5 : (Iterable<Map>)(Iterable<?>)(var1)) {
         if (var3 > 0) {
            var2.append(",");
         }

         var2.append(this.a(var5));
         ++var3;
      }

      var2.append("]");
      return var2.toString();
   }

   public String url() {
      return "/json";
   }
}
