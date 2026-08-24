package com.bstek.urule.console.database.manager.jar;

import com.bstek.urule.console.database.model.DynamicJar;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DynamicJarQueryImpl implements DynamicJarQuery {
   private Long id;
   private String groupId;
   private String name;
   private String desc;
   private List queryParameters = new ArrayList();

   protected DynamicJarQueryImpl() {
   }

   public List list() {
      Connection connection = JdbcUtils.getConnection();
      String text = "select ID_,NAME_,DESC_,CREATE_USER_,UPDATE_USER_,CREATE_DATE_,UPDATE_DATE_,GROUP_ID_ from URULE_DYNAMIC_JAR";
      StringBuilder stringBuilder = this.buildWhereClause();
      if (stringBuilder.length() > 0) {
         text = text + " where" + stringBuilder.toString();
      }

      text = text + " order by CREATE_DATE_ desc";

      ArrayList listResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            DynamicJar dynamicJar = new DynamicJar();
            dynamicJar.setId(resultSet.getLong(1));
            dynamicJar.setName(resultSet.getString(2));
            dynamicJar.setDesc(resultSet.getString(3));
            dynamicJar.setCreateUser(resultSet.getString(4));
            dynamicJar.setUpdateUser(resultSet.getString(5));
            dynamicJar.setCreateDate(resultSet.getTimestamp(6));
            dynamicJar.setUpdateDate(resultSet.getTimestamp(7));
            dynamicJar.setGroupId(resultSet.getString(8));
            items.add(dynamicJar);
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

   private StringBuilder buildWhereClause() {
      this.queryParameters.clear();
      StringBuilder stringBuilder = new StringBuilder();
      if (this.name != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" NAME_ like ?");
         this.queryParameters.add("%" + this.name + "%");
      }

      if (this.desc != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" DESC_ like ?");
         this.queryParameters.add("%" + this.desc + "%");
      }

      if (this.id != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" ID_=?");
         this.queryParameters.add(this.id);
      }

      if (this.groupId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" GROUP_ID_=?");
         this.queryParameters.add(this.groupId);
      }

      return stringBuilder;
   }

   public DynamicJarQuery id(long id) {
      this.id = id;
      return this;
   }

   public DynamicJarQuery groupId(String groupId) {
      this.groupId = groupId;
      return this;
   }

   public DynamicJarQuery nameLike(String name) {
      this.name = name;
      return this;
   }

   public DynamicJarQuery descLike(String desc) {
      this.desc = desc;
      return this;
   }
}
