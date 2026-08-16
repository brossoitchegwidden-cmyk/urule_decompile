package com.bstek.urule.console.database.manager.file;

import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DirectoryManagerImpl implements DirectoryManager {
   public void add(RuleFile var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var1.setId(IDGenerator.getInstance().nextId(IDType.FILE));
         var1.setModifyDate(new Date());
         var1.setCreateDate(new Date());
         PreparedStatement var3 = var2.prepareStatement("insert into URULE_PACKAGE (ID_, NAME_, TYPE_, PARENT_ID_, PROJECT_ID_, CREATE_USER_, CREATE_DATE_, UPDATE_USER_, UPDATE_DATE_, DELETED_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         var3.setLong(1, var1.getId());
         var3.setString(2, var1.getName());
         var3.setString(3, var1.getType());
         var3.setLong(4, var1.getParentId());
         var3.setLong(5, var1.getProjectId());
         var3.setString(6, var1.getCreateUser());
         var3.setTimestamp(7, new Timestamp(var1.getCreateDate().getTime()));
         var3.setString(8, var1.getCreateUser());
         var3.setTimestamp(9, new Timestamp(var1.getModifyDate().getTime()));
         var3.setBoolean(10, var1.isDeleted());
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public RuleFile get(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      RuleFile var7;
      try {
         PreparedStatement var4 = var3.prepareStatement("select NAME_, PARENT_ID_, TYPE_, UPDATE_USER_, UPDATE_DATE_, DELETED_, PROJECT_ID_ from URULE_PACKAGE where ID_=?");
         var4.setLong(1, var1);
         ResultSet var5 = var4.executeQuery();
         RuleFile var6 = null;
         if (var5.next()) {
            var6 = new RuleFile();
            var6.setId(var1);
            var6.setName(var5.getString(1));
            var6.setParentId(var5.getLong(2));
            var6.setType(var5.getString(3));
            var6.setUpdateUser(var5.getString(4));
            var6.setModifyDate(var5.getTimestamp(5));
            var6.setDeleted(var5.getBoolean(6));
            var6.setProjectId(var5.getLong(7));
            var6.setDirectory(true);
         }

         JdbcUtils.closeResultSet(var5);
         JdbcUtils.closeStatement(var4);
         var7 = var6;
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

      return var7;
   }

   public void remove(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         PreparedStatement var4 = var3.prepareStatement("delete from URULE_PACKAGE where ID_=?");
         var4.setLong(1, var1);
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public RuleFile loadDir(long var1, long var3, String var5, String var6) {
      Connection var7 = JdbcUtils.getConnection();

      RuleFile var11;
      try {
         PreparedStatement var8 = var7.prepareStatement("select NAME_, PARENT_ID_, TYPE_, UPDATE_USER_, UPDATE_DATE_, DELETED_,ID_ from URULE_PACKAGE where PARENT_ID_=? and PROJECT_ID_=? and TYPE_=? and NAME_=?");
         var8.setLong(1, var3);
         var8.setLong(2, var1);
         var8.setString(3, var6);
         var8.setString(4, var5);
         ResultSet var9 = var8.executeQuery();
         RuleFile var10 = null;
         if (var9.next()) {
            var10 = new RuleFile();
            var10.setName(var9.getString(1));
            var10.setParentId(var9.getLong(2));
            var10.setType(var9.getString(3));
            var10.setUpdateUser(var9.getString(4));
            var10.setModifyDate(var9.getTimestamp(5));
            var10.setDeleted(var9.getBoolean(6));
            var10.setId(var9.getLong(7));
         }

         JdbcUtils.closeResultSet(var9);
         JdbcUtils.closeStatement(var8);
         var11 = var10;
      } catch (Exception var15) {
         throw new RuleException(var15);
      } finally {
         JdbcUtils.closeConnection(var7);
      }

      return var11;
   }

   public void updateDeleteFlag(long var1, boolean var3, String var4) {
      Connection var5 = JdbcUtils.getConnection();

      try {
         PreparedStatement var6 = var5.prepareStatement("update URULE_PACKAGE set DELETED_=?, UPDATE_USER_=?, UPDATE_DATE_=? where ID_=?");
         if (var3) {
            var6.setBoolean(1, var3);
            var6.setString(2, var4);
            var6.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            var6.setLong(4, var1);
            var6.executeUpdate();
         } else {
            this.a(var6, var1, var4);
         }

         JdbcUtils.closeStatement(var6);
      } catch (Exception var10) {
         throw new RuleException(var10);
      } finally {
         JdbcUtils.closeConnection(var5);
      }

   }

   private void a(PreparedStatement var1, long var2, String var4) {
      RuleFile var5 = this.get(var2);
      if (var5 != null && var5.isDeleted() && var5.getParentId() > 0L) {
         this.a(var1, var5.getParentId(), var4);
      }

      try {
         var1.setBoolean(1, false);
         var1.setString(2, var4);
         var1.setDate(3, new java.sql.Date(System.currentTimeMillis()));
         var1.setLong(4, var2);
         var1.executeUpdate();
      } catch (Exception var7) {
         throw new RuleException(var7);
      }
   }

   public void changeName(long var1, String var3, String var4) {
      Connection var5 = JdbcUtils.getConnection();

      try {
         PreparedStatement var6 = var5.prepareStatement("update URULE_PACKAGE set NAME_=?, UPDATE_USER_=?, UPDATE_DATE_=? where ID_=?");
         var6.setString(1, var3);
         var6.setString(2, var4);
         var6.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
         var6.setLong(4, var1);
         var6.executeUpdate();
         JdbcUtils.closeStatement(var6);
      } catch (Exception var10) {
         throw new RuleException(var10);
      } finally {
         JdbcUtils.closeConnection(var5);
      }

   }

   public boolean checkExist(long var1, long var3, String var5, String var6) {
      Connection var7 = JdbcUtils.getConnection();

      boolean var11;
      try {
         PreparedStatement var8 = var7.prepareStatement("select count(*) from URULE_PACKAGE where PROJECT_ID_=? and NAME_=? and PARENT_ID_=? and TYPE_=? and DELETED_=?");
         var8.setLong(1, var1);
         var8.setString(2, var6);
         var8.setLong(3, var3);
         var8.setString(4, var5);
         var8.setBoolean(5, false);
         ResultSet var9 = var8.executeQuery();
         var9.next();
         int var10 = var9.getInt(1);
         JdbcUtils.closeResultSet(var9);
         JdbcUtils.closeStatement(var8);
         var11 = var10 > 0;
      } catch (Exception var15) {
         throw new RuleException(var15);
      } finally {
         JdbcUtils.closeConnection(var7);
      }

      return var11;
   }

   public List list(long var1, long var3) {
      Connection var5 = JdbcUtils.getConnection();

      ArrayList var15;
      try {
         PreparedStatement var6 = var5.prepareStatement("select ID_, NAME_, TYPE_ from URULE_PACKAGE where PARENT_ID_=? and PROJECT_ID_=? and DELETED_=? order by NAME_ asc");
         ArrayList var7 = new ArrayList();
         var6.setLong(1, var3);
         var6.setLong(2, var1);
         var6.setBoolean(3, false);
         ResultSet var8 = var6.executeQuery();

         while(var8.next()) {
            RuleFile var9 = new RuleFile();
            var9.setDirectory(true);
            var9.setId(var8.getLong(1));
            var9.setParentId(var3);
            var9.setName(var8.getString(2));
            var9.setType(var8.getString(3));
            var9.setProjectId(var1);
            var7.add(var9);
         }

         JdbcUtils.closeResultSet(var8);
         JdbcUtils.closeStatement(var6);
         var15 = var7;
      } catch (Exception var13) {
         throw new RuleException(var13);
      } finally {
         JdbcUtils.closeConnection(var5);
      }

      return var15;
   }

   public List list(long var1, long var3, String var5) {
      Connection var6 = JdbcUtils.getConnection();

      ArrayList var16;
      try {
         PreparedStatement var7 = var6.prepareStatement("select ID_, NAME_, TYPE_ from URULE_PACKAGE where PARENT_ID_=? and PROJECT_ID_=? and TYPE_=? and DELETED_=? order by NAME_ asc");
         ArrayList var8 = new ArrayList();
         var7.setLong(1, var3);
         var7.setLong(2, var1);
         var7.setString(3, var5);
         var7.setBoolean(4, false);
         ResultSet var9 = var7.executeQuery();

         while(var9.next()) {
            RuleFile var10 = new RuleFile();
            var10.setDirectory(true);
            var10.setId(var9.getLong(1));
            var10.setParentId(var3);
            var10.setName(var9.getString(2));
            var10.setType(var9.getString(3));
            var10.setProjectId(var1);
            var8.add(var10);
         }

         JdbcUtils.closeResultSet(var9);
         JdbcUtils.closeStatement(var7);
         var16 = var8;
      } catch (Exception var14) {
         throw new RuleException(var14);
      } finally {
         JdbcUtils.closeConnection(var6);
      }

      return var16;
   }

   public void changeParent(long var1, long var3) {
      Connection var5 = JdbcUtils.getConnection();

      try {
         PreparedStatement var6 = var5.prepareStatement("update URULE_PACKAGE set PARENT_ID_=? where ID_=?");
         var6.setLong(1, var3);
         var6.setLong(2, var1);
         var6.executeUpdate();
         JdbcUtils.closeStatement(var6);
      } catch (Exception var10) {
         throw new RuleException(var10);
      } finally {
         JdbcUtils.closeConnection(var5);
      }

   }

   public void deleteByProjectId(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         PreparedStatement var4 = var3.prepareStatement("delete from URULE_PACKAGE where PROJECT_ID_=?");
         var4.setLong(1, var1);
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public void changeGeneral(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         PreparedStatement var4 = var3.prepareStatement("update URULE_PACKAGE set TYPE_=? where PROJECT_ID_=?");
         var4.setString(1, ResourceType.General.name());
         var4.setLong(2, var1);
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public void changeType(long var1, String var3) {
      Connection var4 = JdbcUtils.getConnection();

      try {
         PreparedStatement var5 = var4.prepareStatement("update URULE_PACKAGE set TYPE_=? where ID_=?");
         var5.setString(1, var3);
         var5.setLong(2, var1);
         var5.executeUpdate();
         JdbcUtils.closeStatement(var5);
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var4);
      }

   }

   public long countByType(long var1, String var3) {
      long var4 = 0L;
      Connection var6 = JdbcUtils.getConnection();

      long var9;
      try {
         PreparedStatement var7 = var6.prepareStatement("select count(ID_) as RC_ from URULE_PACKAGE where PROJECT_ID_=? and TYPE_=?");
         var7.setLong(1, var1);
         var7.setString(2, var3);
         ResultSet var8 = var7.executeQuery();
         if (var8.next()) {
            var4 = var8.getLong(1);
         }

         JdbcUtils.closeResultSet(var8);
         JdbcUtils.closeStatement(var7);
         var9 = var4;
      } catch (Exception var14) {
         throw new RuleException(var14);
      } finally {
         JdbcUtils.closeConnection(var6);
      }

      return var9;
   }

   public boolean hasTypeFolder(long var1) {
      boolean var3 = false;
      Connection var4 = JdbcUtils.getConnection();

      boolean var9;
      try {
         long var5 = 0L;
         PreparedStatement var7 = var4.prepareStatement("select count(ID_) as RC_ from URULE_PACKAGE where PROJECT_ID_=? and TYPE_<>?");
         var7.setLong(1, var1);
         var7.setString(2, ResourceType.General.name());
         ResultSet var8 = var7.executeQuery();
         if (var8.next()) {
            var5 = var8.getLong(1);
         }

         var3 = var5 > 0L;
         JdbcUtils.closeResultSet(var8);
         JdbcUtils.closeStatement(var7);
         var9 = var3;
      } catch (Exception var13) {
         throw new RuleException(var13);
      } finally {
         JdbcUtils.closeConnection(var4);
      }

      return var9;
   }
}
