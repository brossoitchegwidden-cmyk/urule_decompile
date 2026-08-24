package com.bstek.urule.console.editor.packet.scenario;

import java.util.Date;
import java.util.List;

public class TestScenario {
   private long id;
   private String name;
   private String desc;
   private Date createDate;
   private String createUser;
   private List inputData;
   private List outputData;

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getDesc() {
      return this.desc;
   }

   public void setDesc(String desc) {
      this.desc = desc;
   }

   public Date getCreateDate() {
      return this.createDate;
   }

   public void setCreateDate(Date createDate) {
      this.createDate = createDate;
   }

   public String getCreateUser() {
      return this.createUser;
   }

   public void setCreateUser(String createUser) {
      this.createUser = createUser;
   }

   public List getInputData() {
      return this.inputData;
   }

   public void setInputData(List inputData) {
      this.inputData = inputData;
   }

   public List getOutputData() {
      return this.outputData;
   }

   public void setOutputData(List outputData) {
      this.outputData = outputData;
   }
}
