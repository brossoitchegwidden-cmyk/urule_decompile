package com.bstek.urule.console.database.manager.log;

import com.bstek.urule.console.database.model.OperationLog;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface OperationLogManager {
   OperationLogManager ins = new OperationLogManagerImpl();

   /**新增日志*/
   void add(OperationLog log);

   /**新增日志*/
   void addBatch(PreparedStatement stmt, OperationLog log) throws SQLException;

   /**删除团队对应的日志*/
   void removeByGroupId(String groupId);

   /**删除项目对应的日志*/
   void removeByProjectId(Long projectId);

   OperationLogQuery newQuery();
}
