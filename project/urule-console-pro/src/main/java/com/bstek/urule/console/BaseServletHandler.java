package com.bstek.urule.console;

import com.bstek.urule.BigDecimalJsonSerializer;
import com.bstek.urule.console.database.manager.project.ProjectManager;
import com.bstek.urule.console.database.manager.user.UserManager;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.User;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.StreamWriteFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.log.NullLogChute;

public abstract class BaseServletHandler implements ServletHandler {
   protected static final String b = "utf-8";
   protected VelocityEngine c;
   protected final String d = "parameters";

   public void init() {
      this.c = new VelocityEngine();
      this.c.setProperty("resource.loader", "class");
      this.c.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
      this.c.setProperty("runtime.log.logsystem", new NullLogChute());
      this.c.init();
   }

   protected ObjectMapper a() {
      ObjectMapper var1 = ((JsonMapper.Builder)JsonMapper.builder().enable(new StreamWriteFeature[]{StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN})).build();
      var1.setDateFormat(new SimpleDateFormat("yyy-MM-dd HH:mm:ss"));
      SimpleModule var2 = new SimpleModule();
      var2.addSerializer(new BigDecimalJsonSerializer());
      var1.registerModule(var2);
      return var1;
   }

   protected void b(String var1, HttpServletRequest var2, HttpServletResponse var3) throws Exception {
      var3.setHeader("Access-Control-Allow-Origin", "*");
      String var4 = this.a(var2);
      if (var4 != null) {
         this.c(var4, var2, var3);
      } else {
         VelocityContext var5 = new VelocityContext();
         var5.put("contextPath", var2.getContextPath());
         var3.setContentType("text/html");
         var3.setCharacterEncoding("utf-8");
         Template var6 = this.c.getTemplate("asserts/urule/html/" + var1, "utf-8");
         PrintWriter var7 = var3.getWriter();
         var6.merge(var5, var7);
         var7.close();
      }

   }

   protected void c(String var1, HttpServletRequest var2, HttpServletResponse var3) throws Exception {
      Method var4 = this.getClass().getMethod(var1, HttpServletRequest.class, HttpServletResponse.class);
      var4.invoke(this, var2, var3);
   }

   protected void a(HttpServletResponse var1, Object var2) throws ServletException, IOException {
      var1.setContentType("text/json");
      var1.setCharacterEncoding("utf-8");
      JsonMapper.Builder var3 = JsonMapper.builder();
      var3.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
      ObjectMapper var4 = var3.build();
      SimpleModule var5 = new SimpleModule();
      var5.addSerializer(new BigDecimalJsonSerializer());
      var4.registerModule(var5);
      var4.setSerializationInclusion(Include.NON_NULL);
      var4.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
      ServletOutputStream var6 = var1.getOutputStream();

      try {
         var4.writeValue(var6, var2);
      } finally {
         ((OutputStream)var6).flush();
         ((OutputStream)var6).close();
      }

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

   protected String a(HttpServletRequest var1) throws ServletException {
      String var2 = var1.getContextPath() + "/urule";
      String var3 = var1.getRequestURI();
      String var4 = var3.substring(var2.length());
      int var5 = var4.indexOf("/", 0);
      if (var5 > -1) {
         String var6 = var4.substring(var5 + 1).trim();
         return var6.length() > 0 ? var6 : null;
      } else {
         return null;
      }
   }

   protected void b(HttpServletRequest var1) {
      String var2 = var1.getParameter("groupId");
      String var3 = var1.getParameter("projectId");
      String var4 = var1.getContextPath();
      String var5 = var1.getRequestURI();
      if (var5.startsWith(var4)) {
         var5 = var5.substring(var4.length());
      }

      boolean var6 = !var5.startsWith("/urule/api/group/add") && !var5.startsWith("/urule/api/project/add");
      ContextHolder.clear();
      if (StringUtils.isNotBlank(var3)) {
         if (var6) {
            if (StringUtils.isBlank(var2)) {
               Project var7 = ProjectManager.ins.get(Long.parseLong(var3));
               if (var7 == null) {
                  throw new InfoException("ProjectId invalid!");
               }

               var2 = var7.getGroupId();
            }

            String var9 = SecurityUtils.getLoginUsername(var1);
            User var8 = UserManager.ins.getProjectUser(Long.parseLong(var3), var9);
            if (var8 == null) {
               throw new PermissionDeniedException("Permission denied for project [" + var3 + "]");
            }
         }

         ContextHolder.setProjectId(Long.parseLong(var3));
      }

      if (StringUtils.isNotBlank(var2)) {
         if (var6) {
            String var10 = SecurityUtils.getLoginUsername(var1);
            User var11 = UserManager.ins.getGroupUser(var2, var10);
            if (var11 == null) {
               throw new PermissionDeniedException("Permission denied for team [" + var2 + "]");
            }
         }

         ContextHolder.setGroupId(var2);
      }

   }
}
