package com.bstek.urule.console.database.manager.packet.apply.detail;

import com.bstek.urule.console.database.model.PacketApplyDetail;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PacketApplyDetailQueryImpl implements PacketApplyDetailQuery {
   private Long id;
   private Long applyId;
   private Long projectId;
   private List queryParameters = new ArrayList();

   public List list() {
      String text = "select ID_,APPLY_ID_,PROJECT_ID_,DESC_,CREATE_USER_,CREATE_DATE_ from URULE_PACKET_APPLY_DETAIL";
      StringBuilder stringBuilder = this.buildWhereClause();
      if (stringBuilder.length() > 0) {
         text = text + " where" + stringBuilder.toString();
      }

      text = text + " order by CREATE_DATE_ desc";
      Connection connection = JdbcUtils.getConnection();

      List listResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ResultSet resultSet = preparedStatement.executeQuery();
         List items = this.readApplicationDetails(resultSet);
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

   private List readApplicationDetails(ResultSet resultSet) throws Exception {
      ArrayList items = new ArrayList();

      while(resultSet.next()) {
         PacketApplyDetail packetApplyDetail = new PacketApplyDetail();
         packetApplyDetail.setId(resultSet.getLong(1));
         packetApplyDetail.setApplyId(resultSet.getLong(2));
         packetApplyDetail.setProjectId(resultSet.getLong(3));
         packetApplyDetail.setDesc(resultSet.getString(4));
         packetApplyDetail.setCreateUser(resultSet.getString(5));
         packetApplyDetail.setCreateDate(new Date(resultSet.getTimestamp(6).getTime()));
         items.add(packetApplyDetail);
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

      if (this.applyId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" APPLY_ID_=?");
         this.queryParameters.add(this.applyId);
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

   public PacketApplyDetailQuery id(long id) {
      this.id = id;
      return this;
   }

   public PacketApplyDetailQuery applyId(long applyId) {
      this.applyId = applyId;
      return this;
   }

   public PacketApplyDetailQuery projectId(long projectId) {
      this.projectId = projectId;
      return this;
   }
}
