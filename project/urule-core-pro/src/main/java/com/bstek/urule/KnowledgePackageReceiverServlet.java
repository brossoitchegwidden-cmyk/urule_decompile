package com.bstek.urule;

import com.bstek.urule.runtime.DynamicSpringConfigLoader;
import com.bstek.urule.runtime.KnowledgePackageImpl;
import com.bstek.urule.runtime.RemoteDynamicJarsBuilder;
import com.bstek.urule.runtime.cache.CacheUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class KnowledgePackageReceiverServlet extends HttpServlet {
   private static final long a = -4342175088856372588L;
   public static final String URL = "/knowledgepackagereceiver";
   private DynamicSpringConfigLoader b;
   private RemoteDynamicJarsBuilder c;
   private Logger d = Logger.getGlobal();

   public void init(ServletConfig var1) throws ServletException {
      super.init(var1);
      WebApplicationContext var2 = WebApplicationContextUtils.getRequiredWebApplicationContext(var1.getServletContext());
      this.b = (DynamicSpringConfigLoader)var2.getBean("urule.dynamicSpringConfigLoader");
      this.c = (RemoteDynamicJarsBuilder)var2.getBean("urule.remoteDynamicJarsBuilder");
   }

   public void doPost(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      String var3 = var1.getParameter("_u");
      String var4 = var1.getParameter("_p");
      if (var3 != null && var4 != null) {
         var3 = URLDecoder.decode(var3, "utf-8");
         var4 = URLDecoder.decode(var4, "utf-8");
         if (var3.equals(this.c.getUser()) && var4.equals(this.c.getPwd())) {
            String var5 = var1.getParameter("dynamicjars");
            String var6 = var1.getParameter("packageId");
            String var7 = var1.getParameter("code");
            SimpleDateFormat var8 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (StringUtils.isNotBlank(var6)) {
               String var9 = var1.getParameter("enable");
               String var10 = null;

               try {
                  var6 = URLDecoder.decode(var6, "utf-8");
                  if (var6.startsWith("/")) {
                     var6 = var6.substring(1, var6.length());
                  }

                  if (StringUtils.isNotBlank(var9)) {
                     boolean var11 = Boolean.valueOf(var9);
                     CacheUtils.getKnowledgeCache().enable(var6, var11);
                     if (StringUtils.isNotBlank(var7)) {
                        CacheUtils.getKnowledgeCache().enable(var7, var11);
                     }
                  } else {
                     ServletInputStream var23 = var1.getInputStream();
                     byte[] var25 = IOUtils.toByteArray(var23);
                     var23.close();
                     String var13 = Utils.uncompress(var25);
                     if (var13 != null) {
                        KnowledgePackageImpl var14 = (KnowledgePackageImpl)Utils.stringToKnowledgePackage(var13);
                        var14.setPackageInfo(var6);
                        var10 = var14.getVersion();
                        CacheUtils.getKnowledgeCache().putKnowledge(var6, var14);
                        if (StringUtils.isNotBlank(var7)) {
                           CacheUtils.getKnowledgeCache().putKnowledge(var7, var14);
                        }
                     }
                  }
               } catch (Exception var16) {
                  var16.printStackTrace();
                  String var12 = this.a(var16);
                  this.a(var2, var12);
                  return;
               }

               if (StringUtils.isNotBlank(var9)) {
                  if (Boolean.valueOf(var9)) {
                     System.out
                        .println(
                           "["
                              + var8.format(new Date())
                              + "] Successfully receive the server side to enable package:"
                              + var6
                              + (StringUtils.isBlank(var7) ? "" : "(" + var7 + ")")
                        );
                  } else {
                     System.out
                        .println(
                           "["
                              + var8.format(new Date())
                              + "] Successfully receive the server side to disable package:"
                              + var6
                              + (StringUtils.isBlank(var7) ? "" : "(" + var7 + ")")
                        );
                  }
               } else {
                  String var24 = "["
                     + var8.format(new Date())
                     + "] Successfully receive the server side to pushed package:"
                     + var6
                     + (StringUtils.isBlank(var7) ? "" : "(" + var7 + ")");
                  if (StringUtils.isNotBlank(var10)) {
                     var24 = var24 + "(" + var10 + ")";
                  }

                  System.out.println(var24);
               }
            } else if (var5 != null && var5.equals("true")) {
               try {
                  String var20 = this.b.buildDynamicJarsStoreDirectPath();
                  ServletInputStream var22 = var1.getInputStream();
                  this.c.unzipDynamicJars(var22, var20);
                  IOUtils.closeQuietly(var22);
                  System.out.println("[" + var8.format(new Date()) + "] Successfully receive the server side to pushed dynamic jars");
                  this.b.loadDynamicJars(var20);
               } catch (Exception var15) {
                  var15.printStackTrace();
                  String var21 = this.a(var15);
                  this.a(var2, var21);
                  return;
               }
            }

            this.a(var2, "ok");
         } else {
            this.a(var2, "User or password is invalid.");
            this.d.warning("User or password is invalid.");
         }
      } else {
         this.a(var2, "User and password can not be null.");
         this.d.warning("User and password can not be null.");
      }
   }

   private void a(HttpServletResponse var1, String var2) throws ServletException, IOException {
      var1.setContentType("text/plain");
      PrintWriter var3 = var1.getWriter();
      var3.write(var2);
      var3.flush();
      var3.close();
   }

   private String a(Throwable var1) {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      PrintStream var3 = new PrintStream(var2);
      var1.printStackTrace(var3);
      String var4 = new String(var2.toByteArray());
      IOUtils.closeQuietly(var3);
      IOUtils.closeQuietly(var2);
      return var4.replaceAll("\n", "<br>");
   }
}
