package com.bstek.urule.console.database.manager.log;

import com.bstek.urule.console.database.model.LoginLog;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface LoginLogManager {
   LoginLogManager ins = new LoginLogManagerImpl();

   /**新增日志*/
   void add(LoginLog log);

   /**新增日志*/
   void addBatch(PreparedStatement stmt, LoginLog log) throws SQLException;

   /**删除用户对应的日志*/
   void removeByUser(String userId);

   LoginLogQuery newQuery();
}
