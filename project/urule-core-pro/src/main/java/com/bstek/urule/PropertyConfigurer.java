package com.bstek.urule;

import java.lang.reflect.Method;
import java.util.Properties;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.support.PropertiesLoaderSupport;

public class PropertyConfigurer implements ApplicationContextAware {
   private static Properties properties = new Properties();
   private static Boolean activeElseRuleEnabled;

   public static String getProperty(String key) {
      return PropertyConfigurer.properties.getProperty(key);
   }

   public static boolean isEnabledActiveElseRule() {
      if (activeElseRuleEnabled == null) {
         activeElseRuleEnabled = Boolean.valueOf(getProperty("urule.enabledActiveElseRule"));
      }

      return activeElseRuleEnabled;
   }

   public void setApplicationContext(ApplicationContext context) throws BeansException {
      for (PropertiesLoaderSupport propertiesLoaderSupport : context.getBeansOfType(PropertiesLoaderSupport.class).values()) {
         this.processPropertiesLoaderSupport(propertiesLoaderSupport);
      }
   }

   private void processPropertiesLoaderSupport(PropertiesLoaderSupport propertiesLoaderSupport) {
      try {
         Method declaredMethod = PropertiesLoaderSupport.class.getDeclaredMethod("mergeProperties");
         declaredMethod.setAccessible(true);
         Object objectValue = declaredMethod.invoke(propertiesLoaderSupport);
         PropertyConfigurer.properties.putAll((Properties)objectValue);
      } catch (Exception exception) {
         throw new RuntimeException(exception);
      }
   }
}
