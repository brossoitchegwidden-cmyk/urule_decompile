package com.bstek.urule.console.database.manager.log;

import com.bstek.urule.console.database.model.KnowledgeLog;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface KnowledgeLogManager {
   KnowledgeLogManager ins = new KnowledgeLogManagerImpl();

   /**新增日志*/
   void add(KnowledgeLog log);

   /**新增日志*/
   void addBatch(PreparedStatement stmt, KnowledgeLog log) throws SQLException;

   /**删除用户对应的日志*/
   void removeByGroupId(String groupId);

   /**删除项目对应的日志*/
   void removeByProject(Long projectId);

   KnowledgeLogQuery newQuery();

   KnowledgeLogCountQuery newCountQuery();
}
