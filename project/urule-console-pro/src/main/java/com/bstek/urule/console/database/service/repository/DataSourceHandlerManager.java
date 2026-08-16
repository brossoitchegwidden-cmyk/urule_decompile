package com.bstek.urule.console.database.service.repository;

import com.bstek.urule.Utils;
import com.bstek.urule.console.database.model.batch.DataSourceType;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DataSourceHandlerManager {
   static final Log a = LogFactory.getLog(DataSourceHandlerManager.class);
   static Map b = new ConcurrentHashMap();
   static Map c = new HashMap();
   static Map d = new ConcurrentHashMap();

   public static DataSource getDataSource(com.bstek.urule.console.database.model.datasource.DataSource var0) throws Exception {
      if (b.containsKey(var0.getId())) {
         return (DataSource)b.get(var0.getId());
      } else {
         DataSource var1 = null;
         if (var0.getType() == DataSourceType.jdbc) {
            var1 = ((BatchDataSourceHandler)c.get(DataSourceType.jdbc)).getDataSource(var0);
         } else if (var0.getType() == DataSourceType.jndi) {
            var1 = ((BatchDataSourceHandler)c.get(DataSourceType.jndi)).getDataSource(var0);
         } else if (var0.getType() == DataSourceType.custom) {
            String var2 = var0.getDataSourceBean();
            BatchDataSourceHandler var3 = (BatchDataSourceHandler)d.get(var2);
            if (var3 == null) {
               try {
                  var3 = (BatchDataSourceHandler)Utils.getApplicationContext().getBean(var2);
               } catch (Exception var5) {
                  a.error(var5);
                  var3 = (BatchDataSourceHandler)Class.forName(var2).newInstance();
               }

               d.put(var2, var3);
            }

            var1 = var3.getDataSource(var0);
         }

         b.put(var0.getId(), var1);
         return var1;
      }
   }

   public static DataSource newDataSource(com.bstek.urule.console.database.model.datasource.DataSource var0) throws Exception {
      DataSource var1 = null;
      if (var0.getType() == DataSourceType.jdbc) {
         var1 = (new JdbcDataSourceHandler()).getDataSource(var0);
      } else if (var0.getType() == DataSourceType.jndi) {
         var1 = (new JndiDataSourceHandler()).getDataSource(var0);
      } else if (var0.getType() == DataSourceType.custom) {
         String var2 = var0.getDataSourceBean();
         BatchDataSourceHandler var3 = null;

         try {
            var3 = (BatchDataSourceHandler)Utils.getApplicationContext().getBean(var2);
         } catch (Exception var5) {
            a.error(var5);
            var3 = (BatchDataSourceHandler)Class.forName(var2).newInstance();
         }

         var1 = var3.getDataSource(var0);
      }

      return var1;
   }

   public static void removeDataSource(Long var0) {
      b.remove(var0);
   }

   static {
      c.put(DataSourceType.jdbc, new JdbcDataSourceHandler());
      c.put(DataSourceType.jndi, new JndiDataSourceHandler());
   }
}
