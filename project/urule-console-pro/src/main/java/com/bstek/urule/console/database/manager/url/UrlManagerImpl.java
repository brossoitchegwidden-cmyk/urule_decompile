package com.bstek.urule.console.database.manager.url;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.UrlConfig;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class UrlManagerImpl implements UrlManager {
   protected UrlManagerImpl() {
   }

   public void add(UrlConfig url) {
      Connection connection = JdbcUtils.getConnection();

      try {
         url.setCreateDate(new Date());
         url.setUpdateDate(new Date());
         url.setId(IDGenerator.getInstance().nextId(IDType.URL_CONFIG));
         PreparedStatement preparedStatement = connection.prepareStatement("insert into URULE_URL_CONFIG (ID_, NAME_,URL_, TYPE_, GROUP_ID_, CREATE_DATE_, UPDATE_DATE_,CREATE_USER_,UPDATE_USER_) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
         preparedStatement.setLong(1, url.getId());
         preparedStatement.setString(2, url.getName());
         preparedStatement.setString(3, url.getUrl());
         preparedStatement.setString(4, url.getType().name());
         preparedStatement.setString(5, url.getGroupId());
         preparedStatement.setTimestamp(6, new Timestamp(url.getCreateDate().getTime()));
         preparedStatement.setTimestamp(7, new Timestamp(url.getUpdateDate().getTime()));
         preparedStatement.setString(8, url.getCreateUser());
         preparedStatement.setString(9, url.getCreateUser());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void delete(long id) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_URL_CONFIG where ID_=?");
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void update(UrlConfig url) {
      Connection connection = JdbcUtils.getConnection();

      try {
         url.setUpdateDate(new Date());
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_URL_CONFIG set NAME_=?, URL_=?, UPDATE_DATE_=? , UPDATE_USER_=? where ID_=?");
         preparedStatement.setString(1, url.getName());
         preparedStatement.setString(2, url.getUrl());
         preparedStatement.setTimestamp(3, new Timestamp(url.getUpdateDate().getTime()));
         preparedStatement.setString(4, url.getUpdateUser());
         preparedStatement.setLong(5, url.getId());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public UrlConfig load(long id) {
      List items = this.newQuery().id(id).list();
      return items.size() > 0 ? (UrlConfig)items.get(0) : null;
   }

   public UrlQuery newQuery() {
      return new UrlQueryImpl();
   }
}
