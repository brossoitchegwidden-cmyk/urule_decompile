package com.bstek.urule.console.database.manager.file;

import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.database.manager.project.ProjectManager;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileQueryImpl implements FileQuery {
   private Long a;
   private List b;
   private String c;
   private String d;
   private String e;
   private String[] f;
   private String g;
   private String h;
   private Boolean i;
   private boolean j;
   private Boolean k;
   private List l = new ArrayList();
   private List m = new ArrayList();

   protected FileQueryImpl() {
   }

   public List tree(Long var1) {
      Connection var2 = JdbcUtils.getConnection();

      ArrayList var15;
      try {
         ArrayList var3 = new ArrayList();
         Project var4 = ProjectManager.ins.get(var1);
         var3.add(this.a(var2, var4));
         if (this.k && var1 != null) {
            String var5 = var4.getGroupId();

            for(Project var8 : (Iterable<Project>)(Iterable<?>)(ProjectManager.ins.newQuery().groupId(var5).type("common").list())) {
               if (var8.getId() != var1) {
                  RuleFile var9 = this.a(var2, var8);
                  var3.add(var9);
               }
            }
         }

         var15 = var3;
      } catch (Exception var13) {
         throw new RuleException(var13);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

      return var15;
   }

   private RuleFile a(Connection var1, Project var2) {
      try {
         RuleFile var3 = new RuleFile();
         var3.setName(var2.getName());
         var3.setType(var2.getType());
         var3.setProjectId(var2.getId());
         var3.setParentId(-1L);
         var3.setDirectory(true);
         var3.setPath("/");
         var3.setId(0L);
         ArrayList var4 = new ArrayList();
         var3.setChildren(var4);
         var4.addAll(this.b(var3, var1));
         var4.addAll(this.a(var3, var1));
         if (this.j) {
            this.a((List)var4);
         }

         return var3;
      } catch (Exception var5) {
         throw new RuleException(var5);
      }
   }

   private void a(List var1) {
      if (var1 != null) {
         ArrayList var2 = new ArrayList();
         var2.addAll(var1);

         for(RuleFile var4 : (Iterable<RuleFile>)(Iterable<?>)(var2)) {
            if (var4.isDirectory()) {
               if (this.a(var4)) {
                  var1.remove(var4);
               }

               this.a(var4.getChildren());
            }
         }

      }
   }

   private boolean a(RuleFile var1) {
      List var2 = var1.getChildren();
      if (var2 != null && var2.size() != 0) {
         for(RuleFile var4 : (Iterable<RuleFile>)(Iterable<?>)(var2)) {
            if (!var4.isDirectory()) {
               return false;
            }

            if (!this.a(var4)) {
               return false;
            }
         }

         return true;
      } else {
         return true;
      }
   }

   public List list(Long var1) {
      Connection var2 = JdbcUtils.getConnection();
      RuleFile var3 = new RuleFile();
      Project var4 = null;
      var3.setPath("/");
      if (var1 != null) {
         var3.setProjectId(var1);
         var4 = ProjectManager.ins.get(var1);
      }

      var3.setId(-1L);
      List var5 = this.b(var3, var2);

      for(RuleFile var7 : (Iterable<RuleFile>)(Iterable<?>)(var5)) {
         PathInfo var8 = new PathInfo();
         var8.setPath("");
         this.a(var2, var7.getParentId(), var8);
         var7.setFileSet(var8.isFileSet());
         String var9 = var8.getPath();
         if (var4 == null) {
            Project var10 = ProjectManager.ins.get(var7.getProjectId());
            var9 = var10.getName() + "/" + var9;
         } else {
            var9 = var4.getName() + "/" + var9;
         }

         var7.setPath(var7.getType() + ":" + var9 + var7.getName());
      }

      JdbcUtils.closeConnection(var2);
      return var5;
   }

   private void a(Connection var1, long var2, PathInfo var4) {
      String var5 = "select ID_, NAME_, TYPE_, PARENT_ID_ from URULE_PACKAGE where ID_=?";
      PreparedStatement var6 = null;
      ResultSet var7 = null;

      try {
         var6 = var1.prepareStatement(var5);
         var6.setLong(1, var2);
         var7 = var6.executeQuery();
         if (var7.next()) {
            long var8 = var7.getLong(1);
            String var10 = var7.getString(2);
            String var11 = var7.getString(3);
            long var12 = var7.getLong(4);
            if (var8 == var12) {
               throw new RuleException("[" + var2 + "] buildPath error!");
            }

            String var14 = var4.getPath();
            var14 = var10 + "/" + var14;
            var4.setPath(var14);
            if (ResourceType.General.name().equals(var11)) {
               var4.setFileSet(true);
            }

            this.a(var1, var12, var4);
         }
      } catch (Exception var18) {
         throw new RuleException(var18);
      } finally {
         JdbcUtils.closeResultSet(var7);
         JdbcUtils.closeStatement(var6);
      }

   }

   private List a(RuleFile var1, Connection var2) {
      try {
         ArrayList var3 = new ArrayList();
         String var4 = "select ID_, NAME_, TYPE_,PARENT_ID_,PROJECT_ID_,CREATE_DATE_,UPDATE_USER_,UPDATE_DATE_ from URULE_PACKAGE";
         StringBuilder var5 = new StringBuilder();
         if (var1.getId() > -1L) {
            var5.append("  where PARENT_ID_=?");
            var3.add(var1.getId());
         }

         if (var1.getProjectId() > 0L) {
            if (var5.length() > 0) {
               var5.append("  and PROJECT_ID_=?");
            } else {
               var5.append("  where PROJECT_ID_=?");
            }

            var3.add(var1.getProjectId());
         }

         var4 = this.a(var5, var3, var4, true);
         var4 = this.b(var4);
         PreparedStatement var6 = var2.prepareStatement(var4);
         ArrayList var7 = new ArrayList();
         JdbcUtils.fillPreparedStatementParameters(var3, var6);
         ResultSet var8 = var6.executeQuery();
         String var9 = var1.getPath();
         if (var9.endsWith("/")) {
            var9 = var9 + var1.getName();
         } else {
            var9 = var9 + "/" + var1.getName();
         }

         while(var8.next()) {
            RuleFile var10 = this.a(var2, var8, var9);
            var7.add(var10);
         }

         JdbcUtils.closeResultSet(var8);
         JdbcUtils.closeStatement(var6);
         return var7;
      } catch (Exception var11) {
         throw new RuleException(var11);
      }
   }

   private List b(RuleFile var1, Connection var2) {
      try {
         ArrayList var3 = new ArrayList();
         String var4 = "select ID_,NAME_, TYPE_,CREATE_DATE_,UPDATE_USER_,UPDATE_DATE_,LOCKED_USER_,PACKAGE_ID_,PROJECT_ID_,LATEST_VERSION_,DIGEST_,DELETED_ from URULE_FILE ";
         StringBuilder var5 = new StringBuilder();
         if (var1.getId() > -1L) {
            var5.append("  where PACKAGE_ID_=?");
            var3.add(var1.getId());
         }

         if (var1.getProjectId() > 0L) {
            if (var1.getId() > -1L) {
               var5.append("  and PROJECT_ID_=?");
            } else {
               var5.append("  where PROJECT_ID_=?");
            }

            var3.add(var1.getProjectId());
         }

         var4 = this.a(var5, var3, var4, false);
         var4 = this.b(var4);
         PreparedStatement var6 = var2.prepareStatement(var4);
         JdbcUtils.fillPreparedStatementParameters(var3, var6);
         ArrayList var7 = new ArrayList();
         ResultSet var8 = var6.executeQuery();
         String var9 = var1.getPath();
         if (var9.endsWith("/")) {
            var9 = var9 + var1.getName();
         } else {
            var9 = var9 + "/" + var1.getName();
         }

         while(var8.next()) {
            RuleFile var10 = this.a(var8, var9);
            var7.add(var10);
         }

         JdbcUtils.closeResultSet(var8);
         JdbcUtils.closeStatement(var6);
         return var7;
      } catch (Exception var11) {
         throw new RuleException(var11);
      }
   }

   private RuleFile a(Connection var1, ResultSet var2, String var3) throws SQLException {
      RuleFile var4 = new RuleFile();
      var4.setDirectory(true);
      var4.setId(var2.getLong(1));
      var4.setName(var2.getString(2));
      var4.setType(var2.getString(3));
      var4.setParentId(var2.getLong(4));
      var4.setProjectId(var2.getLong(5));
      var4.setCreateDate(var2.getTimestamp(6));
      var4.setUpdateUser(var2.getString(7));
      var4.setModifyDate(var2.getTimestamp(8));
      var4.setPath(var3);
      ArrayList var5 = new ArrayList();
      var4.setChildren(var5);
      var5.addAll(this.b(var4, var1));
      var5.addAll(this.a(var4, var1));
      return var4;
   }

   private RuleFile a(ResultSet var1, String var2) throws SQLException {
      RuleFile var3 = new RuleFile();
      var3.setDirectory(false);
      var3.setId(var1.getLong(1));
      var3.setName(var1.getString(2));
      var3.setType(var1.getString(3));
      var3.setCreateDate(var1.getTimestamp(4));
      var3.setUpdateUser(var1.getString(5));
      var3.setModifyDate(var1.getTimestamp(6));
      var3.setLockedUser(var1.getString(7));
      var3.setParentId(var1.getLong(8));
      var3.setProjectId(var1.getLong(9));
      var3.setLatestVersion(var1.getString(10));
      var3.setDigest(var1.getString(11));
      var3.setDeleted(var1.getBoolean(12));
      if (var2.endsWith("/")) {
         var2 = var2 + var3.getName();
      } else {
         var2 = var2 + "/" + var3.getName();
      }

      var3.setPath(var3.getType() + ":" + var2);
      return var3;
   }

   private String a(String var1) {
      String var2 = var1;
      if (!var1.equals(ResourceType.VariableLibrary.name()) && !var1.equals(ResourceType.ParameterLibrary.name()) && !var1.equals(ResourceType.ConstantLibrary.name()) && !var1.equals(ResourceType.ActionLibrary.name())) {
         if (!var1.equals(ResourceType.DecisionTable.name()) && !var1.contentEquals(ResourceType.CrossDecisionTable.name())) {
            if (var1.equals(ResourceType.Scorecard.name()) || var1.equals(ResourceType.ComplexScorecard.name())) {
               var2 = ResourceType.Scorecard.name();
            }
         } else {
            var2 = ResourceType.DecisionTable.name();
         }
      } else {
         var2 = ResourceType.Library.name();
      }

      return var2;
   }

   private String a(StringBuilder var1, List var2, String var3, boolean var4) {
      if (this.a != null) {
         if (var1.length() > 0) {
            var1.append(" and ");
         } else {
            var1.append(" where ");
         }

         var1.append(" ID_ = ?");
         var2.add(this.a);
      }

      if (this.b != null && this.b.size() > 0) {
         if (var1.length() > 0) {
            var1.append(" and ");
         } else {
            var1.append(" where ");
         }

         StringBuilder var5 = new StringBuilder();

         for(long var7 : (Iterable<Long>)(Iterable<?>)(this.b)) {
            if (var5.length() > 0) {
               var5.append(",");
            }

            var5.append("?");
            var2.add(var7);
         }

         if (var5.length() > 0) {
            var1.append(" ID_ in (" + var5.toString() + ") ");
         }
      }

      if (this.c != null) {
         if (var1.length() > 0) {
            var1.append(" and ");
         } else {
            var1.append(" where ");
         }

         var1.append(" NAME_=?");
         var2.add(this.c);
      }

      if (!var4 && this.d != null) {
         if (var1.length() > 0) {
            var1.append(" and ");
         } else {
            var1.append(" where ");
         }

         var1.append(" NAME_ like ?");
         var2.add("%" + this.d + "%");
      }

      if (this.e != null) {
         if (var1.length() > 0) {
            var1.append(" and ");
         } else {
            var1.append(" where ");
         }

         var1.append(" TYPE_ = ?");
         String var10 = this.e;
         if (var4) {
            var10 = this.a(this.e);
         }

         var2.add(var10);
      }

      if (this.f != null) {
         if (var1.length() > 0) {
            var1.append(" and ");
         } else {
            var1.append(" where ");
         }

         StringBuilder var11 = new StringBuilder();

         for(int var12 = 0; var12 < this.f.length; ++var12) {
            if (var12 > 0) {
               var11.append(",");
            }

            var11.append("?");
            String var13 = this.f[var12];
            if (var4) {
               var13 = this.a(var13);
            }

            var2.add(var13);
         }

         var1.append("TYPE_ in (" + var11.toString() + ")");
      }

      if (this.g != null) {
         if (var1.length() > 0) {
            var1.append(" and ");
         } else {
            var1.append(" where ");
         }

         var1.append(" LOCKED_USER_ = ?");
         var2.add(this.g);
      }

      if (this.h != null) {
         if (var1.length() > 0) {
            var1.append(" and ");
         } else {
            var1.append(" where ");
         }

         var1.append(" UPDATE_USER_ = ?");
         var2.add(this.h);
      }

      if (this.i != null) {
         if (var1.length() > 0) {
            var1.append(" and ");
         } else {
            var1.append(" where ");
         }

         var1.append(" DELETED_ = ?");
         var2.add(this.i);
      }

      var3 = var3 + var1.toString();
      return var3;
   }

   private String b(String var1) {
      StringBuilder var2 = new StringBuilder();
      StringBuilder var3 = new StringBuilder();

      for(String var5 : (Iterable<String>)(Iterable<?>)(this.l)) {
         if (var2.length() > 0) {
            var2.append(",");
         }

         var2.append(var5);
      }

      for(String var7 : (Iterable<String>)(Iterable<?>)(this.m)) {
         if (var3.length() > 0) {
            var3.append(",");
         }

         var3.append(var7);
      }

      if (var2.length() > 0 || var3.length() > 0) {
         var1 = var1 + " order by ";
      }

      if (var2.length() > 0) {
         var1 = var1 + var2.toString() + " asc";
         if (var3.length() > 0) {
            var1 = var1 + "," + var3.toString() + " desc";
         }
      } else if (var3.length() > 0) {
         var1 = var1 + var3.toString() + " desc";
      }

      return var1;
   }

   public FileQuery id(long var1) {
      this.a = var1;
      return this;
   }

   public FileQuery ids(List var1) {
      this.b = var1;
      return this;
   }

   public FileQuery name(String var1) {
      this.c = var1;
      return this;
   }

   public FileQuery nameLike(String var1) {
      this.d = var1;
      return this;
   }

   public FileQuery type(String var1) {
      this.e = var1;
      return this;
   }

   public FileQuery types(String[] var1) {
      this.f = var1;
      return this;
   }

   public FileQuery lockedUser(String var1) {
      this.g = var1;
      return this;
   }

   public FileQuery deleted(boolean var1) {
      this.i = var1;
      return this;
   }

   public FileQuery removeEmpty(boolean var1) {
      this.j = var1;
      return this;
   }

   public FileQuery containCommonProject(boolean var1) {
      this.k = var1;
      return this;
   }

   public FileQuery desc(String var1) {
      this.m.add(var1);
      return this;
   }

   public FileQuery asc(String var1) {
      this.l.add(var1);
      return this;
   }

   public FileQuery updateUser(String var1) {
      this.h = var1;
      return this;
   }

   class PathInfo {
      private String b;
      private boolean c;

      public String getPath() {
         return this.b;
      }

      public void setPath(String var1) {
         this.b = var1;
      }

      public boolean isFileSet() {
         return this.c;
      }

      public void setFileSet(boolean var1) {
         this.c = var1;
      }
   }
}
