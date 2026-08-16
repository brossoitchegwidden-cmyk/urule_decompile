package com.bstek.urule.console.database.manager.file;

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

public class FileCountQueryImpl implements FileCountQuery {
   private Long a;
   private Date b;
   private Date c;

   public FileCountQuery projectId(Long var1) {
      this.a = var1;
      return this;
   }

   public FileCountQuery updateDateBegin(Date var1) {
      this.b = var1;
      return this;
   }

   public FileCountQuery updateDateEnd(Date var1) {
      this.c = var1;
      return this;
   }

   public List getRuleCommits() {
      Connection var1 = JdbcUtils.getConnection();

      ArrayList var11;
      try {
         PreparedStatement var2 = var1.prepareStatement("select ID_, UPDATE_DATE_ from URULE_FILE where PROJECT_ID_=? and UPDATE_DATE_>=? and UPDATE_DATE_<=?");
         var2.setLong(1, this.a);
         var2.setTimestamp(2, new Timestamp(this.b.getTime()));
         var2.setTimestamp(3, new Timestamp(this.c.getTime()));
         ResultSet var3 = var2.executeQuery();
         ArrayList var4 = new ArrayList();

         while(var3.next()) {
            RuleFile var5 = new RuleFile();
            var5.setId(var3.getLong(1));
            var5.setModifyDate(var3.getTimestamp(2));
            var4.add(var5);
         }

         JdbcUtils.closeResultSet(var3);
         JdbcUtils.closeStatement(var2);
         var11 = var4;
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var1);
      }

      return var11;
   }

   public List getUserCommits() {
      Connection var1 = JdbcUtils.getConnection();

      ArrayList var11;
      try {
         PreparedStatement var2 = var1.prepareStatement("select ID_, UPDATE_USER_, UPDATE_DATE_ from URULE_FILE where PROJECT_ID_=? and UPDATE_DATE_>=? and UPDATE_DATE_<=?");
         var2.setLong(1, this.a);
         var2.setTimestamp(2, new Timestamp(this.b.getTime()));
         var2.setTimestamp(3, new Timestamp(this.c.getTime()));
         ResultSet var3 = var2.executeQuery();
         ArrayList var4 = new ArrayList();

         while(var3.next()) {
            RuleFile var5 = new RuleFile();
            var5.setId(var3.getLong(1));
            var5.setUpdateUser(var3.getString(2));
            var5.setModifyDate(var3.getDate(3));
            var4.add(var5);
         }

         JdbcUtils.closeResultSet(var3);
         JdbcUtils.closeStatement(var2);
         var11 = var4;
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var1);
      }

      return var11;
   }

   public Integer getRuleCount() {
      int var1 = 0;
      Connection var2 = JdbcUtils.getConnection();

      Integer var5;
      try {
         PreparedStatement var3 = var2.prepareStatement("select COUNT(*) RULE_COUNT_ from URULE_FILE where PROJECT_ID_=? and DELETED_=?");
         var3.setLong(1, this.a);
         var3.setBoolean(2, false);
         ResultSet var4 = var3.executeQuery();
         if (var4.next()) {
            var1 = var4.getInt(1);
         }

         JdbcUtils.closeResultSet(var4);
         JdbcUtils.closeStatement(var3);
         var5 = var1;
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

      return var5;
   }
}
