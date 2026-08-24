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
   public void getConfigurations(Configuration condition, Page page) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "select ID_, KEY_, VALUE_, LABEL_, TYPE_, CREATE_DATE_, UPDATE_DATE_ from URULE_PROPERTY";
         String text2 = "";
         if (StringUtils.isNotBlank(condition.getKey())) {
            if (StringUtils.isNotBlank(text2)) {
               text2 = text2 + " or ";
            }

            text2 = text2 + "KEY_ like :key ";
         }

         if (StringUtils.isNotBlank(condition.getLabel())) {
            if (StringUtils.isNotBlank(text2)) {
               text2 = text2 + " or ";
            }

            text2 = text2 + "LABEL_ like :label ";
         }

         if (StringUtils.isNotBlank(condition.getType())) {
            if (StringUtils.isNotBlank(text2)) {
               text2 = text2 + " or ";
            }

            text2 = text2 + "TYPE_ = :type ";
         }

         if (StringUtils.isNotBlank(text2)) {
            text2 = " where " + text2;
         }

         ParsedSql sql = NamedSQLUtils.parseSql(JdbcUtils.getPageSql(text + text2, page.getStartRow(), page.getPageSize()));
         PreparedStatement preparedStatement = connection.prepareStatement(JdbcUtils.getOriginSql(sql.getOriginalSql()));

         for(int index = 0; index < sql.getParameterNames().size(); ++index) {
            String text3 = (String)sql.getParameterNames().get(index);
            int number = index + 1;
            if ("key".equals(text3)) {
               preparedStatement.setString(number, "%" + condition.getKey() + "%");
            }

            if ("label".equals(text3)) {
               preparedStatement.setString(number, "%" + condition.getLabel() + "%");
            }

            if ("type".equals(text3)) {
               preparedStatement.setString(number, condition.getType());
            }
         }

         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            Configuration configuration = new Configuration();
            configuration.setId(resultSet.getLong(1));
            configuration.setKey(resultSet.getString(2));
            configuration.setValue(resultSet.getString(3));
            configuration.setLabel(resultSet.getString(4));
            configuration.setType(resultSet.getString(5));
            configuration.setCreateDate(resultSet.getTimestamp(6));
            configuration.setUpdateDate(resultSet.getTimestamp(7));
            items.add(configuration);
         }

         JdbcUtils.closeResultSet(resultSet);
         page.setData(items);
         String countSql = JdbcUtils.getCountSql(text + text2);
         sql = NamedSQLUtils.parseSql(countSql);
         preparedStatement = connection.prepareStatement(JdbcUtils.getOriginSql(sql.getOriginalSql()));

         for(int index2 = 0; index2 < sql.getParameterNames().size(); ++index2) {
            String text4 = (String)sql.getParameterNames().get(index2);
            int number2 = index2 + 1;
            if ("key".equals(text4)) {
               preparedStatement.setString(number2, "%" + condition.getKey() + "%");
            }

            if ("label".equals(text4)) {
               preparedStatement.setString(number2, "%" + condition.getLabel() + "%");
            }

            if ("type".equals(text4)) {
               preparedStatement.setString(number2, condition.getType());
            }
         }

         resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            page.setTotalRows(resultSet.getLong(1));
         }

         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void updateConfiguration(Configuration configuration) {
      Connection connection = JdbcUtils.getConnection();

      try {
         configuration.setUpdateDate(new Timestamp(System.currentTimeMillis()));
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_PROPERTY set KEY_=?, LABEL_=?, TYPE_=?, VALUE_=?, UPDATE_DATE_=? where ID_=?");
         preparedStatement.setString(1, configuration.getKey());
         preparedStatement.setString(2, configuration.getLabel());
         preparedStatement.setString(3, configuration.getType());
         preparedStatement.setString(4, configuration.getValue());
         preparedStatement.setTimestamp(5, new Timestamp(configuration.getUpdateDate().getTime()));
         preparedStatement.setLong(6, configuration.getId());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void insertConfiguration(Configuration configuration) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("insert into URULE_PROPERTY (ID_, KEY_, LABEL_, TYPE_, VALUE_, CREATE_DATE_, UPDATE_DATE_) values(?,?,?,?,?,?,?)");
         long longValue = IDGenerator.getInstance().nextId(IDType.PROPERTY);
         configuration.setCreateDate(new Timestamp(System.currentTimeMillis()));
         configuration.setUpdateDate(new Timestamp(System.currentTimeMillis()));
         configuration.setId(longValue);
         preparedStatement.setLong(1, configuration.getId());
         preparedStatement.setString(2, configuration.getKey());
         preparedStatement.setString(3, configuration.getLabel());
         preparedStatement.setString(4, configuration.getType());
         preparedStatement.setString(5, configuration.getValue());
         preparedStatement.setTimestamp(6, new Timestamp(configuration.getCreateDate().getTime()));
         preparedStatement.setTimestamp(7, new Timestamp(configuration.getUpdateDate().getTime()));
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public void deleteConfiguration(long id) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_PROPERTY where ID_=? and TYPE_!='system'");
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public Configuration getConfiguration(String key) {
      Connection connection = JdbcUtils.getConnection();

      Configuration configuration;
      try {
         String text = "select ID_, KEY_, VALUE_, LABEL_, TYPE_, CREATE_DATE_, UPDATE_DATE_ from URULE_PROPERTY WHERE KEY_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, key);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (!resultSet.next()) {
            JdbcUtils.closeStatement(preparedStatement);
            return null;
         }

         Configuration configuration2 = new Configuration();
         configuration2.setId(resultSet.getLong(1));
         configuration2.setKey(resultSet.getString(2));
         configuration2.setValue(resultSet.getString(3));
         configuration2.setLabel(resultSet.getString(4));
         configuration2.setType(resultSet.getString(5));
         configuration2.setCreateDate(resultSet.getTimestamp(6));
         configuration2.setUpdateDate(resultSet.getTimestamp(7));
         configuration = configuration2;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return configuration;
   }
}
