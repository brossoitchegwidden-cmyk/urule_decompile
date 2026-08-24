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
   public static final UploadFile uploadFile(HttpServletRequest req) throws Exception {
      DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
      ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
      servletFileUpload.setHeaderEncoding("UTF-8");

      for(FileItem fileItem : servletFileUpload.parseRequest(req)) {
         String fieldName = fileItem.getFieldName();
         if (fieldName.equals("file")) {
            fileItem.getFieldName();
            return new UploadFile(fileItem.getName(), fileItem.getInputStream());
         }
      }

      return null;
   }

   public static final void downloadFile(String fileName, InputStream input, HttpServletResponse resp) throws Exception {
      resp.setContentType("application/octet-stream;charset=ISO8859-1");
      fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
      resp.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
      ServletOutputStream outputStream = resp.getOutputStream();

      try {
         IOUtils.copy(input, outputStream);
      } finally {
         ((OutputStream)outputStream).flush();
         ((OutputStream)outputStream).close();
      }

   }

   public static String formatXml(String content) {
      if (StringUtils.isBlank(content)) {
         return content;
      } else {
         String xml = null;

         try {
            Document text = DocumentHelper.parseText(content);
            OutputFormat prettyPrint = OutputFormat.createPrettyPrint();
            StringWriter stringWriter = new StringWriter();
            XMLWriter xMLWriter = new XMLWriter(stringWriter, prettyPrint);
            xMLWriter.write(text);
            xml = stringWriter.toString();
         } catch (Exception exception) {
            xml = content;
         }

         return xml;
      }
   }

   /**累加版本号*/
   public static final String getMaxVersion(String version) {
      String maxVersion = "1.0.0";
      if (StringUtils.isBlank(version)) {
         return maxVersion;
      } else {
         try {
            String[] parts = version.split("\\.");
            String text = "";

            for(String text2 : parts) {
               text = text + text2;
            }

            int number = Integer.parseInt(text);
            ++number;
            String maxVersion2 = "";
            String text3 = Integer.toString(number);
            maxVersion2 = text3.substring(0, text3.length() - 2) + "." + text3.substring(text3.length() - 2, text3.length() - 1) + "." + text3.substring(text3.length() - 1, text3.length());
            if (maxVersion2.endsWith(".")) {
               maxVersion2 = maxVersion2.substring(0, maxVersion2.length() - 1);
            }

            return maxVersion2;
         } catch (Exception exception) {
            return maxVersion;
         }
      }
   }
}
