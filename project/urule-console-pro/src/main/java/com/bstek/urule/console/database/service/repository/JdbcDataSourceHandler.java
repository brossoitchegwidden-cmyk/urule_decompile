package com.bstek.urule.console.database.service.repository;

import com.bstek.urule.console.database.datasource.BasicDataSourceWrapper;
import com.bstek.urule.console.util.StringUtils;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

public class JdbcDataSourceHandler implements BatchDataSourceHandler {
   public DataSource getDataSource(com.bstek.urule.console.database.model.datasource.DataSource var1) {
      BasicDataSourceWrapper var2 = new BasicDataSourceWrapper();
      ((BasicDataSource)var2).setDriverClassName(var1.getDbDriver());
      ((BasicDataSource)var2).setUrl(var1.getDbUrl());
      ((BasicDataSource)var2).setUsername(var1.getDbUser());
      ((BasicDataSource)var2).setPassword(var1.getDbPwd());
      ((BasicDataSource)var2).setInitialSize(var1.getDbInitialsize());
      ((BasicDataSource)var2).setMaxTotal(var1.getDbMaxTotal());
      ((BasicDataSource)var2).setMaxIdle(var1.getDbMaxIdle());
      ((BasicDataSource)var2).setMinIdle(var1.getDbMinIdle());
      String var3 = var1.getDbValidationQuery();
      if (StringUtils.isNotEmpty(var3)) {
         ((BasicDataSource)var2).setValidationQuery(var3);
      }

      return var2;
   }
}
