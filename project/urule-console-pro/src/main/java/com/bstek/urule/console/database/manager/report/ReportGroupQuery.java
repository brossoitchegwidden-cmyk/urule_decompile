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
   public static List countUserCreateProjects(String var0) {
      Connection var1 = JdbcUtils.getConnection();
      String var2 = "select CREATE_USER_, COUNT(ID_) from URULE_PROJECT WHERE GROUP_ID_=?";
      var2 = var2 + " GROUP BY CREATE_USER_";

      ArrayList var13;
      try {
         PreparedStatement var3 = var1.prepareStatement(var2);
         var3.setString(1, var0);
         ArrayList var4 = new ArrayList();
         ResultSet var5 = var3.executeQuery();

         while(var5.next()) {
            LinkedHashMap var6 = new LinkedHashMap();
            var6.put("createUser", var5.getString(1));
            var6.put("count", var5.getInt(2));
            var4.add(var6);
         }

         JdbcUtils.closeStatement(var3);
         var13 = var4;
      } catch (Exception var10) {
         throw new RuleException(var10);
      } finally {
         JdbcUtils.closeConnection(var1);
      }

      return var13;
   }

   public static List listPacketDeploys(String var0) {
      Connection var1 = JdbcUtils.getConnection();
      String var2 = "SELECT URULE_DEPLOYED_PACKET.ID_, URULE_DEPLOYED_PACKET.PACKET_ID_, URULE_PACKET.NAME_, URULE_DEPLOYED_PACKET.DESC_, URULE_DEPLOYED_PACKET.CREATE_USER_, URULE_DEPLOYED_PACKET.CREATE_DATE_, URULE_PROJECT.NAME_ from URULE_DEPLOYED_PACKET";
      var2 = var2 + " LEFT JOIN URULE_PACKET ON URULE_PACKET.ID_=URULE_DEPLOYED_PACKET.PACKET_ID_";
      var2 = var2 + " LEFT JOIN URULE_PROJECT ON URULE_PROJECT.ID_=URULE_DEPLOYED_PACKET.PROJECT_ID_";
      var2 = var2 + " WHERE URULE_PROJECT.GROUP_ID_=? order by URULE_DEPLOYED_PACKET.CREATE_DATE_ DESC";

      ArrayList var15;
      try {
         PreparedStatement var3 = var1.prepareStatement(var2);
         var3.setString(1, var0);
         ArrayList var4 = new ArrayList();
         ResultSet var5 = var3.executeQuery();

         while(var5.next()) {
            PacketDeployVO var6 = new PacketDeployVO();
            var6.setId(var5.getLong(1));
            var6.setPacketId(var5.getLong(2));
            var6.setPacketName(var5.getString(3));
            var6.setDesc(var5.getString(4));
            var6.setCreateUser(var5.getString(5));
            var6.setCreateDate(var5.getTimestamp(6));
            var6.setProjectName(var5.getString(7));
            var4.add(var6);
         }

         JdbcUtils.closeStatement(var3);
         var15 = var4;
      } catch (Exception var10) {
         throw new RuleException(var10);
      } finally {
         JdbcUtils.closeConnection(var1);
      }

      return var15;
   }

   public static List listLastModifyFilesByGroupId(String var0) {
      Connection var1 = JdbcUtils.getConnection();
      String var2 = "select URULE_FILE.ID_, URULE_FILE.NAME_, URULE_FILE.TYPE_, URULE_FILE.UPDATE_USER_, URULE_FILE.UPDATE_DATE_, URULE_FILE.PROJECT_ID_ from URULE_FILE";
      var2 = var2 + " left join URULE_PROJECT on URULE_PROJECT.ID_=URULE_FILE.PROJECT_ID_";
      var2 = var2 + " where URULE_PROJECT.GROUP_ID_=? order by URULE_FILE.UPDATE_DATE_ DESC";

      ArrayList var14;
      try {
         PreparedStatement var3 = var1.prepareStatement(var2);
         var3.setString(1, var0);
         ArrayList var4 = new ArrayList();
         ResultSet var5 = var3.executeQuery();

         while(var5.next()) {
            RuleFile var6 = new RuleFile();
            var6.setId(var5.getLong(1));
            var6.setName(var5.getString(2));
            var6.setType(var5.getString(3));
            var6.setUpdateUser(var5.getString(4));
            var6.setCreateUser(var5.getString(4));
            var6.setModifyDate(var5.getDate(5));
            var6.setProjectId(var5.getLong(6));
            var4.add(var6);
         }

         JdbcUtils.closeStatement(var3);
         var14 = var4;
      } catch (Exception var10) {
         throw new RuleException(var10);
      } finally {
         JdbcUtils.closeConnection(var1);
      }

      return var14;
   }

   public static List countProjectRuleFiles(String var0) {
      Connection var1 = JdbcUtils.getConnection();
      String var2 = "select SUMMARY_.ID_, URULE_PROJECT.NAME_, SUMMARY_.RECORD_COUNT_ from ( select URULE_PROJECT.ID_, COUNT(URULE_FILE.ID_) as RECORD_COUNT_ from URULE_FILE LEFT JOIN URULE_PROJECT ON URULE_FILE.PROJECT_ID_=URULE_PROJECT.ID_";
      var2 = var2 + " where URULE_PROJECT.GROUP_ID_=? and URULE_FILE.DELETED_=?";
      var2 = var2 + " GROUP BY URULE_PROJECT.ID_ ) SUMMARY_";
      var2 = var2 + " left join URULE_PROJECT ON URULE_PROJECT.ID_=SUMMARY_.ID_";

      ArrayList var15;
      try {
         PreparedStatement var3 = var1.prepareStatement(var2);
         var3.setString(1, var0);
         var3.setBoolean(2, false);
         ArrayList var4 = new ArrayList();
         ResultSet var5 = var3.executeQuery();

         while(var5.next()) {
            LinkedHashMap var6 = new LinkedHashMap();
            var6.put("projectId", var5.getLong(1));
            var6.put("projectName", var5.getString(2));
            var6.put("count", var5.getInt(3));
            var4.add(var6);
         }

         JdbcUtils.closeStatement(var3);
         var15 = var4;
      } catch (Exception var10) {
         throw new RuleException(var10);
      } finally {
         JdbcUtils.closeConnection(var1);
      }

      return var15;
   }

   public static int countFileByGroupId(String var0) {
      int var1 = 0;
      Connection var2 = JdbcUtils.getConnection();
      String var3 = "select count(URULE_FILE.ID_) from URULE_FILE";
      var3 = var3 + " left join URULE_PROJECT on URULE_PROJECT.ID_=URULE_FILE.PROJECT_ID_";
      var3 = var3 + " where URULE_PROJECT.GROUP_ID_=?";

      int var6;
      try {
         PreparedStatement var4 = var2.prepareStatement(var3);
         var4.setString(1, var0);
         ResultSet var5 = var4.executeQuery();
         if (var5.next()) {
            var1 = var5.getInt(1);
         }

         JdbcUtils.closeStatement(var4);
         var6 = var1;
      } catch (Exception var10) {
         throw new RuleException(var10);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

      return var6;
   }

   public static int countKnByGroupId(String var0) {
      int var1 = 0;
      Connection var2 = JdbcUtils.getConnection();
      String var3 = "select count(URULE_PACKET.ID_) from URULE_PACKET";
      var3 = var3 + " left join URULE_PROJECT on URULE_PROJECT.ID_=URULE_PACKET.PROJECT_ID_";
      var3 = var3 + " where URULE_PROJECT.GROUP_ID_=?";

      int var6;
      try {
         PreparedStatement var4 = var2.prepareStatement(var3);
         var4.setString(1, var0);
         ResultSet var5 = var4.executeQuery();
         if (var5.next()) {
            var1 = var5.getInt(1);
         }

         JdbcUtils.closeStatement(var4);
         var6 = var1;
      } catch (Exception var10) {
         throw new RuleException(var10);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

      return var6;
   }

   public static int countBatchByGroupId(String var0) {
      int var1 = 0;
      Connection var2 = JdbcUtils.getConnection();
      String var3 = "select count(URULE_BATCH.ID_) from URULE_BATCH";
      var3 = var3 + " left join URULE_PROJECT on URULE_PROJECT.ID_=URULE_BATCH.PROJECT_ID_";
      var3 = var3 + " where URULE_PROJECT.GROUP_ID_=?";

      int var6;
      try {
         PreparedStatement var4 = var2.prepareStatement(var3);
         var4.setString(1, var0);
         ResultSet var5 = var4.executeQuery();
         if (var5.next()) {
            var1 = var5.getInt(1);
         }

         JdbcUtils.closeStatement(var4);
         var6 = var1;
      } catch (Exception var10) {
         throw new RuleException(var10);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

      return var6;
   }

   public static List getUserLoginCountByDay(String var0, Date var1, Date var2) {
      int var3 = a(var1, var2);
      if (var3 > 31) {
         throw new RuleException("无法统计超过一个月的数据");
      } else {
         ArrayList var4 = new ArrayList();
         Date var5 = new Date(var1.getTime());
         Connection var6 = JdbcUtils.getConnection();
         String var7 = "select count(URULE_LOG_USERLOGIN.ID_) from URULE_LOG_USERLOGIN";
         var7 = var7 + " left join URULE_GROUP_USER on URULE_GROUP_USER.USER_ID_=URULE_LOG_USERLOGIN.USER_ID_";
         var7 = var7 + " where URULE_GROUP_USER.GROUP_ID_=? and URULE_LOG_USERLOGIN.CREATE_DATE_>? and URULE_LOG_USERLOGIN.CREATE_DATE_<?";

         try {
            PreparedStatement var8 = var6.prepareStatement(var7);

            for(int var9 = 0; var9 < var3; ++var9) {
               Calendar var10 = Calendar.getInstance();
               var10.setTime(var5);
               var10.add(6, 1);
               Date var11 = var10.getTime();
               int var12 = 0;
               var8.setString(1, var0);
               var8.setTimestamp(2, new Timestamp(var5.getTime()));
               var8.setTimestamp(3, new Timestamp(var11.getTime()));
               ResultSet var13 = var8.executeQuery();
               if (var13.next()) {
                  var12 = var13.getInt(1);
               }

               LinkedHashMap var14 = new LinkedHashMap();
               var14.put("count", var12);
               var14.put("loginDate", var5);
               var4.add(var14);
               var5 = var10.getTime();
            }

            JdbcUtils.closeStatement(var8);
         } catch (Exception var18) {
            throw new RuleException(var18);
         } finally {
            JdbcUtils.closeConnection(var6);
         }

         return var4;
      }
   }

   private static int a(Date var0, Date var1) {
      Calendar var2 = Calendar.getInstance();
      var2.setTime(var0);
      Calendar var3 = Calendar.getInstance();
      var3.setTime(var1);
      int var4 = var2.get(6);
      int var5 = var3.get(6);
      int var6 = var2.get(1);
      int var7 = var3.get(1);
      if (var6 == var7) {
         return var5 - var4;
      } else {
         int var8 = 0;

         for(int var9 = var6; var9 < var7; ++var9) {
            if ((var9 % 4 != 0 || var9 % 100 == 0) && var9 % 400 != 0) {
               var8 += 365;
            } else {
               var8 += 366;
            }
         }

         return var8 + (var5 - var4);
      }
   }
}
