package com.bstek.urule.console.database.service.repository;

import com.bstek.urule.console.util.StringUtils;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class JndiDataSourceHandler implements BatchDataSourceHandler {
   public DataSource getDataSource(com.bstek.urule.console.database.model.datasource.DataSource var1) {
      String var2 = var1.getDbJndiName();

      try {
         InitialContext var3 = new InitialContext();
         if (StringUtils.isNotEmpty(var2) && !var2.startsWith("java:comp/env/")) {
            var2 = "java:comp/env/" + var2;
         }

         DataSource var4 = (DataSource)var3.lookup(var2);
         return var4;
      } catch (NamingException var5) {
         var5.printStackTrace();
         return null;
      }
   }
}
