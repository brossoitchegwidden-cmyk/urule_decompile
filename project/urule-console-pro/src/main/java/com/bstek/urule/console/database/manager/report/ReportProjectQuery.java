package com.bstek.urule.console.database.manager.report;

import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class ReportProjectQuery {
   public static List listLastModifyFiles(Long projectId) {
      Connection connection = JdbcUtils.getConnection();
      String text = "select URULE_FILE.ID_, URULE_FILE.NAME_, URULE_FILE.TYPE_, URULE_FILE.UPDATE_USER_, URULE_FILE.UPDATE_DATE_, URULE_FILE.PROJECT_ID_ from URULE_FILE";
      text = text + " where URULE_FILE.PROJECT_ID_=? order by URULE_FILE.UPDATE_DATE_ DESC";

      ArrayList listLastModifyFilesResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, projectId);
         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            RuleFile ruleFile = new RuleFile();
            ruleFile.setId(resultSet.getLong(1));
            ruleFile.setName(resultSet.getString(2));
            ruleFile.setType(resultSet.getString(3));
            ruleFile.setUpdateUser(resultSet.getString(4));
            ruleFile.setCreateUser(resultSet.getString(4));
            ruleFile.setModifyDate(resultSet.getDate(5));
            ruleFile.setProjectId(resultSet.getLong(6));
            items.add(ruleFile);
         }

         JdbcUtils.closeStatement(preparedStatement);
         listLastModifyFilesResult = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return listLastModifyFilesResult;
   }

   public static List listPacketDeploys(Long projectId) {
      Connection connection = JdbcUtils.getConnection();
      String text = "SELECT URULE_DEPLOYED_PACKET.ID_, URULE_DEPLOYED_PACKET.PACKET_ID_, URULE_PACKET.NAME_, URULE_DEPLOYED_PACKET.DESC_, URULE_DEPLOYED_PACKET.CREATE_USER_, URULE_DEPLOYED_PACKET.CREATE_DATE_ from URULE_DEPLOYED_PACKET";
      text = text + " LEFT JOIN URULE_PACKET ON URULE_PACKET.ID_=URULE_DEPLOYED_PACKET.PACKET_ID_";
      text = text + " WHERE URULE_DEPLOYED_PACKET.PROJECT_ID_=? order by URULE_DEPLOYED_PACKET.CREATE_DATE_ DESC";

      ArrayList listPacketDeploysResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, projectId);
         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            PacketDeployVO packetDeployVO = new PacketDeployVO();
            packetDeployVO.setId(resultSet.getLong(1));
            packetDeployVO.setPacketId(resultSet.getLong(2));
            packetDeployVO.setPacketName(resultSet.getString(3));
            packetDeployVO.setDesc(resultSet.getString(4));
            packetDeployVO.setCreateUser(resultSet.getString(4));
            packetDeployVO.setCreateDate(resultSet.getTimestamp(5));
            items.add(packetDeployVO);
         }

         JdbcUtils.closeStatement(preparedStatement);
         listPacketDeploysResult = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return listPacketDeploysResult;
   }

   public static List countUserRuleFiles(Long projectId) {
      Connection connection = JdbcUtils.getConnection();
      String text = "select URULE_FILE.CREATE_USER_, COUNT(URULE_FILE.ID_) from URULE_FILE";
      text = text + " where URULE_FILE.PROJECT_ID_=? and URULE_FILE.DELETED_=?";
      text = text + " GROUP BY URULE_FILE.CREATE_USER_";

      ArrayList countUserRuleFilesResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, projectId);
         preparedStatement.setBoolean(2, false);
         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            LinkedHashMap valuesByKey = new LinkedHashMap();
            valuesByKey.put("createUser", resultSet.getString(1));
            valuesByKey.put("count", resultSet.getInt(2));
            items.add(valuesByKey);
         }

         JdbcUtils.closeStatement(preparedStatement);
         countUserRuleFilesResult = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return countUserRuleFilesResult;
   }

   public static int countFile(Long projectId) {
      int number = 0;
      Connection connection = JdbcUtils.getConnection();
      String text = "select count(URULE_FILE.ID_) from URULE_FILE";
      text = text + " where URULE_FILE.PROJECT_ID_=? and URULE_FILE.DELETED_=?";

      int countFileResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, projectId);
         preparedStatement.setBoolean(2, false);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            number = resultSet.getInt(1);
         }

         JdbcUtils.closeStatement(preparedStatement);
         countFileResult = number;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return countFileResult;
   }

   public static int countPacket(Long projectId) {
      int number = 0;
      Connection connection = JdbcUtils.getConnection();
      String text = "select count(URULE_PACKET.ID_) from URULE_PACKET";
      text = text + " where URULE_PACKET.PROJECT_ID_=?";

      int countPacketResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, projectId);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            number = resultSet.getInt(1);
         }

         JdbcUtils.closeStatement(preparedStatement);
         countPacketResult = number;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return countPacketResult;
   }

   public static int countBatch(Long projectId) {
      int number = 0;
      Connection connection = JdbcUtils.getConnection();
      String text = "select count(URULE_BATCH.ID_) from URULE_BATCH";
      text = text + " where URULE_BATCH.PROJECT_ID_=?";

      int countBatchResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, projectId);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            number = resultSet.getInt(1);
         }

         JdbcUtils.closeStatement(preparedStatement);
         countBatchResult = number;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return countBatchResult;
   }

   /**统计每天用户登录信息(时间范围为一个月内)*/
   public static List countUserLogin(Long projectId, Date startDate, Date endDate) {
      int number = calculateDaysBetween(startDate, endDate);
      if (number > 31) {
         throw new RuleException("无法统计超过一个月的数据");
      } else {
         ArrayList countUserLoginResult = new ArrayList();
         Date date = new Date(startDate.getTime());
         Connection connection = JdbcUtils.getConnection();
         String text = "select count(URULE_LOG_USERLOGIN.ID_) from URULE_LOG_USERLOGIN";
         text = text + " left join URULE_PROJECT_USER on URULE_PROJECT_USER.USER_ID_=URULE_LOG_USERLOGIN.USER_ID_";
         text = text + " where URULE_PROJECT_USER.PROJECT_ID_=? and URULE_LOG_USERLOGIN.CREATE_DATE_>? and URULE_LOG_USERLOGIN.CREATE_DATE_<?";

         try {
            PreparedStatement preparedStatement = connection.prepareStatement(text);

            for(int index = 0; index < number; ++index) {
               Calendar calendar = Calendar.getInstance();
               calendar.setTime(date);
               calendar.add(6, 1);
               Date time = calendar.getTime();
               int number2 = 0;
               preparedStatement.setLong(1, projectId);
               preparedStatement.setTimestamp(2, new Timestamp(date.getTime()));
               preparedStatement.setTimestamp(3, new Timestamp(time.getTime()));
               ResultSet resultSet = preparedStatement.executeQuery();
               if (resultSet.next()) {
                  number2 = resultSet.getInt(1);
               }

               LinkedHashMap valuesByKey = new LinkedHashMap();
               valuesByKey.put("count", number2);
               valuesByKey.put("loginDate", date);
               countUserLoginResult.add(valuesByKey);
               date = calendar.getTime();
            }

            JdbcUtils.closeStatement(preparedStatement);
         } catch (Exception exception) {
            throw new RuleException(exception);
         } finally {
            JdbcUtils.closeConnection(connection);
         }

         return countUserLoginResult;
      }
   }

   private static int calculateDaysBetween(Date dateValue, Date dateValue2) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(dateValue);
      Calendar calendar2 = Calendar.getInstance();
      calendar2.setTime(dateValue2);
      int number = calendar.get(6);
      int number2 = calendar2.get(6);
      int number3 = calendar.get(1);
      int number4 = calendar2.get(1);
      if (number3 == number4) {
         return number2 - number;
      } else {
         int number5 = 0;

         for(int index = number3; index < number4; ++index) {
            if ((index % 4 != 0 || index % 100 == 0) && index % 400 != 0) {
               number5 += 365;
            } else {
               number5 += 366;
            }
         }

         return number5 + (number2 - number);
      }
   }
}
