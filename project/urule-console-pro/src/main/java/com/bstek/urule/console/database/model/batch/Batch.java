package com.bstek.urule.console.database.model.batch;

import com.bstek.urule.console.batch.BatchStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class Batch {
   private Long id;
   private Long projectId;
   private String name;
   private boolean enable;
   private int skipLimit;
   private boolean async = true;
   private String callbackUrl;
   private BatchStatus status;
   private String listener;
   private boolean threadMulti = true;
   private Integer threadSize = 10;
   private Integer threadDataSize = 100;
   private Long providerId;
   private Long resolverId;
   private Long packetId;
   private String packetName;
   private String packetInputData;
   private boolean restEnable;
   private boolean restSecurityEnable;
   private String restSecurityUser;
   private String restSecurityPassword;
   private String inputData;
   private String desc;
   private String createUser;
   private String updateUser;
   private Date createDate;
   private Date updateDate;
   private List params;
   private List packetParams;
   private Map complexPacketParams;
   private List outParameterNameList;
   private BatchDataProvider dataProvider;
   private BatchDataResolver dataResolver;

   public BatchStatus getStatus() {
      return this.status;
   }

   public void setStatus(BatchStatus status) {
      this.status = status;
   }

   public String getListener() {
      return this.listener;
   }

   public void setListener(String listener) {
      this.listener = listener;
   }

   public boolean isAsync() {
      return this.async;
   }

   public void setAsync(boolean async) {
      this.async = async;
   }

   public boolean isThreadMulti() {
      return this.threadMulti;
   }

   public void setThreadMulti(boolean threadMulti) {
      this.threadMulti = threadMulti;
   }

   public Integer getThreadSize() {
      return this.threadSize;
   }

   public void setThreadSize(Integer threadSize) {
      this.threadSize = threadSize;
   }

   public Integer getThreadDataSize() {
      return this.threadDataSize;
   }

   public void setThreadDataSize(Integer threadDataSize) {
      this.threadDataSize = threadDataSize;
   }

   public List getParams() {
      return this.params;
   }

   public void setParams(List params) {
      this.params = params;
   }

   public List getPacketParams() {
      return this.packetParams;
   }

   public void setPacketParams(List packetParams) {
      this.packetParams = packetParams;
   }

   public BatchDataProvider getDataProvider() {
      return this.dataProvider;
   }

   public void setDataProvider(BatchDataProvider dataProvider) {
      this.dataProvider = dataProvider;
   }

   public BatchDataResolver getDataResolver() {
      return this.dataResolver;
   }

   public void setDataResolver(BatchDataResolver dataResolver) {
      this.dataResolver = dataResolver;
   }

   public Long getId() {
      return this.id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Long getProviderId() {
      return this.providerId;
   }

   public void setProviderId(Long providerId) {
      this.providerId = providerId;
   }

   public Long getResolverId() {
      return this.resolverId;
   }

   public void setResolverId(Long resolverId) {
      this.resolverId = resolverId;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Long getProjectId() {
      return this.projectId;
   }

   public void setProjectId(Long projectId) {
      this.projectId = projectId;
   }

   public String getDesc() {
      return this.desc;
   }

   public void setDesc(String desc) {
      this.desc = desc;
   }

   public Long getPacketId() {
      return this.packetId;
   }

   public void setPacketId(Long packetId) {
      this.packetId = packetId;
   }

   public String getPacketInputData() {
      return this.packetInputData;
   }

   public void setPacketInputData(String packetInputData) {
      this.packetInputData = packetInputData;
   }

   public boolean isRestEnable() {
      return this.restEnable;
   }

   public void setRestEnable(boolean restEnable) {
      this.restEnable = restEnable;
   }

   public boolean isRestSecurityEnable() {
      return this.restSecurityEnable;
   }

   public void setRestSecurityEnable(boolean restSecurityEnable) {
      this.restSecurityEnable = restSecurityEnable;
   }

   public String getRestSecurityUser() {
      return this.restSecurityUser;
   }

   public void setRestSecurityUser(String restSecurityUser) {
      this.restSecurityUser = restSecurityUser;
   }

   public String getRestSecurityPassword() {
      return this.restSecurityPassword;
   }

   public void setRestSecurityPassword(String restSecurityPassword) {
      this.restSecurityPassword = restSecurityPassword;
   }

   public String getInputData() {
      return this.inputData;
   }

   public void setInputData(String inputData) {
      this.inputData = inputData;
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

   public String getCallbackUrl() {
      return this.callbackUrl;
   }

   public void setCallbackUrl(String callbackUrl) {
      this.callbackUrl = callbackUrl;
   }

   public boolean isEnable() {
      return this.enable;
   }

   public void setEnable(boolean enable) {
      this.enable = enable;
   }

   public Map getComplexPacketParams() {
      return this.complexPacketParams;
   }

   public void setComplexPacketParams(Map complexPacketParams) {
      this.complexPacketParams = complexPacketParams;
   }

   public List getOutParameterNameList() {
      return this.outParameterNameList;
   }

   public void setOutParameterNameList(List outParameterNameList) {
      this.outParameterNameList = outParameterNameList;
   }

   public int getSkipLimit() {
      return this.skipLimit;
   }

   public void setSkipLimit(int skipLimit) {
      this.skipLimit = skipLimit;
   }

   public String getPacketName() {
      return this.packetName;
   }

   public void setPacketName(String packetName) {
      this.packetName = packetName;
   }
}
