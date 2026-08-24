package com.bstek.urule.console.database.model.datasource;

import com.bstek.urule.console.database.model.batch.DataSourceType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class DataSource {
   private Long id;
   private String name;
   private String groupId;
   private DataSourceType type;
   private String dbJndiName;
   private String dataSourceBean;
   private String dbDriver;
   private String dbUrl;
   private String dbUser;
   private String dbPwd;
   private String dbValidationQuery;
   private int dbInitialsize;
   private int dbMaxIdle;
   private int dbMaxTotal;
   private int dbMinIdle;
   private String desc;
   private String createUser;
   private String updateUser;
   private Date createDate;
   private Date updateDate;

   public String getDesc() {
      return this.desc;
   }

   public void setDesc(String desc) {
      this.desc = desc;
   }

   public Long getId() {
      return this.id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getCreateUser() {
      return this.createUser;
   }

   public void setCreateUser(String createUser) {
      this.createUser = createUser;
   }

   public String getUpdateUser() {
      return this.updateUser;
   }

   public void setUpdateUser(String updateUser) {
      this.updateUser = updateUser;
   }

   public Date getCreateDate() {
      return this.createDate;
   }

   public void setCreateDate(Date createDate) {
      this.createDate = createDate;
   }

   public Date getUpdateDate() {
      return this.updateDate;
   }

   public void setUpdateDate(Date updateDate) {
      this.updateDate = updateDate;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public DataSourceType getType() {
      return this.type;
   }

   public void setType(DataSourceType type) {
      this.type = type;
   }

   public String getDbJndiName() {
      return this.dbJndiName;
   }

   public void setDbJndiName(String dbJndiName) {
      this.dbJndiName = dbJndiName;
   }

   public String getDataSourceBean() {
      return this.dataSourceBean;
   }

   public void setDataSourceBean(String dataSourceBean) {
      this.dataSourceBean = dataSourceBean;
   }

   public String getDbDriver() {
      return this.dbDriver;
   }

   public void setDbDriver(String dbDriver) {
      this.dbDriver = dbDriver;
   }

   public String getDbUrl() {
      return this.dbUrl;
   }

   public void setDbUrl(String dbUrl) {
      this.dbUrl = dbUrl;
   }

   public String getDbUser() {
      return this.dbUser;
   }

   public void setDbUser(String dbUser) {
      this.dbUser = dbUser;
   }

   public String getDbPwd() {
      return this.dbPwd;
   }

   public void setDbPwd(String dbPwd) {
      this.dbPwd = dbPwd;
   }

   public String getDbValidationQuery() {
      return this.dbValidationQuery;
   }

   public void setDbValidationQuery(String dbValidationQuery) {
      this.dbValidationQuery = dbValidationQuery;
   }

   public int getDbInitialsize() {
      return this.dbInitialsize;
   }

   public void setDbInitialsize(int dbInitialsize) {
      this.dbInitialsize = dbInitialsize;
   }

   public int getDbMaxIdle() {
      return this.dbMaxIdle;
   }

   public void setDbMaxIdle(int dbMaxIdle) {
      this.dbMaxIdle = dbMaxIdle;
   }

   public int getDbMaxTotal() {
      return this.dbMaxTotal;
   }

   public void setDbMaxTotal(int dbMaxTotal) {
      this.dbMaxTotal = dbMaxTotal;
   }

   public int getDbMinIdle() {
      return this.dbMinIdle;
   }

   public void setDbMinIdle(int dbMinIdle) {
      this.dbMinIdle = dbMinIdle;
   }
}
