package com.bstek.urule.console.database.manager.packet.apply;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.manager.packet.apply.detail.PacketApplyDetailManager;
import com.bstek.urule.console.database.model.ApplyStatus;
import com.bstek.urule.console.database.model.PacketApply;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class PacketApplyManagerImpl implements PacketApplyManager {
   protected PacketApplyManagerImpl() {
   }

   public void add(PacketApply apply) {
      Connection connection = JdbcUtils.getConnection();

      try {
         apply.setCreateDate(new Date());
         apply.setUpdateDate(new Date());
         String text = "insert into URULE_PACKET_APPLY(ID_,PACKET_ID_,DEPLOYED_PACKET_ID_,PROJECT_ID_,TYPE_,TITLE_,DESC_,APPROVER_,STATUS_,CREATE_USER_,CREATE_DATE_,UPDATE_DATE_) values(?,?,?,?,?,?,?,?,?,?,?,?)";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         apply.setId(IDGenerator.getInstance().nextId(IDType.PACKET_APPLY));
         preparedStatement.setLong(1, apply.getId());
         preparedStatement.setLong(2, apply.getPacketId());
         preparedStatement.setLong(3, apply.getDeployedPacketId());
         preparedStatement.setLong(4, apply.getProjectId());
         preparedStatement.setString(5, apply.getType().name());
         preparedStatement.setString(6, apply.getTitle());
         preparedStatement.setString(7, apply.getDesc());
         preparedStatement.setString(8, apply.getApprover());
         preparedStatement.setString(9, apply.getStatus().name());
         preparedStatement.setString(10, apply.getCreateUser());
         preparedStatement.setTimestamp(11, new Timestamp(apply.getCreateDate().getTime()));
         preparedStatement.setTimestamp(12, new Timestamp(apply.getUpdateDate().getTime()));
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
         String text = "delete from URULE_PACKET_APPLY where ID_=?";
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

   public void deleteByPacketId(long id) {
      Connection connection = JdbcUtils.getConnection();

      try {
         for(PacketApply packetApply : (Iterable<PacketApply>)(Iterable<?>)(this.newQuery().packetId(id).list())) {
            PacketApplyDetailManager.ins.deleteByApplyId(packetApply.getId());
         }

         String text = "delete from URULE_PACKET_APPLY where PACKET_ID_=?";
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

   public PacketApply load(long id) {
      List items = this.newQuery().id(id).list();
      return items.size() > 0 ? (PacketApply)items.get(0) : null;
   }

   public void update(long id, ApplyStatus status) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "update URULE_PACKET_APPLY set STATUS_=?,UPDATE_DATE_=? where ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, status.name());
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

   public void updateDeployedPacketId(long id, long deployedPacketId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "update URULE_PACKET_APPLY set DEPLOYED_PACKET_ID_=?,UPDATE_DATE_=? where ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, deployedPacketId);
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

   public PacketApplyQuery newQuery() {
      return new PacketApplyQueryImpl();
   }

   public void deleteByProjectId(long projectId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PacketApplyDetailManager.ins.deleteByProjectId(projectId);
         String text = "delete from URULE_PACKET_APPLY where PROJECT_ID_=?";
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
}
