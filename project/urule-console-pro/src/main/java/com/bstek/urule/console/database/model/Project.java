package com.bstek.urule.console.database.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class Project {
   private Long id;
   private String name;
   private String type;
   private ProjectViewModel viewModel;
   private String desc;
   private String groupId;
   private String enableApproveUser;
   private String disableApproveUser;
   private String deployApproveUser;
   private String createUser;
   private String updateUser;
   private Date createDate;
   private Date updateDate;

   public Project() {
      this.viewModel = ProjectViewModel.category;
   }

   public Long getId() {
      return this.id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public ProjectViewModel getViewModel() {
      return this.viewModel;
   }

   public void setViewModel(ProjectViewModel viewModel) {
      this.viewModel = viewModel;
   }

   public String getDesc() {
      return this.desc;
   }

   public void setDesc(String desc) {
      this.desc = desc;
   }

   public String getGroupId() {
      return this.groupId;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
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

   public String getCreateUser() {
      return this.createUser;
   }

   public void setCreateUser(String createUser) {
      this.createUser = createUser;
   }

   public String getEnableApproveUser() {
      return this.enableApproveUser;
   }

   public void setEnableApproveUser(String enableApproveUser) {
      this.enableApproveUser = enableApproveUser;
   }

   public String getDisableApproveUser() {
      return this.disableApproveUser;
   }

   public void setDisableApproveUser(String disableApproveUser) {
      this.disableApproveUser = disableApproveUser;
   }

   public String getDeployApproveUser() {
      return this.deployApproveUser;
   }

   public void setDeployApproveUser(String deployApproveUser) {
      this.deployApproveUser = deployApproveUser;
   }
}
