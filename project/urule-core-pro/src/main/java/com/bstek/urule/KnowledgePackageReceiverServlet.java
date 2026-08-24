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
   private static final long serialVersionUID = -4342175088856372588L;
   public static final String URL = "/knowledgepackagereceiver";
   private DynamicSpringConfigLoader dynamicSpringConfigLoader;
   private RemoteDynamicJarsBuilder remoteDynamicJarsBuilder;
   private Logger logger = Logger.getGlobal();

   public void init(ServletConfig config) throws ServletException {
      super.init(config);
      WebApplicationContext requiredWebApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
      this.dynamicSpringConfigLoader = (DynamicSpringConfigLoader)requiredWebApplicationContext.getBean("urule.dynamicSpringConfigLoader");
      this.remoteDynamicJarsBuilder = (RemoteDynamicJarsBuilder)requiredWebApplicationContext.getBean("urule.remoteDynamicJarsBuilder");
   }

   public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      String parameter = req.getParameter("_u");
      String parameter2 = req.getParameter("_p");
      if (parameter != null && parameter2 != null) {
         parameter = URLDecoder.decode(parameter, "utf-8");
         parameter2 = URLDecoder.decode(parameter2, "utf-8");
         if (parameter.equals(this.remoteDynamicJarsBuilder.getUser()) && parameter2.equals(this.remoteDynamicJarsBuilder.getPwd())) {
            String parameter3 = req.getParameter("dynamicjars");
            String substring = req.getParameter("packageId");
            String parameter4 = req.getParameter("code");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (StringUtils.isNotBlank(substring)) {
               String parameter5 = req.getParameter("enable");
               String version = null;

               try {
                  substring = URLDecoder.decode(substring, "utf-8");
                  if (substring.startsWith("/")) {
                     substring = substring.substring(1, substring.length());
                  }

                  if (StringUtils.isNotBlank(parameter5)) {
                     boolean flag = Boolean.valueOf(parameter5);
                     CacheUtils.getKnowledgeCache().enable(substring, flag);
                     if (StringUtils.isNotBlank(parameter4)) {
                        CacheUtils.getKnowledgeCache().enable(parameter4, flag);
                     }
                  } else {
                     ServletInputStream inputStream = req.getInputStream();
                     byte[] bytes = IOUtils.toByteArray(inputStream);
                     inputStream.close();
                     String text = Utils.uncompress(bytes);
                     if (text != null) {
                        KnowledgePackageImpl knowledgePackageImpl = (KnowledgePackageImpl)Utils.stringToKnowledgePackage(text);
                        knowledgePackageImpl.setPackageInfo(substring);
                        version = knowledgePackageImpl.getVersion();
                        CacheUtils.getKnowledgeCache().putKnowledge(substring, knowledgePackageImpl);
                        if (StringUtils.isNotBlank(parameter4)) {
                           CacheUtils.getKnowledgeCache().putKnowledge(parameter4, knowledgePackageImpl);
                        }
                     }
                  }
               } catch (Exception exception) {
                  java.util.logging.Logger.getLogger(KnowledgePackageReceiverServlet.class.getName()).log(java.util.logging.Level.SEVERE, exception.getMessage(), exception);
                  String text2 = this.formatStackTrace(exception);
                  this.writePlainTextResponse(resp, text2);
                  return;
               }

               if (StringUtils.isNotBlank(parameter5)) {
                  if (Boolean.valueOf(parameter5)) {
                     System.out
                        .println(
                           "["
                              + simpleDateFormat.format(new Date())
                              + "] Successfully receive the server side to enable package:"
                              + substring
                              + (StringUtils.isBlank(parameter4) ? "" : "(" + parameter4 + ")")
                        );
                  } else {
                     System.out
                        .println(
                           "["
                              + simpleDateFormat.format(new Date())
                              + "] Successfully receive the server side to disable package:"
                              + substring
                              + (StringUtils.isBlank(parameter4) ? "" : "(" + parameter4 + ")")
                        );
                  }
               } else {
                  String text3 = "["
                     + simpleDateFormat.format(new Date())
                     + "] Successfully receive the server side to pushed package:"
                     + substring
                     + (StringUtils.isBlank(parameter4) ? "" : "(" + parameter4 + ")");
                  if (StringUtils.isNotBlank(version)) {
                     text3 = text3 + "(" + version + ")";
                  }

                  System.out.println(text3);
               }
            } else if (parameter3 != null && parameter3.equals("true")) {
               try {
                  String dynamicJarsStoreDirectPath = this.dynamicSpringConfigLoader.buildDynamicJarsStoreDirectPath();
                  ServletInputStream inputStream2 = req.getInputStream();
                  this.remoteDynamicJarsBuilder.unzipDynamicJars(inputStream2, dynamicJarsStoreDirectPath);
                  IOUtils.closeQuietly(inputStream2);
                  System.out.println("[" + simpleDateFormat.format(new Date()) + "] Successfully receive the server side to pushed dynamic jars");
                  this.dynamicSpringConfigLoader.loadDynamicJars(dynamicJarsStoreDirectPath);
               } catch (Exception exception2) {
                  java.util.logging.Logger.getLogger(KnowledgePackageReceiverServlet.class.getName()).log(java.util.logging.Level.SEVERE, exception2.getMessage(), exception2);
                  String text4 = this.formatStackTrace(exception2);
                  this.writePlainTextResponse(resp, text4);
                  return;
               }
            }

            this.writePlainTextResponse(resp, "ok");
         } else {
            this.writePlainTextResponse(resp, "User or password is invalid.");
            this.logger.warning("User or password is invalid.");
         }
      } else {
         this.writePlainTextResponse(resp, "User and password can not be null.");
         this.logger.warning("User and password can not be null.");
      }
   }

   private void writePlainTextResponse(HttpServletResponse httpServletResponse, String text) throws ServletException, IOException {
      httpServletResponse.setContentType("text/plain");
      PrintWriter writer = httpServletResponse.getWriter();
      writer.write(text);
      writer.flush();
      writer.close();
   }

   private String formatStackTrace(Throwable throwable) {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      PrintStream printStream = new PrintStream(byteArrayOutputStream);
      throwable.printStackTrace(printStream);
      String string = new String(byteArrayOutputStream.toByteArray());
      IOUtils.closeQuietly(printStream);
      IOUtils.closeQuietly(byteArrayOutputStream);
      return string.replaceAll("\n", "<br>");
   }
}
