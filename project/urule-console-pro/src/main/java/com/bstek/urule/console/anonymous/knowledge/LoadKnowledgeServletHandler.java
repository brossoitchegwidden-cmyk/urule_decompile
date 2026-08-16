package com.bstek.urule.console.anonymous.knowledge;

import com.bstek.urule.Configure;
import com.bstek.urule.Utils;
import com.bstek.urule.console.anonymous.AnonymousServletHandler;
import com.bstek.urule.console.anonymous.ValidateUtils;
import com.bstek.urule.console.cache.packet.PacketCache;
import com.bstek.urule.console.cache.packet.PacketData;
import com.bstek.urule.console.util.ServiceUtils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.bstek.urule.runtime.RemoteDynamicJarsBuilder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;

public class LoadKnowledgeServletHandler extends AnonymousServletHandler {
   public static final String URL = "/loadknowledge";
   private RemoteDynamicJarsBuilder a = ServiceUtils.getRemoteDynamicJarsBuilder();

   public void execute(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      if (ValidateUtils.validateUserPwd(var1, this.a.getUser(), this.a.getPwd())) {
         String var3 = var1.getParameter("packageId");
         if (StringUtils.isEmpty(var3)) {
            String var16 = "<h1>PackageId can not be null<h1>";
            this.a(var2, var16);
         } else {
            var3 = Utils.decodeURL(var3);
            String var4 = var1.getParameter("timestamp");
            PacketData var5 = PacketCache.ins.getPacket(var3);
            if (var5 == null) {
               long var6 = 0L;

               try {
                  var6 = Long.valueOf(var3);
               } catch (NumberFormatException var14) {
                  throw new RuleException("Package [" + var3 + "] not exist");
               }

               var5 = PacketCache.ins.getPacket(var6);
            }

            if (var5 == null) {
               throw new RuleException("Package [" + var3 + "] not exist");
            } else {
               KnowledgePackageWrapper var18 = var5.getKnowledgePackageWrapper();
               KnowledgePackage var7 = var18.getKnowledgePackage();
               boolean var8 = false;
               String var9 = var1.getParameter("debug");
               if (var9 != null && var9.equals("true") && Utils.isDebug()) {
                  var8 = true;
               }

               if (StringUtils.isNotEmpty(var4)) {
                  long var10 = Long.valueOf(var4);
                  long var12 = var7.getTimestamp();
                  if (var12 > var10) {
                     if (var8) {
                        this.a(var2, (Object)var18);
                     } else {
                        this.a(var2, var5);
                     }
                  }
               } else if (var8) {
                  this.a(var2, (Object)var18);
               } else {
                  this.a(var2, var5);
               }

            }
         }
      }
   }

   private void a(HttpServletResponse var1, String var2) throws IOException {
      var1.setContentType("text/html");
      PrintWriter var3 = var1.getWriter();
      var3.write("<html>");
      var3.write("<header>");
      var3.write("</header>");
      var3.write("<body>");
      var3.write(var2);
      var3.write("</body>");
      var3.write("</html>");
      var3.flush();
      var3.close();
   }

   private void a(HttpServletResponse var1, PacketData var2) throws IOException {
      var1.setContentType("text/json");
      var1.setCharacterEncoding("UTF-8");
      byte[] var3 = PacketCache.ins.getKnowledgeContent(var2.getPacket().getId());
      if (var3 == null) {
         JsonMapper.Builder var4 = JsonMapper.builder();
         var4.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
         ObjectMapper var5 = var4.build();
         var5.setSerializationInclusion(Include.NON_NULL);
         var5.setDateFormat(new SimpleDateFormat(Configure.getDateFormat()));
         String var6 = var5.writeValueAsString(var2.getKnowledgePackageWrapper());
         var3 = Utils.compress(var6);
      }

      ServletOutputStream var7 = var1.getOutputStream();
      ((OutputStream)var7).write(var3);
      ((OutputStream)var7).flush();
      ((OutputStream)var7).close();
   }

   public String url() {
      return "/loadknowledge";
   }
}
