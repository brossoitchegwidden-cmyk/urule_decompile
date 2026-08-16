package com.bstek.urule.console.database.manager.log;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.KnowledgeLog;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class KnowledgeLogManagerImpl implements KnowledgeLogManager {
   public void add(KnowledgeLog var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var1.setCreateDate(new Timestamp(System.currentTimeMillis()));
         long var3 = IDGenerator.getInstance().nextId(IDType.LOG_KNOWLEDGE);
         PreparedStatement var5 = var2.prepareStatement("insert into URULE_LOG_KNOWLEDGE (ID_, USER_, KNOWLEDGE_ID_, KNOWLEDGE_NAME_, VERSION_, IN_PARAMS_, OUT_PARAMS_, LOGS_, TIME_, IP_, USER_AGENT_, START_TIME_, END_TIME_, GROUP_ID_, GROUP_NAME_, PROJECT_ID_, PROJECT_NAME_, CREATE_DATE_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         var1.setId(var3);
         var5.setLong(1, var1.getId());
         var5.setString(2, var1.getUsername());
         var5.setLong(3, var1.getKnowledgeId());
         var5.setString(4, var1.getKnowledgeName());
         var5.setString(5, var1.getVersion());
         var5.setString(6, var1.getInParams());
         var5.setString(7, var1.getOutParams());
         var5.setString(8, var1.getLogs());
         var5.setLong(9, var1.getTime());
         var5.setString(10, var1.getIp());
         var5.setString(11, var1.getUserAgent());
         var5.setTimestamp(12, new Timestamp(var1.getStartTime().getTime()));
         var5.setTimestamp(13, new Timestamp(var1.getEndTime().getTime()));
         var5.setString(14, var1.getGroupId());
         var5.setString(15, var1.getGroupName());
         var5.setLong(16, var1.getProjectId());
         var5.setString(17, var1.getProjectName());
         var5.setTimestamp(18, new Timestamp(var1.getCreateDate().getTime()));
         var5.executeUpdate();
         JdbcUtils.closeStatement(var5);
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void removeByProject(Long var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_LOG_KNOWLEDGE where PROJECT_ID_=?");
         var3.setLong(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void removeByGroupId(String var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_LOG_KNOWLEDGE where GROUP_ID_=?");
         var3.setString(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public KnowledgeLogQuery newQuery() {
      return new KnowledgeLogQueryImpl();
   }

   public KnowledgeLogCountQuery newCountQuery() {
      return new KnowledgeLogCountQueryImpl();
   }

   public void addBatch(PreparedStatement var1, KnowledgeLog var2) throws SQLException {
      var2.setCreateDate(new Timestamp(System.currentTimeMillis()));
      long var3 = IDGenerator.getInstance().nextId(IDType.LOG_KNOWLEDGE);
      var2.setId(var3);
      var1.setLong(1, var2.getId());
      var1.setString(2, var2.getUsername());
      var1.setLong(3, var2.getKnowledgeId());
      var1.setString(4, var2.getKnowledgeName());
      var1.setString(5, var2.getVersion());
      var1.setString(6, var2.getInParams());
      var1.setString(7, var2.getOutParams());
      var1.setString(8, var2.getLogs());
      var1.setLong(9, var2.getTime());
      var1.setString(10, var2.getIp());
      var1.setString(11, var2.getUserAgent());
      var1.setTimestamp(12, new Timestamp(var2.getStartTime().getTime()));
      var1.setTimestamp(13, new Timestamp(var2.getEndTime().getTime()));
      var1.setString(14, var2.getGroupId());
      var1.setString(15, var2.getGroupName());
      var1.setLong(16, var2.getProjectId());
      var1.setString(17, var2.getProjectName());
      var1.setTimestamp(18, new Timestamp(var2.getCreateDate().getTime()));
      var1.addBatch();
   }
}
