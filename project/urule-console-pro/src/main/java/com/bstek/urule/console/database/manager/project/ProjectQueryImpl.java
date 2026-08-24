package com.bstek.urule.console.database.manager.project;

import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProjectQueryImpl implements ProjectQuery {
   private String userId;
   private String name;
   private String nameLike;
   private String type;
   private String groupId;
   private String createDateOrder;
   private String nameOrder;
   private List queryParameters = new ArrayList();

   public List listIds() {
      Connection connection = JdbcUtils.getConnection();
      String text = "select ID_ from URULE_PROJECT";
      StringBuilder stringBuilder = this.buildQueryConditions();
      if (stringBuilder.length() > 0) {
         text = text + " where" + stringBuilder.toString();
      }

      ArrayList listIdsResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            items.add(resultSet.getLong("ID_"));
         }

         JdbcUtils.closeStatement(preparedStatement);
         listIdsResult = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return listIdsResult;
   }

   public List list() {
      Connection connection = JdbcUtils.getConnection();
      String text = "select ID_, NAME_, TYPE_, DESC_, GROUP_ID_, CREATE_USER_, CREATE_DATE_, UPDATE_USER_, UPDATE_DATE_ from URULE_PROJECT";
      StringBuilder stringBuilder = this.buildQueryConditions();
      if (stringBuilder.length() > 0) {
         text = text + " where" + stringBuilder.toString();
      }

      StringBuilder stringBuilder2 = this.buildOrderByClause();
      if (stringBuilder2.length() > 0) {
         text = text + " order by" + stringBuilder2.toString();
      }

      ArrayList listResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            Project project = new Project();
            project.setId(resultSet.getLong("ID_"));
            project.setName(resultSet.getString("NAME_"));
            project.setType(resultSet.getString("TYPE_"));
            project.setDesc(resultSet.getString("DESC_"));
            project.setGroupId(resultSet.getString("GROUP_ID_"));
            project.setCreateUser(resultSet.getString("CREATE_USER_"));
            project.setCreateDate(resultSet.getTimestamp("CREATE_DATE_"));
            project.setUpdateUser(resultSet.getString("UPDATE_USER_"));
            project.setUpdateDate(resultSet.getTimestamp("UPDATE_DATE_"));
            items.add(project);
         }

         JdbcUtils.closeStatement(preparedStatement);
         listResult = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return listResult;
   }

   private StringBuilder buildOrderByClause() {
      StringBuilder stringBuilder = new StringBuilder();
      if (this.createDateOrder != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" CREATE_DATE_ ").append(this.createDateOrder);
      }

      if (this.nameOrder != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" NAME_ ").append(this.nameOrder);
      }

      return stringBuilder;
   }

   private StringBuilder buildQueryConditions() {
      this.queryParameters.clear();
      StringBuilder stringBuilder = new StringBuilder();
      if (this.name != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" NAME_ = ?");
         this.queryParameters.add(this.name);
      }

      if (this.nameLike != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" NAME_ like ?");
         this.queryParameters.add("%" + this.nameLike + "%");
      }

      if (this.type != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" TYPE_=?");
         this.queryParameters.add(this.type);
      }

      if (this.groupId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" GROUP_ID_=?");
         this.queryParameters.add(this.groupId);
      }

      if (this.userId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" (CREATE_USER_=? or ID_ in (select PROJECT_ID_ from URULE_PROJECT_USER where USER_ID_=?)) ");
         this.queryParameters.add(this.userId);
         this.queryParameters.add(this.userId);
      }

      return stringBuilder;
   }

   public ProjectQuery name(String name) {
      this.name = name;
      return this;
   }

   public ProjectQuery nameLike(String name) {
      this.nameLike = name;
      return this;
   }

   public ProjectQuery type(String type) {
      this.type = type;
      return this;
   }

   public ProjectQuery groupId(String groupId) {
      this.groupId = groupId;
      return this;
   }

   public ProjectQuery orderbyCreateDate(String asc) {
      this.createDateOrder = asc;
      return this;
   }

   public ProjectQuery orderbyName(String asc) {
      this.nameOrder = asc;
      return this;
   }

   public ProjectQuery userId(String userId) {
      this.userId = userId;
      return this;
   }
}
