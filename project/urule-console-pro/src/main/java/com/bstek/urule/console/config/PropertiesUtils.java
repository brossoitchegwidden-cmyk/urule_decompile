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
   public static Properties loadConfigFile(String var0) {
      if (StringUtils.isEmpty(var0)) {
         return null;
      } else {
         Object var1 = null;

         try {
            var1 = new FileInputStream(var0);
         } catch (FileNotFoundException var10) {
            var1 = Configure.class.getClassLoader().getResourceAsStream(var0);
         }

         if (var1 == null) {
            return null;
         } else {
            Properties var3;
            try {
               Properties var2 = new Properties();
               var2.load((InputStream)var1);
               var3 = var2;
            } catch (IOException var8) {
               throw new RuleException(var8);
            } finally {
               IOUtils.closeQuietly((InputStream)var1);
            }

            return var3;
         }
      }
   }

   public static void writeConfigFile(String var0, Properties var1) {
      try {
         FileOutputStream var2 = new FileOutputStream(var0);
         OutputStreamWriter var3 = new OutputStreamWriter(var2, "utf-8");
         var1.store(var3, "系统自动生成的配置文件");
         var2.close();
      } catch (IOException var4) {
         var4.printStackTrace();
      }

   }
}
