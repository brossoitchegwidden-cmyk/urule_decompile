package com.bstek.urule.console.editor.packet.scenario;

import java.util.ArrayList;
import java.util.List;

public class TestResult {
   private String scenarioId;
   private String scenarioDesc;
   private long consumeTime;
   private List logs = new ArrayList();
   private List matchedRuleList = new ArrayList();
   private List notMatchedRuleList = new ArrayList();
   private List flowNodeList = new ArrayList();
   private String inputData;
   private String outputData;
   private List valueCompares;

   public String getScenarioId() {
      return this.scenarioId;
   }

   public void setScenarioId(String scenarioId) {
      this.scenarioId = scenarioId;
   }

   public String getScenarioDesc() {
      return this.scenarioDesc;
   }

   public void setScenarioDesc(String scenarioDesc) {
      this.scenarioDesc = scenarioDesc;
   }

   public long getConsumeTime() {
      return this.consumeTime;
   }

   public void setConsumeTime(long consumeTime) {
      this.consumeTime = consumeTime;
   }

   public List getValueCompares() {
      return this.valueCompares;
   }

   public void setValueCompares(List valueCompares) {
      this.valueCompares = valueCompares;
   }

   public List getLogs() {
      return this.logs;
   }

   public void addLogs(List logs) {
      this.logs.addAll(logs);
   }

   public List getMatchedRuleList() {
      return this.matchedRuleList;
   }

   public void addMatchedRuleList(List matchedRuleList) {
      this.matchedRuleList.addAll(matchedRuleList);
   }

   public List getNotMatchedRuleList() {
      return this.notMatchedRuleList;
   }

   public void addNotMatchedRuleList(List notMatchedRuleList) {
      this.notMatchedRuleList.addAll(notMatchedRuleList);
   }

   public List getFlowNodeList() {
      return this.flowNodeList;
   }

   public void addFlowNodeList(List flowNodeList) {
      this.flowNodeList.addAll(flowNodeList);
   }

   public String getInputData() {
      return this.inputData;
   }

   public void setInputData(String inputData) {
      this.inputData = inputData;
   }

   public String getOutputData() {
      return this.outputData;
   }

   public void setOutputData(String outputData) {
      this.outputData = outputData;
   }
}
