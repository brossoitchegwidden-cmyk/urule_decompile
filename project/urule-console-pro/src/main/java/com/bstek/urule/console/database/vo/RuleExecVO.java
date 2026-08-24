package com.bstek.urule.console.database.vo;

public class RuleExecVO {
   private Long knowledgeId;
   private String knowledgeName;
   private int time;
   private int count;

   public Long getKnowledgeId() {
      return this.knowledgeId;
   }

   public void setKnowledgeId(Long knowledgeId) {
      this.knowledgeId = knowledgeId;
   }

   public String getKnowledgeName() {
      return this.knowledgeName;
   }

   public void setKnowledgeName(String knowledgeName) {
      this.knowledgeName = knowledgeName;
   }

   public int getCount() {
      return this.count;
   }

   public void setCount(int count) {
      this.count = count;
   }

   public int getTime() {
      return this.time;
   }

   public void setTime(int time) {
      this.time = time;
   }
}
