package com.bstek.urule.console.database.service.repository;

import com.bstek.urule.console.util.StringUtils;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class JndiDataSourceHandler implements BatchDataSourceHandler {
   public DataSource getDataSource(com.bstek.urule.console.database.model.datasource.DataSource repo) {
      String dbJndiName = repo.getDbJndiName();

      try {
         InitialContext initialContext = new InitialContext();
         if (StringUtils.isNotEmpty(dbJndiName) && !dbJndiName.startsWith("java:comp/env/")) {
            dbJndiName = "java:comp/env/" + dbJndiName;
         }

         DataSource dataSource = (DataSource)initialContext.lookup(dbJndiName);
         return dataSource;
      } catch (NamingException namingException) {
         java.util.logging.Logger.getLogger(JndiDataSourceHandler.class.getName()).log(java.util.logging.Level.SEVERE, namingException.getMessage(), namingException);
         return null;
      }
   }
}
