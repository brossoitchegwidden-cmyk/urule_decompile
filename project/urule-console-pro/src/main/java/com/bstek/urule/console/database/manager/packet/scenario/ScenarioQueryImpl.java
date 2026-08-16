package com.bstek.urule.console.database.manager.packet.scenario;

import com.bstek.urule.console.database.model.Scenario;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScenarioQueryImpl implements ScenarioQuery {
   private Long a;
   private Long b;
   private Long c;
   private String d;
   private String e;
   private List f = new ArrayList();

   protected ScenarioQueryImpl() {
   }

   public List list() {
      String var1 = "select ID_,PACKET_ID_,PROJECT_ID_,NAME_,DESC_,INPUT_DATA_,OUTPUT_DATA_,EXCEL_FILE_NAME_,CREATE_USER_,UPDATE_USER_,CREATE_DATE_,UPDATE_DATE_ from URULE_PACKET_SCENARIO";
      StringBuilder var2 = this.a();
      if (var2.length() > 0) {
         var1 = var1 + " where" + var2.toString();
      }

      var1 = var1 + " order by CREATE_DATE_ desc";
      Connection var3 = JdbcUtils.getConnection();

      ArrayList var14;
      try {
         PreparedStatement var4 = var3.prepareStatement(var1);
         JdbcUtils.fillPreparedStatementParameters(this.f, var4);
         ResultSet var5 = var4.executeQuery();
         ArrayList var6 = new ArrayList();

         while(var5.next()) {
            Scenario var7 = new Scenario();
            var7.setId(var5.getLong(1));
            var7.setPacketId(var5.getLong(2));
            var7.setProjectId(var5.getLong(3));
            var7.setName(var5.getString(4));
            var7.setDesc(var5.getString(5));
            var7.setInputData(var5.getString(6));
            var7.setOutputData(var5.getString(7));
            var7.setExcelFileName(var5.getString(8));
            var7.setCreateUser(var5.getString(9));
            var7.setUpdateUser(var5.getString(10));
            var7.setCreateDate(new Date(var5.getTimestamp(11).getTime()));
            var7.setUpdateDate(new Date(var5.getTimestamp(12).getTime()));
            var6.add(var7);
         }

         JdbcUtils.closeResultSet(var5);
         JdbcUtils.closeStatement(var4);
         var14 = var6;
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

      return var14;
   }

   private StringBuilder a() {
      this.f.clear();
      StringBuilder var1 = new StringBuilder();
      if (this.a != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" ID_=?");
         this.f.add(this.a);
      }

      if (this.b != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" PACKET_ID_=?");
         this.f.add(this.b);
      }

      if (this.c != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" PROJECT_ID_=?");
         this.f.add(this.c);
      }

      if (this.d != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" NAME_ like ?");
         this.f.add("%" + this.d + "%");
      }

      if (this.e != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" DESC_ like ?");
         this.f.add("%" + this.e + "%");
      }

      return var1;
   }

   public ScenarioQuery id(long var1) {
      this.a = var1;
      return this;
   }

   public ScenarioQuery packetId(long var1) {
      this.b = var1;
      return this;
   }

   public ScenarioQuery nameLike(String var1) {
      this.d = var1;
      return this;
   }

   public ScenarioQuery descLike(String var1) {
      this.e = var1;
      return this;
   }

   public ScenarioQuery projectId(long var1) {
      this.c = var1;
      return this;
   }
}
