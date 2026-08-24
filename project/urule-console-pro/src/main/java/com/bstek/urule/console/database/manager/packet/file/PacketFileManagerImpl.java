package com.bstek.urule.console.database.manager.packet.file;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.PacketFile;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class PacketFileManagerImpl implements PacketFileManager {
   protected PacketFileManagerImpl() {
   }

   public PacketFile load(long id) {
      List items = this.newQuery().id(id).list();
      return items.size() > 0 ? (PacketFile)items.get(0) : null;
   }

   public void add(PacketFile file) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "insert into URULE_PACKET_FILE(ID_,PACKET_ID_,FILE_ID_,PROJECT_ID_,PATH_,VERSION_,DESC_,CREATE_USER_,UPDATE_USER_,CREATE_DATE_,UPDATE_DATE_) values(?,?,?,?,?,?,?,?,?,?,?)";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         file.setId(IDGenerator.getInstance().nextId(IDType.PACKET_FILE));
         preparedStatement.setLong(1, file.getId());
         preparedStatement.setLong(2, file.getPacketId());
         preparedStatement.setLong(3, file.getFileId());
         preparedStatement.setLong(4, file.getProjectId());
         preparedStatement.setString(5, file.getPath());
         preparedStatement.setString(6, file.getVersion());
         preparedStatement.setString(7, file.getDesc());
         preparedStatement.setString(8, file.getCreateUser());
         preparedStatement.setString(9, file.getCreateUser());
         preparedStatement.setTimestamp(10, new Timestamp((new Date()).getTime()));
         preparedStatement.setTimestamp(11, new Timestamp((new Date()).getTime()));
         file.setCreateDate(new Date());
         file.setUpdateDate(new Date());
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
         String text = "delete from URULE_PACKET_FILE where ID_=?";
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

   public void deleteByPacketId(long packetId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "delete from URULE_PACKET_FILE where PACKET_ID_=?";
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

   public void update(PacketFile file) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "update URULE_PACKET_FILE set FILE_ID_=?,PATH_=?,VERSION_=?,DESC_=?,UPDATE_USER_=?,UPDATE_DATE_=? where ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, file.getFileId());
         preparedStatement.setString(2, file.getPath());
         preparedStatement.setString(3, file.getVersion());
         preparedStatement.setString(4, file.getDesc());
         preparedStatement.setString(5, file.getUpdateUser());
         preparedStatement.setTimestamp(6, new Timestamp((new Date()).getTime()));
         preparedStatement.setLong(7, file.getId());
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
         String text = "delete from URULE_PACKET_FILE where PROJECT_ID_=?";
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

   public PacketFileQuery newQuery() {
      return new PacketFileQueryImpl();
   }
}
