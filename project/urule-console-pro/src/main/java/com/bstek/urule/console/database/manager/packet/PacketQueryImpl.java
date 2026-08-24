package com.bstek.urule.console.database.manager.packet;

import com.bstek.urule.console.database.manager.packet.apply.PacketApplyManager;
import com.bstek.urule.console.database.manager.packet.deploy.PacketDeployManager;
import com.bstek.urule.console.database.manager.packet.file.PacketFileManager;
import com.bstek.urule.console.database.manager.packet.packge.PacketPackageManager;
import com.bstek.urule.console.database.model.Packet;
import com.bstek.urule.console.database.model.PacketPackage;
import com.bstek.urule.console.database.model.PacketType;
import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PacketQueryImpl implements PacketQuery {
   private Long id;
   private Long projectId;
   private String name;
   private String nameLike;
   private String code;
   private String codeLike;
   private String type;
   private String idLike;
   private String desc;
   private String createUser;
   private Boolean enable;
   private Boolean restEnable;
   private Boolean auditEnable;
   private List queryParameters = new ArrayList();

   protected PacketQueryImpl() {
   }

   public List list() {
      String text = "select ID_,PROJECT_ID_,NAME_,CODE_,TYPE_,DESC_,INPUT_DATA_,OUTPUT_DATA_,ENABLE_,AUDIT_ENABLE_,REST_SECURITY_ENABLE_,REST_SECURITY_USER_,REST_SECURITY_PASSWORD_,REST_INPUT_,REST_OUTPUT_,CREATE_USER_,UPDATE_USER_,CREATE_DATE_,UPDATE_DATE_,REST_ENABLE_,AUDIT_INPUT_,AUDIT_OUTPUT_ from URULE_PACKET";
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
         List items = this.readPackets(resultSet);
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

   public Page paging(int pageIndex, int pageSize) {
      String pageSql = "select ID_,PROJECT_ID_,NAME_,CODE_,TYPE_,DESC_,INPUT_DATA_,OUTPUT_DATA_,ENABLE_,AUDIT_ENABLE_,REST_SECURITY_ENABLE_,REST_SECURITY_USER_,REST_SECURITY_PASSWORD_,REST_INPUT_,REST_OUTPUT_,CREATE_USER_,UPDATE_USER_,CREATE_DATE_,UPDATE_DATE_,REST_ENABLE_,AUDIT_INPUT_,AUDIT_OUTPUT_ from URULE_PACKET";
      StringBuilder stringBuilder = this.buildWhereClause();
      if (stringBuilder.length() > 0) {
         pageSql = pageSql + " where" + stringBuilder.toString();
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
         List items = this.readPackets(resultSet);
         page2.setData(items);
         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         pageSql = "select count(*) from URULE_PACKET";
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

   private List readPackets(ResultSet resultSet) throws Exception {
      ArrayList items;
      Packet packet;
      for(items = new ArrayList(); resultSet.next(); items.add(packet)) {
         packet = new Packet();
         packet.setId(resultSet.getLong(1));
         packet.setProjectId(resultSet.getLong(2));
         packet.setName(resultSet.getString(3));
         packet.setCode(resultSet.getString(4));
         String string = resultSet.getString(5);
         if (string != null) {
            packet.setType(PacketType.valueOf(string));
         } else {
            packet.setType(PacketType.file);
         }

         packet.setDesc(resultSet.getString(6));
         packet.setInputData(resultSet.getString(7));
         packet.setOutputData(resultSet.getString(8));
         packet.setEnable(resultSet.getBoolean(9));
         packet.setAuditEnable(resultSet.getBoolean(10));
         packet.setRestSecurityEnable(resultSet.getBoolean(11));
         packet.setRestSecurityUser(resultSet.getString(12));
         packet.setRestSecurityPassword(resultSet.getString(13));
         packet.setRestInput(resultSet.getString(14));
         packet.setRestOutput(resultSet.getString(15));
         packet.setCreateUser(resultSet.getString(16));
         packet.setUpdateUser(resultSet.getString(17));
         packet.setCreateDate(new Date(resultSet.getTimestamp(18).getTime()));
         packet.setUpdateDate(new Date(resultSet.getTimestamp(19).getTime()));
         packet.setRestEnable(resultSet.getBoolean(20));
         packet.setAuditInput(resultSet.getString(21));
         packet.setAuditOutput(resultSet.getString(22));
         List items2 = PacketDeployManager.ins.newQuery().packetId(packet.getId()).list();
         if (items2.size() > 0) {
            packet.setDeleteEnable(false);
         } else {
            List items3 = PacketApplyManager.ins.newQuery().packetId(packet.getId()).list();
            if (items3.size() > 0) {
               packet.setDeleteEnable(false);
            }
         }

         PacketPackage byPacketId = PacketPackageManager.ins.loadByPacketId(packet.getId());
         if (byPacketId == null) {
            packet.setPacketPackage(new PacketPackage());
         } else {
            packet.setPacketPackage(byPacketId);
         }

         packet.setFiles(PacketFileManager.ins.newQuery().packetId(packet.getId()).list());
         packet.setDeployedCount(PacketDeployManager.ins.newQuery().packetId(packet.getId()).count());
         if (byPacketId != null && byPacketId.getPacketId() > 1L) {
            packet.setDeployedCount(1L);
         }
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

      if (this.idLike != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" ID_ like ?");
         this.queryParameters.add("%" + this.idLike + "%");
      }

      if (this.code != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" CODE_=?");
         this.queryParameters.add(this.code);
      }

      if (this.codeLike != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" CODE_ like ?");
         this.queryParameters.add("%" + this.codeLike + "%");
      }

      if (this.projectId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" PROJECT_ID_=?");
         this.queryParameters.add(this.projectId);
      }

      if (this.enable != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" ENABLE_=?");
         this.queryParameters.add(this.enable);
      }

      if (this.restEnable != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" REST_ENABLE_=?");
         this.queryParameters.add(this.restEnable);
      }

      if (this.auditEnable != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" AUDIT_ENABLE_=?");
         this.queryParameters.add(this.auditEnable);
      }

      if (this.name != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" NAME_ = ?");
         this.queryParameters.add(this.name);
      }

      if (this.nameLike != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" NAME_ like ?");
         this.queryParameters.add("%" + this.nameLike + "%");
      }

      if (this.type != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" TYPE_ like ?");
         this.queryParameters.add("%" + this.type + "%");
      }

      if (this.type != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" TYPE_ like ?");
         this.queryParameters.add("%" + this.type + "%");
      }

      if (this.desc != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" DESC_ like ?");
         this.queryParameters.add("%" + this.desc + "%");
      }

      if (this.createUser != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" CREATE_USER_ like ?");
         this.queryParameters.add("%" + this.createUser + "%");
      }

      return stringBuilder;
   }

   public PacketQuery id(long id) {
      this.id = id;
      return this;
   }

   public PacketQuery code(String code) {
      this.code = code;
      return this;
   }

   public PacketQuery idLike(String id) {
      this.idLike = id;
      return null;
   }

   public PacketQuery projectId(long projectId) {
      this.projectId = projectId;
      return this;
   }

   public PacketQuery nameLike(String nameLike) {
      this.nameLike = nameLike;
      return this;
   }

   public PacketQuery typeLike(String type) {
      this.type = type;
      return this;
   }

   public PacketQuery descLike(String desc) {
      this.desc = desc;
      return this;
   }

   public PacketQuery createUserLike(String createUser) {
      this.createUser = createUser;
      return this;
   }

   public PacketQuery enable(boolean enable) {
      this.enable = enable;
      return this;
   }

   public PacketQuery restEnable(boolean restEnable) {
      this.restEnable = restEnable;
      return this;
   }

   public PacketQuery auditEnable(boolean auditEnable) {
      this.auditEnable = auditEnable;
      return this;
   }

   public PacketQuery codeLike(String code) {
      this.codeLike = code;
      return this;
   }

   public PacketQuery name(String name) {
      this.name = name;
      return this;
   }
}
