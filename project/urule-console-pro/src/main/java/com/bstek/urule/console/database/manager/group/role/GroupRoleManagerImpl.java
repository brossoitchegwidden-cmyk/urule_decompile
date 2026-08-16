package com.bstek.urule.console.database.manager.group.role;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.GroupRole;
import com.bstek.urule.console.database.model.User;
import com.bstek.urule.console.database.model.UserRole;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class GroupRoleManagerImpl implements GroupRoleManager {
   public List loadRoles(String var1) {
      Connection var2 = JdbcUtils.getConnection();

      ArrayList var13;
      try {
         String var3 = "select ID_, NAME_, TYPE_, CREATE_DATE_, UPDATE_DATE_ from URULE_GROUP_ROLE WHERE GROUP_ID_=?";
         PreparedStatement var4 = var2.prepareStatement(var3);
         var4.setString(1, var1);
         ArrayList var5 = new ArrayList();
         ResultSet var6 = var4.executeQuery();

         while(var6.next()) {
            GroupRole var7 = new GroupRole();
            var7.setId(var6.getLong(1));
            var7.setName(var6.getString(2));
            var7.setType(var6.getString(3));
            var7.setCreateDate(var6.getTimestamp(4));
            var7.setUpdateDate(var6.getTimestamp(5));
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

   public List loadUserRoles(String var1, String var2) {
      Connection var3 = JdbcUtils.getConnection();

      ArrayList var14;
      try {
         String var4 = "select URULE_GROUP_ROLE.ID_, URULE_GROUP_ROLE.NAME_, URULE_GROUP_ROLE.TYPE_ from URULE_GROUP_ROLE  LEFT JOIN URULE_GROUP_USER_ROLE  ON URULE_GROUP_ROLE.ID_=URULE_GROUP_USER_ROLE.ROLE_ID_ WHERE URULE_GROUP_ROLE.GROUP_ID_=? AND URULE_GROUP_USER_ROLE.USER_ID_=?";
         PreparedStatement var5 = var3.prepareStatement(var4);
         var5.setString(1, var1);
         var5.setString(2, var2);
         ArrayList var6 = new ArrayList();
         ResultSet var7 = var5.executeQuery();

         while(var7.next()) {
            GroupRole var8 = new GroupRole();
            var8.setId(var7.getLong(1));
            var8.setName(var7.getString(2));
            var8.setType(var7.getString(3));
            var6.add(var8);
         }

         JdbcUtils.closeResultSet(var7);
         JdbcUtils.closeStatement(var5);
         var14 = var6;
      } catch (Exception var12) {
         throw new RuleException(var12);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

      return var14;
   }

   public List loadRoleUsers(String var1, long var2) {
      Connection var4 = JdbcUtils.getConnection();

      ArrayList var15;
      try {
         String var5 = "select URULE_GROUP_USER.USER_ID_, URULE_GROUP_USER.USER_NAME_ from URULE_GROUP_USER LEFT JOIN URULE_GROUP_USER_ROLE on URULE_GROUP_USER_ROLE.USER_ID_=URULE_GROUP_USER.USER_ID_ WHERE URULE_GROUP_USER.GROUP_ID_=? and URULE_GROUP_USER_ROLE.ROLE_ID_=?";
         PreparedStatement var6 = var4.prepareStatement(var5);
         var6.setString(1, var1);
         var6.setLong(2, var2);
         ArrayList var7 = new ArrayList();
         ResultSet var8 = var6.executeQuery();

         while(var8.next()) {
            User var9 = new User();
            var9.setId(var8.getString(1));
            var9.setName(var8.getString(2));
            var7.add(var9);
         }

         JdbcUtils.closeResultSet(var8);
         JdbcUtils.closeStatement(var6);
         var15 = var7;
      } catch (Exception var13) {
         throw new RuleException(var13);
      } finally {
         JdbcUtils.closeConnection(var4);
      }

      return var15;
   }

   public void add(GroupRole var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var1.setCreateDate(new Timestamp(System.currentTimeMillis()));
         var1.setUpdateDate(new Timestamp(System.currentTimeMillis()));
         long var3 = IDGenerator.getInstance().nextId(IDType.GROUP_ROLE);
         var1.setId(var3);
         PreparedStatement var5 = var2.prepareStatement("insert into URULE_GROUP_ROLE (ID_, NAME_, TYPE_, GROUP_ID_, CREATE_USER_, CREATE_DATE_,UPDATE_DATE_) values (?, ?, ?, ?, ?, ?, ?)");
         var5.setLong(1, var1.getId());
         var5.setString(2, var1.getName());
         var5.setString(3, var1.getType());
         var5.setString(4, var1.getGroupId());
         var5.setString(5, var1.getCreateUser());
         var5.setTimestamp(6, new Timestamp(var1.getCreateDate().getTime()));
         var5.setTimestamp(7, new Timestamp(var1.getUpdateDate().getTime()));
         var5.executeUpdate();
         JdbcUtils.closeStatement(var5);
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void update(GroupRole var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var1.setUpdateDate(new Timestamp(System.currentTimeMillis()));
         PreparedStatement var3 = var2.prepareStatement("update URULE_GROUP_ROLE set NAME_=?, UPDATE_DATE_=? where ID_=?");
         var3.setString(1, var1.getName());
         var3.setTimestamp(2, new Timestamp(var1.getUpdateDate().getTime()));
         var3.setLong(3, var1.getId());
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void remove(Long var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_GROUP_ROLE where ID_=?");
         var3.setLong(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public boolean checkExist(String var1, String var2) {
      boolean var3 = false;
      Connection var4 = JdbcUtils.getConnection();

      try {
         PreparedStatement var5 = var4.prepareStatement("select ID_ FROM URULE_GROUP_ROLE where GROUP_ID_=? and NAME_=?");
         var5.setString(1, var1);
         var5.setString(2, var2);
         ResultSet var6 = var5.executeQuery();
         if (var6.next()) {
            var3 = true;
         }

         JdbcUtils.closeResultSet(var6);
         JdbcUtils.closeStatement(var5);
      } catch (Exception var10) {
         throw new RuleException(var10);
      } finally {
         JdbcUtils.closeConnection(var4);
      }

      return var3;
   }

   public void addUserRole(String var1, String var2, long var3) {
      Connection var5 = JdbcUtils.getConnection();

      try {
         long var6 = IDGenerator.getInstance().nextId(IDType.GROUP_USER_ROLE);
         PreparedStatement var8 = var5.prepareStatement("insert into URULE_GROUP_USER_ROLE (ID_, GROUP_ID_, USER_ID_, ROLE_ID_) values (?, ?, ?, ?)");
         var8.setLong(1, var6);
         var8.setString(2, var1);
         var8.setString(3, var2);
         var8.setLong(4, var3);
         var8.executeUpdate();
         JdbcUtils.closeStatement(var8);
      } catch (Exception var12) {
         throw new RuleException(var12);
      } finally {
         JdbcUtils.closeConnection(var5);
      }

   }

   public void removeUserRole(String var1, String var2, long var3) {
      Connection var5 = JdbcUtils.getConnection();

      try {
         PreparedStatement var6 = var5.prepareStatement("delete from URULE_GROUP_USER_ROLE WHERE USER_ID_=? and ROLE_ID_=? and GROUP_ID_=?");
         var6.setString(1, var2);
         var6.setLong(2, var3);
         var6.setString(3, var1);
         var6.executeUpdate();
         JdbcUtils.closeStatement(var6);
      } catch (Exception var10) {
         throw new RuleException(var10);
      } finally {
         JdbcUtils.closeConnection(var5);
      }

   }

   public GroupRole get(long var1) {
      GroupRole var3 = null;
      Connection var4 = JdbcUtils.getConnection();

      GroupRole var8;
      try {
         String var5 = "select ID_, NAME_, TYPE_, GROUP_ID_, CREATE_DATE_, UPDATE_DATE_ from URULE_GROUP_ROLE WHERE ID_=?";
         PreparedStatement var6 = var4.prepareStatement(var5);
         var6.setLong(1, var1);
         ResultSet var7 = var6.executeQuery();
         if (var7.next()) {
            var3 = new GroupRole();
            var3.setId(var7.getLong(1));
            var3.setName(var7.getString(2));
            var3.setType(var7.getString(3));
            var3.setGroupId(var7.getString(4));
            var3.setCreateDate(var7.getTimestamp(5));
            var3.setUpdateDate(var7.getTimestamp(6));
         }

         JdbcUtils.closeResultSet(var7);
         JdbcUtils.closeStatement(var6);
         var8 = var3;
      } catch (Exception var12) {
         throw new RuleException(var12);
      } finally {
         JdbcUtils.closeConnection(var4);
      }

      return var8;
   }

   public void removeRoleUsers(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         PreparedStatement var4 = var3.prepareStatement("delete from URULE_GROUP_USER_ROLE WHERE ROLE_ID_=?");
         var4.setLong(1, var1);
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public void removeUserRoles(String var1, String var2) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         PreparedStatement var4 = var3.prepareStatement("delete from URULE_GROUP_USER_ROLE WHERE USER_ID_=? and GROUP_ID_=?");
         var4.setString(1, var2);
         var4.setString(2, var1);
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public GroupRole get(String var1, String var2) {
      GroupRole var3 = null;
      Connection var4 = JdbcUtils.getConnection();

      GroupRole var8;
      try {
         String var5 = "select ID_, NAME_, TYPE_, CREATE_DATE_, UPDATE_DATE_ from URULE_GROUP_ROLE WHERE GROUP_ID_=? and NAME_=?";
         PreparedStatement var6 = var4.prepareStatement(var5);
         var6.setString(1, var1);
         var6.setString(2, var2);
         ResultSet var7 = var6.executeQuery();
         if (var7.next()) {
            var3 = new GroupRole();
            var3.setId(var7.getLong(1));
            var3.setName(var7.getString(2));
            var3.setType(var7.getString(3));
            var3.setCreateDate(var7.getTimestamp(4));
            var3.setUpdateDate(var7.getTimestamp(5));
         }

         JdbcUtils.closeResultSet(var7);
         JdbcUtils.closeStatement(var6);
         var8 = var3;
      } catch (Exception var12) {
         throw new RuleException(var12);
      } finally {
         JdbcUtils.closeConnection(var4);
      }

      return var8;
   }

   public UserRole getUserRole(String var1, long var2) {
      UserRole var4 = null;
      Connection var5 = JdbcUtils.getConnection();

      UserRole var9;
      try {
         String var6 = "select ID_, ROLE_ID_, USER_ID_ from URULE_GROUP_USER_ROLE WHERE ROLE_ID_=? and USER_ID_=?";
         PreparedStatement var7 = var5.prepareStatement(var6);
         var7.setLong(1, var2);
         var7.setString(2, var1);
         ResultSet var8 = var7.executeQuery();
         if (var8.next()) {
            var4 = new UserRole();
            var4.setId(var8.getLong(1));
            var4.setRoleId(var8.getLong(2));
            var4.setUserId(var8.getString(3));
         }

         JdbcUtils.closeResultSet(var8);
         JdbcUtils.closeStatement(var7);
         var9 = var4;
      } catch (Exception var13) {
         throw new RuleException(var13);
      } finally {
         JdbcUtils.closeConnection(var5);
      }

      return var9;
   }

   public void removeByGroupId(String var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_GROUP_USER_ROLE where GROUP_ID_=?");
         var3.setString(1, var1);
         var3.executeUpdate();
         var3 = var2.prepareStatement("delete FROM URULE_GROUP_ROLE where GROUP_ID_=?");
         var3.setString(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }
}
