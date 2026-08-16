package com.bstek.urule.console.editor.packet.scenario;

import java.util.Date;
import java.util.List;

public class TestScenario {
   private long a;
   private String b;
   private String c;
   private Date d;
   private String e;
   private List f;
   private List g;

   public long getId() {
      return this.a;
   }

   public void setId(long var1) {
      this.a = var1;
   }

   public String getName() {
      return this.b;
   }

   public void setName(String var1) {
      this.b = var1;
   }

   public String getDesc() {
      return this.c;
   }

   public void setDesc(String var1) {
      this.c = var1;
   }

   public Date getCreateDate() {
      return this.d;
   }

   public void setCreateDate(Date var1) {
      this.d = var1;
   }

   public String getCreateUser() {
      return this.e;
   }

   public void setCreateUser(String var1) {
      this.e = var1;
   }

   public List getInputData() {
      return this.f;
   }

   public void setInputData(List var1) {
      this.f = var1;
   }

   public List getOutputData() {
      return this.g;
   }

   public void setOutputData(List var1) {
      this.g = var1;
   }
}
