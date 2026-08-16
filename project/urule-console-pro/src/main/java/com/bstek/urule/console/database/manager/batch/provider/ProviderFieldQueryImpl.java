package com.bstek.urule.console.database.manager.batch.provider;

import com.bstek.urule.console.database.model.batch.BatchDataProviderField;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProviderFieldQueryImpl implements ProviderFieldQuery {
   private Long a;
   private Long b;
   private Long c;
   private String d;
   private String e;
   private List f = new ArrayList();

   public ProviderFieldQuery id(Long var1) {
      this.a = var1;
      return this;
   }

   public ProviderFieldQuery providerId(Long var1) {
      this.c = var1;
      return this;
   }

   public ProviderFieldQuery nameLike(String var1) {
      this.d = var1;
      return this;
   }

   public ProviderFieldQuery descLike(String var1) {
      this.e = var1;
      return this;
   }

   private StringBuilder a() {
      this.f.clear();
      StringBuilder var1 = new StringBuilder();
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

      if (this.a != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" ID_=?");
         this.f.add(this.a);
      }

      if (this.c != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" PROVIDER_ID_=?");
         this.f.add(this.c);
      }

      if (this.b != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" BATCH_ID_=?");
         this.f.add(this.b);
      }

      return var1;
   }

   public List list() {
      Connection var1 = JdbcUtils.getConnection();

      ArrayList var13;
      try {
         String var2 = "SELECT SRC_PROPERTY_, DATA_TYPE_, DEST_PROPERTY_, CLAZZ_PATH_, DATA_PROVIDER_ID_, CREATE_USER_, CREATE_DATE_, UPDATE_USER_, UPDATE_DATE_, ID_, PROJECT_ID_, BATCH_ID_, PROVIDER_ID_ FROM URULE_BATCH_PROVIDER_FIELD ";
         StringBuilder var3 = this.a();
         if (var3.length() > 0) {
            var2 = var2 + " where" + var3.toString();
         }

         PreparedStatement var4 = var1.prepareStatement(var2);
         JdbcUtils.fillPreparedStatementParameters(this.f, var4);
         ArrayList var5 = new ArrayList();
         ResultSet var6 = var4.executeQuery();

         while(var6.next()) {
            BatchDataProviderField var7 = new BatchDataProviderField();
            var7.setSrcProperty(var6.getString(1));
            var7.setDataType(var6.getString(2));
            var7.setDestProperty(var6.getString(3));
            var7.setClassPath(var6.getString(4));
            var7.setDataProviderId(var6.getLong(5));
            var7.setCreateUser(var6.getString(6));
            var7.setCreateDate(var6.getTimestamp(7));
            var7.setUpdateUser(var6.getString(8));
            var7.setUpdateDate(var6.getTimestamp(9));
            var7.setId(var6.getLong(10));
            var7.setProjectId(var6.getLong(11));
            var7.setBatchId(var6.getLong(12));
            var7.setProviderId(var6.getLong(13));
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

   public ProviderFieldQuery batchId(Long var1) {
      this.b = var1;
      return this;
   }
}
