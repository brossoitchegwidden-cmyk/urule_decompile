package com.bstek.urule.console.database.manager.file;

import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.database.manager.project.ProjectManager;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Builds parameterized file and directory queries for the repository tree. */
public class FileQueryImpl implements FileQuery {
   private Long id;
   private List ids;
   private String name;
   private String namePattern;
   private String type;
   private String[] types;
   private String lockedUser;
   private String updateUser;
   private Boolean deleted;
   private boolean removeEmpty;
   private boolean containCommonProject;
   private List<String> ascendingProperties = new ArrayList<>();
   private List<String> descendingProperties = new ArrayList<>();

   protected FileQueryImpl() {
   }

   public List tree(Long projectId) {
      Connection connection = JdbcUtils.getConnection();

      ArrayList treeResult;
      try {
         ArrayList items = new ArrayList();
         Project project = ProjectManager.ins.get(projectId);
         items.add(this.buildProjectRoot(connection, project));
         if (this.containCommonProject && projectId != null) {
            String groupId = project.getGroupId();

            for(Project project2 : (Iterable<Project>)(Iterable<?>)(ProjectManager.ins.newQuery().groupId(groupId).type("common").list())) {
               if (!project2.getId().equals(projectId)) {
                  RuleFile ruleFile = this.buildProjectRoot(connection, project2);
                  items.add(ruleFile);
               }
            }
         }

         treeResult = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return treeResult;
   }

   private RuleFile buildProjectRoot(Connection connection, Project project) {
      try {
         RuleFile ruleFile = new RuleFile();
         ruleFile.setName(project.getName());
         ruleFile.setType(project.getType());
         ruleFile.setProjectId(project.getId());
         ruleFile.setParentId(-1L);
         ruleFile.setDirectory(true);
         ruleFile.setPath("/");
         ruleFile.setId(0L);
         ArrayList items = new ArrayList();
         ruleFile.setChildren(items);
         items.addAll(this.queryFiles(ruleFile, connection));
         items.addAll(this.queryDirectories(ruleFile, connection));
         if (this.removeEmpty) {
            this.removeEmptyDirectories(items);
         }

         return ruleFile;
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   private void removeEmptyDirectories(List<RuleFile> files) {
      if (files != null) {
         List<RuleFile> snapshot = new ArrayList<>();
         snapshot.addAll(files);

         for(RuleFile ruleFile : snapshot) {
            if (ruleFile.isDirectory()) {
               if (this.isEmptyDirectory(ruleFile)) {
                  files.remove(ruleFile);
               }

               this.removeEmptyDirectories(ruleFile.getChildren());
            }
         }

      }
   }

   private boolean isEmptyDirectory(RuleFile ruleFile) {
      List children = ruleFile.getChildren();
      if (children != null && children.size() != 0) {
         for(RuleFile ruleFile2 : (Iterable<RuleFile>)(Iterable<?>)(children)) {
            if (!ruleFile2.isDirectory()) {
               return false;
            }

            if (!this.isEmptyDirectory(ruleFile2)) {
               return false;
            }
         }

         return true;
      } else {
         return true;
      }
   }

   public List list(Long projectId) {
      Connection connection = JdbcUtils.getConnection();
      RuleFile ruleFile = new RuleFile();
      Project project = null;
      ruleFile.setPath("/");
      if (projectId != null) {
         ruleFile.setProjectId(projectId);
         project = ProjectManager.ins.get(projectId);
      }

      ruleFile.setId(-1L);
      List listResult = this.queryFiles(ruleFile, connection);

      for(RuleFile ruleFile2 : (Iterable<RuleFile>)(Iterable<?>)(listResult)) {
         PathInfo pathInfo = new PathInfo();
         pathInfo.setPath("");
         this.buildPath(connection, ruleFile2.getParentId(), pathInfo);
         ruleFile2.setFileSet(pathInfo.isFileSet());
         String path = pathInfo.getPath();
         if (project == null) {
            Project project2 = ProjectManager.ins.get(ruleFile2.getProjectId());
            path = project2.getName() + "/" + path;
         } else {
            path = project.getName() + "/" + path;
         }

         ruleFile2.setPath(ruleFile2.getType() + ":" + path + ruleFile2.getName());
      }

      JdbcUtils.closeConnection(connection);
      return listResult;
   }

   private void buildPath(Connection connection, long parentId, PathInfo pathInfo) {
      String text = "select ID_, NAME_, TYPE_, PARENT_ID_ from URULE_PACKAGE where ID_=?";
      PreparedStatement preparedStatement = null;
      ResultSet resultSet = null;

      try {
         preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, parentId);
         resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            long longValue = resultSet.getLong(1);
            String string = resultSet.getString(2);
            String string2 = resultSet.getString(3);
            long longValue2 = resultSet.getLong(4);
            if (longValue == longValue2) {
               throw new RuleException("[" + parentId + "] buildPath error!");
            }

            String path = pathInfo.getPath();
            path = string + "/" + path;
            pathInfo.setPath(path);
            if (ResourceType.General.name().equals(string2)) {
               pathInfo.setFileSet(true);
            }

            this.buildPath(connection, longValue2, pathInfo);
         }
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
      }

   }

   private List<RuleFile> queryDirectories(RuleFile ruleFile, Connection connection) {
      try {
         ArrayList items = new ArrayList();
         String text = "select ID_, NAME_, TYPE_,PARENT_ID_,PROJECT_ID_,CREATE_DATE_,UPDATE_USER_,UPDATE_DATE_ from URULE_PACKAGE";
         StringBuilder stringBuilder = new StringBuilder();
         if (ruleFile.getId() > -1L) {
            stringBuilder.append("  where PARENT_ID_=?");
            items.add(ruleFile.getId());
         }

         if (ruleFile.getProjectId() > 0L) {
            if (stringBuilder.length() > 0) {
               stringBuilder.append("  and PROJECT_ID_=?");
            } else {
               stringBuilder.append("  where PROJECT_ID_=?");
            }

            items.add(ruleFile.getProjectId());
         }

         text = this.appendFilters(stringBuilder, items, text, true);
         text = this.appendOrderBy(text);
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         ArrayList directories = new ArrayList();
         JdbcUtils.fillPreparedStatementParameters(items, preparedStatement);
         ResultSet resultSet = preparedStatement.executeQuery();
         String path = ruleFile.getPath();
         if (path.endsWith("/")) {
            path = path + ruleFile.getName();
         } else {
            path = path + "/" + ruleFile.getName();
         }

         while(resultSet.next()) {
            RuleFile directoryRuleFile = this.buildDirectoryRuleFile(connection, resultSet, path);
            directories.add(directoryRuleFile);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         return directories;
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   private List<RuleFile> queryFiles(RuleFile ruleFile, Connection connection) {
      try {
         ArrayList items = new ArrayList();
         String text = "select ID_,NAME_, TYPE_,CREATE_DATE_,UPDATE_USER_,UPDATE_DATE_,LOCKED_USER_,PACKAGE_ID_,PROJECT_ID_,LATEST_VERSION_,DIGEST_,DELETED_ from URULE_FILE ";
         StringBuilder stringBuilder = new StringBuilder();
         if (ruleFile.getId() > -1L) {
            stringBuilder.append("  where PACKAGE_ID_=?");
            items.add(ruleFile.getId());
         }

         if (ruleFile.getProjectId() > 0L) {
            if (ruleFile.getId() > -1L) {
               stringBuilder.append("  and PROJECT_ID_=?");
            } else {
               stringBuilder.append("  where PROJECT_ID_=?");
            }

            items.add(ruleFile.getProjectId());
         }

         text = this.appendFilters(stringBuilder, items, text, false);
         text = this.appendOrderBy(text);
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         JdbcUtils.fillPreparedStatementParameters(items, preparedStatement);
         ArrayList files = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();
         String path = ruleFile.getPath();
         if (path.endsWith("/")) {
            path = path + ruleFile.getName();
         } else {
            path = path + "/" + ruleFile.getName();
         }

         while(resultSet.next()) {
            RuleFile fileRuleFile = this.buildFileRuleFile(resultSet, path);
            files.add(fileRuleFile);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         return files;
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   private RuleFile buildDirectoryRuleFile(Connection connection, ResultSet resultSet, String path) throws SQLException {
      RuleFile ruleFile = new RuleFile();
      ruleFile.setDirectory(true);
      ruleFile.setId(resultSet.getLong(1));
      ruleFile.setName(resultSet.getString(2));
      ruleFile.setType(resultSet.getString(3));
      ruleFile.setParentId(resultSet.getLong(4));
      ruleFile.setProjectId(resultSet.getLong(5));
      ruleFile.setCreateDate(resultSet.getTimestamp(6));
      ruleFile.setUpdateUser(resultSet.getString(7));
      ruleFile.setModifyDate(resultSet.getTimestamp(8));
      ruleFile.setPath(path);
      ArrayList items = new ArrayList();
      ruleFile.setChildren(items);
      items.addAll(this.queryFiles(ruleFile, connection));
      items.addAll(this.queryDirectories(ruleFile, connection));
      return ruleFile;
   }

   private RuleFile buildFileRuleFile(ResultSet resultSet, String path) throws SQLException {
      RuleFile ruleFile = new RuleFile();
      ruleFile.setDirectory(false);
      ruleFile.setId(resultSet.getLong(1));
      ruleFile.setName(resultSet.getString(2));
      ruleFile.setType(resultSet.getString(3));
      ruleFile.setCreateDate(resultSet.getTimestamp(4));
      ruleFile.setUpdateUser(resultSet.getString(5));
      ruleFile.setModifyDate(resultSet.getTimestamp(6));
      ruleFile.setLockedUser(resultSet.getString(7));
      ruleFile.setParentId(resultSet.getLong(8));
      ruleFile.setProjectId(resultSet.getLong(9));
      ruleFile.setLatestVersion(resultSet.getString(10));
      ruleFile.setDigest(resultSet.getString(11));
      ruleFile.setDeleted(resultSet.getBoolean(12));
      if (path.endsWith("/")) {
         path = path + ruleFile.getName();
      } else {
         path = path + "/" + ruleFile.getName();
      }

      ruleFile.setPath(ruleFile.getType() + ":" + path);
      return ruleFile;
   }

   private String normalizeDirectoryResourceType(String resourceType) {
      String normalizedType = resourceType;
      if (!resourceType.equals(ResourceType.VariableLibrary.name()) && !resourceType.equals(ResourceType.ParameterLibrary.name()) && !resourceType.equals(ResourceType.ConstantLibrary.name()) && !resourceType.equals(ResourceType.ActionLibrary.name())) {
         if (!resourceType.equals(ResourceType.DecisionTable.name()) && !resourceType.contentEquals(ResourceType.CrossDecisionTable.name())) {
            if (resourceType.equals(ResourceType.Scorecard.name()) || resourceType.equals(ResourceType.ComplexScorecard.name())) {
               normalizedType = ResourceType.Scorecard.name();
            }
         } else {
            normalizedType = ResourceType.DecisionTable.name();
         }
      } else {
         normalizedType = ResourceType.Library.name();
      }

      return normalizedType;
   }

   private String appendFilters(StringBuilder stringBuilder, List items, String baseSql, boolean directoryQuery) {
      if (this.id != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and ");
         } else {
            stringBuilder.append(" where ");
         }

         stringBuilder.append(" ID_ = ?");
         items.add(this.id);
      }

      if (this.ids != null && this.ids.size() > 0) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and ");
         } else {
            stringBuilder.append(" where ");
         }

         StringBuilder stringBuilder2 = new StringBuilder();

         for(long longValue : (Iterable<Long>)(Iterable<?>)(this.ids)) {
            if (stringBuilder2.length() > 0) {
               stringBuilder2.append(",");
            }

            stringBuilder2.append("?");
            items.add(longValue);
         }

         if (stringBuilder2.length() > 0) {
            stringBuilder.append(" ID_ in (" + stringBuilder2.toString() + ") ");
         }
      }

      if (this.name != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and ");
         } else {
            stringBuilder.append(" where ");
         }

         stringBuilder.append(" NAME_=?");
         items.add(this.name);
      }

      if (!directoryQuery && this.namePattern != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and ");
         } else {
            stringBuilder.append(" where ");
         }

         stringBuilder.append(" NAME_ like ?");
         items.add("%" + this.namePattern + "%");
      }

      if (this.type != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and ");
         } else {
            stringBuilder.append(" where ");
         }

         stringBuilder.append(" TYPE_ = ?");
         String text = this.type;
         if (directoryQuery) {
            text = this.normalizeDirectoryResourceType(this.type);
         }

         items.add(text);
      }

      if (this.types != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and ");
         } else {
            stringBuilder.append(" where ");
         }

         StringBuilder stringBuilder3 = new StringBuilder();

         for(int index = 0; index < this.types.length; ++index) {
            if (index > 0) {
               stringBuilder3.append(",");
            }

            stringBuilder3.append("?");
            String text2 = this.types[index];
            if (directoryQuery) {
               text2 = this.normalizeDirectoryResourceType(text2);
            }

            items.add(text2);
         }

         stringBuilder.append("TYPE_ in (" + stringBuilder3.toString() + ")");
      }

      if (this.lockedUser != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and ");
         } else {
            stringBuilder.append(" where ");
         }

         stringBuilder.append(" LOCKED_USER_ = ?");
         items.add(this.lockedUser);
      }

