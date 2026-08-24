package com.bstek.urule.console.config;

import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.io.IOUtils;

public class PlaceholderConfigurer {
   private String defaultConfigResource = "com/bstek/urule/core/configure/config.properties";
   private String overrideConfigResource = "urule.properties";
   private Properties prop;

   public PlaceholderConfigurer() {
      this.prop = this.resolveProperties(this.defaultConfigResource);
      Properties properties = this.resolveProperties(this.overrideConfigResource);
      if (properties != null) {
         this.prop.putAll(properties);
      }

   }

   public Properties getProp() {
      return this.prop;
   }

   private Properties resolveProperties(String text) {
      if (StringUtils.isEmpty(text)) {
         return null;
      } else {
         InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(text);
         if (resourceAsStream == null) {
            return null;
         } else {
            Properties properties;
            try {
               Properties properties2 = new Properties();
               properties2.load(resourceAsStream);
               properties = properties2;
            } catch (IOException iOException) {
               throw new RuleException(iOException);
            } finally {
               IOUtils.closeQuietly(resourceAsStream);
            }

            return properties;
         }
      }
   }
}
