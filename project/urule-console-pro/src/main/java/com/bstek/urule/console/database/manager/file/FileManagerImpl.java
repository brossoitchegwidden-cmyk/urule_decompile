package com.bstek.urule.console.database.manager.file;

import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.MD5Utils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileManagerImpl implements FileManager {
   public FileQuery newQuery() {
      return new FileQueryImpl();
   }

   public RuleFile get(long var1) {
      FileQueryImpl var3 = new FileQueryImpl();
      List var4 = var3.id(var1).list((Long)null);
      if (var4.size() == 0) {
         throw new RuleException("File 【" + var1 + "】 not exist!");
      } else {
         return (RuleFile)var4.get(0);
      }
   }

   public String loadContent(long var1) {
      Connection var3 = JdbcUtils.getConnection();
      String var4 = null;

      String var7;
      try {
         PreparedStatement var5 = var3.prepareStatement("select CONTENT_ from URULE_FILE where ID_=?");
         var5.setLong(1, var1);

         ResultSet var6;
         for(var6 = var5.executeQuery(); var6.next(); var4 = var6.getString(1)) {
         }

         JdbcUtils.closeResultSet(var6);
         JdbcUtils.closeStatement(var5);
         var7 = var4;
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

      return var7;
   }

   public void add(RuleFile var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var1.setId(IDGenerator.getInstance().nextId(IDType.FILE));
         var1.setCreateDate(new Date(System.currentTimeMillis()));
         var1.setModifyDate(new Date(System.currentTimeMillis()));
         PreparedStatement var3 = var2.prepareStatement("insert into URULE_FILE (ID_, NAME_, TYPE_, PACKAGE_ID_, CONTENT_, CREATE_USER_, CREATE_DATE_,UPDATE_USER_, UPDATE_DATE_,PROJECT_ID_,LATEST_VERSION_,DELETED_) values (?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?)");
         var3.setLong(1, var1.getId());
         var3.setString(2, var1.getName());
         var3.setString(3, var1.getType());
         var3.setLong(4, var1.getParentId());
         var3.setString(5, var1.getContent());
         var3.setString(6, var1.getCreateUser());
         var3.setTimestamp(7, new Timestamp(var1.getCreateDate().getTime()));
         var3.setString(8, var1.getCreateUser());
         var3.setTimestamp(9, new Timestamp(var1.getModifyDate().getTime()));
         var3.setLong(10, var1.getProjectId());
         var3.setString(11, var1.getLatestVersion());
         var3.setBoolean(12, var1.isDeleted());
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void updateContent(long var1, String var3, String var4) {
      Connection var5 = JdbcUtils.getConnection();

      try {
         PreparedStatement var6 = var5.prepareStatement("update URULE_FILE set CONTENT_=?, DIGEST_=?, UPDATE_USER_=?, UPDATE_DATE_=? where ID_=?");
         var6.setString(1, var4);
         var6.setString(2, MD5Utils.stringToMD5(var4));
         var6.setString(3, var3);
         var6.setTimestamp(4, new Timestamp((new Date()).getTime()));
         var6.setLong(5, var1);
         var6.executeUpdate();
         JdbcUtils.closeStatement(var6);
      } catch (Exception var10) {
         throw new RuleException(var10);
      } finally {
         JdbcUtils.closeConnection(var5);
      }

   }

   public List list(long var1, long var3) {
      ArrayList var5 = new ArrayList();
      var5.addAll(this.a(var1, var3));
      return var5;
   }

   public List list(long var1, long var3, String var5) {
      ArrayList var6 = new ArrayList();
      if (var5.contentEquals(ResourceType.Library.name())) {
         var6.addAll(this.a(var1, var3, ResourceType.VariableLibrary.name()));
         var6.addAll(this.a(var1, var3, ResourceType.ParameterLibrary.name()));
         var6.addAll(this.a(var1, var3, ResourceType.ConstantLibrary.name()));
         var6.addAll(this.a(var1, var3, ResourceType.ActionLibrary.name()));
      } else if (var5.contentEquals(ResourceType.DecisionTable.name())) {
         var6.addAll(this.a(var1, var3, ResourceType.DecisionTable.name()));
         var6.addAll(this.a(var1, var3, ResourceType.CrossDecisionTable.name()));
      } else if (var5.contentEquals(ResourceType.Scorecard.name())) {
         var6.addAll(this.a(var1, var3, ResourceType.Scorecard.name()));
         var6.addAll(this.a(var1, var3, ResourceType.ComplexScorecard.name()));
      } else {
         var6.addAll(this.a(var1, var3, var5));
      }

      return var6;
   }

   private List a(long var1, long var3) {
      Connection var5 = JdbcUtils.getConnection();

      ArrayList var15;
      try {
         PreparedStatement var6 = var5.prepareStatement("select ID_,NAME_, TYPE_, CREATE_USER_, LOCKED_USER_, UPDATE_DATE_,DIGEST_ from URULE_FILE where PACKAGE_ID_=? and PROJECT_ID_=? and DELETED_=? order by NAME_ asc");
         var6.setLong(1, var3);
         var6.setLong(2, var1);
         var6.setBoolean(3, false);
         ArrayList var7 = new ArrayList();
         ResultSet var8 = var6.executeQuery();

         while(var8.next()) {
            RuleFile var9 = new RuleFile();
            var9.setDirectory(false);
            var9.setId(var8.getLong(1));
            var9.setName(var8.getString(2));
            var9.setType(var8.getString(3));
            var9.setCreateUser(var8.getString(4));
            var9.setLockedUser(var8.getString(5));
            var9.setModifyDate(var8.getTimestamp(6));
            var9.setDigest(var8.getString(7));
            var9.setParentId(var3);
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

   private List a(long var1, long var3, String var5) {
      Connection var6 = JdbcUtils.getConnection();

      ArrayList var16;
      try {
         PreparedStatement var7 = var6.prepareStatement("select ID_,NAME_, TYPE_, CREATE_USER_, LOCKED_USER_, UPDATE_DATE_,DIGEST_ from URULE_FILE where PACKAGE_ID_=? and TYPE_=? and PROJECT_ID_=? and DELETED_=? order by NAME_ asc");
         var7.setLong(1, var3);
         var7.setString(2, var5);
         var7.setLong(3, var1);
         var7.setBoolean(4, false);
         ArrayList var8 = new ArrayList();
         ResultSet var9 = var7.executeQuery();

         while(var9.next()) {
            RuleFile var10 = new RuleFile();
            var10.setDirectory(false);
            var10.setId(var9.getLong(1));
            var10.setName(var9.getString(2));
            var10.setType(var9.getString(3));
            var10.setCreateUser(var9.getString(4));
            var10.setLockedUser(var9.getString(5));
            var10.setModifyDate(var9.getTimestamp(6));
            var10.setDigest(var9.getString(7));
            var10.setParentId(var3);
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
         PreparedStatement var6 = var5.prepareStatement("update URULE_FILE set PACKAGE_ID_=? where ID_=?");
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

   public boolean checkExist(long var1, long var3, String var5, String var6) {
      Connection var7 = JdbcUtils.getConnection();

      boolean var11;
      try {
         PreparedStatement var8 = var7.prepareStatement("select count(*) from URULE_FILE where PROJECT_ID_=? and NAME_=? and PACKAGE_ID_=? and TYPE_=? and DELETED_=?");
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

   public void rename(long var1, String var3, String var4) {
      Connection var5 = JdbcUtils.getConnection();

      try {
         PreparedStatement var6 = var5.prepareStatement("update URULE_FILE set NAME_=?, UPDATE_USER_=?, UPDATE_DATE_=?  where ID_=?");
         var6.setString(1, var4);
         var6.setString(2, var3);
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

   public void remove(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         PreparedStatement var4 = var3.prepareStatement("delete from URULE_FILE where ID_=?");
         var4.setLong(1, var1);
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public void lock(long var1, String var3) {
      Connection var4 = JdbcUtils.getConnection();

      try {
         PreparedStatement var5 = var4.prepareStatement("update URULE_FILE set LOCKED_USER_=?, UPDATE_USER_=?, UPDATE_DATE_=?  where ID_=?");
         var5.setString(1, var3);
         var5.setString(2, var3);
         var5.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
         var5.setLong(4, var1);
         var5.executeUpdate();
         JdbcUtils.closeStatement(var5);
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var4);
      }

   }

   public void unlock(long var1, String var3, String var4) {
      Connection var5 = JdbcUtils.getConnection();

      try {
         PreparedStatement var6 = var5.prepareStatement("update URULE_FILE set LOCKED_USER_=null, LATEST_VERSION_=?, UPDATE_USER_=?, UPDATE_DATE_=?  where ID_=?");
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

   public void updateDeleteFlag(long var1, boolean var3, String var4) {
      Connection var5 = JdbcUtils.getConnection();

      try {
         RuleFile var6 = this.get(var1);
         if (var6 != null) {
            PreparedStatement var7 = var5.prepareStatement("update URULE_FILE set DELETED_=?, UPDATE_USER_=?, UPDATE_DATE_=? where ID_=?");
            var7.setBoolean(1, var3);
            var7.setString(2, var4);
            var7.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            var7.setLong(4, var1);
            var7.executeUpdate();
            JdbcUtils.closeStatement(var7);
         }
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var5);
      }

   }

   public void deleteByProjectId(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         PreparedStatement var4 = var3.prepareStatement("delete from URULE_FILE where PROJECT_ID_=?");
         var4.setLong(1, var1);
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public FileCountQuery newCountQuery() {
      return new FileCountQueryImpl();
   }

   public void update(RuleFile var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("update URULE_FILE set NAME_=?, TYPE_=?, PACKAGE_ID_=?, CONTENT_=?, CREATE_USER_=?, CREATE_DATE_=?,UPDATE_USER_=?, UPDATE_DATE_=?,PROJECT_ID_=?,LATEST_VERSION_=?,DELETED_=? where ID_=?");
         var3.setString(1, var1.getName());
         var3.setString(2, var1.getType());
         var3.setLong(3, var1.getParentId());
         var3.setString(4, var1.getContent());
         var3.setString(5, var1.getCreateUser());
         var3.setTimestamp(6, new Timestamp(var1.getCreateDate().getTime()));
         var3.setString(7, var1.getCreateUser());
         var3.setTimestamp(8, new Timestamp(var1.getModifyDate().getTime()));
         var3.setLong(9, var1.getProjectId());
         var3.setString(10, var1.getLatestVersion());
         var3.setBoolean(11, var1.isDeleted());
         var3.setLong(12, var1.getId());
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }
}
