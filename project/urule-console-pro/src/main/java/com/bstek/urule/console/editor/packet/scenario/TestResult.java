package com.bstek.urule.console.editor.packet.scenario;

import java.util.ArrayList;
import java.util.List;

public class TestResult {
   private String a;
   private String b;
   private long c;
   private List d = new ArrayList();
   private List e = new ArrayList();
   private List f = new ArrayList();
   private List g = new ArrayList();
   private String h;
   private String i;
   private List j;

   public String getScenarioId() {
      return this.a;
   }

   public void setScenarioId(String var1) {
      this.a = var1;
   }

   public String getScenarioDesc() {
      return this.b;
   }

   public void setScenarioDesc(String var1) {
      this.b = var1;
   }

   public long getConsumeTime() {
      return this.c;
   }

   public void setConsumeTime(long var1) {
      this.c = var1;
   }

   public List getValueCompares() {
      return this.j;
   }

   public void setValueCompares(List var1) {
      this.j = var1;
   }

   public List getLogs() {
      return this.d;
   }

   public void addLogs(List var1) {
      this.d.addAll(var1);
   }

   public List getMatchedRuleList() {
      return this.e;
   }

   public void addMatchedRuleList(List var1) {
      this.e.addAll(var1);
   }

   public List getNotMatchedRuleList() {
      return this.f;
   }

   public void addNotMatchedRuleList(List var1) {
      this.f.addAll(var1);
   }

   public List getFlowNodeList() {
      return this.g;
   }

   public void addFlowNodeList(List var1) {
      this.g.addAll(var1);
   }

   public String getInputData() {
      return this.h;
   }

   public void setInputData(String var1) {
      this.h = var1;
   }

   public String getOutputData() {
      return this.i;
   }

   public void setOutputData(String var1) {
      this.i = var1;
   }
}
