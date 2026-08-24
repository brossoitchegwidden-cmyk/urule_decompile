package com.bstek.urule.console.database.manager.packet.packge;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.PacketPackage;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

public class PacketPackageManagerImpl implements PacketPackageManager {
   protected PacketPackageManagerImpl() {
   }

   public void add(PacketPackage pk) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "insert into URULE_PACKET_PACKAGE(ID_,PACKET_ID_,DESC_,CREATE_USER_,UPDATE_USER_,CREATE_DATE_,UPDATE_DATE_,PROJECT_ID_,CONTENT_) values(?,?,?,?,?,?,?,?,?)";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         pk.setId(IDGenerator.getInstance().nextId(IDType.PACKET_PACKAGE));
         preparedStatement.setLong(1, pk.getId());
         preparedStatement.setLong(2, pk.getPacketId());
         preparedStatement.setString(3, pk.getDesc());
         preparedStatement.setString(4, pk.getCreateUser());
         preparedStatement.setString(5, pk.getCreateUser());
         preparedStatement.setTimestamp(6, new Timestamp((new Date()).getTime()));
         preparedStatement.setTimestamp(7, new Timestamp((new Date()).getTime()));
         preparedStatement.setLong(8, pk.getProjectId());
         preparedStatement.setString(9, pk.getContent());
         pk.setCreateDate(new Date());
         pk.setUpdateDate(new Date());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void update(PacketPackage pk) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "update URULE_PACKET_PACKAGE set DESC_=?,UPDATE_USER_=?,UPDATE_DATE_=?,CONTENT_=?  where ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, pk.getDesc());
         preparedStatement.setString(2, pk.getUpdateUser());
         preparedStatement.setTimestamp(3, new Timestamp((new Date()).getTime()));
         preparedStatement.setString(4, pk.getContent());
         preparedStatement.setLong(5, pk.getId());
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
         String text = "delete from URULE_PACKET_PACKAGE where PACKET_ID_=?";
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

   public void deleteByProjectId(long projectId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         String text = "delete from URULE_PACKET_PACKAGE where PROJECT_ID_=?";
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

   public PacketPackage loadByPacketId(long packetId) {
      Connection connection = JdbcUtils.getConnection();

      PacketPackage packetPackage;
      try {
         String text = "select ID_,DESC_,CREATE_USER_,UPDATE_USER_,CREATE_DATE_,UPDATE_DATE_,PROJECT_ID_ from URULE_PACKET_PACKAGE where PACKET_ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, packetId);
         ResultSet resultSet = preparedStatement.executeQuery();
         PacketPackage packetPackage2 = null;

         while(resultSet.next()) {
            packetPackage2 = new PacketPackage();
            packetPackage2.setId(resultSet.getLong(1));
            packetPackage2.setPacketId(packetId);
            packetPackage2.setDesc(resultSet.getString(2));
            packetPackage2.setCreateUser(resultSet.getString(3));
            packetPackage2.setUpdateUser(resultSet.getString(4));
            packetPackage2.setCreateDate(new Date(resultSet.getTimestamp(5).getTime()));
            packetPackage2.setUpdateDate(new Date(resultSet.getTimestamp(6).getTime()));
            packetPackage2.setProjectId(resultSet.getLong(7));
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         packetPackage = packetPackage2;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return packetPackage;
   }

   public String loadContent(long id) {
      Connection connection = JdbcUtils.getConnection();

      String content;
      try {
         String text = "select CONTENT_ from URULE_PACKET_PACKAGE where ID_=?";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, id);
         ResultSet resultSet = preparedStatement.executeQuery();

         String string;
         for(string = null; resultSet.next(); string = resultSet.getString(1)) {
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         content = string;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return content;
   }
}
