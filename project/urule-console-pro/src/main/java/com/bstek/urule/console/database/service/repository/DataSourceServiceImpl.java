package com.bstek.urule.console.database.service.repository;

import com.bstek.urule.console.database.manager.repository.DataSourceManager;
import com.bstek.urule.console.database.model.datasource.DataSource;

public class DataSourceServiceImpl implements DataSourceService {
   public void add(DataSource var1) {
      DataSourceManager.ins.add(var1);
   }

   public void update(DataSource var1) {
      DataSourceManager.ins.update(var1);
      DataSourceHandlerManager.removeDataSource(var1.getId());
   }

   public void remove(Long var1) {
      DataSourceManager.ins.remove(var1);
      DataSourceHandlerManager.removeDataSource(var1);
   }
}
