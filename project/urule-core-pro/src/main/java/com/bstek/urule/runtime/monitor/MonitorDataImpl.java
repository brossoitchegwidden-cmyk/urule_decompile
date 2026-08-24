package com.bstek.urule.runtime.monitor;

import com.bstek.urule.model.rete.RuleData;
import com.bstek.urule.runtime.log.FlowNodeLog;
import com.bstek.urule.runtime.log.Log;
import com.bstek.urule.runtime.log.MatchedRuleLog;
import java.util.ArrayList;
import java.util.List;

public class MonitorDataImpl implements MonitorData {
   private String packageInfo;
   private long totalDuration;
   private String version;
   private List<MatchedRuleLog> matchedRuleList = new ArrayList<>();
   private List<RuleData> notMatchRuleList = new ArrayList<>();
   private List<FlowNodeLog> firedFlowNodeList = new ArrayList<>();
   private List<Log> logs = new ArrayList<>();
   private List<IOData> inputData;
   private List<IOData> outputData;

   @Override
   public long getTotalDuration() {
      return this.totalDuration;
   }

   public void setTotalDuration(long totalDuration) {
      this.totalDuration = totalDuration;
   }

   @Override
   public List<MatchedRuleLog> getMatchedRuleList() {
      return this.matchedRuleList;
   }

   public void addMatchedRuleList(List<MatchedRuleLog> matchedRuleList) {
      this.matchedRuleList.addAll(matchedRuleList);
   }

   @Override
   public List<RuleData> getNotMatchRuleList() {
      return this.notMatchRuleList;
   }

   public void addNotMatchRuleList(List<RuleData> notMatchRuleList) {
      this.notMatchRuleList.addAll(notMatchRuleList);
   }

   @Override
   public List<FlowNodeLog> getFiredFlowNodeList() {
      return this.firedFlowNodeList;
   }

   public void addFiredFlowNodeList(List<FlowNodeLog> firedFlowNodeList) {
      this.firedFlowNodeList.addAll(firedFlowNodeList);
   }

   @Override
   public String getPackageInfo() {
      return this.packageInfo;
   }

   public void setPackageInfo(String packageInfo) {
      this.packageInfo = packageInfo;
   }

   @Override
   public List<Log> getLogs() {
      return this.logs;
   }

   public void addLogs(List<Log> logs) {
      this.logs.addAll(logs);
   }

   @Override
   public List<IOData> getInputData() {
      return this.inputData;
   }

   @Override
   public List<IOData> getOutputData() {
      return this.outputData;
   }

   public void setInputData(List<IOData> inputData) {
      this.inputData = inputData;
   }

   public void setOutputData(List<IOData> outputData) {
      this.outputData = outputData;
   }

   @Override
   public String getVersion() {
      return this.version;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   @Override
   public String toString() {
      return "MonitorDataImpl [packageInfo="
         + this.packageInfo
         + ", totalDuration="
         + this.totalDuration
         + ",version="
         + this.version
         + ", matchedRuleList="
         + this.matchedRuleList
         + ", notMatchRuleList="
         + this.notMatchRuleList
         + ", firedFlowNodeList="
         + this.firedFlowNodeList
         + ", logs="
         + this.logs
         + ", inputData="
         + this.inputData
         + ", outputData="
         + this.outputData
         + "]";
   }
}
