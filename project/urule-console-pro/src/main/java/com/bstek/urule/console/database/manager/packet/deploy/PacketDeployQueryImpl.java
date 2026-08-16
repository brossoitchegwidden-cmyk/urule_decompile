package com.bstek.urule.console.database.manager.packet.deploy;

import com.bstek.urule.console.database.manager.packet.deploy.file.PacketDeployFileManager;
import com.bstek.urule.console.database.model.ApplyStatus;
import com.bstek.urule.console.database.model.PacketDeploy;
import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PacketDeployQueryImpl implements PacketDeployQuery {
   private Long a;
   private Long b;
   private Long c;
   private Long d;
   private String e;
   private Boolean f;
   private ApplyStatus g;
   private String h;
   private String i;
   private List j = new ArrayList();

   protected PacketDeployQueryImpl() {
   }

   public Page paging(int var1, int var2) {
      String var3 = "select ID_,PACKET_ID_,PROJECT_ID_,DESC_,VERSION_,CREATE_USER_,CREATE_DATE_,ENABLE_,STATUS_,DIGEST_ from URULE_DEPLOYED_PACKET";
      this.j.clear();
      StringBuilder var4 = this.a();
      if (var4.length() > 0) {
         var3 = var3 + " where " + var4.toString();
      }

      var3 = var3 + " order by CREATE_DATE_ desc";
      Connection var5 = JdbcUtils.getConnection();

      Page var10;
      try {
         Page var6 = new Page(var1, var2);
         var3 = JdbcUtils.getPageSql(var3, var6.getStartRow(), var2);
         PreparedStatement var7 = var5.prepareStatement(var3);
         JdbcUtils.fillPreparedStatementParameters(this.j, var7);
         ResultSet var8 = var7.executeQuery();
         List var9 = this.a(var8);
         var6.setData(var9);
         JdbcUtils.closeResultSet(var8);
         JdbcUtils.closeStatement(var7);
         var3 = "select count(*) from URULE_DEPLOYED_PACKET";
         if (var4.length() > 0) {
            var3 = var3 + " where" + var4.toString();
         }

         var7 = var5.prepareStatement(var3);
         JdbcUtils.fillPreparedStatementParameters(this.j, var7);
         var8 = var7.executeQuery();
         if (var8.next()) {
            var6.setTotalRows(var8.getLong(1));
         }

         JdbcUtils.closeResultSet(var8);
         JdbcUtils.closeStatement(var7);
         var10 = var6;
      } catch (Exception var14) {
         throw new RuleException(var14);
      } finally {
         JdbcUtils.closeConnection(var5);
      }

      return var10;
   }

   public List list() {
      String var1 = "select ID_,PACKET_ID_,PROJECT_ID_,DESC_,VERSION_,CREATE_USER_,CREATE_DATE_,ENABLE_,STATUS_,DIGEST_ from URULE_DEPLOYED_PACKET";
      return this.a(var1, false);
   }

   public List listWithContent() {
      String var1 = "select ID_,PACKET_ID_,PROJECT_ID_,DESC_,CONTENT_,VERSION_,CREATE_USER_,CREATE_DATE_,ENABLE_,STATUS_,DIGEST_ from URULE_DEPLOYED_PACKET";
      return this.a(var1, true);
   }

   private List a(String var1, boolean var2) {
      this.j.clear();
      StringBuilder var3 = this.a();
      if (var3.length() > 0) {
         var1 = var1 + " where " + var3.toString();
      }

      var1 = var1 + " order by CREATE_DATE_ desc";
      Connection var4 = JdbcUtils.getConnection();
      PreparedStatement var5 = null;
      ResultSet var6 = null;

      List var7;
      try {
         var5 = var4.prepareStatement(var1);
         JdbcUtils.fillPreparedStatementParameters(this.j, var5);
         var6 = var5.executeQuery();
         if (!var2) {
            var7 = this.a(var6);
            return var7;
         }

         var7 = this.b(var6);
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeResultSet(var6);
         JdbcUtils.closeStatement(var5);
         JdbcUtils.closeConnection(var4);
      }

      return var7;
   }

   public long count() {
      String var1 = "select count(*) from URULE_DEPLOYED_PACKET";
      this.j.clear();
      StringBuilder var2 = this.a();
      if (var2.length() > 0) {
         var1 = var1 + " where " + var2.toString();
      }

      Connection var3 = JdbcUtils.getConnection();
      PreparedStatement var4 = null;
      ResultSet var5 = null;

      long var8;
      try {
         var4 = var3.prepareStatement(var1);
         JdbcUtils.fillPreparedStatementParameters(this.j, var4);
         var5 = var4.executeQuery();
         long var6 = 0L;
         if (var5.next()) {
            var6 = var5.getLong(1);
         }

         var8 = var6;
      } catch (Exception var13) {
         throw new RuleException(var13);
      } finally {
         JdbcUtils.closeResultSet(var5);
         JdbcUtils.closeStatement(var4);
         JdbcUtils.closeConnection(var3);
      }

      return var8;
   }

   private List a(ResultSet var1) throws SQLException {
      ArrayList var2 = new ArrayList();

      while(var1.next()) {
         PacketDeploy var3 = new PacketDeploy();
         var3.setId(var1.getLong(1));
         var3.setPacketId(var1.getLong(2));
         var3.setProjectId(var1.getLong(3));
         var3.setDesc(var1.getString(4));
         var3.setVersion(var1.getString(5));
         var3.setCreateUser(var1.getString(6));
         var3.setCreateDate(new Date(var1.getTimestamp(7).getTime()));
         var3.setEnable(var1.getBoolean(8));
         var3.setStatus(ApplyStatus.valueOf(var1.getString(9)));
         var3.setDigest(var1.getString(10));
         var3.setFiles(PacketDeployFileManager.ins.loadFiles(var3.getId()));
         var2.add(var3);
      }

      return var2;
   }

   private List b(ResultSet var1) throws SQLException {
      ArrayList var2 = new ArrayList();

      while(var1.next()) {
         PacketDeploy var3 = new PacketDeploy();
         var3.setId(var1.getLong(1));
         var3.setPacketId(var1.getLong(2));
         var3.setProjectId(var1.getLong(3));
         var3.setDesc(var1.getString(4));
         var3.setContent(var1.getString(5));
         var3.setVersion(var1.getString(6));
         var3.setCreateUser(var1.getString(7));
         var3.setCreateDate(new Date(var1.getTimestamp(8).getTime()));
         var3.setEnable(var1.getBoolean(9));
         var3.setStatus(ApplyStatus.valueOf(var1.getString(10)));
         var3.setDigest(var1.getString(11));
         var3.setFiles(PacketDeployFileManager.ins.loadFilesWithContent(var3.getId()));
         var2.add(var3);
      }

      return var2;
   }

   private StringBuilder a() {
      StringBuilder var1 = new StringBuilder();
      if (this.a != null) {
         if (var1.length() == 0) {
            var1.append(" ID_=?");
         } else {
            var1.append(" and ID_=?");
         }

         this.j.add(this.a);
      }

      if (this.b != null) {
         if (var1.length() == 0) {
            var1.append(" PACKET_ID_=?");
         } else {
            var1.append(" and PACKET_ID_=?");
         }

         this.j.add(this.b);
      }

      if (this.c != null) {
         if (var1.length() == 0) {
            var1.append(" APPLY_ID_=?");
         } else {
            var1.append(" and APPLY_ID_=?");
         }

         this.j.add(this.c);
      }

      if (this.d != null) {
         if (var1.length() == 0) {
            var1.append(" PROJECT_ID_=?");
         } else {
            var1.append(" and PROJECT_ID_=?");
         }

         this.j.add(this.d);
      }

      if (this.g != null) {
         if (var1.length() == 0) {
            var1.append(" STATUS_=?");
         } else {
            var1.append(" and STATUS_=?");
         }

         this.j.add(this.g.name());
      }

      if (this.e != null) {
         if (var1.length() == 0) {
            var1.append(" VERSION_=?");
         } else {
            var1.append(" and VERSION_=?");
         }

         this.j.add(this.e);
      }

      if (this.h != null) {
         if (var1.length() == 0) {
            var1.append(" VERSION_ like ?");
         } else {
            var1.append(" and VERSION_ like ?");
         }

         this.j.add("%" + this.h + "%");
      }

      if (this.i != null) {
         if (var1.length() == 0) {
            var1.append(" DESC_ like ?");
         } else {
            var1.append(" and DESC_ like ?");
         }

         this.j.add("%" + this.i + "%");
      }

      if (this.f != null) {
         if (var1.length() == 0) {
            var1.append(" ENABLE_=?");
         } else {
            var1.append(" and ENABLE_=?");
         }

         this.j.add(this.f);
      }

      return var1;
   }

   public PacketDeployQuery id(long var1) {
      this.a = var1;
      return this;
   }

   public PacketDeployQuery packetId(long var1) {
      this.b = var1;
      return this;
   }

   public PacketDeployQuery applyId(long var1) {
      this.c = var1;
      return this;
   }

   public PacketDeployQuery version(String var1) {
      this.e = var1;
      return this;
   }

   public PacketDeployQuery enable(boolean var1) {
      this.f = var1;
      return this;
   }

   public PacketDeployQuery versionLike(String var1) {
      this.h = var1;
      return this;
   }

   public PacketDeployQuery descLike(String var1) {
      this.i = var1;
      return this;
   }

   public PacketDeployQuery status(ApplyStatus var1) {
      this.g = var1;
      return this;
   }

   public PacketDeployQuery projectId(long var1) {
      this.d = var1;
      return this;
   }
}
