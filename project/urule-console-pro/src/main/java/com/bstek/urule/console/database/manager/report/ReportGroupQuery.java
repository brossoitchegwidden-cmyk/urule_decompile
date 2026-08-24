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

public class ReportGroupQuery {
   public static List countUserCreateProjects(String groupId) {
      Connection connection = JdbcUtils.getConnection();
      String text = "select CREATE_USER_, COUNT(ID_) from URULE_PROJECT WHERE GROUP_ID_=?";
      text = text + " GROUP BY CREATE_USER_";

      ArrayList countUserCreateProjectsResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, groupId);
         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            LinkedHashMap valuesByKey = new LinkedHashMap();
            valuesByKey.put("createUser", resultSet.getString(1));
            valuesByKey.put("count", resultSet.getInt(2));
            items.add(valuesByKey);
         }

         JdbcUtils.closeStatement(preparedStatement);
         countUserCreateProjectsResult = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return countUserCreateProjectsResult;
   }

   public static List listPacketDeploys(String groupId) {
      Connection connection = JdbcUtils.getConnection();
      String text = "SELECT URULE_DEPLOYED_PACKET.ID_, URULE_DEPLOYED_PACKET.PACKET_ID_, URULE_PACKET.NAME_, URULE_DEPLOYED_PACKET.DESC_, URULE_DEPLOYED_PACKET.CREATE_USER_, URULE_DEPLOYED_PACKET.CREATE_DATE_, URULE_PROJECT.NAME_ from URULE_DEPLOYED_PACKET";
      text = text + " LEFT JOIN URULE_PACKET ON URULE_PACKET.ID_=URULE_DEPLOYED_PACKET.PACKET_ID_";
      text = text + " LEFT JOIN URULE_PROJECT ON URULE_PROJECT.ID_=URULE_DEPLOYED_PACKET.PROJECT_ID_";
      text = text + " WHERE URULE_PROJECT.GROUP_ID_=? order by URULE_DEPLOYED_PACKET.CREATE_DATE_ DESC";

      ArrayList listPacketDeploysResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, groupId);
         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            PacketDeployVO packetDeployVO = new PacketDeployVO();
            packetDeployVO.setId(resultSet.getLong(1));
            packetDeployVO.setPacketId(resultSet.getLong(2));
            packetDeployVO.setPacketName(resultSet.getString(3));
            packetDeployVO.setDesc(resultSet.getString(4));
            packetDeployVO.setCreateUser(resultSet.getString(5));
            packetDeployVO.setCreateDate(resultSet.getTimestamp(6));
            packetDeployVO.setProjectName(resultSet.getString(7));
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

   public static List listLastModifyFilesByGroupId(String groupId) {
      Connection connection = JdbcUtils.getConnection();
      String text = "select URULE_FILE.ID_, URULE_FILE.NAME_, URULE_FILE.TYPE_, URULE_FILE.UPDATE_USER_, URULE_FILE.UPDATE_DATE_, URULE_FILE.PROJECT_ID_ from URULE_FILE";
      text = text + " left join URULE_PROJECT on URULE_PROJECT.ID_=URULE_FILE.PROJECT_ID_";
      text = text + " where URULE_PROJECT.GROUP_ID_=? order by URULE_FILE.UPDATE_DATE_ DESC";

      ArrayList listLastModifyFilesByGroupIdResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, groupId);
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
         listLastModifyFilesByGroupIdResult = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return listLastModifyFilesByGroupIdResult;
   }

   public static List countProjectRuleFiles(String groupId) {
      Connection connection = JdbcUtils.getConnection();
      String text = "select SUMMARY_.ID_, URULE_PROJECT.NAME_, SUMMARY_.RECORD_COUNT_ from ( select URULE_PROJECT.ID_, COUNT(URULE_FILE.ID_) as RECORD_COUNT_ from URULE_FILE LEFT JOIN URULE_PROJECT ON URULE_FILE.PROJECT_ID_=URULE_PROJECT.ID_";
      text = text + " where URULE_PROJECT.GROUP_ID_=? and URULE_FILE.DELETED_=?";
      text = text + " GROUP BY URULE_PROJECT.ID_ ) SUMMARY_";
      text = text + " left join URULE_PROJECT ON URULE_PROJECT.ID_=SUMMARY_.ID_";

      ArrayList countProjectRuleFilesResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, groupId);
         preparedStatement.setBoolean(2, false);
         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            LinkedHashMap valuesByKey = new LinkedHashMap();
            valuesByKey.put("projectId", resultSet.getLong(1));
            valuesByKey.put("projectName", resultSet.getString(2));
            valuesByKey.put("count", resultSet.getInt(3));
            items.add(valuesByKey);
         }

         JdbcUtils.closeStatement(preparedStatement);
         countProjectRuleFilesResult = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return countProjectRuleFilesResult;
   }

   public static int countFileByGroupId(String groupId) {
      int number = 0;
      Connection connection = JdbcUtils.getConnection();
      String text = "select count(URULE_FILE.ID_) from URULE_FILE";
      text = text + " left join URULE_PROJECT on URULE_PROJECT.ID_=URULE_FILE.PROJECT_ID_";
      text = text + " where URULE_PROJECT.GROUP_ID_=?";

      int countFileByGroupIdResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, groupId);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            number = resultSet.getInt(1);
         }

         JdbcUtils.closeStatement(preparedStatement);
         countFileByGroupIdResult = number;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return countFileByGroupIdResult;
   }

   public static int countKnByGroupId(String groupId) {
      int number = 0;
      Connection connection = JdbcUtils.getConnection();
      String text = "select count(URULE_PACKET.ID_) from URULE_PACKET";
      text = text + " left join URULE_PROJECT on URULE_PROJECT.ID_=URULE_PACKET.PROJECT_ID_";
      text = text + " where URULE_PROJECT.GROUP_ID_=?";

      int countKnByGroupIdResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, groupId);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            number = resultSet.getInt(1);
         }

         JdbcUtils.closeStatement(preparedStatement);
         countKnByGroupIdResult = number;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return countKnByGroupIdResult;
   }

   public static int countBatchByGroupId(String groupId) {
      int number = 0;
      Connection connection = JdbcUtils.getConnection();
      String text = "select count(URULE_BATCH.ID_) from URULE_BATCH";
      text = text + " left join URULE_PROJECT on URULE_PROJECT.ID_=URULE_BATCH.PROJECT_ID_";
      text = text + " where URULE_PROJECT.GROUP_ID_=?";

      int countBatchByGroupIdResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, groupId);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            number = resultSet.getInt(1);
         }

         JdbcUtils.closeStatement(preparedStatement);
         countBatchByGroupIdResult = number;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return countBatchByGroupIdResult;
   }

   /**统计每天用户登录信息(时间范围为一个月内)*/
   public static List getUserLoginCountByDay(String groupId, Date startDate, Date endDate) {
      int number = calculateDaysBetween(startDate, endDate);
      if (number > 31) {
         throw new RuleException("无法统计超过一个月的数据");
      } else {
         ArrayList userLoginCountByDay = new ArrayList();
         Date date = new Date(startDate.getTime());
         Connection connection = JdbcUtils.getConnection();
         String text = "select count(URULE_LOG_USERLOGIN.ID_) from URULE_LOG_USERLOGIN";
         text = text + " left join URULE_GROUP_USER on URULE_GROUP_USER.USER_ID_=URULE_LOG_USERLOGIN.USER_ID_";
         text = text + " where URULE_GROUP_USER.GROUP_ID_=? and URULE_LOG_USERLOGIN.CREATE_DATE_>? and URULE_LOG_USERLOGIN.CREATE_DATE_<?";

         try {
            PreparedStatement preparedStatement = connection.prepareStatement(text);

            for(int index = 0; index < number; ++index) {
               Calendar calendar = Calendar.getInstance();
               calendar.setTime(date);
               calendar.add(6, 1);
               Date time = calendar.getTime();
               int number2 = 0;
               preparedStatement.setString(1, groupId);
               preparedStatement.setTimestamp(2, new Timestamp(date.getTime()));
               preparedStatement.setTimestamp(3, new Timestamp(time.getTime()));
               ResultSet resultSet = preparedStatement.executeQuery();
               if (resultSet.next()) {
                  number2 = resultSet.getInt(1);
               }

               LinkedHashMap valuesByKey = new LinkedHashMap();
               valuesByKey.put("count", number2);
               valuesByKey.put("loginDate", date);
               userLoginCountByDay.add(valuesByKey);
               date = calendar.getTime();
            }

            JdbcUtils.closeStatement(preparedStatement);
         } catch (Exception exception) {
            throw new RuleException(exception);
         } finally {
            JdbcUtils.closeConnection(connection);
         }

         return userLoginCountByDay;
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
