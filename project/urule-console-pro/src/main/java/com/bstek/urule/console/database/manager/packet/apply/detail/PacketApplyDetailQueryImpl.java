package com.bstek.urule.console.database.manager.packet.apply.detail;

import com.bstek.urule.console.database.model.PacketApplyDetail;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PacketApplyDetailQueryImpl implements PacketApplyDetailQuery {
   private Long a;
   private Long b;
   private Long c;
   private List d = new ArrayList();

   public List list() {
      String var1 = "select ID_,APPLY_ID_,PROJECT_ID_,DESC_,CREATE_USER_,CREATE_DATE_ from URULE_PACKET_APPLY_DETAIL";
      StringBuilder var2 = this.a();
      if (var2.length() > 0) {
         var1 = var1 + " where" + var2.toString();
      }

      var1 = var1 + " order by CREATE_DATE_ desc";
      Connection var3 = JdbcUtils.getConnection();

      List var7;
      try {
         PreparedStatement var4 = var3.prepareStatement(var1);
         JdbcUtils.fillPreparedStatementParameters(this.d, var4);
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
         PacketApplyDetail var3 = new PacketApplyDetail();
         var3.setId(var1.getLong(1));
         var3.setApplyId(var1.getLong(2));
         var3.setProjectId(var1.getLong(3));
         var3.setDesc(var1.getString(4));
         var3.setCreateUser(var1.getString(5));
         var3.setCreateDate(new Date(var1.getTimestamp(6).getTime()));
         var2.add(var3);
      }

      return var2;
   }

   private StringBuilder a() {
      this.d.clear();
      StringBuilder var1 = new StringBuilder();
      if (this.a != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" ID_=?");
         this.d.add(this.a);
      }

      if (this.b != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" APPLY_ID_=?");
         this.d.add(this.b);
      }

      if (this.c != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" PROJECT_ID_=?");
         this.d.add(this.c);
      }

      return var1;
   }

   public PacketApplyDetailQuery id(long var1) {
      this.a = var1;
      return this;
   }

   public PacketApplyDetailQuery applyId(long var1) {
      this.b = var1;
      return this;
   }

   public PacketApplyDetailQuery projectId(long var1) {
      this.c = var1;
      return this;
   }
}
