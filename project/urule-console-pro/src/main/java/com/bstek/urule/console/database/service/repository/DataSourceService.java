package com.bstek.urule.console.database.service.repository;

import com.bstek.urule.console.database.model.datasource.DataSource;

public interface DataSourceService {
   DataSourceService ins = new DataSourceServiceImpl();

   void add(DataSource dataSource);

   void update(DataSource dataSource);

   void remove(Long id);
}
