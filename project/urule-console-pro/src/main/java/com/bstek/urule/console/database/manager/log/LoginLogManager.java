package com.bstek.urule.console.database.manager.log;

import com.bstek.urule.console.database.model.LoginLog;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface LoginLogManager {
   LoginLogManager ins = new LoginLogManagerImpl();

   void add(LoginLog var1);

   void addBatch(PreparedStatement var1, LoginLog var2) throws SQLException;

   void removeByUser(String var1);

   LoginLogQuery newQuery();
}
