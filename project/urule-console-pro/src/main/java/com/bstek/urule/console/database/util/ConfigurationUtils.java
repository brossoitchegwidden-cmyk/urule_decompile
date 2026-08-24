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
   private static String SELECT_CONFIGURATION_SQL = "select ID_, KEY_, VALUE_, LABEL_, CREATE_DATE_, UPDATE_DATE_ from URULE_PROPERTY";
   private static String UPDATE_CONFIGURATION_SQL = "update URULE_PROPERTY set VALUE_=? , UPDATE_DATE_=? where KEY_=?";

   public static List getConfigurations(Connection conn) {
      ArrayList configurations = new ArrayList();

      try {
         Statement statement = conn.createStatement();
         ResultSet resultSet = statement.executeQuery(ConfigurationUtils.SELECT_CONFIGURATION_SQL);

         while(resultSet.next()) {
            Configuration configuration = new Configuration();
            configuration.setId(resultSet.getLong(1));
            configuration.setKey(resultSet.getString(2));
            configuration.setValue(resultSet.getString(3));
            configuration.setLabel(resultSet.getString(4));
            configuration.setCreateDate(resultSet.getTimestamp(5));
            configuration.setUpdateDate(resultSet.getTimestamp(6));
            configurations.add(configuration);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(statement);
         return configurations;
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   public static void updateConfiguration(Connection conn, Configuration configuration) {
      try {
         PreparedStatement preparedStatement = conn.prepareStatement(ConfigurationUtils.UPDATE_CONFIGURATION_SQL);
         preparedStatement.setString(1, configuration.getKey());
         preparedStatement.setString(2, configuration.getValue());
         preparedStatement.setDate(3, new Date(System.currentTimeMillis()));
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }
}
