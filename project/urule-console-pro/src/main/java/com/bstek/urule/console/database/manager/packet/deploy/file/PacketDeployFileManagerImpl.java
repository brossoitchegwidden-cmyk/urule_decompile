package com.bstek.urule.console.database.manager.packet.deploy.file;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.PacketDeployFile;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class PacketDeployFileManagerImpl implements PacketDeployFileManager {
   protected PacketDeployFileManagerImpl() {
   }

   public PacketDeployFile load(long id) {
      List items = this.newQuery().id(id).listWithContent();
      return items.size() > 0 ? (PacketDeployFile)items.get(0) : null;
   }

   public List loadFiles(long packetDeployId) {
      return this.newQuery().packetDeployId(packetDeployId).list();
   }

   public List loadFilesWithContent(long packetDeployId) {
      return this.newQuery().packetDeployId(packetDeployId).listWithContent();
   }

   public void deleteByDeployId(long deployId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "delete from URULE_DEPLOYED_PACKET_FILE where DEPLOYED_PACKET_ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, deployId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void add(PacketDeployFile packet) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "insert into URULE_DEPLOYED_PACKET_FILE(ID_,DEPLOYED_PACKET_ID_,FILE_ID_,PROJECT_ID_,PATH_,VERSION_,CONTENT_,CREATE_USER_,CREATE_DATE_,DIGEST_) values(?,?,?,?,?,?,?,?,?,?)";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         packet.setId(IDGenerator.getInstance().nextId(IDType.DEPLOYED_PACKET_FILE));
         preparedStatement.setLong(1, packet.getId());
         preparedStatement.setLong(2, packet.getPacketDeployId());
         preparedStatement.setLong(3, packet.getFileId());
         preparedStatement.setLong(4, packet.getProjectId());
         preparedStatement.setString(5, packet.getPath());
         preparedStatement.setString(6, packet.getVersion());
         preparedStatement.setString(7, packet.getContent());
         preparedStatement.setString(8, packet.getCreateUser());
         preparedStatement.setTimestamp(9, new Timestamp((new Date()).getTime()));
         preparedStatement.setString(10, packet.getDigest());
         packet.setCreateDate(new Date());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public PacketDeployFileQuery newQuery() {
      return new PacketDeployFileQueryImpl();
   }

   public void deleteByProjectId(long projectId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "delete from URULE_DEPLOYED_PACKET_FILE where PROJECT_ID_=?";
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
