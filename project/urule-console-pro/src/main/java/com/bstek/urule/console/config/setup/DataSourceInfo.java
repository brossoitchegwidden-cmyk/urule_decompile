package com.bstek.urule.console.config.setup;

public class DataSourceInfo {
   private String driver;
   private String url;
   private String username;
   private String password;
   private String connectionType;
   private String platform;
   private String jndi;
   private String connectionClassName;
   private int initialSize;
   private int maxTotal;
   private int maxIdle;
   private int minIdle;
   private String validationQuery;

   public String getDriver() {
      return this.driver;
   }

   public void setDriver(String driver) {
      this.driver = driver;
   }

   public String getUrl() {
      return this.url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public String getUsername() {
      return this.username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getConnectionType() {
      return this.connectionType;
   }

   public void setConnectionType(String connectionType) {
      this.connectionType = connectionType;
   }

   public String getPlatform() {
      return this.platform;
   }

   public void setPlatform(String platform) {
      this.platform = platform;
   }

   public String getJndi() {
      return this.jndi;
   }

   public void setJndi(String jndi) {
      this.jndi = jndi;
   }

   public String getConnectionClassName() {
      return this.connectionClassName;
   }

   public void setConnectionClassName(String connectionClassName) {
      this.connectionClassName = connectionClassName;
   }

   public int getInitialSize() {
      return this.initialSize;
   }

   public void setInitialSize(int initialSize) {
      this.initialSize = initialSize;
   }

   public int getMaxTotal() {
      return this.maxTotal;
   }

   public void setMaxTotal(int maxTotal) {
      this.maxTotal = maxTotal;
   }

   public int getMaxIdle() {
      return this.maxIdle;
   }

   public void setMaxIdle(int maxIdle) {
      this.maxIdle = maxIdle;
   }

   public int getMinIdle() {
      return this.minIdle;
   }

   public void setMinIdle(int minIdle) {
      this.minIdle = minIdle;
   }

   public String getValidationQuery() {
      return this.validationQuery;
   }

   public void setValidationQuery(String validationQuery) {
      this.validationQuery = validationQuery;
   }
}
