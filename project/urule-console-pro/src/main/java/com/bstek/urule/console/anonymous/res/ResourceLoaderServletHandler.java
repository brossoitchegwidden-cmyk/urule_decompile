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

   public void execute(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      String var3 = var1.getContextPath() + "/urule" + "/res";
      String var4 = var1.getRequestURI();
      String var5 = var4.substring(var3.length() + 1);
      if (var5.endsWith(".js")) {
         var2.setContentType("text/javascript");
      } else if (var5.endsWith(".css")) {
         var2.setContentType("text/css");
      } else if (var5.endsWith(".png")) {
         var2.setContentType("image/png");
      } else if (var5.endsWith(".jpg")) {
         var2.setContentType("image/jpeg");
      } else if (var5.endsWith(".svg")) {
         var2.setContentType("image/svg+xml");
      } else {
         var2.setContentType("application/octet-stream");
      }

      long var6 = var1.getDateHeader("If-Modified-Since");
      long var8 = 0L;

      try {
         URL var10 = this.getClass().getClassLoader().getResource(var5);
         var8 = var10.openConnection().getLastModified();
      } catch (Exception var16) {
      }

      if (var8 != 0L && var6 != 0L && Math.abs(var8 - var6) < 1000L) {
         var2.setStatus(304);
      } else {
         var2.addDateHeader("Last-Modified", var8);
         var2.addHeader("Cache-Control", "no-cache");
         InputStream var17 = this.getClass().getClassLoader().getResourceAsStream(var5);
         if (var17 == null) {
            throw new RuleException("Resource【" + var5 + "】not exist！");
         }

         ServletOutputStream var11 = var2.getOutputStream();

         try {
            IOUtils.copy(var17, var11);
         } finally {
            IOUtils.closeQuietly(var17);
            IOUtils.closeQuietly(var11);
         }
      }

   }

   public void init() {
   }

   public String url() {
      return "/res";
   }
}
