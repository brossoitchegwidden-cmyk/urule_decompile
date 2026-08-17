package com.bstek.urule.console;

import com.bstek.urule.builder.ParsePhaseHolder;
import com.bstek.urule.console.anonymous.AnonymousServletHandler;
import com.bstek.urule.console.config.bootstrap.BootstrapManager;
import com.bstek.urule.exception.RuleAssertException;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.rete.RuleDueException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

public class URuleServlet extends HttpServlet {
   private static final long a = -532678553551034556L;
   private PageServletHandler b;
   private Map c = new HashMap();
   private Map d = new HashMap();

   public void init(ServletConfig var1) throws ServletException {
      for(ServletHandler var4 : ServiceLoader.load(ServletHandler.class)) {
         var4.init();
         String var5 = var4.url();
         if (var4 instanceof ApiServletHandler && ((ApiServletHandler)var4).useApiPrefix()) {
            var5 = "/api" + var5;
         }

         if (this.c.containsKey(var5) || this.d.containsKey(var5)) {
            throw new RuntimeException("Handler [" + var5 + "] not unique.");
         }

         if (var4 instanceof ApiServletHandler) {
            this.c.put(var5, var4);
         } else {
            if (!(var4 instanceof AnonymousServletHandler)) {
               throw new RuleException("Unsupport ServletHandler :" + var5);
            }

            this.d.put(var5, var4);
         }
      }

      ServletHandler var6 = new com.bstek.urule.console.admin.license.LicenseServletHandler();
      var6.init();
      String var7 = var6.url();
      if (this.c.containsKey(var7) || this.d.containsKey(var7)) {
         throw new RuntimeException("Handler [" + var7 + "] not unique.");
      }
      this.c.put(var7, var6);

      this.b = new PageServletHandler();
      this.b.init();
   }

   protected void service(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      HttpServletResponseWrapper var4 = new HttpServletResponseWrapper(var2);
      String var5 = var1.getRequestURI();
      String var6 = var1.getContextPath() + "/urule";
      String var7 = var5.startsWith(var6) ? var5.substring(var6.length()) : var5;
      if (var7.length() < 1) {
         RequestHolder.setRequest(var1);
         RequestHolder.clean();
      } else {
         Object var8 = null;
         if (!var7.startsWith("/api") && !var7.startsWith("/rest")) {
            int var12 = var7.indexOf("/");
            if (var12 == 0) {
               String var10 = var7.substring(1, var7.length());
               var12 = var10.indexOf("/") + 1;
            }

            if (var12 > 0) {
               var7 = var7.substring(0, var12);
            }
         } else {
            int var9 = var7.lastIndexOf("/");
            var7 = var7.substring(0, var9);
         }

         if (this.c.containsKey(var7)) {
            var8 = (ServletHandler)this.c.get(var7);
         } else if (this.d.containsKey(var7)) {
            var8 = (ServletHandler)this.d.get(var7);
         } else {
            var8 = this.b;
         }

         if (BootstrapManager.get().needCheckBootstrapped(var5) && !BootstrapManager.get().isBootstrapped()) {
            var4.sendRedirect(var6 + "/setup");
         } else {
            RequestHolder.setRequest(var1);
            this.doServletHandler(var1, var4, (ServletHandler)var8);
         }
      }
   }

   public void doServletHandler(HttpServletRequest var1, HttpServletResponse var2, ServletHandler var3) throws IOException, ServletException {
      try {
         try {
            var3.execute(var1, var2);
            return;
         } catch (Exception var22) {
            StringBuilder var5 = new StringBuilder();
            Throwable var6 = this.b(var22, var5);
            if (!(var6 instanceof RuleDueException)) {
               var2.setCharacterEncoding("UTF-8");
               String var7 = NullPointerException.class.getName();
               if (!(var6 instanceof NullPointerException)) {
                  var7 = var6.getMessage();
               }

               if (var7 == null) {
                  var7 = NullPointerException.class.getName();
               }

               var2.addHeader("errorMsg", URLEncoder.encode(var7, "utf-8"));
               var2.setStatus(500);
               String var8 = this.getErrorMsg(var6);
               if (var6 instanceof PermissionDeniedException) {
                  var2.setContentType("text/html;charset=utf-8");
                  var2.setCharacterEncoding("utf-8");
                  PrintWriter var24 = var2.getWriter();
                  var24.write("<h2>" + var8 + "</h2>");
                  var24.flush();
                  var24.close();
                  return;
               }

               HashMap var9 = new HashMap();
               String var10 = this.a(var6, var5);
               var9.put("errorMsg", var8);
               var9.put("stack", var10);
               ObjectMapper var11 = JsonMapper.builder().build();
               ServletOutputStream var12 = var2.getOutputStream();

               try {
                  var11.writeValue(var12, var9);
               } finally {
                  ((OutputStream)var12).flush();
                  ((OutputStream)var12).close();
               }

               if (!(var6 instanceof RuleException) && !(var6 instanceof InfoException)) {
                  var6.printStackTrace();
               }

               return;
            }
         }

         var2.setStatus(888);
      } finally {
         ParsePhaseHolder.cleanParsePhase();
         RequestHolder.clean();
      }

   }

   protected String getErrorMsg(Throwable var1) {
      String var2 = var1.getMessage();
      if (var2 == null || var2.contentEquals("")) {
         var2 = var1.getClass().getName();
      }

      return var2;
   }

   private String a(Throwable var1, StringBuilder var2) {
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

   private Throwable b(Throwable var1, StringBuilder var2) {
      if (var1 instanceof RuleAssertException) {
         RuleAssertException var3 = (RuleAssertException)var1;
         String var4 = var3.getTipMsg();
         if (var4 != null) {
            var2.append(var4);
         }
      }

      return var1.getCause() != null ? this.b(var1.getCause(), var2) : var1;
   }
}
