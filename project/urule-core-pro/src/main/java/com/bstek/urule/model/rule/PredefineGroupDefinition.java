package com.bstek.urule.model.rule;

import java.util.List;

public class PredefineGroupDefinition {
   private int priority;
   private List<Predefine> predefines;
   private String filPath;

   public int getPriority() {
      return this.priority;
   }

   public void setPriority(int var1) {
      this.priority = var1;
   }

   public List<Predefine> getPredefines() {
      return this.predefines;
   }

   public void setPredefines(List<Predefine> var1) {
      this.predefines = var1;
   }

   public String getFilePath() {
      return this.filPath;
   }

   public void setFilePath(String var1) {
      this.filPath = var1;
   }
}
