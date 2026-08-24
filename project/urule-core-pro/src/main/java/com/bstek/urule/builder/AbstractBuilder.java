package com.bstek.urule.builder;

import com.bstek.urule.builder.resource.ResourceBuilder;
import com.bstek.urule.builder.resource.ResourceProvider;
import com.bstek.urule.exception.RuleException;
import java.util.Collection;
import java.util.Iterator;
import java.util.ServiceLoader;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public abstract class AbstractBuilder implements ApplicationContextAware {
   private ResourceProvider resourceProvider;
   protected ApplicationContext applicationContext;
   protected Collection<ResourceBuilder> resourceBuilders;

   public ResourceBase newResourceBase() {
      return new ResourceBase(this.resolveResourceProvider());
   }

   protected Element parseResource(String content) {
      try {
         Document text = DocumentHelper.parseText(content);
         return text.getRootElement();
      } catch (DocumentException documentException) {
         throw new RuleException(documentException);
      }
   }

   private ResourceProvider resolveResourceProvider() {
      if (this.resourceProvider == null) {
         ServiceLoader serviceLoader = ServiceLoader.load(ResourceProvider.class);
         Iterator iterator = serviceLoader.iterator();
         if (iterator.hasNext()) {
            ResourceProvider resourceProvider = (ResourceProvider)iterator.next();
            this.resourceProvider = resourceProvider;
         }
      }

      return this.resourceProvider;
   }

   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      this.resourceBuilders = applicationContext.getBeansOfType(ResourceBuilder.class).values();
      this.applicationContext = applicationContext;
      applicationContext.getBeansWithAnnotation(SuppressWarnings.class);
   }
}
