package com.bstek.urule.console.database.manager.packet;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.manager.packet.apply.PacketApplyManager;
import com.bstek.urule.console.database.manager.packet.deploy.PacketDeployManager;
import com.bstek.urule.console.database.manager.packet.file.PacketFileManager;
import com.bstek.urule.console.database.manager.packet.packge.PacketPackageManager;
import com.bstek.urule.console.database.model.Packet;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PacketManagerImpl implements PacketManager {
   protected PacketManagerImpl() {
   }

   public Packet load(long id) {
      List items = this.newQuery().id(id).list();
      return items.size() > 0 ? (Packet)items.get(0) : null;
   }

   public Packet load(String code) {
      List items = this.newQuery().code(code).list();
      return items.size() > 0 ? (Packet)items.get(0) : null;
   }

   public void add(Packet packet) {
      Connection connection = JdbcUtils.getConnection();

      try {
         packet.setCreateDate(new Date());
         packet.setUpdateDate(new Date());
         String text = "insert into URULE_PACKET(ID_,PROJECT_ID_,NAME_,CODE_,TYPE_,DESC_,INPUT_DATA_,OUTPUT_DATA_,ENABLE_,CREATE_USER_,UPDATE_USER_,CREATE_DATE_,UPDATE_DATE_,REST_INPUT_,REST_OUTPUT_,AUDIT_ENABLE_,AUDIT_INPUT_,AUDIT_OUTPUT_,REST_ENABLE_,REST_SECURITY_ENABLE_,REST_SECURITY_USER_,REST_SECURITY_PASSWORD_) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         packet.setId(IDGenerator.getInstance().nextId(IDType.PACKET));
         preparedStatement.setLong(1, packet.getId());
         preparedStatement.setLong(2, packet.getProjectId());
         preparedStatement.setString(3, packet.getName());
         if (StringUtils.isBlank(packet.getCode())) {
            packet.setCode(UUID.randomUUID().toString().replaceAll("-", ""));
         }

         preparedStatement.setString(4, packet.getCode());
         preparedStatement.setString(5, packet.getType().name());
         preparedStatement.setString(6, packet.getDesc());
         preparedStatement.setString(7, packet.getInputData());
         preparedStatement.setString(8, packet.getOutputData());
         preparedStatement.setBoolean(9, packet.isEnable());
         preparedStatement.setString(10, packet.getCreateUser());
         preparedStatement.setString(11, packet.getCreateUser());
         preparedStatement.setTimestamp(12, new Timestamp(packet.getCreateDate().getTime()));
         preparedStatement.setTimestamp(13, new Timestamp(packet.getUpdateDate().getTime()));
         preparedStatement.setString(14, packet.getRestInput());
         preparedStatement.setString(15, packet.getRestOutput());
         preparedStatement.setBoolean(16, packet.isAuditEnable());
         preparedStatement.setString(17, packet.getAuditInput());
         preparedStatement.setString(18, packet.getAuditOutput());
         preparedStatement.setBoolean(19, packet.isRestEnable());
         preparedStatement.setBoolean(20, packet.isRestSecurityEnable());
         preparedStatement.setString(21, packet.getRestSecurityUser());
         preparedStatement.setString(22, packet.getRestSecurityPassword());
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
         String text = "delete from URULE_PACKET where ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
         PacketFileManager.ins.deleteByPacketId(id);
         PacketApplyManager.ins.deleteByPacketId(id);
         PacketDeployManager.ins.deleteByPacketId(id);
         PacketPackageManager.ins.deleteByPacketId(id);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void update(Packet packet) {
      Connection connection = JdbcUtils.getConnection();

      try {
         packet.setUpdateDate(new Date());
         String text = "update URULE_PACKET set NAME_=?,CODE_=?,DESC_=?,INPUT_DATA_=?,OUTPUT_DATA_=?,ENABLE_=?,UPDATE_DATE_=?,UPDATE_USER_=? where ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, packet.getName());
         if (StringUtils.isBlank(packet.getCode())) {
            packet.setCode(UUID.randomUUID().toString().replaceAll("-", ""));
         }

         preparedStatement.setString(2, packet.getCode());
         preparedStatement.setString(3, packet.getDesc());
         preparedStatement.setString(4, packet.getInputData());
         preparedStatement.setString(5, packet.getOutputData());
         preparedStatement.setBoolean(6, packet.isEnable());
         preparedStatement.setTimestamp(7, new Timestamp(packet.getUpdateDate().getTime()));
         preparedStatement.setString(8, packet.getUpdateUser());
         preparedStatement.setLong(9, packet.getId());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void update(long id, boolean enable) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "update URULE_PACKET set ENABLE_=?,UPDATE_DATE_=? where ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setBoolean(1, enable);
         preparedStatement.setTimestamp(2, new Timestamp((new Date()).getTime()));
         preparedStatement.setLong(3, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void updateRestConfig(Packet packet) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "update URULE_PACKET set REST_SECURITY_ENABLE_=?,REST_SECURITY_USER_=?,REST_SECURITY_PASSWORD_=?,REST_INPUT_=?,REST_OUTPUT_=?,UPDATE_DATE_=?,UPDATE_USER_=?,REST_ENABLE_=? where ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setBoolean(1, packet.isRestSecurityEnable());
         preparedStatement.setString(2, packet.getRestSecurityUser());
         preparedStatement.setString(3, packet.getRestSecurityPassword());
         preparedStatement.setString(4, packet.getRestInput());
         preparedStatement.setString(5, packet.getRestOutput());
         preparedStatement.setTimestamp(6, new Timestamp(packet.getUpdateDate().getTime()));
         preparedStatement.setString(7, packet.getUpdateUser());
         preparedStatement.setBoolean(8, packet.isRestEnable());
         preparedStatement.setLong(9, packet.getId());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void updateAuditConfig(Packet packet) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "update URULE_PACKET set AUDIT_ENABLE_=?,UPDATE_DATE_=?,UPDATE_USER_=?,AUDIT_INPUT_=?,AUDIT_OUTPUT_=? where ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setBoolean(1, packet.isAuditEnable());
         preparedStatement.setTimestamp(2, new Timestamp(packet.getUpdateDate().getTime()));
         preparedStatement.setString(3, packet.getUpdateUser());
         preparedStatement.setString(4, packet.getAuditInput());
         preparedStatement.setString(5, packet.getAuditOutput());
         preparedStatement.setLong(6, packet.getId());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public PacketQuery newQuery() {
      return new PacketQueryImpl();
   }

   public void deleteByProjectId(long projectId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "delete from URULE_PACKET where PROJECT_ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, projectId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
         PacketFileManager.ins.deleteByProjectId(projectId);
         PacketPackageManager.ins.deleteByProjectId(projectId);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public int getCount(long projectId) {
      int number = 0;
      Connection connection = JdbcUtils.getConnection();

      int count;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement("select COUNT(*) RULE_COUNT_ from URULE_PACKET where PROJECT_ID_=?");
         preparedStatement.setLong(1, projectId);
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            number = resultSet.getInt(1);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         count = number;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return count;
   }
}
