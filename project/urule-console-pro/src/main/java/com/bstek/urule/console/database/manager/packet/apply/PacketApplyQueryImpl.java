package com.bstek.urule.console.database.manager.packet.apply;

import com.bstek.urule.console.database.manager.packet.apply.detail.PacketApplyDetailManager;
import com.bstek.urule.console.database.model.ApplyStatus;
import com.bstek.urule.console.database.model.ApplyType;
import com.bstek.urule.console.database.model.PacketApply;
import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PacketApplyQueryImpl implements PacketApplyQuery {
   private Long a;
   private Long b;
   private Long c;
   private ApplyType d;
   private ApplyStatus[] e;
   private ApplyStatus f;
   private ApplyStatus g;
   private String h;
   private String i;
   private String j;
   private String k;
   private String l;
   private Date m;
   private Date n;
   private List o = new ArrayList();

   public Page paging(int var1, int var2) {
      String var3 = "select ID_,PACKET_ID_,DEPLOYED_PACKET_ID_,PROJECT_ID_,TYPE_,TITLE_,DESC_,APPROVER_,STATUS_,CREATE_USER_,CREATE_DATE_,UPDATE_DATE_ from URULE_PACKET_APPLY";
      StringBuilder var4 = this.a();
      if (var4.length() > 0) {
         var3 = var3 + " where" + var4.toString();
      }

      var3 = var3 + " order by UPDATE_DATE_ desc";
      Connection var5 = JdbcUtils.getConnection();

      Page var10;
      try {
         Page var6 = new Page(var1, var2);
         var3 = JdbcUtils.getPageSql(var3, var6.getStartRow(), var2);
         PreparedStatement var7 = var5.prepareStatement(var3);
         JdbcUtils.fillPreparedStatementParameters(this.o, var7);
         ResultSet var8 = var7.executeQuery();
         List var9 = this.a(var8);
         var6.setData(var9);
         JdbcUtils.closeResultSet(var8);
         JdbcUtils.closeStatement(var7);
         var3 = "select count(*) from URULE_PACKET_APPLY";
         if (var4.length() > 0) {
            var3 = var3 + " where" + var4.toString();
         }

         var7 = var5.prepareStatement(var3);
         JdbcUtils.fillPreparedStatementParameters(this.o, var7);
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
      String var1 = "select ID_,PACKET_ID_,DEPLOYED_PACKET_ID_,PROJECT_ID_,TYPE_,TITLE_,DESC_,APPROVER_,STATUS_,CREATE_USER_,CREATE_DATE_,UPDATE_DATE_ from URULE_PACKET_APPLY";
      StringBuilder var2 = this.a();
      if (var2.length() > 0) {
         var1 = var1 + " where" + var2.toString();
      }

      var1 = var1 + " order by UPDATE_DATE_ desc";
      Connection var3 = JdbcUtils.getConnection();

      List var7;
      try {
         PreparedStatement var4 = var3.prepareStatement(var1);
         JdbcUtils.fillPreparedStatementParameters(this.o, var4);
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

   private List a(ResultSet var1) throws Exception {
      ArrayList var2 = new ArrayList();

      while(var1.next()) {
         PacketApply var3 = new PacketApply();
         var3.setId(var1.getLong(1));
         var3.setPacketId(var1.getLong(2));
         var3.setDeployedPacketId(var1.getLong(3));
         var3.setProjectId(var1.getLong(4));
         var3.setType(ApplyType.valueOf(var1.getString(5)));
         var3.setTitle(var1.getString(6));
         var3.setDesc(var1.getString(7));
         var3.setApprover(var1.getString(8));
         var3.setStatus(ApplyStatus.valueOf(var1.getString(9)));
         var3.setCreateUser(var1.getString(10));
         var3.setCreateDate(new Date(var1.getTimestamp(11).getTime()));
         var3.setUpdateDate(new Date(var1.getTimestamp(12).getTime()));
         var3.setDetails(PacketApplyDetailManager.ins.loadByApplyId(var3.getId()));
         var2.add(var3);
      }

      return var2;
   }

   private StringBuilder a() {
      this.o.clear();
      StringBuilder var1 = new StringBuilder();
      if (this.a != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" ID_=?");
         this.o.add(this.a);
      }

      if (this.c != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" PROJECT_ID_=?");
         this.o.add(this.c);
      }

      if (this.b != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" PACKET_ID_=?");
         this.o.add(this.b);
      }

      if (this.d != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" TYPE_=?");
         this.o.add(this.d.name());
      }

      if (this.e != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" STATUS_ in (");

         for(int var2 = 0; var2 < this.e.length; ++var2) {
            if (var2 > 0) {
               var1.append(",");
            }

            var1.append("?");
            this.o.add(this.e[var2].name());
         }

         var1.append(")");
      }

      if (this.f != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" STATUS_=?");
         this.o.add(this.f.name());
      }

      if (this.g != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" STATUS_ <> ?");
         this.o.add(this.g.name());
      }

      if (this.h != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" TITLE_ like ?");
         this.o.add("%" + this.h + "%");
      }

      if (this.i != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" DESC_ like ?");
         this.o.add("%" + this.i + "%");
      }

      if (this.j != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" APPROVER_=?");
         this.o.add(this.j);
      }

      if (this.k != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" CREATE_USER_=?");
         this.o.add(this.k);
      }

      if (this.l != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" CREATE_USER_ like ?");
         this.o.add("%" + this.l + "%");
      }

      if (this.m != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" CREATE_DATE_>=?");
         this.o.add(this.m);
      }

      if (this.n != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" CREATE_DATE_<=?");
         this.o.add(this.n);
      }

      return var1;
   }

   public PacketApplyQuery id(long var1) {
      this.a = var1;
      return this;
   }

   public PacketApplyQuery projectId(long var1) {
      this.c = var1;
      return this;
   }

   public PacketApplyQuery packetId(long var1) {
      this.b = var1;
      return this;
   }

   public PacketApplyQuery titleLike(String var1) {
      this.h = var1;
      return this;
   }

   public PacketApplyQuery descLike(String var1) {
      this.i = var1;
      return this;
   }

   public PacketApplyQuery approver(String var1) {
      this.j = var1;
      return this;
   }

   public PacketApplyQuery createUserLike(String var1) {
      this.l = var1;
      return this;
   }

   public PacketApplyQuery createUser(String var1) {
      this.k = var1;
      return this;
   }

   public PacketApplyQuery type(ApplyType var1) {
      this.d = var1;
      return this;
   }

   public PacketApplyQuery status(ApplyStatus var1) {
      this.f = var1;
      return this;
   }

   public PacketApplyQuery notStatus(ApplyStatus var1) {
      this.g = var1;
      return this;
   }

   public PacketApplyQuery statusIn(ApplyStatus[] var1) {
      this.e = var1;
      return this;
   }

   public PacketApplyQuery startDate(Date var1) {
      this.m = var1;
      return this;
   }

   public PacketApplyQuery endDate(Date var1) {
      this.n = var1;
      return this;
   }
}
