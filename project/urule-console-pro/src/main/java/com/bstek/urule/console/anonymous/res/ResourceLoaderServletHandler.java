package com.bstek.urule.console.anonymous.res;

import com.bstek.urule.console.anonymous.AnonymousServletHandler;
import com.bstek.urule.exception.RuleException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

public class ResourceLoaderServletHandler extends AnonymousServletHandler {
   public static final String RES_PREFIX = "/res";

   public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      String text = req.getContextPath() + "/urule" + "/res";
      String requestURI = req.getRequestURI();
      String substring = requestURI.substring(text.length() + 1);
      if (substring.endsWith(".js")) {
         resp.setContentType("text/javascript");
      } else if (substring.endsWith(".css")) {
         resp.setContentType("text/css");
      } else if (substring.endsWith(".png")) {
         resp.setContentType("image/png");
      } else if (substring.endsWith(".jpg")) {
         resp.setContentType("image/jpeg");
      } else if (substring.endsWith(".svg")) {
         resp.setContentType("image/svg+xml");
      } else {
         resp.setContentType("application/octet-stream");
      }

      long dateHeader = req.getDateHeader("If-Modified-Since");
      long lastModified = 0L;

      try {
         URL resource = this.getClass().getClassLoader().getResource(substring);
         lastModified = resource.openConnection().getLastModified();
      } catch (Exception exception) {
      }

      if (lastModified != 0L && dateHeader != 0L && Math.abs(lastModified - dateHeader) < 1000L) {
         resp.setStatus(304);
      } else {
         resp.addDateHeader("Last-Modified", lastModified);
         resp.addHeader("Cache-Control", "no-cache");
         InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(substring);
         if (resourceAsStream == null) {
            throw new RuleException("Resource【" + substring + "】not exist！");
         }

         ServletOutputStream outputStream = resp.getOutputStream();

         try {
            IOUtils.copy(resourceAsStream, outputStream);
         } finally {
            IOUtils.closeQuietly(resourceAsStream);
            IOUtils.closeQuietly(outputStream);
         }
      }

   }

   public void init() {
   }

   public String url() {
      return "/res";
   }
}
