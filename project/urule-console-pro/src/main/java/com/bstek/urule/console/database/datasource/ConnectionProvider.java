package com.bstek.urule.console.database.datasource;

import java.sql.Connection;

public interface ConnectionProvider {
   String BEAN_ID = "urule.connectionProvider";

   Connection getConnection();
}
