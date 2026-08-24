package com.bstek.urule.console.database.manager.repository;

import com.bstek.urule.console.database.model.datasource.DataSource;

public interface DataSourceManager {
   DataSourceManager ins = new DataSourceManagerImpl();

   DataSource get(Long id);

   void add(DataSource dataSource);

   void update(DataSource dataSource);

   void remove(Long id);

   DataSourceQuery createQuery();
}
