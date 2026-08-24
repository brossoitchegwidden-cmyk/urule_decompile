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
   private static final Log logger = LogFactory.getLog(JdbcConfigManager.class);
   private DataSource dataSource;

   public JdbcConfigManager(ApplicationConfig applicationConfig) {
      super(applicationConfig);
   }
   public void load() {
      this.initializeProperties();
      super.load();
   }

   protected void initializeConnectionSource() {
      JdbcConfigManager.logger.debug("[URULE-CONSOLE]初始化规则DBCP数据源...");
      BasicDataSourceWrapper basicDataSourceWrapper = new BasicDataSourceWrapper();
      ((BasicDataSource)basicDataSourceWrapper).setDriverClassName(this.getProperty("urule.store.database.driver"));
      String property = this.getProperty("urule.store.database.url");
      String text = "%URULE_HOME%";
      if (StringUtils.isNotBlank(property) && property.contains(text)) {
         property = property.replaceAll(text, this.getURuleHome());
      }

      ((BasicDataSource)basicDataSourceWrapper).setUrl(property);
      ((BasicDataSource)basicDataSourceWrapper).setUsername(this.getProperty("urule.store.database.username"));
      ((BasicDataSource)basicDataSourceWrapper).setPassword(this.getProperty("urule.store.database.password"));
      String property2 = this.getProperty("urule.store.database.initialsize");
      if (StringUtils.isNotBlank(property2)) {
         ((BasicDataSource)basicDataSourceWrapper).setInitialSize(Integer.parseInt(property2));
      }

      String property3 = this.getProperty("urule.store.database.maxTotal");
      if (StringUtils.isNotBlank(property3)) {
         ((BasicDataSource)basicDataSourceWrapper).setMaxTotal(Integer.parseInt(property3));
      }

      String property4 = this.getProperty("urule.store.database.maxIdle");
      if (StringUtils.isNotBlank(property4)) {
         ((BasicDataSource)basicDataSourceWrapper).setMaxIdle(Integer.parseInt(property4));
      }

      String property5 = this.getProperty("urule.store.database.minIdle");
      if (StringUtils.isNotBlank(property5)) {
         ((BasicDataSource)basicDataSourceWrapper).setMinIdle(Integer.parseInt(property5));
      }

      String property6 = this.getProperty("urule.store.database.validationQuery");
      if (StringUtils.isNotBlank(property6)) {
         ((BasicDataSource)basicDataSourceWrapper).setValidationQuery(property6);
      }

      String property7 = this.getProperty("urule.store.database.testOnBorrow");
      if (StringUtils.isNotBlank(property7)) {
         ((BasicDataSource)basicDataSourceWrapper).setTestOnBorrow(Boolean.getBoolean(property7));
      }

      String property8 = this.getProperty("urule.store.database.testWhileIdle");
      if (StringUtils.isNotBlank(property8)) {
         ((BasicDataSource)basicDataSourceWrapper).setTestWhileIdle(Boolean.getBoolean(property8));
      }

      String property9 = this.getProperty("urule.store.database.timeBetweenEvictionRunsMillis");
      if (StringUtils.isNotBlank(property9)) {
         ((BasicDataSource)basicDataSourceWrapper).setTimeBetweenEvictionRunsMillis(Long.parseLong(property9));
      }

      this.dataSource = basicDataSourceWrapper;
   }

   public Connection getConnection() throws Exception {
      return this.dataSource.getConnection();
   }

   public void initConfig(SetupInfo setupInfo) {
      JdbcConfigManager.logger.debug("生成规则DBCP配置参数配置文件...");
      Properties properties = this.getApplicationProperties();
      DataSourceInfo dataSourceInfo = setupInfo.getDataSourceInfo();
      properties.put("urule.store.database.driver", dataSourceInfo.getDriver());
      properties.put("urule.store.database.url", dataSourceInfo.getUrl());
      properties.put("urule.store.database.username", dataSourceInfo.getUsername());
      if (StringUtils.isNotBlank(dataSourceInfo.getPassword())) {
         properties.put("urule.store.database.password", dataSourceInfo.getPassword());
      }

      properties.put("urule.store.database.platform", dataSourceInfo.getPlatform());
   }
}
