package com.bstek.urule.console.config.manager;

import com.bstek.urule.console.config.ApplicationConfig;
import com.bstek.urule.console.config.ConfigManager;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

public abstract class AbstractConfigManager implements ConfigManager {
   private static final Log b = LogFactory.getLog(AbstractConfigManager.class);
   protected Properties a;
   private ApplicationConfig c;

   public AbstractConfigManager(ApplicationConfig var1) {
      this.c = var1;
   }

   protected void a() {
      b.debug("[URULE-CONSOLE]初始化规则属性配置信息...");
      this.a = this.b();
   }

   public String getProperty(String var1) {
      if (this.a != null && this.a.containsKey(var1)) {
         return this.a.getProperty(var1);
      } else if (this.b() != null && this.b().containsKey(var1)) {
         return this.b().getProperty(var1);
      } else {
         return this.c() != null && this.c().getEnvironment().containsProperty(var1) ? this.c().getEnvironment().getProperty(var1) : null;
      }
   }

   public String getURuleHome() {
      return this.c.getApplicationHome();
   }

   protected Properties b() {
      return this.c.getProperties();
   }

   protected ApplicationContext c() {
      return this.c.getApplicationContext();
   }
}
