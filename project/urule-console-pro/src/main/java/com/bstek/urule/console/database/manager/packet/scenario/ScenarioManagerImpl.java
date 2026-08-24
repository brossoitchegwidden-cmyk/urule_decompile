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

   public Scenario load(long id) {
      List items = this.newQuery().id(id).list();
      return items.size() > 0 ? (Scenario)items.get(0) : null;
   }

   public byte[] loadExcelFile(long id) {
      String text = "select EXCEL_FILE_ from URULE_PACKET_SCENARIO where ID_=?";
      Connection connection = JdbcUtils.getConnection();

      byte[] excelFile;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, id);
         ResultSet resultSet = preparedStatement.executeQuery();
         byte[] bytes = null;
         if (resultSet.next()) {
            bytes = resultSet.getBytes(1);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         excelFile = bytes;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return excelFile;
   }

   public void uploadExcel(long id, String username, byte[] bytes) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "update URULE_PACKET_SCENARIO set EXCEL_FILE_=?,UPDATE_USER_=?,UPDATE_DATE_=? where ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setBytes(1, bytes);
         preparedStatement.setString(2, username);
         preparedStatement.setTimestamp(3, new Timestamp((new Date()).getTime()));
         preparedStatement.setLong(4, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public Scenario add(Scenario sc) {
      Connection connection = JdbcUtils.getConnection();

      Scenario scenario;
      try {
         String text = "insert into URULE_PACKET_SCENARIO(ID_,PACKET_ID_,PROJECT_ID_,NAME_,DESC_,INPUT_DATA_,OUTPUT_DATA_,CREATE_USER_,UPDATE_USER_,CREATE_DATE_,UPDATE_DATE_) values(?,?,?,?,?,?,?,?,?,?,?)";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         sc.setId(IDGenerator.getInstance().nextId(IDType.PACKET_SCENARIO));
         preparedStatement.setLong(1, sc.getId());
         preparedStatement.setLong(2, sc.getPacketId());
         preparedStatement.setLong(3, sc.getProjectId());
         preparedStatement.setString(4, sc.getName());
         preparedStatement.setString(5, sc.getDesc());
         preparedStatement.setString(6, sc.getInputData());
         preparedStatement.setString(7, sc.getOutputData());
         preparedStatement.setString(8, sc.getCreateUser());
         preparedStatement.setString(9, sc.getCreateUser());
         preparedStatement.setTimestamp(10, new Timestamp((new Date()).getTime()));
         preparedStatement.setTimestamp(11, new Timestamp((new Date()).getTime()));
         sc.setCreateDate(new Date());
         sc.setUpdateDate(new Date());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
         scenario = sc;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return scenario;
   }

   public void update(Scenario sc) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "update URULE_PACKET_SCENARIO set NAME_=?,DESC_=?,EXCEL_FILE_NAME_=?,INPUT_DATA_=?,OUTPUT_DATA_=?,UPDATE_USER_=?,UPDATE_DATE_=? where ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, sc.getName());
         preparedStatement.setString(2, sc.getDesc());
         preparedStatement.setString(3, sc.getExcelFileName());
         preparedStatement.setString(4, sc.getInputData());
         preparedStatement.setString(5, sc.getOutputData());
         preparedStatement.setString(6, sc.getUpdateUser());
         preparedStatement.setTimestamp(7, new Timestamp(sc.getUpdateDate().getTime()));
         preparedStatement.setLong(8, sc.getId());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void delete(long id) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "delete from URULE_PACKET_SCENARIO where ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void deleteByProjectId(long projectId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PacketDeployFileManager.ins.deleteByProjectId(projectId);
         String text = "delete from URULE_PACKET_SCENARIO where PROJECT_ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, projectId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void deleteByPacketId(long packetId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "delete from URULE_PACKET_SCENARIO where PACKET_ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, packetId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
}
