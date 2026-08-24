package com.bstek.urule.console.database.manager.packet.file;

import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.model.PacketFile;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PacketFileQueryImpl implements PacketFileQuery {
   private Long id;
   private Long packetId;
   private Long projectId;
   private List queryParameters = new ArrayList();

   protected PacketFileQueryImpl() {
   }

   public List list() {
      String text = "select ID_,PACKET_ID_,FILE_ID_,PROJECT_ID_,PATH_,VERSION_,DESC_,CREATE_USER_,UPDATE_USER_,CREATE_DATE_,UPDATE_DATE_ from URULE_PACKET_FILE";
      StringBuilder stringBuilder = this.buildWhereClause();
      if (stringBuilder.length() > 0) {
         text = text + " where" + stringBuilder.toString();
      }

      Connection connection = JdbcUtils.getConnection();

      List listResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ResultSet resultSet = preparedStatement.executeQuery();
         List items = this.readPacketFiles(resultSet);
         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         listResult = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return listResult;
   }

   private List readPacketFiles(ResultSet resultSet) throws Exception {
      ArrayList items = new ArrayList();

      while(resultSet.next()) {
         PacketFile packetFile = new PacketFile();
         packetFile.setId(resultSet.getLong(1));
         packetFile.setPacketId(resultSet.getLong(2));
         packetFile.setFileId(resultSet.getLong(3));
         packetFile.setProjectId(resultSet.getLong(4));

         try {
            RuleFile ruleFile = FileManager.ins.get(packetFile.getFileId());
            packetFile.setPath(ruleFile.getPath());
         } catch (RuleException ruleException) {
            packetFile.setPath("文件已删除");
         }

         packetFile.setVersion(resultSet.getString(6));
         packetFile.setDesc(resultSet.getString(7));
         packetFile.setCreateUser(resultSet.getString(8));
         packetFile.setUpdateUser(resultSet.getString(9));
         packetFile.setCreateDate(new Date(resultSet.getTimestamp(10).getTime()));
         packetFile.setUpdateDate(new Date(resultSet.getTimestamp(11).getTime()));
         items.add(packetFile);
      }

      return items;
   }

   private StringBuilder buildWhereClause() {
      this.queryParameters.clear();
      StringBuilder stringBuilder = new StringBuilder();
      if (this.id != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" ID_=?");
         this.queryParameters.add(this.id);
      }

      if (this.packetId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" PACKET_ID_=?");
         this.queryParameters.add(this.packetId);
      }

      if (this.projectId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" PROJECT_ID_=?");
         this.queryParameters.add(this.projectId);
      }

      return stringBuilder;
   }

   public PacketFileQuery id(long id) {
      this.id = id;
      return this;
   }

   public PacketFileQuery packetId(long packetId) {
      this.packetId = packetId;
      return this;
   }

   public PacketFileQuery projectId(long projectId) {
      this.projectId = projectId;
      return this;
   }
}
