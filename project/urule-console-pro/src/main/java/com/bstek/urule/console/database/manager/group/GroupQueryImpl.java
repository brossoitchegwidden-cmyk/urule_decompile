package com.bstek.urule.console.database.manager.group;

import com.bstek.urule.console.database.model.Group;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GroupQueryImpl implements GroupQuery {
   private String name;
   public List list(String userId) {
      Connection connection = JdbcUtils.getConnection();

      ArrayList listResult;
      try {
         String text = "SELECT URULE_GROUP.ID_, URULE_GROUP.NAME_, URULE_GROUP.DESC_, URULE_GROUP.CREATE_USER_, URULE_GROUP.CREATE_DATE_ FROM URULE_GROUP  LEFT JOIN URULE_GROUP_USER on URULE_GROUP.ID_=URULE_GROUP_USER.GROUP_ID_ WHERE URULE_GROUP_USER.USER_ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, userId);
         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            Group group = new Group();
            group.setId(resultSet.getString(1));
            group.setName(resultSet.getString(2));
            group.setDesc(resultSet.getString(3));
            group.setCreateUser(resultSet.getString(4));
            group.setCreateDate(resultSet.getTimestamp(5));
            items.add(group);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         listResult = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return listResult;
   }
   public List list() {
      Connection connection = JdbcUtils.getConnection();

      ArrayList listResult;
      try {
         String text = "SELECT URULE_GROUP.ID_, URULE_GROUP.NAME_, URULE_GROUP.DESC_, URULE_GROUP.CREATE_USER_, URULE_GROUP.CREATE_DATE_ FROM URULE_GROUP ";
         String text2 = "";
         if (StringUtils.isNotBlank(this.name)) {
            text2 = " WHERE NAME_=?";
         }

         PreparedStatement preparedStatement = connection.prepareStatement(text + text2);
         if (StringUtils.isNotBlank(text2)) {
            preparedStatement.setString(1, this.name);
         }

         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            Group group = new Group();
            group.setId(resultSet.getString(1));
            group.setName(resultSet.getString(2));
            group.setDesc(resultSet.getString(3));
            group.setCreateUser(resultSet.getString(4));
            group.setCreateDate(resultSet.getTimestamp(5));
            items.add(group);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         listResult = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return listResult;
   }

   public GroupQuery name(String name) {
      this.name = name;
      return this;
   }
}
