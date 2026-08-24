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
   static final Log logger = LogFactory.getLog(DataSourceHandlerManager.class);
   static Map dataSourceCache = new ConcurrentHashMap();
   static Map builtInHandlers = new HashMap();
   static Map customHandlerCache = new ConcurrentHashMap();

   public static DataSource getDataSource(com.bstek.urule.console.database.model.datasource.DataSource repo) throws Exception {
      if (dataSourceCache.containsKey(repo.getId())) {
         return (DataSource)dataSourceCache.get(repo.getId());
      } else {
         DataSource dataSource = null;
         if (repo.getType() == DataSourceType.jdbc) {
            dataSource = ((BatchDataSourceHandler)builtInHandlers.get(DataSourceType.jdbc)).getDataSource(repo);
         } else if (repo.getType() == DataSourceType.jndi) {
            dataSource = ((BatchDataSourceHandler)builtInHandlers.get(DataSourceType.jndi)).getDataSource(repo);
         } else if (repo.getType() == DataSourceType.custom) {
            String dataSourceBean = repo.getDataSourceBean();
            BatchDataSourceHandler batchDataSourceHandler = (BatchDataSourceHandler)customHandlerCache.get(dataSourceBean);
            if (batchDataSourceHandler == null) {
               try {
                  batchDataSourceHandler = (BatchDataSourceHandler)Utils.getApplicationContext().getBean(dataSourceBean);
               } catch (Exception exception) {
                  DataSourceHandlerManager.logger.error(exception);
                  batchDataSourceHandler = (BatchDataSourceHandler)Class.forName(dataSourceBean).newInstance();
               }

               customHandlerCache.put(dataSourceBean, batchDataSourceHandler);
            }

            dataSource = batchDataSourceHandler.getDataSource(repo);
         }

         dataSourceCache.put(repo.getId(), dataSource);
         return dataSource;
      }
   }

   public static DataSource newDataSource(com.bstek.urule.console.database.model.datasource.DataSource repo) throws Exception {
      DataSource dataSource = null;
      if (repo.getType() == DataSourceType.jdbc) {
         dataSource = (new JdbcDataSourceHandler()).getDataSource(repo);
      } else if (repo.getType() == DataSourceType.jndi) {
         dataSource = (new JndiDataSourceHandler()).getDataSource(repo);
      } else if (repo.getType() == DataSourceType.custom) {
         String dataSourceBean = repo.getDataSourceBean();
         BatchDataSourceHandler batchDataSourceHandler = null;

         try {
            batchDataSourceHandler = (BatchDataSourceHandler)Utils.getApplicationContext().getBean(dataSourceBean);
         } catch (Exception exception) {
            DataSourceHandlerManager.logger.error(exception);
            batchDataSourceHandler = (BatchDataSourceHandler)Class.forName(dataSourceBean).newInstance();
         }

         dataSource = batchDataSourceHandler.getDataSource(repo);
      }

      return dataSource;
   }

   public static void removeDataSource(Long id) {
      dataSourceCache.remove(id);
   }

   static {
      builtInHandlers.put(DataSourceType.jdbc, new JdbcDataSourceHandler());
      builtInHandlers.put(DataSourceType.jndi, new JndiDataSourceHandler());
   }
}
