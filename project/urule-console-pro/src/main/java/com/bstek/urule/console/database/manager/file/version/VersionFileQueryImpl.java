package com.bstek.urule.console.database.manager.file.version;

import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.model.VersionFile;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VersionFileQueryImpl implements VersionFileQuery {
   private Long id;
   private Long fileId;
   private Long projectId;
   private String exactVersion;
   private String version;
   private String note;
   private List queryParameters = new ArrayList();

   protected VersionFileQueryImpl() {
   }

   public List list() {
      Connection connection = JdbcUtils.getConnection();

      List listResult;
      try {
         String text = "select ID_,FILE_ID_,PROJECT_ID_,NAME_, VERSION_, CREATE_DATE_,CREATE_USER_,NOTE_,DIGEST_ from URULE_VERSION_FILE";
         StringBuilder stringBuilder = this.buildWhereClause();
         if (stringBuilder.length() > 0) {
            text = text + " where" + stringBuilder.toString();
         }

         text = text + " order by CREATE_DATE_ desc";
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ResultSet resultSet = preparedStatement.executeQuery();
         List items = this.readVersionFiles(resultSet);
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
      Connection connection = JdbcUtils.getConnection();

      Page page;
      try {
         String pageSql = "select ID_,FILE_ID_,PROJECT_ID_,NAME_, VERSION_, CREATE_DATE_,CREATE_USER_,NOTE_,DIGEST_ from URULE_VERSION_FILE";
         StringBuilder stringBuilder = this.buildWhereClause();
         if (stringBuilder.length() > 0) {
            pageSql = pageSql + " where" + stringBuilder.toString();
         }

         pageSql = pageSql + " order by CREATE_DATE_ desc";
         Page page2 = new Page(pageIndex, pageSize);
         pageSql = JdbcUtils.getPageSql(pageSql, page2.getStartRow(), pageSize);
         PreparedStatement preparedStatement = connection.prepareStatement(pageSql);
         JdbcUtils.fillPreparedStatementParameters(this.queryParameters, preparedStatement);
         ResultSet resultSet = preparedStatement.executeQuery();
         List items = this.readVersionFiles(resultSet);
         page2.setData(items);
         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         pageSql = "select count(*) from URULE_VERSION_FILE";
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

   private List readVersionFiles(ResultSet resultSet) throws SQLException {
      ArrayList items = new ArrayList();

      while(resultSet.next()) {
         VersionFile versionFile = new VersionFile();
         versionFile.setId(resultSet.getLong(1));
         versionFile.setFileId(resultSet.getLong(2));
         versionFile.setProjectId(resultSet.getLong(3));
         versionFile.setName(resultSet.getString(4));
         versionFile.setVersion(resultSet.getString(5));
         versionFile.setCreateDate(resultSet.getTimestamp(6));
         versionFile.setCreateUser(resultSet.getString(7));
         versionFile.setNote(resultSet.getString(8));
         versionFile.setDigest(resultSet.getString(9));
         items.add(versionFile);
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

      if (this.fileId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" FILE_ID_=?");
         this.queryParameters.add(this.fileId);
      }

      if (this.projectId != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" PROJECT_ID_=?");
         this.queryParameters.add(this.projectId);
      }

      if (this.exactVersion != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" VERSION_=?");
         this.queryParameters.add(this.exactVersion);
      }

      if (this.version != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" VERSION_ like ?");
         this.queryParameters.add("%" + this.version + "%");
      }

      if (this.note != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and");
         }

         stringBuilder.append(" NOTE_ like ?");
         this.queryParameters.add("%" + this.note + "%");
      }

      return stringBuilder;
   }

   public VersionFileQuery id(long id) {
      this.id = id;
      return this;
   }

   public VersionFileQuery fileId(long fileId) {
      this.fileId = fileId;
      return this;
   }

   public VersionFileQuery versionLike(String version) {
      this.version = version;
      return this;
   }

   public VersionFileQuery version(String version) {
      this.exactVersion = version;
      return this;
   }

   public VersionFileQuery noteLike(String note) {
      this.note = note;
      return this;
   }

   public VersionFileQuery projectId(long projectId) {
      this.projectId = projectId;
      return this;
   }
}
