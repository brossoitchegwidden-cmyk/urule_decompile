package com.bstek.urule.console.database.manager.packet;

import com.bstek.urule.console.database.manager.packet.apply.PacketApplyManager;
import com.bstek.urule.console.database.manager.packet.deploy.PacketDeployManager;
import com.bstek.urule.console.database.manager.packet.file.PacketFileManager;
import com.bstek.urule.console.database.manager.packet.packge.PacketPackageManager;
import com.bstek.urule.console.database.model.Packet;
import com.bstek.urule.console.database.model.PacketPackage;
import com.bstek.urule.console.database.model.PacketType;
import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PacketQueryImpl implements PacketQuery {
   private Long a;
   private Long b;
   private String c;
   private String d;
   private String e;
   private String f;
   private String g;
   private String h;
   private String i;
   private String j;
   private Boolean k;
   private Boolean l;
   private Boolean m;
   private List n = new ArrayList();

   protected PacketQueryImpl() {
   }

   public List list() {
      String var1 = "select ID_,PROJECT_ID_,NAME_,CODE_,TYPE_,DESC_,INPUT_DATA_,OUTPUT_DATA_,ENABLE_,AUDIT_ENABLE_,REST_SECURITY_ENABLE_,REST_SECURITY_USER_,REST_SECURITY_PASSWORD_,REST_INPUT_,REST_OUTPUT_,CREATE_USER_,UPDATE_USER_,CREATE_DATE_,UPDATE_DATE_,REST_ENABLE_,AUDIT_INPUT_,AUDIT_OUTPUT_ from URULE_PACKET";
      StringBuilder var2 = this.a();
      if (var2.length() > 0) {
         var1 = var1 + " where" + var2.toString();
      }

      var1 = var1 + " order by CREATE_DATE_ desc";
      Connection var3 = JdbcUtils.getConnection();

      List var7;
      try {
         PreparedStatement var4 = var3.prepareStatement(var1);
         JdbcUtils.fillPreparedStatementParameters(this.n, var4);
         ResultSet var5 = var4.executeQuery();
         List var6 = this.a(var5);
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

   public Page paging(int var1, int var2) {
      String var3 = "select ID_,PROJECT_ID_,NAME_,CODE_,TYPE_,DESC_,INPUT_DATA_,OUTPUT_DATA_,ENABLE_,AUDIT_ENABLE_,REST_SECURITY_ENABLE_,REST_SECURITY_USER_,REST_SECURITY_PASSWORD_,REST_INPUT_,REST_OUTPUT_,CREATE_USER_,UPDATE_USER_,CREATE_DATE_,UPDATE_DATE_,REST_ENABLE_,AUDIT_INPUT_,AUDIT_OUTPUT_ from URULE_PACKET";
      StringBuilder var4 = this.a();
      if (var4.length() > 0) {
         var3 = var3 + " where" + var4.toString();
      }

      var3 = var3 + " order by CREATE_DATE_ desc";
      Connection var5 = JdbcUtils.getConnection();

      Page var10;
      try {
         Page var6 = new Page(var1, var2);
         var3 = JdbcUtils.getPageSql(var3, var6.getStartRow(), var2);
         PreparedStatement var7 = var5.prepareStatement(var3);
         JdbcUtils.fillPreparedStatementParameters(this.n, var7);
         ResultSet var8 = var7.executeQuery();
         List var9 = this.a(var8);
         var6.setData(var9);
         JdbcUtils.closeResultSet(var8);
         JdbcUtils.closeStatement(var7);
         var3 = "select count(*) from URULE_PACKET";
         if (var4.length() > 0) {
            var3 = var3 + " where" + var4.toString();
         }

         var7 = var5.prepareStatement(var3);
         JdbcUtils.fillPreparedStatementParameters(this.n, var7);
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

   private List a(ResultSet var1) throws Exception {
      ArrayList var2;
      Packet var3;
      for(var2 = new ArrayList(); var1.next(); var2.add(var3)) {
         var3 = new Packet();
         var3.setId(var1.getLong(1));
         var3.setProjectId(var1.getLong(2));
         var3.setName(var1.getString(3));
         var3.setCode(var1.getString(4));
         String var4 = var1.getString(5);
         if (var4 != null) {
            var3.setType(PacketType.valueOf(var4));
         } else {
            var3.setType(PacketType.file);
         }

         var3.setDesc(var1.getString(6));
         var3.setInputData(var1.getString(7));
         var3.setOutputData(var1.getString(8));
         var3.setEnable(var1.getBoolean(9));
         var3.setAuditEnable(var1.getBoolean(10));
         var3.setRestSecurityEnable(var1.getBoolean(11));
         var3.setRestSecurityUser(var1.getString(12));
         var3.setRestSecurityPassword(var1.getString(13));
         var3.setRestInput(var1.getString(14));
         var3.setRestOutput(var1.getString(15));
         var3.setCreateUser(var1.getString(16));
         var3.setUpdateUser(var1.getString(17));
         var3.setCreateDate(new Date(var1.getTimestamp(18).getTime()));
         var3.setUpdateDate(new Date(var1.getTimestamp(19).getTime()));
         var3.setRestEnable(var1.getBoolean(20));
         var3.setAuditInput(var1.getString(21));
         var3.setAuditOutput(var1.getString(22));
         List var5 = PacketDeployManager.ins.newQuery().packetId(var3.getId()).list();
         if (var5.size() > 0) {
            var3.setDeleteEnable(false);
         } else {
            List var6 = PacketApplyManager.ins.newQuery().packetId(var3.getId()).list();
            if (var6.size() > 0) {
               var3.setDeleteEnable(false);
            }
         }

         PacketPackage var7 = PacketPackageManager.ins.loadByPacketId(var3.getId());
         if (var7 == null) {
            var3.setPacketPackage(new PacketPackage());
         } else {
            var3.setPacketPackage(var7);
         }

         var3.setFiles(PacketFileManager.ins.newQuery().packetId(var3.getId()).list());
         var3.setDeployedCount(PacketDeployManager.ins.newQuery().packetId(var3.getId()).count());
         if (var7 != null && var7.getPacketId() > 1L) {
            var3.setDeployedCount(1L);
         }
      }

      return var2;
   }

   private StringBuilder a() {
      this.n.clear();
      StringBuilder var1 = new StringBuilder();
      if (this.a != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" ID_=?");
         this.n.add(this.a);
      }

      if (this.h != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" ID_ like ?");
         this.n.add("%" + this.h + "%");
      }

      if (this.e != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" CODE_=?");
         this.n.add(this.e);
      }

      if (this.f != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" CODE_ like ?");
         this.n.add("%" + this.f + "%");
      }

      if (this.b != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" PROJECT_ID_=?");
         this.n.add(this.b);
      }

      if (this.k != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" ENABLE_=?");
         this.n.add(this.k);
      }

      if (this.l != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" REST_ENABLE_=?");
         this.n.add(this.l);
      }

      if (this.m != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" AUDIT_ENABLE_=?");
         this.n.add(this.m);
      }

      if (this.c != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" NAME_ = ?");
         this.n.add(this.c);
      }

      if (this.d != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" NAME_ like ?");
         this.n.add("%" + this.d + "%");
      }

      if (this.g != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" TYPE_ like ?");
         this.n.add("%" + this.g + "%");
      }

      if (this.g != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" TYPE_ like ?");
         this.n.add("%" + this.g + "%");
      }

      if (this.i != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" DESC_ like ?");
         this.n.add("%" + this.i + "%");
      }

      if (this.j != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" CREATE_USER_ like ?");
         this.n.add("%" + this.j + "%");
      }

      return var1;
   }

   public PacketQuery id(long var1) {
      this.a = var1;
      return this;
   }

   public PacketQuery code(String var1) {
      this.e = var1;
      return this;
   }

   public PacketQuery idLike(String var1) {
      this.h = var1;
      return null;
   }

   public PacketQuery projectId(long var1) {
      this.b = var1;
      return this;
   }

   public PacketQuery nameLike(String var1) {
      this.d = var1;
      return this;
   }

   public PacketQuery typeLike(String var1) {
      this.g = var1;
      return this;
   }

   public PacketQuery descLike(String var1) {
      this.i = var1;
      return this;
   }

   public PacketQuery createUserLike(String var1) {
      this.j = var1;
      return this;
   }

   public PacketQuery enable(boolean var1) {
      this.k = var1;
      return this;
   }

   public PacketQuery restEnable(boolean var1) {
      this.l = var1;
      return this;
   }

   public PacketQuery auditEnable(boolean var1) {
      this.m = var1;
      return this;
   }

   public PacketQuery codeLike(String var1) {
      this.f = var1;
      return this;
   }

   public PacketQuery name(String var1) {
      this.c = var1;
      return this;
   }
}
