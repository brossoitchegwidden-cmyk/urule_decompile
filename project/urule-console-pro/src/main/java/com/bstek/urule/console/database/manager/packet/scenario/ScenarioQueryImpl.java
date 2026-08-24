package com.bstek.urule.console.database.manager.packet.scenario;

import com.bstek.urule.console.database.model.Scenario;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScenarioQueryImpl implements ScenarioQuery {
   private Long id;
   private Long packetId;
   private Long projectId;
   private String name;
   private String desc;
   private List queryParameters = new ArrayList();

   protected ScenarioQueryImpl() {
   }

   public List list() {
      String text = "select ID_,PACKET_ID_,PROJECT_ID_,NAME_,DESC_,INPUT_DATA_,OUTPUT_DATA_,EXCEL_FILE_NAME_,CREATE_USER_,UPDATE_USER_,CREATE_DATE_,UPDATE_DATE_ from URULE_PACKET_SCENARIO";
      StringBuilder stringBuilder = this.buildWhereClause();
      if (stringBuilder.length() > 0) {
         text = text + " where" + stringBuilder.toString();
      }

      text = text + " order by CREATE_DATE_ desc";
      Connection connection = JdbcUtils.getConnection();

      ArrayList listResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ResultSet resultSet = preparedStatement.executeQuery();
         ArrayList items = new ArrayList();

         while(resultSet.next()) {
            Scenario scenario = new Scenario();
            scenario.setId(resultSet.getLong(1));
            scenario.setPacketId(resultSet.getLong(2));
            scenario.setProjectId(resultSet.getLong(3));
            scenario.setName(resultSet.getString(4));
            scenario.setDesc(resultSet.getString(5));
            scenario.setInputData(resultSet.getString(6));
            scenario.setOutputData(resultSet.getString(7));
            scenario.setExcelFileName(resultSet.getString(8));
            scenario.setCreateUser(resultSet.getString(9));
            scenario.setUpdateUser(resultSet.getString(10));
            scenario.setCreateDate(new Date(resultSet.getTimestamp(11).getTime()));
            scenario.setUpdateDate(new Date(resultSet.getTimestamp(12).getTime()));
            items.add(scenario);
         }

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

      if (this.name != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" NAME_ like ?");
         this.queryParameters.add("%" + this.name + "%");
      }

      if (this.desc != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" DESC_ like ?");
         this.queryParameters.add("%" + this.desc + "%");
      }

      return stringBuilder;
   }

   public ScenarioQuery id(long id) {
      this.id = id;
      return this;
   }

   public ScenarioQuery packetId(long packetId) {
      this.packetId = packetId;
      return this;
   }

   public ScenarioQuery nameLike(String name) {
      this.name = name;
      return this;
   }

   public ScenarioQuery descLike(String desc) {
      this.desc = desc;
      return this;
   }

   public ScenarioQuery projectId(long projectId) {
      this.projectId = projectId;
      return this;
   }
}
