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
   private static final Log b = LogFactory.getLog(JdbcConfigManager.class);
   private DataSource c;

   public JNDIConfigManager(ApplicationConfig var1) {
      super(var1);
   }

   public void load() {
      this.a();
      super.load();
   }

   protected void d() {
      String var1 = this.getProperty("urule.store.database.jndiname");
      b.debug("初始化报表JNDI数据源...");

      try {
         InitialContext var2 = new InitialContext();
         if (StringUtils.isNotEmpty(var1) && !var1.startsWith("java:comp/env/")) {
            var1 = "java:comp/env/" + var1;
         }

         DataSource var3 = (DataSource)var2.lookup(var1);
         this.c = var3;
      } catch (NamingException var4) {
         var4.printStackTrace();
      }

   }

   public Connection getConnection() throws Exception {
      return this.c.getConnection();
   }

   protected void initConfig(SetupInfo var1) {
      Properties var2 = this.b();
      var2.put("urule.store.database.jndiname", var1.getDataSourceInfo().getJndi());
   }
}
