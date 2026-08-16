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
   private ResourceProvider c;
   protected ApplicationContext a;
   protected Collection<ResourceBuilder> b;

   public ResourceBase newResourceBase() {
      return new ResourceBase(this.a());
   }

   protected Element a(String var1) {
      try {
         Document var2 = DocumentHelper.parseText(var1);
         return var2.getRootElement();
      } catch (DocumentException var4) {
         throw new RuleException(var4);
      }
   }

   private ResourceProvider a() {
      if (this.c == null) {
         ServiceLoader var1 = ServiceLoader.load(ResourceProvider.class);
         Iterator var2 = var1.iterator();
         if (var2.hasNext()) {
            ResourceProvider var3 = (ResourceProvider)var2.next();
            this.c = var3;
         }
      }

      return this.c;
   }

   public void setApplicationContext(ApplicationContext var1) throws BeansException {
      this.b = var1.getBeansOfType(ResourceBuilder.class).values();
      this.a = var1;
      var1.getBeansWithAnnotation(SuppressWarnings.class);
   }
}
