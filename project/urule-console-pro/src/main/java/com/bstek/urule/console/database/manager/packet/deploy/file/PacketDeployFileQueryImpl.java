package com.bstek.urule.console.database.manager.packet.deploy.file;

import com.bstek.urule.console.database.model.PacketDeployFile;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.FileUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PacketDeployFileQueryImpl implements PacketDeployFileQuery {
   private Long id;
   private Long packetDeployId;
   private Long projectId;
   private List queryParameters = new ArrayList();

   protected PacketDeployFileQueryImpl() {
   }

   public List list() {
      String text = "select ID_,DEPLOYED_PACKET_ID_,FILE_ID_,PROJECT_ID_,PATH_,VERSION_,CREATE_USER_,CREATE_DATE_,DIGEST_ from URULE_DEPLOYED_PACKET_FILE";
      return this.queryDeploymentFiles(text, false);
   }

   public List listWithContent() {
      String text = "select ID_,DEPLOYED_PACKET_ID_,FILE_ID_,PROJECT_ID_,PATH_,VERSION_,CONTENT_,CREATE_USER_,CREATE_DATE_,DIGEST_ from URULE_DEPLOYED_PACKET_FILE";
      return this.queryDeploymentFiles(text, true);
   }

   private List queryDeploymentFiles(String text, boolean flag) {
      this.queryParameters.clear();
      StringBuilder stringBuilder = this.buildWhereClause();
      if (stringBuilder.length() > 0) {
         text = text + " where " + stringBuilder.toString();
      }

      text = text + " order by CREATE_DATE_ desc";
      Connection connection = JdbcUtils.getConnection();
      PreparedStatement preparedStatement = null;
      ResultSet resultSet = null;

      ArrayList items;
      try {
         preparedStatement = connection.prepareStatement(text);

         for(int index = 0; index < this.queryParameters.size(); ++index) {
            Object objectValue = this.queryParameters.get(index);
            if (objectValue instanceof Long) {
               Long objectValue2 = (Long)objectValue;
               preparedStatement.setLong(index + 1, objectValue2);
            } else if (objectValue instanceof String) {
               String objectValue3 = (String)objectValue;
               preparedStatement.setString(index + 1, objectValue3);
            } else {
               preparedStatement.setObject(index + 1, objectValue);
            }
         }

         resultSet = preparedStatement.executeQuery();

         ArrayList items2 = new ArrayList();
         while(resultSet.next()) {
            PacketDeployFile packetDeployFile;
            if (flag) {
               packetDeployFile = this.resolvePacketDeployFile(resultSet);
            } else {
               packetDeployFile = this.mapDeploymentFile(resultSet);
            }

            items2.add(packetDeployFile);
         }

         items = items2;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         JdbcUtils.closeConnection(connection);
      }

      return items;
   }

   private PacketDeployFile mapDeploymentFile(ResultSet resultSet) throws SQLException {
      PacketDeployFile packetDeployFile = new PacketDeployFile();
      packetDeployFile.setId(resultSet.getLong(1));
      packetDeployFile.setPacketDeployId(resultSet.getLong(2));
      packetDeployFile.setFileId(resultSet.getLong(3));
      packetDeployFile.setProjectId(resultSet.getLong(4));
      packetDeployFile.setPath(resultSet.getString(5));
      packetDeployFile.setVersion(resultSet.getString(6));
      packetDeployFile.setCreateUser(resultSet.getString(7));
      packetDeployFile.setCreateDate(new Date(resultSet.getTimestamp(8).getTime()));
      packetDeployFile.setDigest(resultSet.getString(9));
      return packetDeployFile;
   }

   private PacketDeployFile resolvePacketDeployFile(ResultSet resultSet) throws SQLException {
      PacketDeployFile packetDeployFile = new PacketDeployFile();
      packetDeployFile.setId(resultSet.getLong(1));
      packetDeployFile.setPacketDeployId(resultSet.getLong(2));
      packetDeployFile.setFileId(resultSet.getLong(3));
      packetDeployFile.setProjectId(resultSet.getLong(4));
      packetDeployFile.setPath(resultSet.getString(5));
      packetDeployFile.setVersion(resultSet.getString(6));
      packetDeployFile.setContent(FileUtils.formatXml(resultSet.getString(7)));
      packetDeployFile.setCreateUser(resultSet.getString(8));
      packetDeployFile.setCreateDate(new Date(resultSet.getTimestamp(9).getTime()));
      packetDeployFile.setDigest(resultSet.getString(10));
      return packetDeployFile;
   }

   private StringBuilder buildWhereClause() {
      StringBuilder stringBuilder = new StringBuilder();
      if (this.id != null) {
         if (stringBuilder.length() == 0) {
            stringBuilder.append(" ID_=?");
         } else {
            stringBuilder.append(" and ID_=?");
         }

         this.queryParameters.add(this.id);
      }

      if (this.packetDeployId != null) {
         if (stringBuilder.length() == 0) {
            stringBuilder.append(" DEPLOYED_PACKET_ID_=?");
         } else {
            stringBuilder.append(" and DEPLOYED_PACKET_ID_=?");
         }

         this.queryParameters.add(this.packetDeployId);
      }

      if (this.projectId != null) {
         if (stringBuilder.length() == 0) {
            stringBuilder.append(" PROJECT_ID_=?");
         } else {
            stringBuilder.append(" and PROJECT_ID_=?");
         }

         this.queryParameters.add(this.projectId);
      }

      return stringBuilder;
   }

   public PacketDeployFileQuery id(long id) {
      this.id = id;
      return this;
   }

   public PacketDeployFileQuery packetDeployId(long packetDeployId) {
      this.packetDeployId = packetDeployId;
      return this;
   }

   public PacketDeployFileQuery projectId(long projectId) {
      this.projectId = projectId;
      return this;
   }
}
