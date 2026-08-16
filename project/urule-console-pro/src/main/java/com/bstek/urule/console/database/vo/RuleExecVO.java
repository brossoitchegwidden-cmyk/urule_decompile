package com.bstek.urule.console.database.vo;

public class RuleExecVO {
   private Long a;
   private String b;
   private int c;
   private int d;

   public Long getKnowledgeId() {
      return this.a;
   }

   public void setKnowledgeId(Long var1) {
      this.a = var1;
   }

   public String getKnowledgeName() {
      return this.b;
   }

   public void setKnowledgeName(String var1) {
      this.b = var1;
   }

   public int getCount() {
      return this.d;
   }

   public void setCount(int var1) {
      this.d = var1;
   }

   public int getTime() {
      return this.c;
   }

   public void setTime(int var1) {
      this.c = var1;
   }
}
