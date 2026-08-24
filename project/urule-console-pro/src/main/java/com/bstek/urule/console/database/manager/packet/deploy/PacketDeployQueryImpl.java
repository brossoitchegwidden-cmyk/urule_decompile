package com.bstek.urule.console.database.manager.packet.deploy;

import com.bstek.urule.console.database.manager.packet.deploy.file.PacketDeployFileManager;
import com.bstek.urule.console.database.model.ApplyStatus;
import com.bstek.urule.console.database.model.PacketDeploy;
import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PacketDeployQueryImpl implements PacketDeployQuery {
   private Long id;
   private Long packetId;
   private Long applyId;
   private Long projectId;
   private String version;
   private Boolean enable;
   private ApplyStatus status;
   private String versionLike;
   private String desc;
   private List queryParameters = new ArrayList();

   protected PacketDeployQueryImpl() {
   }

   public Page paging(int pageIndex, int pageSize) {
      String pageSql = "select ID_,PACKET_ID_,PROJECT_ID_,DESC_,VERSION_,CREATE_USER_,CREATE_DATE_,ENABLE_,STATUS_,DIGEST_ from URULE_DEPLOYED_PACKET";
      this.queryParameters.clear();
      StringBuilder stringBuilder = this.buildWhereClause();
      if (stringBuilder.length() > 0) {
         pageSql = pageSql + " where " + stringBuilder.toString();
      }

      pageSql = pageSql + " order by CREATE_DATE_ desc";
      Connection connection = JdbcUtils.getConnection();

      Page page;
      try {
         Page page2 = new Page(pageIndex, pageSize);
         pageSql = JdbcUtils.getPageSql(pageSql, page2.getStartRow(), pageSize);
         PreparedStatement preparedStatement = connection.prepareStatement(pageSql);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ResultSet resultSet = preparedStatement.executeQuery();
         List items = this.readDeployments(resultSet);
         page2.setData(items);
         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         pageSql = "select count(*) from URULE_DEPLOYED_PACKET";
         if (stringBuilder.length() > 0) {
            pageSql = pageSql + " where" + stringBuilder.toString();
         }

         preparedStatement = connection.prepareStatement(pageSql);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            page2.setTotalRows(resultSet.getLong(1));
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         page = page2;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return page;
   }

   public List list() {
      String text = "select ID_,PACKET_ID_,PROJECT_ID_,DESC_,VERSION_,CREATE_USER_,CREATE_DATE_,ENABLE_,STATUS_,DIGEST_ from URULE_DEPLOYED_PACKET";
      return this.queryDeployments(text, false);
   }

   public List listWithContent() {
      String text = "select ID_,PACKET_ID_,PROJECT_ID_,DESC_,CONTENT_,VERSION_,CREATE_USER_,CREATE_DATE_,ENABLE_,STATUS_,DIGEST_ from URULE_DEPLOYED_PACKET";
      return this.queryDeployments(text, true);
   }

   private List queryDeployments(String text, boolean flag) {
      this.queryParameters.clear();
      StringBuilder stringBuilder = this.buildWhereClause();
      if (stringBuilder.length() > 0) {
         text = text + " where " + stringBuilder.toString();
      }

      text = text + " order by CREATE_DATE_ desc";
      Connection connection = JdbcUtils.getConnection();
      PreparedStatement preparedStatement = null;
      ResultSet resultSet = null;

      List items;
      try {
         preparedStatement = connection.prepareStatement(text);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         resultSet = preparedStatement.executeQuery();
         if (!flag) {
            items = this.readDeployments(resultSet);
            return items;
         }

         items = this.readDeploymentsWithContent(resultSet);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         JdbcUtils.closeConnection(connection);
      }

      return items;
   }

   public long count() {
      String text = "select count(*) from URULE_DEPLOYED_PACKET";
      this.queryParameters.clear();
      StringBuilder stringBuilder = this.buildWhereClause();
      if (stringBuilder.length() > 0) {
         text = text + " where " + stringBuilder.toString();
      }

      Connection connection = JdbcUtils.getConnection();
      PreparedStatement preparedStatement = null;
      ResultSet resultSet = null;

      long countResult;
      try {
         preparedStatement = connection.prepareStatement(text);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         resultSet = preparedStatement.executeQuery();
         long longValue = 0L;
         if (resultSet.next()) {
            longValue = resultSet.getLong(1);
         }

         countResult = longValue;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         JdbcUtils.closeConnection(connection);
      }

      return countResult;
   }

   private List readDeployments(ResultSet resultSet) throws SQLException {
      ArrayList items = new ArrayList();

      while(resultSet.next()) {
         PacketDeploy packetDeploy = new PacketDeploy();
         packetDeploy.setId(resultSet.getLong(1));
         packetDeploy.setPacketId(resultSet.getLong(2));
         packetDeploy.setProjectId(resultSet.getLong(3));
         packetDeploy.setDesc(resultSet.getString(4));
         packetDeploy.setVersion(resultSet.getString(5));
         packetDeploy.setCreateUser(resultSet.getString(6));
         packetDeploy.setCreateDate(new Date(resultSet.getTimestamp(7).getTime()));
         packetDeploy.setEnable(resultSet.getBoolean(8));
         packetDeploy.setStatus(ApplyStatus.valueOf(resultSet.getString(9)));
         packetDeploy.setDigest(resultSet.getString(10));
         packetDeploy.setFiles(PacketDeployFileManager.ins.loadFiles(packetDeploy.getId()));
         items.add(packetDeploy);
      }

      return items;
   }

   private List readDeploymentsWithContent(ResultSet resultSet) throws SQLException {
      ArrayList items = new ArrayList();

      while(resultSet.next()) {
         PacketDeploy packetDeploy = new PacketDeploy();
         packetDeploy.setId(resultSet.getLong(1));
         packetDeploy.setPacketId(resultSet.getLong(2));
         packetDeploy.setProjectId(resultSet.getLong(3));
         packetDeploy.setDesc(resultSet.getString(4));
         packetDeploy.setContent(resultSet.getString(5));
         packetDeploy.setVersion(resultSet.getString(6));
         packetDeploy.setCreateUser(resultSet.getString(7));
         packetDeploy.setCreateDate(new Date(resultSet.getTimestamp(8).getTime()));
         packetDeploy.setEnable(resultSet.getBoolean(9));
         packetDeploy.setStatus(ApplyStatus.valueOf(resultSet.getString(10)));
         packetDeploy.setDigest(resultSet.getString(11));
         packetDeploy.setFiles(PacketDeployFileManager.ins.loadFilesWithContent(packetDeploy.getId()));
         items.add(packetDeploy);
      }

      return items;
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

      if (this.packetId != null) {
         if (stringBuilder.length() == 0) {
            stringBuilder.append(" PACKET_ID_=?");
         } else {
            stringBuilder.append(" and PACKET_ID_=?");
         }

         this.queryParameters.add(this.packetId);
      }

      if (this.applyId != null) {
         if (stringBuilder.length() == 0) {
            stringBuilder.append(" APPLY_ID_=?");
         } else {
            stringBuilder.append(" and APPLY_ID_=?");
         }

         this.queryParameters.add(this.applyId);
      }

      if (this.projectId != null) {
         if (stringBuilder.length() == 0) {
            stringBuilder.append(" PROJECT_ID_=?");
         } else {
            stringBuilder.append(" and PROJECT_ID_=?");
         }

         this.queryParameters.add(this.projectId);
      }

      if (this.status != null) {
         if (stringBuilder.length() == 0) {
            stringBuilder.append(" STATUS_=?");
         } else {
            stringBuilder.append(" and STATUS_=?");
         }

         this.queryParameters.add(this.status.name());
      }

      if (this.version != null) {
         if (stringBuilder.length() == 0) {
            stringBuilder.append(" VERSION_=?");
         } else {
            stringBuilder.append(" and VERSION_=?");
         }

         this.queryParameters.add(this.version);
      }

      if (this.versionLike != null) {
         if (stringBuilder.length() == 0) {
            stringBuilder.append(" VERSION_ like ?");
         } else {
            stringBuilder.append(" and VERSION_ like ?");
         }

         this.queryParameters.add("%" + this.versionLike + "%");
      }

      if (this.desc != null) {
         if (stringBuilder.length() == 0) {
            stringBuilder.append(" DESC_ like ?");
         } else {
            stringBuilder.append(" and DESC_ like ?");
         }

         this.queryParameters.add("%" + this.desc + "%");
      }

      if (this.enable != null) {
         if (stringBuilder.length() == 0) {
            stringBuilder.append(" ENABLE_=?");
         } else {
            stringBuilder.append(" and ENABLE_=?");
         }

         this.queryParameters.add(this.enable);
      }

      return stringBuilder;
   }

   public PacketDeployQuery id(long id) {
      this.id = id;
      return this;
   }

   public PacketDeployQuery packetId(long packetId) {
      this.packetId = packetId;
      return this;
   }

   public PacketDeployQuery applyId(long applyId) {
      this.applyId = applyId;
      return this;
   }

   public PacketDeployQuery version(String version) {
      this.version = version;
      return this;
   }

   public PacketDeployQuery enable(boolean enable) {
      this.enable = enable;
      return this;
   }

   public PacketDeployQuery versionLike(String version) {
      this.versionLike = version;
      return this;
   }

   public PacketDeployQuery descLike(String desc) {
      this.desc = desc;
      return this;
   }

   public PacketDeployQuery status(ApplyStatus status) {
      this.status = status;
      return this;
   }

   public PacketDeployQuery projectId(long projectId) {
      this.projectId = projectId;
      return this;
   }
}
