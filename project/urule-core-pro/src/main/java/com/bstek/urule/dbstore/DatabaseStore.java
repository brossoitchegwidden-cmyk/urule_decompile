package com.bstek.urule.dbstore;

import com.bstek.urule.dbstore.service.DbService;
import javax.sql.DataSource;

public interface DatabaseStore {
   String TABLE_NAME = "URULE_KP_STORE";

   void init(DataSource ds) throws Exception;

   boolean support(String dbname);

   DbService getDbService();
}
