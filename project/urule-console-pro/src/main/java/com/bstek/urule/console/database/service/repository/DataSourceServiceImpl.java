package com.bstek.urule.console.database.service.repository;

import com.bstek.urule.console.database.manager.repository.DataSourceManager;
import com.bstek.urule.console.database.model.datasource.DataSource;

public class DataSourceServiceImpl implements DataSourceService {
   public void add(DataSource dataSource) {
      DataSourceManager.ins.add(dataSource);
   }

   public void update(DataSource dataSource) {
      DataSourceManager.ins.update(dataSource);
      DataSourceHandlerManager.removeDataSource(dataSource.getId());
   }

   public void remove(Long id) {
      DataSourceManager.ins.remove(id);
      DataSourceHandlerManager.removeDataSource(id);
   }
}
