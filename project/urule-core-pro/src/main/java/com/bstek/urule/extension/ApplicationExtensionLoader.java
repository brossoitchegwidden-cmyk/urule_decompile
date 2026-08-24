package com.bstek.urule.extension;

import com.bstek.urule.exception.RuleException;
import java.util.Iterator;
import java.util.ServiceLoader;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationExtensionLoader implements ApplicationContextAware {
   private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(ApplicationExtensionLoader.class.getName());

   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      ServiceLoader serviceLoader = ServiceLoader.load(ApplicationExtension.class);
      Iterator iterator = serviceLoader.iterator();

      try {
         while (iterator.hasNext()) {
            ApplicationExtension applicationExtension = (ApplicationExtension)iterator.next();
            LOGGER.info("加载扩展[" + applicationExtension.name() + "]");
            applicationExtension.loadExtension(applicationContext);
         }
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }
}