      if (this.updateUser != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and ");
         } else {
            stringBuilder.append(" where ");
         }

         stringBuilder.append(" UPDATE_USER_ = ?");
         items.add(this.updateUser);
      }

      if (this.deleted != null) {
         if (stringBuilder.length() > 0) {
            stringBuilder.append(" and ");
         } else {
            stringBuilder.append(" where ");
         }

         stringBuilder.append(" DELETED_ = ?");
         items.add(this.deleted);
      }

      baseSql = baseSql + stringBuilder.toString();
      return baseSql;
   }

   private String appendOrderBy(String sql) {
      StringBuilder ascendingColumns = new StringBuilder();
      StringBuilder descendingColumns = new StringBuilder();

      for(String property : this.ascendingProperties) {
         if (ascendingColumns.length() > 0) {
            ascendingColumns.append(",");
         }

         ascendingColumns.append(property);
      }

      for(String property : this.descendingProperties) {
         if (descendingColumns.length() > 0) {
            descendingColumns.append(",");
         }

         descendingColumns.append(property);
      }

      if (ascendingColumns.length() > 0 || descendingColumns.length() > 0) {
         sql = sql + " order by ";
      }

      if (ascendingColumns.length() > 0) {
         sql = sql + ascendingColumns.toString() + " asc";
         if (descendingColumns.length() > 0) {
            sql = sql + "," + descendingColumns.toString() + " desc";
         }
      } else if (descendingColumns.length() > 0) {
         sql = sql + descendingColumns.toString() + " desc";
      }

      return sql;
   }

   public FileQuery id(long id) {
      this.id = id;
      return this;
   }

   public FileQuery ids(List ids) {
      this.ids = ids;
      return this;
   }

   public FileQuery name(String name) {
      this.name = name;
      return this;
   }

   public FileQuery nameLike(String name) {
      this.namePattern = name;
      return this;
   }

   public FileQuery type(String type) {
      this.type = type;
      return this;
   }

   public FileQuery types(String[] types) {
      this.types = types;
      return this;
   }

   public FileQuery lockedUser(String lockedUser) {
      this.lockedUser = lockedUser;
      return this;
   }

   public FileQuery deleted(boolean deleted) {
      this.deleted = deleted;
      return this;
   }

   public FileQuery removeEmpty(boolean removeEmpty) {
      this.removeEmpty = removeEmpty;
      return this;
   }

   public FileQuery containCommonProject(boolean containCommonProject) {
      this.containCommonProject = containCommonProject;
      return this;
   }

   public FileQuery desc(String property) {
      this.descendingProperties.add(property);
      return this;
   }

   public FileQuery asc(String property) {
      this.ascendingProperties.add(property);
      return this;
   }

   public FileQuery updateUser(String updateUser) {
      this.updateUser = updateUser;
      return this;
   }

   class PathInfo {
      private String path;
      private boolean fileSet;

      public String getPath() {
         return this.path;
      }

      public void setPath(String path) {
         this.path = path;
      }

      public boolean isFileSet() {
         return this.fileSet;
      }

      public void setFileSet(boolean fileSet) {
         this.fileSet = fileSet;
      }
   }
}
