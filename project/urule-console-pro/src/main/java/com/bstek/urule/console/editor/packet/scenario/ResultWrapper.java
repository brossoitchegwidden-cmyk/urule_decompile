package com.bstek.urule.console.editor.packet.scenario;

import java.util.List;

public class ResultWrapper {
   private String scenarioName;
   private String packageName;
   private long totalTime;
   private long prepareTime;
   private List resultList;

   public String getScenarioName() {
      return this.scenarioName;
   }

   public void setScenarioName(String scenarioName) {
      this.scenarioName = scenarioName;
   }

   public String getPackageName() {
      return this.packageName;
   }

   public void setPackageName(String packageName) {
      this.packageName = packageName;
   }

   public long getTotalTime() {
      return this.totalTime;
   }

   public void setTotalTime(long totalTime) {
      this.totalTime = totalTime;
   }

   public long getPrepareTime() {
      return this.prepareTime;
   }

   public void setPrepareTime(long prepareTime) {
      this.prepareTime = prepareTime;
   }

   public List getResultList() {
      return this.resultList;
   }

   public void setResultList(List resultList) {
      this.resultList = resultList;
   }
}
