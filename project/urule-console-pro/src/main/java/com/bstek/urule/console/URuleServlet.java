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
   private static final long serialVersionUID = -532678553551034556L;
   private PageServletHandler pageServletHandler;
   private Map apiHandlersByPath = new HashMap();
   private Map anonymousHandlersByPath = new HashMap();

   public void init(ServletConfig config) throws ServletException {
      for(ServletHandler servletHandler : ServiceLoader.load(ServletHandler.class)) {
         servletHandler.init();
         String text = servletHandler.url();
         if (servletHandler instanceof ApiServletHandler && ((ApiServletHandler)servletHandler).useApiPrefix()) {
            text = "/api" + text;
         }

         if (this.apiHandlersByPath.containsKey(text) || this.anonymousHandlersByPath.containsKey(text)) {
            throw new RuntimeException("Handler [" + text + "] not unique.");
         }

         if (servletHandler instanceof ApiServletHandler) {
            this.apiHandlersByPath.put(text, servletHandler);
         } else {
            if (!(servletHandler instanceof AnonymousServletHandler)) {
               throw new RuleException("Unsupport ServletHandler :" + text);
            }

            this.anonymousHandlersByPath.put(text, servletHandler);
         }
      }

      ServletHandler licenseServletHandler = new com.bstek.urule.console.admin.license.LicenseServletHandler();
      licenseServletHandler.init();
      String text2 = licenseServletHandler.url();
      if (this.apiHandlersByPath.containsKey(text2) || this.anonymousHandlersByPath.containsKey(text2)) {
         throw new RuntimeException("Handler [" + text2 + "] not unique.");
      }
      this.apiHandlersByPath.put(text2, licenseServletHandler);

      this.pageServletHandler = new PageServletHandler();
      this.pageServletHandler.init();
   }

   protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      HttpServletResponseWrapper httpServletResponseWrapper = new HttpServletResponseWrapper(response);
      String requestURI = request.getRequestURI();
      String text = request.getContextPath() + "/urule";
      String substring = requestURI.startsWith(text) ? requestURI.substring(text.length()) : requestURI;
      if (substring.length() < 1) {
         RequestHolder.setRequest(request);
         RequestHolder.clean();
      } else {
         Object servletHandler = null;
         if (!substring.startsWith("/api") && !substring.startsWith("/rest")) {
            int number = substring.indexOf("/");
            if (number == 0) {
               String substring2 = substring.substring(1, substring.length());
               number = substring2.indexOf("/") + 1;
            }

            if (number > 0) {
               substring = substring.substring(0, number);
            }
         } else {
            int number2 = substring.lastIndexOf("/");
            substring = substring.substring(0, number2);
         }

         if (this.apiHandlersByPath.containsKey(substring)) {
            servletHandler = (ServletHandler)this.apiHandlersByPath.get(substring);
         } else if (this.anonymousHandlersByPath.containsKey(substring)) {
            servletHandler = (ServletHandler)this.anonymousHandlersByPath.get(substring);
         } else {
            servletHandler = this.pageServletHandler;
         }

         if (BootstrapManager.get().needCheckBootstrapped(requestURI) && !BootstrapManager.get().isBootstrapped()) {
            httpServletResponseWrapper.sendRedirect(text + "/setup");
         } else {
            RequestHolder.setRequest(request);
            this.doServletHandler(request, httpServletResponseWrapper, (ServletHandler)servletHandler);
         }
      }
   }

   public void doServletHandler(HttpServletRequest req, HttpServletResponse resp, ServletHandler targetHandler) throws IOException, ServletException {
      try {
         try {
            targetHandler.execute(req, resp);
            return;
         } catch (Exception exception) {
            StringBuilder stringBuilder = new StringBuilder();
            Throwable throwable = this.resolveThrowable(exception, stringBuilder);
            if (!(throwable instanceof RuleDueException)) {
               resp.setCharacterEncoding("UTF-8");
               String name = NullPointerException.class.getName();
               if (!(throwable instanceof NullPointerException)) {
                  name = throwable.getMessage();
               }

               if (name == null) {
                  name = NullPointerException.class.getName();
               }

               resp.addHeader("errorMsg", URLEncoder.encode(name, "utf-8"));
               resp.setStatus(500);
               String errorMsg = this.getErrorMsg(throwable);
               if (throwable instanceof PermissionDeniedException) {
                  resp.setContentType("text/html;charset=utf-8");
                  resp.setCharacterEncoding("utf-8");
                  PrintWriter writer = resp.getWriter();
                  writer.write("<h2>" + errorMsg + "</h2>");
                  writer.flush();
                  writer.close();
                  return;
               }

               HashMap valuesByKey = new HashMap();
               String text = this.formatStackTrace(throwable, stringBuilder);
               valuesByKey.put("errorMsg", errorMsg);
               valuesByKey.put("stack", text);
               ObjectMapper objectMapper = JsonMapper.builder().build();
               ServletOutputStream outputStream = resp.getOutputStream();

               try {
                  objectMapper.writeValue(outputStream, valuesByKey);
               } finally {
                  ((OutputStream)outputStream).flush();
                  ((OutputStream)outputStream).close();
               }

               if (!(throwable instanceof RuleException) && !(throwable instanceof InfoException)) {
                  java.util.logging.Logger.getLogger(URuleServlet.class.getName()).log(java.util.logging.Level.SEVERE, throwable.getMessage(), throwable);
               }

               return;
            }
         }

         resp.setStatus(888);
      } finally {
         ParsePhaseHolder.cleanParsePhase();
         RequestHolder.clean();
      }

   }

   /**获取异常消息*/
   protected String getErrorMsg(Throwable throwable) {
      String message = throwable.getMessage();
      if (message == null || message.contentEquals("")) {
         message = throwable.getClass().getName();
      }

      return message;
   }

   private String formatStackTrace(Throwable throwable, StringBuilder stringBuilder) {
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

   private Throwable resolveThrowable(Throwable throwable, StringBuilder stringBuilder) {
      if (throwable instanceof RuleAssertException) {
         RuleAssertException ruleAssertException = (RuleAssertException)throwable;
         String tipMsg = ruleAssertException.getTipMsg();
         if (tipMsg != null) {
            stringBuilder.append(tipMsg);
         }
      }

      return throwable.getCause() != null ? this.resolveThrowable(throwable.getCause(), stringBuilder) : throwable;
   }
}
