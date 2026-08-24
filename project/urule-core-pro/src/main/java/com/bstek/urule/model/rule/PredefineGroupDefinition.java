package com.bstek.urule.model.rule;

import java.util.List;

public class PredefineGroupDefinition {
   private int priority;
   private List<Predefine> predefines;
   private String filPath;

   public int getPriority() {
      return this.priority;
   }

   public void setPriority(int priority) {
      this.priority = priority;
   }

   public List<Predefine> getPredefines() {
      return this.predefines;
   }

   public void setPredefines(List<Predefine> predefines) {
      this.predefines = predefines;
   }

   public String getFilePath() {
      return this.filPath;
   }

   public void setFilePath(String path) {
      this.filPath = path;
   }
}
