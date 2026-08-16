package com.bstek.urule.console.database.manager.packet.scenario;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.manager.packet.deploy.file.PacketDeployFileManager;
import com.bstek.urule.console.database.model.Scenario;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class ScenarioManagerImpl implements ScenarioManager {
   protected ScenarioManagerImpl() {
   }

   public ScenarioQuery newQuery() {
      return new ScenarioQueryImpl();
   }

   public Scenario load(long var1) {
      List var3 = this.newQuery().id(var1).list();
      return var3.size() > 0 ? (Scenario)var3.get(0) : null;
   }

   public byte[] loadExcelFile(long var1) {
      String var3 = "select EXCEL_FILE_ from URULE_PACKET_SCENARIO where ID_=?";
      Connection var4 = JdbcUtils.getConnection();

      byte[] var8;
      try {
         PreparedStatement var5 = var4.prepareStatement(var3);
         var5.setLong(1, var1);
         ResultSet var6 = var5.executeQuery();
         byte[] var7 = null;
         if (var6.next()) {
            var7 = var6.getBytes(1);
         }

         JdbcUtils.closeResultSet(var6);
         JdbcUtils.closeStatement(var5);
         var8 = var7;
      } catch (Exception var12) {
         throw new RuleException(var12);
      } finally {
         JdbcUtils.closeConnection(var4);
      }

      return var8;
   }

   public void uploadExcel(long var1, String var3, byte[] var4) {
      Connection var5 = JdbcUtils.getConnection();

      try {
         String var6 = "update URULE_PACKET_SCENARIO set EXCEL_FILE_=?,UPDATE_USER_=?,UPDATE_DATE_=? where ID_=?";
         PreparedStatement var7 = var5.prepareStatement(var6);
         var7.setBytes(1, var4);
         var7.setString(2, var3);
         var7.setTimestamp(3, new Timestamp((new Date()).getTime()));
         var7.setLong(4, var1);
         var7.executeUpdate();
         JdbcUtils.closeStatement(var7);
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var5);
      }

   }

   public Scenario add(Scenario var1) {
      Connection var2 = JdbcUtils.getConnection();

      Scenario var5;
      try {
         String var3 = "insert into URULE_PACKET_SCENARIO(ID_,PACKET_ID_,PROJECT_ID_,NAME_,DESC_,INPUT_DATA_,OUTPUT_DATA_,CREATE_USER_,UPDATE_USER_,CREATE_DATE_,UPDATE_DATE_) values(?,?,?,?,?,?,?,?,?,?,?)";
         PreparedStatement var4 = var2.prepareStatement(var3);
         var1.setId(IDGenerator.getInstance().nextId(IDType.PACKET_SCENARIO));
         var4.setLong(1, var1.getId());
         var4.setLong(2, var1.getPacketId());
         var4.setLong(3, var1.getProjectId());
         var4.setString(4, var1.getName());
         var4.setString(5, var1.getDesc());
         var4.setString(6, var1.getInputData());
         var4.setString(7, var1.getOutputData());
         var4.setString(8, var1.getCreateUser());
         var4.setString(9, var1.getCreateUser());
         var4.setTimestamp(10, new Timestamp((new Date()).getTime()));
         var4.setTimestamp(11, new Timestamp((new Date()).getTime()));
         var1.setCreateDate(new Date());
         var1.setUpdateDate(new Date());
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
         var5 = var1;
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

      return var5;
   }

   public void update(Scenario var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         String var3 = "update URULE_PACKET_SCENARIO set NAME_=?,DESC_=?,EXCEL_FILE_NAME_=?,INPUT_DATA_=?,OUTPUT_DATA_=?,UPDATE_USER_=?,UPDATE_DATE_=? where ID_=?";
         PreparedStatement var4 = var2.prepareStatement(var3);
         var4.setString(1, var1.getName());
         var4.setString(2, var1.getDesc());
         var4.setString(3, var1.getExcelFileName());
         var4.setString(4, var1.getInputData());
         var4.setString(5, var1.getOutputData());
         var4.setString(6, var1.getUpdateUser());
         var4.setTimestamp(7, new Timestamp(var1.getUpdateDate().getTime()));
         var4.setLong(8, var1.getId());
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void delete(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         String var4 = "delete from URULE_PACKET_SCENARIO where ID_=?";
         PreparedStatement var5 = var3.prepareStatement(var4);
         var5.setLong(1, var1);
         var5.executeUpdate();
         JdbcUtils.closeStatement(var5);
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public void deleteByProjectId(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         PacketDeployFileManager.ins.deleteByProjectId(var1);
         String var4 = "delete from URULE_PACKET_SCENARIO where PROJECT_ID_=?";
         PreparedStatement var5 = var3.prepareStatement(var4);
         var5.setLong(1, var1);
         var5.executeUpdate();
         JdbcUtils.closeStatement(var5);
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public void deleteByPacketId(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         String var4 = "delete from URULE_PACKET_SCENARIO where PACKET_ID_=?";
         PreparedStatement var5 = var3.prepareStatement(var4);
         var5.setLong(1, var1);
         var5.executeUpdate();
         JdbcUtils.closeStatement(var5);
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }
}
