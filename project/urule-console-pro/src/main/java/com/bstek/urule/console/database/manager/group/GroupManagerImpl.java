package com.bstek.urule.console.database.manager.group;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.manager.group.user.UserQuery;
import com.bstek.urule.console.database.manager.group.user.UserQueryImpl;
import com.bstek.urule.console.database.model.Group;
import com.bstek.urule.console.database.model.User;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class GroupManagerImpl implements GroupManager {
   public Group get(String var1) {
      Connection var2 = JdbcUtils.getConnection();

      Group var7;
      try {
         String var3 = "SELECT ID_, NAME_, CREATE_USER_, CREATE_DATE_, DESC_ FROM URULE_GROUP  WHERE URULE_GROUP.ID_=?";
         PreparedStatement var4 = var2.prepareStatement(var3);
         var4.setString(1, var1);
         Group var5 = null;
         ResultSet var6 = var4.executeQuery();
         if (var6.next()) {
            var5 = new Group();
            var5.setId(var6.getString(1));
            var5.setName(var6.getString(2));
            var5.setCreateUser(var6.getString(3));
            var5.setCreateDate(var6.getTimestamp(4));
            var5.setDesc(var6.getString(5));
         }

         JdbcUtils.closeResultSet(var6);
         JdbcUtils.closeStatement(var4);
         var7 = var5;
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

      return var7;
   }

   public void add(Group var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var1.setCreateDate(new Timestamp(System.currentTimeMillis()));
         var1.setUpdateDate(new Timestamp(System.currentTimeMillis()));
         PreparedStatement var3 = var2.prepareStatement("insert into URULE_GROUP (ID_, NAME_, DESC_, CREATE_USER_,CREATE_DATE_) values (?, ?, ?, ?, ?)");
         var3.setString(1, var1.getId());
         var3.setString(2, var1.getName());
         var3.setString(3, var1.getDesc());
         var3.setString(4, var1.getCreateUser());
         var3.setTimestamp(5, new Timestamp(var1.getCreateDate().getTime()));
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void update(Group var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var1.setUpdateDate(new Timestamp(System.currentTimeMillis()));
         PreparedStatement var3 = var2.prepareStatement("update URULE_GROUP set NAME_=?, DESC_=?, UPDATE_USER_=?, UPDATE_DATE_=? where ID_=?");
         var3.setString(1, var1.getName());
         var3.setString(2, var1.getDesc());
         var3.setString(3, var1.getUpdateUser());
         var3.setTimestamp(4, new Timestamp(var1.getUpdateDate().getTime()));
         var3.setString(5, var1.getId());
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
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_GROUP where ID_=?");
         var3.setString(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void addGroupUser(String var1, String var2, String var3) {
      Connection var4 = JdbcUtils.getConnection();

      try {
         PreparedStatement var5 = var4.prepareStatement("INSERT INTO URULE_GROUP_USER (ID_, GROUP_ID_, USER_ID_, USER_NAME_) VALUES (?, ?, ?, ?)");
         var5.setLong(1, IDGenerator.getInstance().nextId(IDType.GROUP_USER));
         var5.setString(2, var1);
         var5.setString(3, var2);
         var5.setString(4, var3);
         var5.executeUpdate();
         JdbcUtils.closeStatement(var5);
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var4);
      }

   }

   public int count() {
      int var1 = 0;
      Connection var2 = JdbcUtils.getConnection();

      int var6;
      try {
         String var3 = "SELECT count(*) GROUP_COUNT_ FROM URULE_GROUP";
         PreparedStatement var4 = var2.prepareStatement(var3);
         ResultSet var5 = var4.executeQuery();
         if (var5.next()) {
            var1 = var5.getInt(1);
         }

         JdbcUtils.closeResultSet(var5);
         JdbcUtils.closeStatement(var4);
         var6 = var1;
      } catch (Exception var10) {
         throw new RuleException(var10);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

      return var6;
   }

   public void removeGroupUser(String var1, String var2) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         PreparedStatement var4 = var3.prepareStatement("delete FROM URULE_GROUP_USER where GROUP_ID_=? AND USER_ID_=?");
         var4.setString(1, var1);
         var4.setString(2, var2);
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public void removeGroupUsers(String var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_GROUP_USER where GROUP_ID_=?");
         var3.setString(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public List getUsers(String var1) {
      Connection var2 = JdbcUtils.getConnection();

      ArrayList var13;
      try {
         String var3 = "SELECT USER_ID_, CREATE_DATE_ FROM URULE_GROUP_USER  WHERE GROUP_ID_=?";
         PreparedStatement var4 = var2.prepareStatement(var3);
         var4.setString(1, var1);
         ArrayList var5 = new ArrayList();
         ResultSet var6 = var4.executeQuery();

         while(var6.next()) {
            User var7 = new User();
            var7.setId(var6.getString(1));
            var7.setCreateDate(var6.getTimestamp(2));
            var5.add(var7);
         }

         JdbcUtils.closeResultSet(var6);
         JdbcUtils.closeStatement(var4);
         var13 = var5;
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

      return var13;
   }

   public GroupQuery createQuery() {
      return new GroupQueryImpl();
   }

   public UserQuery createUserQuery() {
      return new UserQueryImpl();
   }

   public User getGroupUser(String var1, String var2) {
      Connection var3 = JdbcUtils.getConnection();

      User var8;
      try {
         String var4 = "SELECT USER_ID_, CREATE_DATE_ FROM URULE_GROUP_USER  WHERE GROUP_ID_=? and USER_ID_=?";
         PreparedStatement var5 = var3.prepareStatement(var4);
         var5.setString(1, var1);
         var5.setString(2, var2);
         User var6 = null;
         ResultSet var7 = var5.executeQuery();
         if (var7.next()) {
            var6 = new User();
            var6.setId(var7.getString(1));
            var6.setCreateDate(var7.getTimestamp(2));
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
}
