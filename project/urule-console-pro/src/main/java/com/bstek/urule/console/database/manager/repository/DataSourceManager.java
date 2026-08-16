package com.bstek.urule.console.database.manager.repository;

import com.bstek.urule.console.database.model.datasource.DataSource;

public interface DataSourceManager {
   DataSourceManager ins = new DataSourceManagerImpl();

   DataSource get(Long var1);

   void add(DataSource var1);

   void update(DataSource var1);

   void remove(Long var1);

   DataSourceQuery createQuery();
}
