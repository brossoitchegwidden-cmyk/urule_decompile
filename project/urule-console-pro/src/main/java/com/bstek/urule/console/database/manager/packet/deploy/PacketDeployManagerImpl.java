package com.bstek.urule.console.database.manager.packet.deploy;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.manager.packet.deploy.file.PacketDeployFileManager;
import com.bstek.urule.console.database.model.ApplyStatus;
import com.bstek.urule.console.database.model.PacketDeploy;
import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.FileUtils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class PacketDeployManagerImpl implements PacketDeployManager {
   protected PacketDeployManagerImpl() {
   }

   public synchronized void add(PacketDeploy deploy) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "insert into URULE_DEPLOYED_PACKET(ID_,PACKET_ID_,DESC_,APPLY_ID_,PROJECT_ID_,CONTENT_,VERSION_,CREATE_USER_,CREATE_DATE_,ENABLE_,STATUS_,DIGEST_) values(?,?,?,?,?,?,?,?,?,?,?,?)";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         if (deploy.getId() == 0L) {
            deploy.setId(IDGenerator.getInstance().nextId(IDType.DEPLOYED_PACKET));
         }

         if (StringUtils.isBlank(deploy.getVersion())) {
            deploy.setVersion(this.calculateNextVersion(deploy.getPacketId()));
         }

         preparedStatement.setLong(1, deploy.getId());
         preparedStatement.setLong(2, deploy.getPacketId());
         preparedStatement.setString(3, deploy.getDesc());
         preparedStatement.setLong(4, deploy.getApplyId());
         preparedStatement.setLong(5, deploy.getProjectId());
         preparedStatement.setString(6, deploy.getContent());
         preparedStatement.setString(7, deploy.getVersion());
         preparedStatement.setString(8, deploy.getCreateUser());
         preparedStatement.setTimestamp(9, new Timestamp((new Date()).getTime()));
         preparedStatement.setBoolean(10, deploy.isEnable());
         preparedStatement.setString(11, deploy.getStatus().name());
         preparedStatement.setString(12, deploy.getDigest());
         deploy.setCreateDate(new Date());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   private String calculateNextVersion(long longValue) {
      Page page = this.newQuery().packetId(longValue).paging(1, 1);
      if (page.getData().size() > 0) {
         String version = ((PacketDeploy)page.getData().get(0)).getVersion();
         return FileUtils.getMaxVersion(version);
      } else {
         return "1.0.0";
      }
   }

   public void delete(long id) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "delete from URULE_DEPLOYED_PACKET where ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
         PacketDeployFileManager.ins.deleteByDeployId(id);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void deleteByApplyId(long applyId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         for(PacketDeploy packetDeploy : (Iterable<PacketDeploy>)(Iterable<?>)(this.newQuery().applyId(applyId).list())) {
            PacketDeployFileManager.ins.deleteByDeployId(packetDeploy.getId());
         }

         String text = "delete from URULE_DEPLOYED_PACKET where APPLY_ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, applyId);
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
         for(PacketDeploy packetDeploy : (Iterable<PacketDeploy>)(Iterable<?>)(this.newQuery().packetId(packetId).list())) {
            PacketDeployFileManager.ins.deleteByDeployId(packetDeploy.getId());
         }

         String text = "delete from URULE_DEPLOYED_PACKET where PACKET_ID_=?";
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

   public void updateEnable(long id, boolean enable) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "update URULE_DEPLOYED_PACKET set ENABLE_=? where ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setBoolean(1, enable);
         preparedStatement.setLong(2, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void updateStatus(long id, ApplyStatus status) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "update URULE_DEPLOYED_PACKET set STATUS_=? where ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, status.name());
         preparedStatement.setLong(2, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void disableAll(long packetId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "update URULE_DEPLOYED_PACKET set ENABLE_=? where PACKET_ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setBoolean(1, false);
         preparedStatement.setLong(2, packetId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public PacketDeploy load(long id) {
      List items = this.newQuery().id(id).listWithContent();
      return items.size() > 0 ? (PacketDeploy)items.get(0) : null;
   }

   public void deleteByProjectId(long projectId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PacketDeployFileManager.ins.deleteByProjectId(projectId);
         String text = "delete from URULE_DEPLOYED_PACKET where PROJECT_ID_=?";
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

   public PacketDeployQuery newQuery() {
      return new PacketDeployQueryImpl();
   }
}
