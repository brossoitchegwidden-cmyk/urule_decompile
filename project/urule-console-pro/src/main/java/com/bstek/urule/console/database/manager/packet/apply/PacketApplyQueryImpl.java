package com.bstek.urule.console.database.manager.packet.apply;

import com.bstek.urule.console.database.manager.packet.apply.detail.PacketApplyDetailManager;
import com.bstek.urule.console.database.model.ApplyStatus;
import com.bstek.urule.console.database.model.ApplyType;
import com.bstek.urule.console.database.model.PacketApply;
import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PacketApplyQueryImpl implements PacketApplyQuery {
   private Long id;
   private Long packetId;
   private Long projectId;
   private ApplyType type;
   private ApplyStatus[] includedStatuses;
   private ApplyStatus status;
   private ApplyStatus excludedStatus;
   private String title;
   private String desc;
   private String approver;
   private String exactCreateUser;
   private String createUser;
   private Date date;
   private Date endDate;
   private List queryParameters = new ArrayList();

   public Page paging(int pageIndex, int pageSize) {
      String pageSql = "select ID_,PACKET_ID_,DEPLOYED_PACKET_ID_,PROJECT_ID_,TYPE_,TITLE_,DESC_,APPROVER_,STATUS_,CREATE_USER_,CREATE_DATE_,UPDATE_DATE_ from URULE_PACKET_APPLY";
      StringBuilder stringBuilder = this.buildWhereClause();
      if (stringBuilder.length() > 0) {
         pageSql = pageSql + " where" + stringBuilder.toString();
      }

      pageSql = pageSql + " order by UPDATE_DATE_ desc";
      Connection connection = JdbcUtils.getConnection();

      Page page;
      try {
         Page page2 = new Page(pageIndex, pageSize);
         pageSql = JdbcUtils.getPageSql(pageSql, page2.getStartRow(), pageSize);
         PreparedStatement preparedStatement = connection.prepareStatement(pageSql);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ResultSet resultSet = preparedStatement.executeQuery();
         List items = this.readPacketApplications(resultSet);
         page2.setData(items);
         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         pageSql = "select count(*) from URULE_PACKET_APPLY";
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
      String text = "select ID_,PACKET_ID_,DEPLOYED_PACKET_ID_,PROJECT_ID_,TYPE_,TITLE_,DESC_,APPROVER_,STATUS_,CREATE_USER_,CREATE_DATE_,UPDATE_DATE_ from URULE_PACKET_APPLY";
      StringBuilder stringBuilder = this.buildWhereClause();
      if (stringBuilder.length() > 0) {
         text = text + " where" + stringBuilder.toString();
      }

      text = text + " order by UPDATE_DATE_ desc";
      Connection connection = JdbcUtils.getConnection();

      List listResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ResultSet resultSet = preparedStatement.executeQuery();
         List items = this.readPacketApplications(resultSet);
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

   private List readPacketApplications(ResultSet resultSet) throws Exception {
      ArrayList items = new ArrayList();

      while(resultSet.next()) {
         PacketApply packetApply = new PacketApply();
         packetApply.setId(resultSet.getLong(1));
         packetApply.setPacketId(resultSet.getLong(2));
         packetApply.setDeployedPacketId(resultSet.getLong(3));
         packetApply.setProjectId(resultSet.getLong(4));
         packetApply.setType(ApplyType.valueOf(resultSet.getString(5)));
         packetApply.setTitle(resultSet.getString(6));
         packetApply.setDesc(resultSet.getString(7));
         packetApply.setApprover(resultSet.getString(8));
         packetApply.setStatus(ApplyStatus.valueOf(resultSet.getString(9)));
         packetApply.setCreateUser(resultSet.getString(10));
         packetApply.setCreateDate(new Date(resultSet.getTimestamp(11).getTime()));
         packetApply.setUpdateDate(new Date(resultSet.getTimestamp(12).getTime()));
         packetApply.setDetails(PacketApplyDetailManager.ins.loadByApplyId(packetApply.getId()));
         items.add(packetApply);
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

      if (this.projectId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" PROJECT_ID_=?");
         this.queryParameters.add(this.projectId);
      }

      if (this.packetId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" PACKET_ID_=?");
         this.queryParameters.add(this.packetId);
      }

      if (this.type != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" TYPE_=?");
         this.queryParameters.add(this.type.name());
      }

      if (this.includedStatuses != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" STATUS_ in (");

         for(int index = 0; index < this.includedStatuses.length; ++index) {
            if (index > 0) {
               stringBuilder.append(",");
            }

            stringBuilder.append("?");
            this.queryParameters.add(this.includedStatuses[index].name());
         }

         stringBuilder.append(")");
      }

      if (this.status != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" STATUS_=?");
         this.queryParameters.add(this.status.name());
      }

      if (this.excludedStatus != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" STATUS_ <> ?");
         this.queryParameters.add(this.excludedStatus.name());
      }

      if (this.title != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" TITLE_ like ?");
         this.queryParameters.add("%" + this.title + "%");
      }

      if (this.desc != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" DESC_ like ?");
         this.queryParameters.add("%" + this.desc + "%");
      }

      if (this.approver != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" APPROVER_=?");
         this.queryParameters.add(this.approver);
      }

      if (this.exactCreateUser != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" CREATE_USER_=?");
         this.queryParameters.add(this.exactCreateUser);
      }

      if (this.createUser != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" CREATE_USER_ like ?");
         this.queryParameters.add("%" + this.createUser + "%");
      }

      if (this.date != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" CREATE_DATE_>=?");
         this.queryParameters.add(this.date);
      }

      if (this.endDate != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" CREATE_DATE_<=?");
         this.queryParameters.add(this.endDate);
      }

      return stringBuilder;
   }

   public PacketApplyQuery id(long id) {
      this.id = id;
      return this;
   }

   public PacketApplyQuery projectId(long projectId) {
      this.projectId = projectId;
      return this;
   }

   public PacketApplyQuery packetId(long packetId) {
      this.packetId = packetId;
      return this;
   }

   public PacketApplyQuery titleLike(String title) {
      this.title = title;
      return this;
   }

   public PacketApplyQuery descLike(String desc) {
      this.desc = desc;
      return this;
   }

   public PacketApplyQuery approver(String approver) {
      this.approver = approver;
      return this;
   }

   public PacketApplyQuery createUserLike(String createUser) {
      this.createUser = createUser;
      return this;
   }

   public PacketApplyQuery createUser(String createUser) {
      this.exactCreateUser = createUser;
      return this;
   }

   public PacketApplyQuery type(ApplyType type) {
      this.type = type;
      return this;
   }

   public PacketApplyQuery status(ApplyStatus status) {
      this.status = status;
      return this;
   }

   public PacketApplyQuery notStatus(ApplyStatus status) {
      this.excludedStatus = status;
      return this;
   }

   public PacketApplyQuery statusIn(ApplyStatus[] status) {
      this.includedStatuses = status;
      return this;
   }

   public PacketApplyQuery startDate(Date date) {
      this.date = date;
      return this;
   }

   public PacketApplyQuery endDate(Date date) {
      this.endDate = date;
      return this;
   }
}
