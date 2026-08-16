package com.bstek.urule.console.database.manager.batch.provider;

import com.bstek.urule.console.database.model.batch.BatchDataProvider;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProviderQueryImpl implements ProviderQuery {
   private Long a;
   private Long b;
   private String c;
   private String d;
   private List e = new ArrayList();

   public ProviderQuery id(Long var1) {
      this.a = var1;
      return this;
   }

   public ProviderQuery batchId(Long var1) {
      this.b = var1;
      return this;
   }

   public ProviderQuery nameLike(String var1) {
      this.c = var1;
      return this;
   }

   public ProviderQuery descLike(String var1) {
      this.d = var1;
      return this;
   }

   public List list() {
      Connection var1 = JdbcUtils.getConnection();

      ArrayList var13;
      try {
         String var2 = "SELECT NAME_, DATASOURCE_ID_, INPUT_DATA_, PACKET_VAR_NAME_, SUPPORT_PAGING_, PAGE_SIZE_, PAGE_SQL_, ORDER_FIELD_, ORDER_FIELD_PARAM_NAME_, PAGE_LIMIT_TYPE_, COUNT_SQL_, DESC_, CREATE_USER_, CREATE_DATE_, UPDATE_USER_, UPDATE_DATE_, ID_, PROJECT_ID_, BATCH_ID_ FROM URULE_BATCH_DATA_PROVIDER ";
         StringBuilder var3 = this.a();
         if (var3.length() > 0) {
            var2 = var2 + " where" + var3.toString();
         }

         PreparedStatement var4 = var1.prepareStatement(var2);
         JdbcUtils.fillPreparedStatementParameters(this.e, var4);
         ArrayList var5 = new ArrayList();
         ResultSet var6 = var4.executeQuery();

         while(var6.next()) {
            BatchDataProvider var7 = new BatchDataProvider();
            var7.setName(var6.getString(1));
            var7.setDatasourceId(var6.getLong(2));
            var7.setInputData(var6.getString(3));
            var7.setPacketVarName(var6.getString(4));
            var7.setSupportsPaging(var6.getBoolean(5));
            var7.setPageSize(var6.getInt(6));
            var7.setPageSql(var6.getString(7));
            var7.setOrderField(var6.getString(8));
            var7.setOrderFieldParamName(var6.getString(9));
            var7.setPageLimitType(var6.getString(10));
            var7.setCountSql(var6.getString(11));
            var7.setDesc(var6.getString(12));
            var7.setCreateUser(var6.getString(13));
            var7.setCreateDate(var6.getTimestamp(14));
            var7.setUpdateUser(var6.getString(15));
            var7.setUpdateDate(var6.getTimestamp(16));
            var7.setId(var6.getLong(17));
            var7.setProjectId(var6.getLong(18));
            var7.setBatchId(var6.getLong(19));
            var5.add(var7);
         }

         JdbcUtils.closeResultSet(var6);
         JdbcUtils.closeStatement(var4);
         var13 = var5;
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var1);
      }

      return var13;
   }

   private StringBuilder a() {
      this.e.clear();
      StringBuilder var1 = new StringBuilder();
      if (this.c != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" NAME_ like ?");
         this.e.add("%" + this.c + "%");
      }

      if (this.d != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" DESC_ like ?");
         this.e.add("%" + this.d + "%");
      }

      if (this.a != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" ID_=?");
         this.e.add(this.a);
      }

      if (this.b != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" BATCH_ID_=?");
         this.e.add(this.b);
      }

      return var1;
   }
}
