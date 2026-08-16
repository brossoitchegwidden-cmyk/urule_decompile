package com.bstek.urule.runtime.monitor;

import com.bstek.urule.model.rete.RuleData;
import com.bstek.urule.runtime.log.FlowNodeLog;
import com.bstek.urule.runtime.log.Log;
import com.bstek.urule.runtime.log.MatchedRuleLog;
import java.util.ArrayList;
import java.util.List;

public class MonitorDataImpl implements MonitorData {
   private String a;
   private long b;
   private String c;
   private List<MatchedRuleLog> d = new ArrayList<>();
   private List<RuleData> e = new ArrayList<>();
   private List<FlowNodeLog> f = new ArrayList<>();
   private List<Log> g = new ArrayList<>();
   private List<IOData> h;
   private List<IOData> i;

   @Override
   public long getTotalDuration() {
      return this.b;
   }

   public void setTotalDuration(long var1) {
      this.b = var1;
   }

   @Override
   public List<MatchedRuleLog> getMatchedRuleList() {
      return this.d;
   }

   public void addMatchedRuleList(List<MatchedRuleLog> var1) {
      this.d.addAll(var1);
   }

   @Override
   public List<RuleData> getNotMatchRuleList() {
      return this.e;
   }

   public void addNotMatchRuleList(List<RuleData> var1) {
      this.e.addAll(var1);
   }

   @Override
   public List<FlowNodeLog> getFiredFlowNodeList() {
      return this.f;
   }

   public void addFiredFlowNodeList(List<FlowNodeLog> var1) {
      this.f.addAll(var1);
   }

   @Override
   public String getPackageInfo() {
      return this.a;
   }

   public void setPackageInfo(String var1) {
      this.a = var1;
   }

   @Override
   public List<Log> getLogs() {
      return this.g;
   }

   public void addLogs(List<Log> var1) {
      this.g.addAll(var1);
   }

   @Override
   public List<IOData> getInputData() {
      return this.h;
   }

   @Override
   public List<IOData> getOutputData() {
      return this.i;
   }

   public void setInputData(List<IOData> var1) {
      this.h = var1;
   }

   public void setOutputData(List<IOData> var1) {
      this.i = var1;
   }

   @Override
   public String getVersion() {
      return this.c;
   }

   public void setVersion(String var1) {
      this.c = var1;
   }

   @Override
   public String toString() {
      return "MonitorDataImpl [packageInfo="
         + this.a
         + ", totalDuration="
         + this.b
         + ",version="
         + this.c
         + ", matchedRuleList="
         + this.d
         + ", notMatchRuleList="
         + this.e
         + ", firedFlowNodeList="
         + this.f
         + ", logs="
         + this.g
         + ", inputData="
         + this.h
         + ", outputData="
         + this.i
         + "]";
   }
}
