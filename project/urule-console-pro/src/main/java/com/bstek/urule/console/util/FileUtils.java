package com.bstek.urule.console.util;

import com.bstek.urule.console.xml.DocumentHelper;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class FileUtils {
   public static final UploadFile uploadFile(HttpServletRequest var0) throws Exception {
      DiskFileItemFactory var1 = new DiskFileItemFactory();
      ServletFileUpload var2 = new ServletFileUpload(var1);
      var2.setHeaderEncoding("UTF-8");

      for(FileItem var5 : var2.parseRequest(var0)) {
         String var6 = var5.getFieldName();
         if (var6.equals("file")) {
            var5.getFieldName();
            return new UploadFile(var5.getName(), var5.getInputStream());
         }
      }

      return null;
   }

   public static final void downloadFile(String var0, InputStream var1, HttpServletResponse var2) throws Exception {
      var2.setContentType("application/octet-stream;charset=ISO8859-1");
      var0 = new String(var0.getBytes("UTF-8"), "ISO8859-1");
      var2.setHeader("Content-Disposition", "attachment;filename=\"" + var0 + "\"");
      ServletOutputStream var3 = var2.getOutputStream();

      try {
         IOUtils.copy(var1, var3);
      } finally {
         ((OutputStream)var3).flush();
         ((OutputStream)var3).close();
      }

   }

   public static String formatXml(String var0) {
      if (StringUtils.isBlank(var0)) {
         return var0;
      } else {
         String var1 = null;

         try {
            Document var2 = DocumentHelper.parseText(var0);
            OutputFormat var3 = OutputFormat.createPrettyPrint();
            StringWriter var4 = new StringWriter();
            XMLWriter var5 = new XMLWriter(var4, var3);
            var5.write(var2);
            var1 = var4.toString();
         } catch (Exception var6) {
            var1 = var0;
         }

         return var1;
      }
   }

   public static final String getMaxVersion(String var0) {
      String var1 = "1.0.0";
      if (StringUtils.isBlank(var0)) {
         return var1;
      } else {
         try {
            String[] var2 = var0.split("\\.");
            String var3 = "";

            for(String var7 : var2) {
               var3 = var3 + var7;
            }

            int var9 = Integer.parseInt(var3);
            ++var9;
            String var11 = "";
            String var13 = Integer.toString(var9);
            var11 = var13.substring(0, var13.length() - 2) + "." + var13.substring(var13.length() - 2, var13.length() - 1) + "." + var13.substring(var13.length() - 1, var13.length());
            if (var11.endsWith(".")) {
               var11 = var11.substring(0, var11.length() - 1);
            }

            return var11;
         } catch (Exception var8) {
            return var1;
         }
      }
   }
}
