package com.bstek.urule.dbstore.service;

import javax.sql.DataSource;

public class MysqlDbService extends DbService {
   public MysqlDbService(DataSource ds) {
      super(ds);
   }
}
