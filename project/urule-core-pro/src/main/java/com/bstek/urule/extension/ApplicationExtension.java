package com.bstek.urule.extension;

import org.springframework.context.ApplicationContext;

public interface ApplicationExtension {
   void loadExtension(ApplicationContext var1) throws Exception;

   String name();
}
