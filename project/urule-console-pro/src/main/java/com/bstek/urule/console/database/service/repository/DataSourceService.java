package com.bstek.urule.console.database.service.repository;

import com.bstek.urule.console.database.model.datasource.DataSource;

public interface DataSourceService {
   DataSourceService ins = new DataSourceServiceImpl();

   void add(DataSource var1);

   void update(DataSource var1);

   void remove(Long var1);
}
