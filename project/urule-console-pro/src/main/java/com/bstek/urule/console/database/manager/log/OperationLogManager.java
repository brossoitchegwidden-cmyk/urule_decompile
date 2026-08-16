package com.bstek.urule.console.database.manager.log;

import com.bstek.urule.console.database.model.OperationLog;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface OperationLogManager {
   OperationLogManager ins = new OperationLogManagerImpl();

   void add(OperationLog var1);

   void addBatch(PreparedStatement var1, OperationLog var2) throws SQLException;

   void removeByGroupId(String var1);

   void removeByProjectId(Long var1);

   OperationLogQuery newQuery();
}
