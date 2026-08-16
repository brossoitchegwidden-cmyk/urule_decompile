package com.bstek.urule.console.database.service.repository;

import javax.sql.DataSource;

public interface BatchDataSourceHandler {
   DataSource getDataSource(com.bstek.urule.console.database.model.datasource.DataSource var1);
}
