package com.bstek.urule.console.database.manager.user;

import com.bstek.urule.console.database.model.User;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UserManagerImpl implements UserManager {
   public void add(User var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var1.setCreateDate(new Timestamp(System.currentTimeMillis()));
         PreparedStatement var3 = var2.prepareStatement("insert into URULE_USER (ID_, NAME_, PASSWORD_, EMAIL_, DESC_, CREATE_USER_,CREATE_DATE_) values (?, ?, ?, ?, ?,?, ?)");
         var3.setString(1, var1.getId());
         var3.setString(2, var1.getName());
         var3.setString(3, var1.getPassword());
         var3.setString(4, var1.getEmail());
         var3.setString(5, var1.getDesc());
         var3.setString(6, var1.getCreateUser());
         var3.setTimestamp(7, new Timestamp(var1.getCreateDate().getTime()));
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void update(User var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var1.setUpdateDate(new Timestamp(System.currentTimeMillis()));
         PreparedStatement var3 = var2.prepareStatement("update URULE_USER set PASSWORD_=?, EMAIL_=?, SECRET_KEY_=?, DESC_=?, UPDATE_DATE_=?, EXPIR_DATE_=?, UPDATE_USER_=?, NAME_=?  where ID_=?");
         var3.setString(1, var1.getPassword());
         var3.setString(2, var1.getEmail());
         var3.setString(3, var1.getSecretKey());
         var3.setString(4, var1.getDesc());
         var3.setTimestamp(5, new Timestamp(var1.getUpdateDate().getTime()));
         var3.setTimestamp(6, var1.getExpirDate() == null ? null : new Timestamp(var1.getExpirDate().getTime()));
         var3.setString(7, var1.getId());
         var3.setString(8, var1.getName());
         var3.setString(9, var1.getUpdateUser());
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void remove(String var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_USER where ID_=?");
         var3.setString(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public User get(String var1) {
      Connection var2 = JdbcUtils.getConnection();

      User var7;
      try {
         String var3 = "select ID_, NAME_, PASSWORD_, EMAIL_, SECRET_KEY_, DESC_, EXPIR_DATE_, CREATE_DATE_, UPDATE_DATE_ from URULE_USER where ID_=?";
         PreparedStatement var4 = var2.prepareStatement(var3);
         var4.setString(1, var1);
         ResultSet var5 = var4.executeQuery();
         User var6 = null;
         if (var5.next()) {
            var6 = new User();
            var6.setId(var5.getString(1));
            var6.setName(var5.getString(2));
            var6.setPassword(var5.getString(3));
            var6.setEmail(var5.getString(4));
            var6.setSecretKey(var5.getString(5));
            var6.setDesc(var5.getString(6));
            var6.setExpirDate(var5.getTimestamp(7));
            var6.setCreateDate(var5.getTimestamp(8));
            var6.setUpdateDate(var5.getTimestamp(9));
         }

         JdbcUtils.closeResultSet(var5);
         JdbcUtils.closeStatement(var4);
         var7 = var6;
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

      return var7;
   }

   public void changePassword(String var1, String var2) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         String var4 = "update URULE_USER set PASSWORD_=? where ID_=?";
         PreparedStatement var5 = var3.prepareStatement(var4);
         var5.setString(1, var1);
         var5.setString(2, var2);
         var5.executeUpdate();
         JdbcUtils.closeStatement(var5);
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public List getUsersByGroupId(String var1) {
      Connection var2 = JdbcUtils.getConnection();

      ArrayList var13;
      try {
         String var3 = "select URULE_USER.ID_, URULE_USER.NAME_, URULE_USER.PASSWORD_, URULE_USER.EMAIL_, URULE_USER.SECRET_KEY_, URULE_USER.DESC_, URULE_USER.EXPIR_DATE_, URULE_USER.CREATE_DATE_, URULE_USER.UPDATE_DATE_ from URULE_USER left join URULE_GROUP_USER on URULE_USER.ID_=URULE_GROUP_USER.USER_ID_  where URULE_GROUP_USER.GROUP_ID_ = ?";
         PreparedStatement var4 = var2.prepareStatement(var3);
         var4.setString(1, var1);
         ResultSet var5 = var4.executeQuery();
         ArrayList var6 = new ArrayList();

         while(var5.next()) {
            User var7 = new User();
            var7.setId(var5.getString(1));
            var7.setName(var5.getString(2));
            var7.setEmail(var5.getString(3));
            var7.setSecretKey(var5.getString(4));
            var7.setEnable(var5.getBoolean(5));
            var7.setDesc(var5.getString(6));
            var7.setExpirDate(var5.getTimestamp(7));
            var7.setCreateDate(var5.getTimestamp(8));
            var7.setUpdateDate(var5.getTimestamp(9));
            var6.add(var7);
         }

         JdbcUtils.closeResultSet(var5);
         JdbcUtils.closeStatement(var4);
         var13 = var6;
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

      return var13;
   }

   public List getUsersByRoleId(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      ArrayList var14;
      try {
         String var4 = "select URULE_USER.ID_, URULE_USER.NAME_, URULE_USER.PASSWORD_, URULE_USER.EMAIL_, URULE_USER.SECRET_KEY_, URULE_USER.DESC_, URULE_USER.EXPIR_DATE_, URULE_USER.CREATE_DATE_, URULE_USER.UPDATE_DATE_ from URULE_USER left join URULE_GROUP_USER_ROLE on URULE_USER.ID_=URULE_GROUP_USER_ROLE.USER_ID_  where URULE_GROUP_USER_ROLE.ROLE_ID_ = ?";
         PreparedStatement var5 = var3.prepareStatement(var4);
         var5.setLong(1, var1);
         ResultSet var6 = var5.executeQuery();
         ArrayList var7 = new ArrayList();

         while(var6.next()) {
            User var8 = new User();
            var8.setId(var6.getString(1));
            var8.setName(var6.getString(2));
            var8.setEmail(var6.getString(3));
            var8.setSecretKey(var6.getString(4));
            var8.setEnable(var6.getBoolean(5));
            var8.setDesc(var6.getString(6));
            var8.setExpirDate(var6.getTimestamp(7));
            var8.setCreateDate(var6.getTimestamp(8));
            var8.setUpdateDate(var6.getTimestamp(9));
            var7.add(var8);
         }

         JdbcUtils.closeResultSet(var6);
         JdbcUtils.closeStatement(var5);
         var14 = var7;
      } catch (Exception var12) {
         throw new RuleException(var12);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

      return var14;
   }

   public List getUsersByProjectId(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      ArrayList var14;
      try {
         String var4 = "select URULE_USER.ID_, NAME_, PASSWORD_, EMAIL_, SECRET_KEY_, DESC_, EXPIR_DATE_, URULE_PROJECT_USER.CREATE_DATE_, UPDATE_DATE_ from URULE_USER left join URULE_PROJECT_USER on URULE_USER.ID_=URULE_PROJECT_USER.USER_ID_  where URULE_PROJECT_USER.PROJECT_ID_=? order by URULE_PROJECT_USER.CREATE_DATE_";
         PreparedStatement var5 = var3.prepareStatement(var4);
         var5.setLong(1, var1);
         ResultSet var6 = var5.executeQuery();
         ArrayList var7 = new ArrayList();

         while(var6.next()) {
            User var8 = new User();
            var8.setId(var6.getString(1));
            var8.setName(var6.getString(2));
            var8.setEmail(var6.getString(3));
            var8.setSecretKey(var6.getString(4));
            var8.setEnable(var6.getBoolean(5));
            var8.setDesc(var6.getString(6));
            var8.setExpirDate(var6.getTimestamp(7));
            var8.setCreateDate(var6.getTimestamp(8));
            var8.setUpdateDate(var6.getTimestamp(9));
            var7.add(var8);
         }

         JdbcUtils.closeResultSet(var6);
         JdbcUtils.closeStatement(var5);
         var14 = var7;
      } catch (Exception var12) {
         throw new RuleException(var12);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

      return var14;
   }

   public User getByEmail(String var1) {
      Connection var2 = JdbcUtils.getConnection();

      User var7;
      try {
         String var3 = "select ID_, NAME_, PASSWORD_, EMAIL_, SECRET_KEY_, DESC_, EXPIR_DATE_, CREATE_DATE_, UPDATE_DATE_ from URULE_USER where EMAIL_=?";
         PreparedStatement var4 = var2.prepareStatement(var3);
         var4.setString(1, var1);
         ResultSet var5 = var4.executeQuery();
         User var6 = null;
         if (var5.next()) {
            var6 = new User();
            var6.setId(var5.getString(1));
            var6.setName(var5.getString(2));
            var6.setPassword(var5.getString(3));
            var6.setEmail(var5.getString(4));
            var6.setSecretKey(var5.getString(5));
            var6.setDesc(var5.getString(6));
            var6.setExpirDate(var5.getTimestamp(7));
            var6.setCreateDate(var5.getTimestamp(8));
            var6.setUpdateDate(var5.getTimestamp(9));
         }

         JdbcUtils.closeResultSet(var5);
         JdbcUtils.closeStatement(var4);
         var7 = var6;
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

      return var7;
   }

   public UserQuery newQuery() {
      return new UserQueryImpl();
   }

   public User getGroupUser(String var1, String var2) {
      Connection var3 = JdbcUtils.getConnection();

      User var8;
      try {
         String var4 = "select USER_ID_, USER_NAME_ from URULE_GROUP_USER where GROUP_ID_=? and USER_ID_=?";
         PreparedStatement var5 = var3.prepareStatement(var4);
         var5.setString(1, var1);
         var5.setString(2, var2);
         User var6 = null;
         ResultSet var7 = var5.executeQuery();
         if (var7.next()) {
            var6 = new User();
            var6.setId(var7.getString(1));
            var6.setName(var7.getString(2));
         }

         JdbcUtils.closeResultSet(var7);
         JdbcUtils.closeStatement(var5);
         var8 = var6;
      } catch (Exception var12) {
         throw new RuleException(var12);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

      return var8;
   }

   public User getProjectUser(long var1, String var3) {
      Connection var4 = JdbcUtils.getConnection();

      User var9;
      try {
         String var5 = "select USER_ID_, USER_NAME_ from URULE_PROJECT_USER where PROJECT_ID_=? and USER_ID_=?";
         PreparedStatement var6 = var4.prepareStatement(var5);
         var6.setLong(1, var1);
         var6.setString(2, var3);
         User var7 = null;
         ResultSet var8 = var6.executeQuery();
         if (var8.next()) {
            var7 = new User();
            var7.setId(var8.getString(1));
            var7.setName(var8.getString(2));
         }

         JdbcUtils.closeResultSet(var8);
         JdbcUtils.closeStatement(var6);
         var9 = var7;
      } catch (Exception var13) {
         throw new RuleException(var13);
      } finally {
         JdbcUtils.closeConnection(var4);
      }

      return var9;
   }

   public void changeEmail(String var1, String var2) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         String var4 = "update URULE_USER set EMAIL_=? where ID_=?";
         PreparedStatement var5 = var3.prepareStatement(var4);
         var5.setString(1, var2);
         var5.setString(2, var1);
         var5.executeUpdate();
         JdbcUtils.closeStatement(var5);
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }
}
