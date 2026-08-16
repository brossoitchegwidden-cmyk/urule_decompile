package com.bstek.urule.console.config.bootstrap;

import com.bstek.urule.console.config.ApplicationConfig;
import com.bstek.urule.console.config.ConfigManager;
import com.bstek.urule.console.config.ConfigureManagerFactory;
import com.bstek.urule.console.config.HomeLocator;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.OrderComparator;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;

public class BootstrapManager implements InitializingBean, ApplicationContextAware {
   private static final Log a = LogFactory.getLog(BootstrapManager.class);
   private static boolean b;
   private static ConfigManager c;
   private static ApplicationConfig d;
   private static String e = "请参考:http://www.bstek.com/resources/doc/4.0/config/2.2setup.html";
   private static String f;
   private static String g;
   private static List h;
   private ApplicationContext i;
   private static BootstrapManager j;

   public void load() {
      try {
         String var1 = HomeLocator.getHomePath();
         d = new ApplicationConfig();
         d.setApplicationContext(this.i);
         System.out.println("[URULE-CONSOLE]URule Home:" + var1);
         a.info("[URULE-CONSOLE]URule Home:" + var1);
         if (StringUtils.isNotBlank(var1)) {
            d.setApplicationHome(var1);
            d.setConfigurationFileName(HomeLocator.getConfigFileName());
            if (d.configFileExists()) {
               a.info("[URULE-CONSOLE]检测到urule.properties配置文件,开始初始化...");
               d.load();
               c = ConfigureManagerFactory.initConfigManager(d);
               c.load();
               d.getSetupStep();
               d.setSetupComplete(true);
               a.info("[URULE-CONSOLE]初始化完成!");
               b = true;
            } else {
               a.error(g);
            }
         } else if (StringUtils.isNotBlank(this.a("urule.config.type"))) {
            a.info("[URULE-CONSOLE]检测到Environment配置信息,开始初始化...");
            d.setConfigType(this.a("urule.config.type"));
            c = ConfigureManagerFactory.initConfigManager(d);
            c.load();
            d.getSetupStep();
            d.setSetupComplete(true);
            a.info("[URULE-CONSOLE]初始化完成!");
            b = true;
         } else {
            a.error(f);
         }

      } catch (Exception var2) {
         throw new RuleException(var2);
      }
   }

   private String a(String var1) {
      Environment var2 = this.i.getEnvironment();
      boolean var3 = var2.containsProperty(var1);
      if (var3) {
         return var2.getProperty(var1);
      } else {
         Object var4 = null;
         Map var5 = this.i.getBeansOfType(PropertySourcesPlaceholderConfigurer.class);
         Object[] var6 = var5.values().toArray();
         ArrayList var7 = new ArrayList();

         for(Object var11 : var6) {
            var7.add((PropertySourcesPlaceholderConfigurer)var11);
         }

         OrderComparator.sort(var7);

         for(PropertySourcesPlaceholderConfigurer var21 : (Iterable<PropertySourcesPlaceholderConfigurer>)(Iterable<?>)(var7)) {
            for(Object var23 : var21.getAppliedPropertySources()) {
               if (var23 instanceof PropertiesPropertySource) {
                  PropertiesPropertySource var12 = (PropertiesPropertySource)var23;
                  var3 = var12.containsProperty(var1);
                  if (var3) {
                     var4 = var12.getProperty(var1);
                     if (var4 != null) {
                        String[] var13 = var12.getPropertyNames();

                        for(String var17 : var13) {
                           Object var18 = var12.getProperty(var17);
                           if (var18 != null) {
                              d.getProperties().setProperty(var17, var18.toString());
                           }
                        }
                     }
                  }
               }
            }
         }

         return var4 != null ? var4.toString() : null;
      }
   }

   public ConfigManager getConfigManager() {
      return c;
   }

   public boolean isBootstrapped() {
      return b;
   }

   public boolean needCheckBootstrapped(String var1) {
      for(String var3 : (Iterable<String>)(Iterable<?>)(h)) {
         if (var1.endsWith(var3)) {
            return false;
         }
      }

      if (var1.indexOf("/urule/setup/init") <= -1 && var1.indexOf("/urule/setup") <= -1) {
         return true;
      } else {
         return false;
      }
   }

   public ApplicationConfig getApplicationConfig() {
      return d;
   }

   public void setBootstrapped(boolean var1) {
      b = var1;
   }

   public void setConfigManager(ConfigManager var1) {
      c = var1;
   }

   public void setApplicationConfig(ApplicationConfig var1) {
      d = var1;
   }

   public List getFilterDefinitionList() {
      return h;
   }

   public void setFilterDefinitionList(List var1) {
      h = var1;
   }

   public void setApplicationContext(ApplicationContext var1) throws BeansException {
      this.i = var1;
      this.load();
      h.add(".js");
      h.add(".css");
      h.add(".png");
      h.add(".jpg");
      h.add(".svg");
   }

   public void afterPropertiesSet() throws Exception {
      j = this;
   }

   public static BootstrapManager get() {
      return j;
   }

   static {
      f = "未能完成系统设置: 未找到URULE PRO相关的数据库连接属性配置." + e;
      g = "未能完成配置文件加载,需要先进行配置文件初始化工作." + e;
      h = new ArrayList();
   }
}
