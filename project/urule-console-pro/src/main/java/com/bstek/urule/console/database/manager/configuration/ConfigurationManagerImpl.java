package com.bstek.urule.console.database.manager.configuration;

import com.bstek.urule.console.config.Configuration;
import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.database.util.NamedSQLUtils;
import com.bstek.urule.console.database.util.ParsedSql;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ConfigurationManagerImpl implements ConfigurationManager {
   public void getConfigurations(Configuration var1, Page var2) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         String var4 = "select ID_, KEY_, VALUE_, LABEL_, TYPE_, CREATE_DATE_, UPDATE_DATE_ from URULE_PROPERTY";
         String var5 = "";
         if (StringUtils.isNotBlank(var1.getKey())) {
            if (StringUtils.isNotBlank(var5)) {
               var5 = var5 + " or ";
            }

            var5 = var5 + "KEY_ like :key ";
         }

         if (StringUtils.isNotBlank(var1.getLabel())) {
            if (StringUtils.isNotBlank(var5)) {
               var5 = var5 + " or ";
            }

            var5 = var5 + "LABEL_ like :label ";
         }

         if (StringUtils.isNotBlank(var1.getType())) {
            if (StringUtils.isNotBlank(var5)) {
               var5 = var5 + " or ";
            }

            var5 = var5 + "TYPE_ = :type ";
         }

         if (StringUtils.isNotBlank(var5)) {
            var5 = " where " + var5;
         }

         ParsedSql var6 = NamedSQLUtils.parseSql(JdbcUtils.getPageSql(var4 + var5, var2.getStartRow(), var2.getPageSize()));
         PreparedStatement var7 = var3.prepareStatement(JdbcUtils.getOriginSql(var6.getOriginalSql()));

         for(int var8 = 0; var8 < var6.getParameterNames().size(); ++var8) {
            String var9 = (String)var6.getParameterNames().get(var8);
            int var10 = var8 + 1;
            if ("key".equals(var9)) {
               var7.setString(var10, "%" + var1.getKey() + "%");
            }

            if ("label".equals(var9)) {
               var7.setString(var10, "%" + var1.getLabel() + "%");
            }

            if ("type".equals(var9)) {
               var7.setString(var10, var1.getType());
            }
         }

         ArrayList var21 = new ArrayList();
         ResultSet var22 = var7.executeQuery();

         while(var22.next()) {
            Configuration var24 = new Configuration();
            var24.setId(var22.getLong(1));
            var24.setKey(var22.getString(2));
            var24.setValue(var22.getString(3));
            var24.setLabel(var22.getString(4));
            var24.setType(var22.getString(5));
            var24.setCreateDate(var22.getTimestamp(6));
            var24.setUpdateDate(var22.getTimestamp(7));
            var21.add(var24);
         }

         JdbcUtils.closeResultSet(var22);
         var2.setData(var21);
         String var25 = JdbcUtils.getCountSql(var4 + var5);
         var6 = NamedSQLUtils.parseSql(var25);
         var7 = var3.prepareStatement(JdbcUtils.getOriginSql(var6.getOriginalSql()));

         for(int var11 = 0; var11 < var6.getParameterNames().size(); ++var11) {
            String var12 = (String)var6.getParameterNames().get(var11);
            int var13 = var11 + 1;
            if ("key".equals(var12)) {
               var7.setString(var13, "%" + var1.getKey() + "%");
            }

            if ("label".equals(var12)) {
               var7.setString(var13, "%" + var1.getLabel() + "%");
            }

            if ("type".equals(var12)) {
               var7.setString(var13, var1.getType());
            }
         }

         var22 = var7.executeQuery();
         if (var22.next()) {
            var2.setTotalRows(var22.getLong(1));
         }

         JdbcUtils.closeStatement(var7);
      } catch (Exception var17) {
         throw new RuleException(var17);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public void updateConfiguration(Configuration var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var1.setUpdateDate(new Timestamp(System.currentTimeMillis()));
         PreparedStatement var3 = var2.prepareStatement("update URULE_PROPERTY set KEY_=?, LABEL_=?, TYPE_=?, VALUE_=?, UPDATE_DATE_=? where ID_=?");
         var3.setString(1, var1.getKey());
         var3.setString(2, var1.getLabel());
         var3.setString(3, var1.getType());
         var3.setString(4, var1.getValue());
         var3.setTimestamp(5, new Timestamp(var1.getUpdateDate().getTime()));
         var3.setLong(6, var1.getId());
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void insertConfiguration(Configuration var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("insert into URULE_PROPERTY (ID_, KEY_, LABEL_, TYPE_, VALUE_, CREATE_DATE_, UPDATE_DATE_) values(?,?,?,?,?,?,?)");
         long var4 = IDGenerator.getInstance().nextId(IDType.PROPERTY);
         var1.setCreateDate(new Timestamp(System.currentTimeMillis()));
         var1.setUpdateDate(new Timestamp(System.currentTimeMillis()));
         var1.setId(var4);
         var3.setLong(1, var1.getId());
         var3.setString(2, var1.getKey());
         var3.setString(3, var1.getLabel());
         var3.setString(4, var1.getType());
         var3.setString(5, var1.getValue());
         var3.setTimestamp(6, new Timestamp(var1.getCreateDate().getTime()));
         var3.setTimestamp(7, new Timestamp(var1.getUpdateDate().getTime()));
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void deleteConfiguration(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         PreparedStatement var4 = var3.prepareStatement("delete FROM URULE_PROPERTY where ID_=? and TYPE_!='system'");
         var4.setLong(1, var1);
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public Configuration getConfiguration(String var1) {
      Connection var2 = JdbcUtils.getConnection();

      Configuration var7;
      try {
         String var3 = "select ID_, KEY_, VALUE_, LABEL_, TYPE_, CREATE_DATE_, UPDATE_DATE_ from URULE_PROPERTY WHERE KEY_=?";
         PreparedStatement var4 = var2.prepareStatement(var3);
         var4.setString(1, var1);
         ResultSet var5 = var4.executeQuery();
         if (!var5.next()) {
            JdbcUtils.closeStatement(var4);
            return null;
         }

         Configuration var6 = new Configuration();
         var6.setId(var5.getLong(1));
         var6.setKey(var5.getString(2));
         var6.setValue(var5.getString(3));
         var6.setLabel(var5.getString(4));
         var6.setType(var5.getString(5));
         var6.setCreateDate(var5.getTimestamp(6));
         var6.setUpdateDate(var5.getTimestamp(7));
         var7 = var6;
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

      return var7;
   }
}
