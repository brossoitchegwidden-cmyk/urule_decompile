package com.bstek.urule.console.config.manager;

import com.bstek.urule.console.config.ApplicationConfig;
import com.bstek.urule.console.config.setup.DataSourceInfo;
import com.bstek.urule.console.config.setup.SetupInfo;
import com.bstek.urule.console.database.datasource.BasicDataSourceWrapper;
import com.bstek.urule.console.util.StringUtils;
import java.sql.Connection;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JdbcConfigManager extends DBConfigManager {
   private static final Log b = LogFactory.getLog(JdbcConfigManager.class);
   private DataSource c;

   public JdbcConfigManager(ApplicationConfig var1) {
      super(var1);
   }

   public void load() {
      this.a();
      super.load();
   }

   protected void d() {
      b.debug("[URULE-CONSOLE]初始化规则DBCP数据源...");
      BasicDataSourceWrapper var1 = new BasicDataSourceWrapper();
      ((BasicDataSource)var1).setDriverClassName(this.getProperty("urule.store.database.driver"));
      String var2 = this.getProperty("urule.store.database.url");
      String var3 = "%URULE_HOME%";
      if (StringUtils.isNotBlank(var2) && var2.contains(var3)) {
         var2 = var2.replaceAll(var3, this.getURuleHome());
      }

      ((BasicDataSource)var1).setUrl(var2);
      ((BasicDataSource)var1).setUsername(this.getProperty("urule.store.database.username"));
      ((BasicDataSource)var1).setPassword(this.getProperty("urule.store.database.password"));
      String var4 = this.getProperty("urule.store.database.initialsize");
      if (StringUtils.isNotBlank(var4)) {
         ((BasicDataSource)var1).setInitialSize(Integer.parseInt(var4));
      }

      String var5 = this.getProperty("urule.store.database.maxTotal");
      if (StringUtils.isNotBlank(var5)) {
         ((BasicDataSource)var1).setMaxTotal(Integer.parseInt(var5));
      }

      String var6 = this.getProperty("urule.store.database.maxIdle");
      if (StringUtils.isNotBlank(var6)) {
         ((BasicDataSource)var1).setMaxIdle(Integer.parseInt(var6));
      }

      String var7 = this.getProperty("urule.store.database.minIdle");
      if (StringUtils.isNotBlank(var7)) {
         ((BasicDataSource)var1).setMinIdle(Integer.parseInt(var7));
      }

      String var8 = this.getProperty("urule.store.database.validationQuery");
      if (StringUtils.isNotBlank(var8)) {
         ((BasicDataSource)var1).setValidationQuery(var8);
      }

      String var9 = this.getProperty("urule.store.database.testOnBorrow");
      if (StringUtils.isNotBlank(var9)) {
         ((BasicDataSource)var1).setTestOnBorrow(Boolean.getBoolean(var9));
      }

      String var10 = this.getProperty("urule.store.database.testWhileIdle");
      if (StringUtils.isNotBlank(var10)) {
         ((BasicDataSource)var1).setTestWhileIdle(Boolean.getBoolean(var10));
      }

      String var11 = this.getProperty("urule.store.database.timeBetweenEvictionRunsMillis");
      if (StringUtils.isNotBlank(var11)) {
         ((BasicDataSource)var1).setTimeBetweenEvictionRunsMillis(Long.parseLong(var11));
      }

      this.c = var1;
   }

   public Connection getConnection() throws Exception {
      return this.c.getConnection();
   }

   public void initConfig(SetupInfo var1) {
      b.debug("生成规则DBCP配置参数配置文件...");
      Properties var2 = this.b();
      DataSourceInfo var3 = var1.getDataSourceInfo();
      var2.put("urule.store.database.driver", var3.getDriver());
      var2.put("urule.store.database.url", var3.getUrl());
      var2.put("urule.store.database.username", var3.getUsername());
      if (StringUtils.isNotBlank(var3.getPassword())) {
         var2.put("urule.store.database.password", var3.getPassword());
      }

      var2.put("urule.store.database.platform", var3.getPlatform());
   }
}
