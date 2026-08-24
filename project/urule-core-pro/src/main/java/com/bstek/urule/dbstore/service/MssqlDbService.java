package com.bstek.urule.dbstore.service;

import javax.sql.DataSource;

public class MssqlDbService extends DbService {
   public MssqlDbService(DataSource ds) {
      super(ds);
   }
}
