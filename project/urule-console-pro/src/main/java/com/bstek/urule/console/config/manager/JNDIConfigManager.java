package com.bstek.urule.console.config.manager;

import com.bstek.urule.console.config.ApplicationConfig;
import com.bstek.urule.console.config.setup.SetupInfo;
import com.bstek.urule.console.util.StringUtils;
import java.sql.Connection;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JNDIConfigManager extends DBConfigManager {
   private static final Log logger = LogFactory.getLog(JdbcConfigManager.class);
   private DataSource dataSource;

   public JNDIConfigManager(ApplicationConfig applicationConfig) {
      super(applicationConfig);
   }
   public void load() {
      this.initializeProperties();
      super.load();
   }

   protected void initializeConnectionSource() {
      String property = this.getProperty("urule.store.database.jndiname");
      JNDIConfigManager.logger.debug("初始化报表JNDI数据源...");

      try {
         InitialContext initialContext = new InitialContext();
         if (StringUtils.isNotEmpty(property) && !property.startsWith("java:comp/env/")) {
            property = "java:comp/env/" + property;
         }

         DataSource dataSource = (DataSource)initialContext.lookup(property);
         this.dataSource = dataSource;
      } catch (NamingException namingException) {
         JNDIConfigManager.logger.error("Unable to initialize JNDI data source", namingException);
      }

   }

   public Connection getConnection() throws Exception {
      return this.dataSource.getConnection();
   }

   protected void initConfig(SetupInfo setupInfo) {
      Properties properties = this.getApplicationProperties();
      properties.put("urule.store.database.jndiname", setupInfo.getDataSourceInfo().getJndi());
   }
}
