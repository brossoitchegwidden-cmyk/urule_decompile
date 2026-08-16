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

   public void add(UrlConfig var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var1.setCreateDate(new Date());
         var1.setUpdateDate(new Date());
         var1.setId(IDGenerator.getInstance().nextId(IDType.URL_CONFIG));
         PreparedStatement var3 = var2.prepareStatement("insert into URULE_URL_CONFIG (ID_, NAME_,URL_, TYPE_, GROUP_ID_, CREATE_DATE_, UPDATE_DATE_,CREATE_USER_,UPDATE_USER_) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
         var3.setLong(1, var1.getId());
         var3.setString(2, var1.getName());
         var3.setString(3, var1.getUrl());
         var3.setString(4, var1.getType().name());
         var3.setString(5, var1.getGroupId());
         var3.setTimestamp(6, new Timestamp(var1.getCreateDate().getTime()));
         var3.setTimestamp(7, new Timestamp(var1.getUpdateDate().getTime()));
         var3.setString(8, var1.getCreateUser());
         var3.setString(9, var1.getCreateUser());
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void delete(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         PreparedStatement var4 = var3.prepareStatement("delete FROM URULE_URL_CONFIG where ID_=?");
         var4.setLong(1, var1);
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public void update(UrlConfig var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var1.setUpdateDate(new Date());
         PreparedStatement var3 = var2.prepareStatement("update URULE_URL_CONFIG set NAME_=?, URL_=?, UPDATE_DATE_=? , UPDATE_USER_=? where ID_=?");
         var3.setString(1, var1.getName());
         var3.setString(2, var1.getUrl());
         var3.setTimestamp(3, new Timestamp(var1.getUpdateDate().getTime()));
         var3.setString(4, var1.getUpdateUser());
         var3.setLong(5, var1.getId());
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public UrlConfig load(long var1) {
      List var3 = this.newQuery().id(var1).list();
      return var3.size() > 0 ? (UrlConfig)var3.get(0) : null;
   }

   public UrlQuery newQuery() {
      return new UrlQueryImpl();
   }
}
