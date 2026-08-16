package com.bstek.urule.console.database.manager.project;

import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.manager.project.user.UserQuery;
import com.bstek.urule.console.database.manager.project.user.UserQueryImpl;
import com.bstek.urule.console.database.model.ApplyType;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.ProjectViewModel;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ProjectManagerImpl implements ProjectManager {
   public Project get(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      Project var13;
      try {
         PreparedStatement var4 = var3.prepareStatement("select ID_, NAME_, TYPE_, VIEW_MODEL_, DESC_, GROUP_ID_, APPROVE_USER_ENABLE_, APPROVE_USER_DISABLE_, APPROVE_USER_DEPLOY_, CREATE_USER_, CREATE_DATE_, UPDATE_DATE_, UPDATE_USER_ from URULE_PROJECT where ID_=?");
         var4.setLong(1, var1);
         ResultSet var5 = var4.executeQuery();
         Project var6 = new Project();
         if (var5.next()) {
            var6.setId(var5.getLong("ID_"));
            var6.setName(var5.getString("NAME_"));
            var6.setType(var5.getString("TYPE_"));
            String var7 = var5.getString("VIEW_MODEL_");
            if (StringUtils.isNotBlank(var7)) {
               var6.setViewModel(ProjectViewModel.valueOf(var7));
            } else {
               var6.setViewModel(ProjectViewModel.category);
            }

            var6.setDesc(var5.getString("DESC_"));
            var6.setGroupId(var5.getString("GROUP_ID_"));
            var6.setEnableApproveUser(var5.getString("APPROVE_USER_ENABLE_"));
            var6.setDisableApproveUser(var5.getString("APPROVE_USER_DISABLE_"));
            var6.setDeployApproveUser(var5.getString("APPROVE_USER_DEPLOY_"));
            var6.setCreateUser(var5.getString("CREATE_USER_"));
            var6.setCreateDate(var5.getTimestamp("CREATE_DATE_"));
            var6.setUpdateDate(var5.getTimestamp("UPDATE_DATE_"));
            var6.setUpdateUser(var5.getString("UPDATE_USER_"));
         }

         JdbcUtils.closeResultSet(var5);
         JdbcUtils.closeStatement(var4);
         var13 = var6;
      } catch (Exception var11) {
         throw new InfoException(var11);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

      return var13;
   }

   public void add(Project var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var1.setCreateDate(new Timestamp(System.currentTimeMillis()));
         var1.setUpdateDate(new Timestamp(System.currentTimeMillis()));
         PreparedStatement var3 = var2.prepareStatement("insert into URULE_PROJECT (ID_, NAME_, TYPE_, VIEW_MODEL_, DESC_, GROUP_ID_, CREATE_DATE_, UPDATE_DATE_,CREATE_USER_,UPDATE_USER_,APPROVE_USER_ENABLE_,APPROVE_USER_DISABLE_,APPROVE_USER_DEPLOY_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         if (var1.getId() == null || var1.getId() == 0L) {
            long var4 = IDGenerator.getInstance().nextId(IDType.PROJECT);
            var1.setId(var4);
         }

         var3.setLong(1, var1.getId());
         var3.setString(2, var1.getName());
         var3.setString(3, var1.getType());
         var3.setString(4, var1.getViewModel() != null ? var1.getViewModel().name() : ProjectViewModel.category.name());
         var3.setString(5, var1.getDesc());
         var3.setString(6, var1.getGroupId());
         var3.setTimestamp(7, new Timestamp(var1.getCreateDate().getTime()));
         var3.setTimestamp(8, new Timestamp(var1.getUpdateDate().getTime()));
         var3.setString(9, var1.getCreateUser());
         var3.setString(10, var1.getCreateUser());
         var3.setString(11, var1.getCreateUser());
         var3.setString(12, var1.getCreateUser());
         var3.setString(13, var1.getCreateUser());
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void update(Project var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var1.setUpdateDate(new Timestamp(System.currentTimeMillis()));
         PreparedStatement var3 = var2.prepareStatement("update URULE_PROJECT set NAME_=?, TYPE_=?, VIEW_MODEL_=?, DESC_=?, UPDATE_USER_=?, UPDATE_DATE_=? where ID_=?");
         var3.setString(1, var1.getName());
         var3.setString(2, var1.getType());
         var3.setString(3, var1.getViewModel() != null ? var1.getViewModel().name() : ProjectViewModel.category.name());
         var3.setString(4, var1.getDesc());
         var3.setString(5, var1.getUpdateUser());
         var3.setTimestamp(6, new Timestamp(var1.getUpdateDate().getTime()));
         var3.setLong(7, var1.getId());
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void remove(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         PreparedStatement var4 = var3.prepareStatement("delete FROM URULE_PROJECT where ID_=?");
         var4.setLong(1, var1);
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public List getProjectsByGroupId(String var1) {
      Connection var2 = JdbcUtils.getConnection();

      ArrayList var14;
      try {
         String var3 = "select ID_, NAME_, TYPE_, VIEW_MODEL_, DESC_, GROUP_ID_, CREATE_DATE_, UPDATE_DATE_, UPDATE_USER_ from URULE_PROJECT WHERE GROUP_ID_=?";
         PreparedStatement var4 = var2.prepareStatement(var3);
         var4.setString(1, var1);
         ArrayList var5 = new ArrayList();
         ResultSet var6 = var4.executeQuery();

         while(var6.next()) {
            Project var7 = new Project();
            var7.setId(var6.getLong("ID_"));
            var7.setName(var6.getString("NAME_"));
            var7.setType(var6.getString("TYPE_"));
            String var8 = var6.getString("VIEW_MODEL_");
            if (StringUtils.isNotBlank(var8)) {
               var7.setViewModel(ProjectViewModel.valueOf(var8));
            }

            var7.setDesc(var6.getString("DESC_"));
            var7.setGroupId(var6.getString("GROUP_ID_"));
            var7.setCreateDate(var6.getTimestamp("CREATE_DATE_"));
            var7.setUpdateDate(var6.getTimestamp("UPDATE_DATE_"));
            var7.setUpdateUser(var6.getString("UPDATE_USER_"));
            var5.add(var7);
         }

         JdbcUtils.closeResultSet(var6);
         JdbcUtils.closeStatement(var4);
         var14 = var5;
      } catch (Exception var12) {
         throw new RuleException(var12);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

      return var14;
   }

   public void addProjectUser(long var1, String var3, String var4) {
      Connection var5 = JdbcUtils.getConnection();

      try {
         PreparedStatement var6 = var5.prepareStatement("INSERT INTO URULE_PROJECT_USER (ID_, PROJECT_ID_, USER_ID_, USER_NAME_, CREATE_DATE_) VALUES (?, ?, ?, ?, ?)");
         var6.setLong(1, IDGenerator.getInstance().nextId(IDType.PROJECT_USER));
         var6.setLong(2, var1);
         var6.setString(3, var3);
         var6.setString(4, var4);
         var6.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
         var6.executeUpdate();
         JdbcUtils.closeStatement(var6);
      } catch (Exception var10) {
         throw new RuleException(var10);
      } finally {
         JdbcUtils.closeConnection(var5);
      }

   }

   public void removeProjectUser(long var1, String var3) {
      Connection var4 = JdbcUtils.getConnection();

      try {
         PreparedStatement var5 = var4.prepareStatement("delete FROM URULE_PROJECT_USER where PROJECT_ID_=? AND USER_ID_=?");
         var5.setLong(1, var1);
         var5.setString(2, var3);
         var5.executeUpdate();
         JdbcUtils.closeStatement(var5);
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var4);
      }

   }

   public void removeProjectUsers(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         PreparedStatement var4 = var3.prepareStatement("delete FROM URULE_PROJECT_USER where PROJECT_ID_=?");
         var4.setLong(1, var1);
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public String getApproveUser(long var1, ApplyType var3) {
      String var4 = null;
      Connection var5 = JdbcUtils.getConnection();

      try {
         PreparedStatement var6 = var5.prepareStatement("select APPROVE_USER_ENABLE_, APPROVE_USER_DISABLE_, APPROVE_USER_DEPLOY_, CREATE_USER_ from URULE_PROJECT where ID_=?");
         var6.setLong(1, var1);
         ResultSet var7 = var6.executeQuery();
         if (var7.next()) {
            if (ApplyType.deploy == var3) {
               var4 = var7.getString(3);
            } else if (ApplyType.enable == var3) {
               var4 = var7.getString(1);
            } else if (ApplyType.disable == var3) {
               var4 = var7.getString(2);
            }
         }

         if (StringUtils.isEmpty(var4)) {
            var4 = var7.getString(4);
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

   public void updateApproveUser(long var1, ApplyType var3, String var4) {
      Connection var5 = JdbcUtils.getConnection();

      try {
         String var6 = "update URULE_PROJECT SET ";
         if (ApplyType.deploy == var3) {
            var6 = var6 + "APPROVE_USER_DEPLOY_=?";
         } else if (ApplyType.enable == var3) {
            var6 = var6 + "APPROVE_USER_ENABLE_=?";
         } else if (ApplyType.disable == var3) {
            var6 = var6 + "APPROVE_USER_DISABLE_=?";
         }

         var6 = var6 + " where ID_=?";
         PreparedStatement var7 = var5.prepareStatement(var6);
         var7.setString(1, var4);
         var7.setLong(2, var1);
         var7.executeUpdate();
         JdbcUtils.closeStatement(var7);
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var5);
      }

   }

   public ProjectQuery newQuery() {
      return new ProjectQueryImpl();
   }

   public UserQuery createUserQuery() {
      return new UserQueryImpl();
   }
}
