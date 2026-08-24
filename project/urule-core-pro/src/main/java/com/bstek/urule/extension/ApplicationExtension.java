package com.bstek.urule.extension;

import org.springframework.context.ApplicationContext;

public interface ApplicationExtension {
   void loadExtension(ApplicationContext context) throws Exception;

   String name();
}
