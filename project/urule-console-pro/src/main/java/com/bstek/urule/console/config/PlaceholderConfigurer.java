package com.bstek.urule.console.config;

import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.io.IOUtils;

public class PlaceholderConfigurer {
   private String a = "com/bstek/urule/core/configure/config.properties";
   private String b = "urule.properties";
   private Properties c;

   public PlaceholderConfigurer() {
      this.c = this.a(this.a);
      Properties var1 = this.a(this.b);
      if (var1 != null) {
         this.c.putAll(var1);
      }

   }

   public Properties getProp() {
      return this.c;
   }

   private Properties a(String var1) {
      if (StringUtils.isEmpty(var1)) {
         return null;
      } else {
         InputStream var2 = this.getClass().getClassLoader().getResourceAsStream(var1);
         if (var2 == null) {
            return null;
         } else {
            Properties var4;
            try {
               Properties var3 = new Properties();
               var3.load(var2);
               var4 = var3;
            } catch (IOException var8) {
               throw new RuleException(var8);
            } finally {
               IOUtils.closeQuietly(var2);
            }

            return var4;
         }
      }
   }
}
