package com.bstek.urule.console.config;

import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;
import org.apache.commons.io.IOUtils;

public class PropertiesUtils {
   public static Properties loadConfigFile(String file) {
      if (StringUtils.isEmpty(file)) {
         return null;
      } else {
         Object objectValue = null;

         try {
            objectValue = new FileInputStream(file);
         } catch (FileNotFoundException fileNotFoundException) {
            objectValue = Configure.class.getClassLoader().getResourceAsStream(file);
         }

         if (objectValue == null) {
            return null;
         } else {
            Properties properties;
            try {
               Properties properties2 = new Properties();
               properties2.load((InputStream)objectValue);
               properties = properties2;
            } catch (IOException iOException) {
               throw new RuleException(iOException);
            } finally {
               IOUtils.closeQuietly((InputStream)objectValue);
            }

            return properties;
         }
      }
   }

   public static void writeConfigFile(String file, Properties properties) {
      try {
         FileOutputStream fileOutputStream = new FileOutputStream(file);
         OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "utf-8");
         properties.store(outputStreamWriter, "系统自动生成的配置文件");
         fileOutputStream.close();
      } catch (IOException iOException) {
         java.util.logging.Logger.getLogger(PropertiesUtils.class.getName()).log(java.util.logging.Level.SEVERE, iOException.getMessage(), iOException);
      }

   }
}
