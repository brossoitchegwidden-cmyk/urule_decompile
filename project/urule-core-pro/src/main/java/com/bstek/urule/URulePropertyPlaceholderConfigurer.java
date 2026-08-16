package com.bstek.urule;

import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

public class URulePropertyPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer {
   public URulePropertyPlaceholderConfigurer() {
      this.setIgnoreUnresolvablePlaceholders(true);
      this.setOrder(100);
   }
}
