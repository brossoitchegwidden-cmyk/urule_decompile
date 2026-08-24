package com.bstek.urule.debug;

import com.bstek.urule.SpringBootHome;
import com.bstek.urule.runtime.log.DataLog;
import com.bstek.urule.runtime.log.Log;
import com.bstek.urule.runtime.log.LogWriter;
import com.bstek.urule.runtime.log.UnitLog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class DefaultHtmlFileLogWriter implements LogWriter {
   private String outputDirectory;

   @Override
   public void write(List<Log> logs) throws IOException {
      if (!StringUtils.isBlank(this.outputDirectory)) {
         StringBuilder stringBuilder = new StringBuilder();
         this.appendLogsHtml(stringBuilder, logs);
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
         String text = this.outputDirectory + "/urule-debug-" + simpleDateFormat.format(new Date()) + ".html";
         StringBuilder stringBuilder2 = new StringBuilder();
         stringBuilder2.append("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>URule Pro调试日志信息</title><body style='font-size:12px'>");
         stringBuilder2.append(stringBuilder.toString());
         stringBuilder2.append("</body></html>");
         FileOutputStream fileOutputStream = new FileOutputStream(new File(text));
         IOUtils.write(stringBuilder2.toString(), fileOutputStream, "utf-8");
         fileOutputStream.flush();
         fileOutputStream.close();
      }
   }

   private void appendLogsHtml(StringBuilder stringBuilder, List<Log> logs) {
      for (Log log : logs) {
         if (log instanceof UnitLog) {
            stringBuilder.append("<div style=\"margin:8px;border:solid 1px black;border-radius:5px;padding:5px\">");
            UnitLog unitLog = (UnitLog)log;
            List logs2 = unitLog.getLogs();
            this.appendLogsHtml(stringBuilder, logs2);
            stringBuilder.append("</div>");
         } else if (log instanceof DataLog) {
            DataLog dataLog = (DataLog)log;
            String htmlMsg = dataLog.getHtmlMsg();
            stringBuilder.append(htmlMsg);
         }
      }
   }

   public void setPath(String text) {
      if (!StringUtils.isBlank(text)) {
         File file = new File(text);
         if (!file.exists()) {
            SpringBootHome springBootHome = new SpringBootHome();
            file = springBootHome.findSpringbootJarHomeDir(this.getClass());

            try {
               String absolutePath = file.getAbsolutePath();
               if (text.startsWith("/")) {
                  text = absolutePath + text;
               } else {
                  text = absolutePath + "/" + text;
               }

               file = new File(text);
               if (!file.exists()) {
                  file.mkdirs();
               }

               text = file.getCanonicalPath();
            } catch (IOException iOException) {
            }
         }

         this.outputDirectory = text;
      }
   }
}
