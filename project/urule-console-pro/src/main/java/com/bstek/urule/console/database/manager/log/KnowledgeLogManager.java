package com.bstek.urule.console.database.manager.log;

import com.bstek.urule.console.database.model.KnowledgeLog;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface KnowledgeLogManager {
   KnowledgeLogManager ins = new KnowledgeLogManagerImpl();

   void add(KnowledgeLog var1);

   void addBatch(PreparedStatement var1, KnowledgeLog var2) throws SQLException;

   void removeByGroupId(String var1);

   void removeByProject(Long var1);

   KnowledgeLogQuery newQuery();

   KnowledgeLogCountQuery newCountQuery();
}
