package com.bstek.urule.console.database.manager.file;

import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileCountQueryImpl implements FileCountQuery {
   private Long projectId;
   private Date date;
   private Date updateDateEnd;

   public FileCountQuery projectId(Long projectId) {
      this.projectId = projectId;
      return this;
   }

   public FileCountQuery updateDateBegin(Date date) {
      this.date = date;
      return this;
   }

   public FileCountQuery updateDateEnd(Date date) {
      this.updateDateEnd = date;
      return this;
   }

   public List getRuleCommits() {
      Connection connection = JdbcUtils.getConnection();

      ArrayList ruleCommits;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement("select ID_, UPDATE_DATE_ from URULE_FILE where PROJECT_ID_=? and UPDATE_DATE_>=? and UPDATE_DATE_<=?");
         preparedStatement.setLong(1, this.projectId);
         preparedStatement.setTimestamp(2, new Timestamp(this.date.getTime()));
         preparedStatement.setTimestamp(3, new Timestamp(this.updateDateEnd.getTime()));
         ResultSet resultSet = preparedStatement.executeQuery();
         ArrayList items = new ArrayList();

         while(resultSet.next()) {
            RuleFile ruleFile = new RuleFile();
            ruleFile.setId(resultSet.getLong(1));
            ruleFile.setModifyDate(resultSet.getTimestamp(2));
            items.add(ruleFile);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         ruleCommits = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return ruleCommits;
   }

   public List getUserCommits() {
      Connection connection = JdbcUtils.getConnection();

      ArrayList userCommits;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement("select ID_, UPDATE_USER_, UPDATE_DATE_ from URULE_FILE where PROJECT_ID_=? and UPDATE_DATE_>=? and UPDATE_DATE_<=?");
         preparedStatement.setLong(1, this.projectId);
         preparedStatement.setTimestamp(2, new Timestamp(this.date.getTime()));
         preparedStatement.setTimestamp(3, new Timestamp(this.updateDateEnd.getTime()));
         ResultSet resultSet = preparedStatement.executeQuery();
         ArrayList items = new ArrayList();

         while(resultSet.next()) {
            RuleFile ruleFile = new RuleFile();
            ruleFile.setId(resultSet.getLong(1));
            ruleFile.setUpdateUser(resultSet.getString(2));
            ruleFile.setModifyDate(resultSet.getDate(3));
            items.add(ruleFile);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         userCommits = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return userCommits;
   }

   public Integer getRuleCount() {
      int number = 0;
      Connection connection = JdbcUtils.getConnection();

      Integer ruleCount;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement("select COUNT(*) RULE_COUNT_ from URULE_FILE where PROJECT_ID_=? and DELETED_=?");
         preparedStatement.setLong(1, this.projectId);
         preparedStatement.setBoolean(2, false);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            number = resultSet.getInt(1);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         ruleCount = number;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return ruleCount;
   }
}
