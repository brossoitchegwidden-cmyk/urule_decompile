package com.bstek.urule.console.database.service.repository;

import com.bstek.urule.console.database.datasource.BasicDataSourceWrapper;
import com.bstek.urule.console.util.StringUtils;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

public class JdbcDataSourceHandler implements BatchDataSourceHandler {
   public DataSource getDataSource(com.bstek.urule.console.database.model.datasource.DataSource repo) {
      BasicDataSourceWrapper basicDataSourceWrapper = new BasicDataSourceWrapper();
      ((BasicDataSource)basicDataSourceWrapper).setDriverClassName(repo.getDbDriver());
      ((BasicDataSource)basicDataSourceWrapper).setUrl(repo.getDbUrl());
      ((BasicDataSource)basicDataSourceWrapper).setUsername(repo.getDbUser());
      ((BasicDataSource)basicDataSourceWrapper).setPassword(repo.getDbPwd());
      ((BasicDataSource)basicDataSourceWrapper).setInitialSize(repo.getDbInitialsize());
      ((BasicDataSource)basicDataSourceWrapper).setMaxTotal(repo.getDbMaxTotal());
      ((BasicDataSource)basicDataSourceWrapper).setMaxIdle(repo.getDbMaxIdle());
      ((BasicDataSource)basicDataSourceWrapper).setMinIdle(repo.getDbMinIdle());
      String dbValidationQuery = repo.getDbValidationQuery();
      if (StringUtils.isNotEmpty(dbValidationQuery)) {
         ((BasicDataSource)basicDataSourceWrapper).setValidationQuery(dbValidationQuery);
      }

      return basicDataSourceWrapper;
   }
}
