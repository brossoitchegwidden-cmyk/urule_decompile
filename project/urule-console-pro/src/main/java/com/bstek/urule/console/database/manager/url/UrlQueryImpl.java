package com.bstek.urule.console.database.manager.url;

import com.bstek.urule.console.database.model.UrlConfig;
import com.bstek.urule.console.database.model.UrlType;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UrlQueryImpl implements UrlQuery {
   private String name;
   private String url;
   private UrlType type;
   private String groupId;
   private Long id;
   private List queryParameters = new ArrayList();

   protected UrlQueryImpl() {
   }

   public List list() {
      Connection connection = JdbcUtils.getConnection();
      String text = "select ID_,NAME_,URL_,TYPE_,GROUP_ID_,CREATE_USER_,UPDATE_USER_,CREATE_DATE_,UPDATE_DATE_ from URULE_URL_CONFIG";
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
            UrlConfig urlConfig = new UrlConfig();
            urlConfig.setId(resultSet.getLong(1));
            urlConfig.setName(resultSet.getString(2));
            urlConfig.setUrl(resultSet.getString(3));
            urlConfig.setType(UrlType.valueOf(resultSet.getString(4)));
            urlConfig.setGroupId(resultSet.getString(5));
            urlConfig.setCreateUser(resultSet.getString(6));
            urlConfig.setUpdateUser(resultSet.getString(7));
            urlConfig.setCreateDate(resultSet.getTimestamp(8));
            urlConfig.setUpdateDate(resultSet.getTimestamp(9));
            items.add(urlConfig);
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

      if (this.url != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" URL_ like ?");
         this.queryParameters.add("%" + this.url + "%");
      }

      if (this.type != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" TYPE_=?");
         this.queryParameters.add(this.type.name());
      }

      if (this.groupId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" GROUP_ID_=?");
         this.queryParameters.add(this.groupId);
      }

      if (this.id != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" ID_=?");
         this.queryParameters.add(this.id);
      }

      return stringBuilder;
   }

   public UrlQuery nameLike(String name) {
      this.name = name;
      return this;
   }

   public UrlQuery urlLike(String url) {
      this.url = url;
      return this;
   }

   public UrlQuery type(UrlType type) {
      this.type = type;
      return this;
   }

   public UrlQuery groupId(String groupId) {
      this.groupId = groupId;
      return this;
   }

   public UrlQuery id(long id) {
      this.id = id;
      return this;
   }
}
