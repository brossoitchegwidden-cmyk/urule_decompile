package com.bstek.urule.console.database.manager.project.role;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.ProjectRole;
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

public class ProjectRoleManagerImpl implements ProjectRoleManager {
   public List loadRoles(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      ArrayList var14;
      try {
         String var4 = "select ID_, NAME_, TYPE_, CREATE_DATE_, UPDATE_DATE_ from URULE_PROJECT_ROLE WHERE PROJECT_ID_=?";
         PreparedStatement var5 = var3.prepareStatement(var4);
         var5.setLong(1, var1);
         ArrayList var6 = new ArrayList();
         ResultSet var7 = var5.executeQuery();

         while(var7.next()) {
            ProjectRole var8 = new ProjectRole();
            var8.setId(var7.getLong(1));
            var8.setName(var7.getString(2));
            var8.setType(var7.getString(3));
            var8.setCreateDate(var7.getTimestamp(4));
            var8.setUpdateDate(var7.getTimestamp(5));
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

   public List loadUserRoles(long var1, String var3) {
      Connection var4 = JdbcUtils.getConnection();

      ArrayList var15;
      try {
         String var5 = "select URULE_PROJECT_ROLE.ID_, URULE_PROJECT_ROLE.NAME_, URULE_PROJECT_ROLE.TYPE_ from URULE_PROJECT_ROLE  LEFT JOIN URULE_PROJECT_USER_ROLE  ON URULE_PROJECT_ROLE.ID_=URULE_PROJECT_USER_ROLE.ROLE_ID_ WHERE URULE_PROJECT_ROLE.PROJECT_ID_=? AND URULE_PROJECT_USER_ROLE.USER_ID_=?";
         PreparedStatement var6 = var4.prepareStatement(var5);
         var6.setLong(1, var1);
         var6.setString(2, var3);
         ArrayList var7 = new ArrayList();
         ResultSet var8 = var6.executeQuery();

         while(var8.next()) {
            ProjectRole var9 = new ProjectRole();
            var9.setId(var8.getLong(1));
            var9.setName(var8.getString(2));
            var9.setType(var8.getString(3));
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

   public List loadRoleUsers(long var1, long var3) {
      Connection var5 = JdbcUtils.getConnection();

      ArrayList var16;
      try {
         String var6 = "select URULE_PROJECT_USER.USER_ID_, URULE_PROJECT_USER.USER_NAME_ from URULE_PROJECT_USER LEFT JOIN URULE_PROJECT_USER_ROLE on URULE_PROJECT_USER_ROLE.USER_ID_=URULE_PROJECT_USER.USER_ID_ WHERE URULE_PROJECT_USER.PROJECT_ID_=? and URULE_PROJECT_USER_ROLE.ROLE_ID_=?";
         PreparedStatement var7 = var5.prepareStatement(var6);
         var7.setLong(1, var1);
         var7.setLong(2, var3);
         ArrayList var8 = new ArrayList();
         ResultSet var9 = var7.executeQuery();

         while(var9.next()) {
            User var10 = new User();
            var10.setId(var9.getString(1));
            var10.setName(var9.getString(2));
            var8.add(var10);
         }

         JdbcUtils.closeResultSet(var9);
         JdbcUtils.closeStatement(var7);
         var16 = var8;
      } catch (Exception var14) {
         throw new RuleException(var14);
      } finally {
         JdbcUtils.closeConnection(var5);
      }

      return var16;
   }

   public void add(ProjectRole var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var1.setCreateDate(new Timestamp(System.currentTimeMillis()));
         var1.setUpdateDate(new Timestamp(System.currentTimeMillis()));
         long var3 = IDGenerator.getInstance().nextId(IDType.PROJECT_ROLE);
         var1.setId(var3);
         PreparedStatement var5 = var2.prepareStatement("insert into URULE_PROJECT_ROLE (ID_, NAME_, TYPE_, PROJECT_ID_, CREATE_USER_, CREATE_DATE_,UPDATE_DATE_) values (?, ?, ?, ?, ?, ?, ?)");
         var5.setLong(1, var1.getId());
         var5.setString(2, var1.getName());
         var5.setString(3, var1.getType());
         var5.setLong(4, var1.getProjectId());
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

   public void update(ProjectRole var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var1.setUpdateDate(new Timestamp(System.currentTimeMillis()));
         PreparedStatement var3 = var2.prepareStatement("update URULE_PROJECT_ROLE set NAME_=?, UPDATE_USER_=?, UPDATE_DATE_=? where ID_=?");
         var3.setString(1, var1.getName());
         var3.setString(2, var1.getUpdateUser());
         var3.setTimestamp(3, new Timestamp(var1.getUpdateDate().getTime()));
         var3.setLong(4, var1.getId());
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
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_PROJECT_ROLE where ID_=?");
         var3.setLong(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public ProjectRole get(long var1) {
      ProjectRole var3 = null;
      Connection var4 = JdbcUtils.getConnection();

      ProjectRole var8;
      try {
         String var5 = "select ID_, NAME_, TYPE_, PROJECT_ID_, CREATE_DATE_, UPDATE_DATE_ from URULE_PROJECT_ROLE WHERE ID_=?";
         PreparedStatement var6 = var4.prepareStatement(var5);
         var6.setLong(1, var1);
         ResultSet var7 = var6.executeQuery();
         if (var7.next()) {
            var3 = new ProjectRole();
            var3.setId(var7.getLong(1));
            var3.setName(var7.getString(2));
            var3.setType(var7.getString(3));
            var3.setProjectId(var7.getLong(4));
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

   public boolean checkExist(long var1, String var3) {
      boolean var4 = false;
      Connection var5 = JdbcUtils.getConnection();

      try {
         PreparedStatement var6 = var5.prepareStatement("select ID_ FROM URULE_PROJECT_ROLE where PROJECT_ID_=? and NAME_=?");
         var6.setLong(1, var1);
         var6.setString(2, var3);
         ResultSet var7 = var6.executeQuery();
         if (var7.next()) {
            var4 = true;
         }

         JdbcUtils.closeResultSet(var7);
         JdbcUtils.closeStatement(var6);
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var5);
      }

      return var4;
   }

   public void addUserRole(long var1, String var3, long var4) {
      Connection var6 = JdbcUtils.getConnection();

      try {
         long var7 = IDGenerator.getInstance().nextId(IDType.PROJECT_ROLE);
         PreparedStatement var9 = var6.prepareStatement("insert into URULE_PROJECT_USER_ROLE (ID_, PROJECT_ID_, USER_ID_, ROLE_ID_) values (?, ?, ?, ?)");
         var9.setLong(1, var7);
         var9.setLong(2, var1);
         var9.setString(3, var3);
         var9.setLong(4, var4);
         var9.executeUpdate();
         JdbcUtils.closeStatement(var9);
      } catch (Exception var13) {
         throw new RuleException(var13);
      } finally {
         JdbcUtils.closeConnection(var6);
      }

   }

   public UserRole getUserRole(String var1, long var2) {
      UserRole var4 = null;
      Connection var5 = JdbcUtils.getConnection();

      UserRole var9;
      try {
         String var6 = "select ID_, ROLE_ID_, USER_ID_ from URULE_PROJECT_USER_ROLE WHERE ROLE_ID_=? and USER_ID_=?";
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

   public void removeUserRole(String var1, long var2) {
      Connection var4 = JdbcUtils.getConnection();

      try {
         PreparedStatement var5 = var4.prepareStatement("delete from URULE_PROJECT_USER_ROLE WHERE USER_ID_=? and ROLE_ID_=?");
         var5.setString(1, var1);
         var5.setLong(2, var2);
         var5.executeUpdate();
         JdbcUtils.closeStatement(var5);
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var4);
      }

   }

   public void removeRoleUsers(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         PreparedStatement var4 = var3.prepareStatement("delete from URULE_PROJECT_USER_ROLE WHERE ROLE_ID_=?");
         var4.setLong(1, var1);
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public void removeUserRoles(String var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete from URULE_PROJECT_USER_ROLE WHERE USER_ID_=?");
         var3.setString(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public ProjectRole get(long var1, String var3) {
      ProjectRole var4 = null;
      Connection var5 = JdbcUtils.getConnection();

      ProjectRole var9;
      try {
         String var6 = "select ID_, NAME_, TYPE_, CREATE_DATE_, UPDATE_DATE_ from URULE_PROJECT_ROLE WHERE PROJECT_ID_=? and NAME_=?";
         PreparedStatement var7 = var5.prepareStatement(var6);
         var7.setLong(1, var1);
         var7.setString(2, var3);
         ResultSet var8 = var7.executeQuery();
         if (var8.next()) {
            var4 = new ProjectRole();
            var4.setId(var8.getLong(1));
            var4.setName(var8.getString(2));
            var4.setType(var8.getString(3));
            var4.setCreateDate(var8.getTimestamp(4));
            var4.setUpdateDate(var8.getTimestamp(5));
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

   public void removeByProjectId(Long var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_PROJECT_USER_ROLE where PROJECT_ID_=?");
         var3.setLong(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
         var3 = var2.prepareStatement("delete FROM URULE_PROJECT_ROLE where PROJECT_ID_=?");
         var3.setLong(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }
}
