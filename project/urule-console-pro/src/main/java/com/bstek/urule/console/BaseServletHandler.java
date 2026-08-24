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
   protected static final String DEFAULT_ENCODING = "utf-8";
   protected VelocityEngine velocityEngine;
   protected static final String PARAMETERS_ATTRIBUTE = "parameters";

   public void init() {
      this.velocityEngine = new VelocityEngine();
      this.velocityEngine.setProperty("resource.loader", "class");
      this.velocityEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
      this.velocityEngine.setProperty("runtime.log.logsystem", new NullLogChute());
      this.velocityEngine.init();
   }

   protected ObjectMapper createObjectMapper() {
      ObjectMapper objectMapper = ((JsonMapper.Builder)JsonMapper.builder().enable(new StreamWriteFeature[]{StreamWriteFeature.WRITE_BIGDECIMAL_AS_PLAIN})).build();
      objectMapper.setDateFormat(new SimpleDateFormat("yyy-MM-dd HH:mm:ss"));
      SimpleModule simpleModule = new SimpleModule();
      simpleModule.addSerializer(new BigDecimalJsonSerializer());
      objectMapper.registerModule(simpleModule);
      return objectMapper;
   }

   protected void renderPage(String templatePath, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
      httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
      String text = this.resolveRequestedMethod(httpServletRequest);
      if (text != null) {
         this.invokeHandlerMethod(text, httpServletRequest, httpServletResponse);
      } else {
         VelocityContext velocityContext = new VelocityContext();
         velocityContext.put("contextPath", httpServletRequest.getContextPath());
         httpServletResponse.setContentType("text/html");
         httpServletResponse.setCharacterEncoding("utf-8");
         Template template = this.velocityEngine.getTemplate("asserts/urule/html/" + templatePath, "utf-8");
         PrintWriter writer = httpServletResponse.getWriter();
         template.merge(velocityContext, writer);
         writer.close();
      }

   }

   protected void invokeHandlerMethod(String methodName, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
      Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
      method.invoke(this, httpServletRequest, httpServletResponse);
   }

   protected void writeObjectToJson(HttpServletResponse resp, Object obj) throws ServletException, IOException {
      resp.setContentType("text/json");
      resp.setCharacterEncoding("utf-8");
      JsonMapper.Builder builder = JsonMapper.builder();
      builder.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
      ObjectMapper objectMapper = builder.build();
      SimpleModule simpleModule = new SimpleModule();
      simpleModule.addSerializer(new BigDecimalJsonSerializer());
      objectMapper.registerModule(simpleModule);
      objectMapper.setSerializationInclusion(Include.NON_NULL);
      objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
      ServletOutputStream outputStream = resp.getOutputStream();

      try {
         objectMapper.writeValue(outputStream, obj);
      } finally {
         ((OutputStream)outputStream).flush();
         ((OutputStream)outputStream).close();
      }

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

   protected String resolveRequestedMethod(HttpServletRequest httpServletRequest) throws ServletException {
      String text = httpServletRequest.getContextPath() + "/urule";
      String requestURI = httpServletRequest.getRequestURI();
      String substring = requestURI.substring(text.length());
      int number = substring.indexOf("/", 0);
      if (number > -1) {
         String trimmedText = substring.substring(number + 1).trim();
         return trimmedText.length() > 0 ? trimmedText : null;
      } else {
         return null;
      }
   }

   protected void initializeRequestContext(HttpServletRequest httpServletRequest) {
      String parameter = httpServletRequest.getParameter("groupId");
      String parameter2 = httpServletRequest.getParameter("projectId");
      String contextPath = httpServletRequest.getContextPath();
      String requestURI = httpServletRequest.getRequestURI();
      if (requestURI.startsWith(contextPath)) {
         requestURI = requestURI.substring(contextPath.length());
      }

      boolean flag = !requestURI.startsWith("/urule/api/group/add") && !requestURI.startsWith("/urule/api/project/add");
      ContextHolder.clear();
      if (StringUtils.isNotBlank(parameter2)) {
         if (flag) {
            if (StringUtils.isBlank(parameter)) {
               Project project = ProjectManager.ins.get(Long.parseLong(parameter2));
               if (project == null) {
                  throw new InfoException("ProjectId invalid!");
               }

               parameter = project.getGroupId();
            }

            String loginUsername = SecurityUtils.getLoginUsername(httpServletRequest);
            User projectUser = UserManager.ins.getProjectUser(Long.parseLong(parameter2), loginUsername);
            if (projectUser == null) {
               throw new PermissionDeniedException("Permission denied for project [" + parameter2 + "]");
            }
         }

         ContextHolder.setProjectId(Long.parseLong(parameter2));
      }

      if (StringUtils.isNotBlank(parameter)) {
         if (flag) {
            String loginUsername2 = SecurityUtils.getLoginUsername(httpServletRequest);
            User groupUser = UserManager.ins.getGroupUser(parameter, loginUsername2);
            if (groupUser == null) {
               throw new PermissionDeniedException("Permission denied for team [" + parameter + "]");
            }
         }

         ContextHolder.setGroupId(parameter);
      }

   }
}
