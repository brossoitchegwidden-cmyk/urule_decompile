package com.bstek.urule.console.database.util;

import com.bstek.urule.console.config.Configuration;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationUtils {
   private static String a = "select ID_, KEY_, VALUE_, LABEL_, CREATE_DATE_, UPDATE_DATE_ from URULE_PROPERTY";
   private static String b = "update URULE_PROPERTY set VALUE_=? , UPDATE_DATE_=? where KEY_=?";

   public static List getConfigurations(Connection var0) {
      ArrayList var1 = new ArrayList();

      try {
         Statement var2 = var0.createStatement();
         ResultSet var3 = var2.executeQuery(a);

         while(var3.next()) {
            Configuration var4 = new Configuration();
            var4.setId(var3.getLong(1));
            var4.setKey(var3.getString(2));
            var4.setValue(var3.getString(3));
            var4.setLabel(var3.getString(4));
            var4.setCreateDate(var3.getTimestamp(5));
            var4.setUpdateDate(var3.getTimestamp(6));
            var1.add(var4);
         }

         JdbcUtils.closeResultSet(var3);
         JdbcUtils.closeStatement(var2);
         return var1;
      } catch (Exception var5) {
         throw new RuleException(var5);
      }
   }

   public static void updateConfiguration(Connection var0, Configuration var1) {
      try {
         PreparedStatement var2 = var0.prepareStatement(b);
         var2.setString(1, var1.getKey());
         var2.setString(2, var1.getValue());
         var2.setDate(3, new Date(System.currentTimeMillis()));
         var2.executeUpdate();
         JdbcUtils.closeStatement(var2);
      } catch (Exception var3) {
         throw new RuleException(var3);
      }
   }
}
