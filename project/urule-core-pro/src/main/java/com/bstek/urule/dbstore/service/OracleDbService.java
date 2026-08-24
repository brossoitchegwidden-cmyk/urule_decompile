package com.bstek.urule.dbstore.service;

import javax.sql.DataSource;

public class OracleDbService extends DbService {
   public OracleDbService(DataSource ds) {
      super(ds);
   }
}
