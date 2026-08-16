package com.bstek.urule.console.database.manager.file.version;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.VersionFile;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.MD5Utils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class VersionFileManagerImpl implements VersionFileManager {
   protected VersionFileManagerImpl() {
   }

   public VersionFile loadFile(long var1) {
      List var3 = this.newQuery().id(var1).list();
      return var3.size() > 0 ? (VersionFile)var3.get(0) : null;
   }

   public String loadFileContent(long var1) {
      Connection var3 = JdbcUtils.getConnection();
      String var4 = null;

      String var7;
      try {
         PreparedStatement var5 = var3.prepareStatement("select CONTENT_ from URULE_VERSION_FILE where ID_=?");
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

   public void updateContent(long var1, String var3) {
      Connection var4 = JdbcUtils.getConnection();
      String var5 = "update URULE_VERSION_FILE set CONTENT_=?,DIGEST_=? where ID_=?";

      try {
         PreparedStatement var6 = var4.prepareStatement(var5);
         var6.setString(1, var3);
         var6.setString(2, MD5Utils.stringToMD5(var3));
         var6.setLong(3, var1);
         var6.executeUpdate();
         JdbcUtils.closeStatement(var6);
      } catch (Exception var10) {
         throw new RuleException(var10);
      } finally {
         JdbcUtils.closeConnection(var4);
      }

   }

   public VersionFile loadFile(long var1, String var3) {
      List var4 = this.newQuery().fileId(var1).version(var3).list();
      return var4.size() > 0 ? (VersionFile)var4.get(0) : null;
   }

   public List loadFiles(long var1) {
      return this.newQuery().fileId(var1).list();
   }

   public void saveFile(VersionFile var1) {
      Connection var2 = JdbcUtils.getConnection();
      String var3 = "insert into URULE_VERSION_FILE(ID_, FILE_ID_, PROJECT_ID_, NAME_, VERSION_, NOTE_, CONTENT_, DIGEST_, CREATE_USER_, CREATE_DATE_) values(?,?,?,?,?,?,?,?,?, ?)";
      long var4 = IDGenerator.getInstance().nextId(IDType.FILE);
      var1.setId(var4);

      try {
         PreparedStatement var6 = var2.prepareStatement(var3);
         var6.setLong(1, var4);
         var6.setLong(2, var1.getFileId());
         var6.setLong(3, var1.getProjectId());
         var6.setString(4, var1.getName());
         var6.setString(5, var1.getVersion());
         var6.setString(6, var1.getNote());
         var6.setString(7, var1.getContent());
         var6.setString(8, MD5Utils.stringToMD5(var1.getContent()));
         var6.setString(9, var1.getCreateUser());
         var6.setTimestamp(10, new Timestamp((new Date()).getTime()));
         var6.executeUpdate();
         JdbcUtils.closeStatement(var6);
      } catch (Exception var10) {
         throw new RuleException(var10);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public VersionFileQuery newQuery() {
      return new VersionFileQueryImpl();
   }

   public void deleteByProjectId(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         PreparedStatement var4 = var3.prepareStatement("delete from URULE_VERSION_FILE where PROJECT_ID_=?");
         var4.setLong(1, var1);
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public void deleteByFileId(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         PreparedStatement var4 = var3.prepareStatement("delete from URULE_VERSION_FILE where FILE_ID_=?");
         var4.setLong(1, var1);
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }
}
