package com.bstek.urule.console.editor.packet.scenario;

import java.util.List;

public class ResultWrapper {
   private String a;
   private String b;
   private long c;
   private long d;
   private List e;

   public String getScenarioName() {
      return this.a;
   }

   public void setScenarioName(String var1) {
      this.a = var1;
   }

   public String getPackageName() {
      return this.b;
   }

   public void setPackageName(String var1) {
      this.b = var1;
   }

   public long getTotalTime() {
      return this.c;
   }

   public void setTotalTime(long var1) {
      this.c = var1;
   }

   public long getPrepareTime() {
      return this.d;
   }

   public void setPrepareTime(long var1) {
      this.d = var1;
   }

   public List getResultList() {
      return this.e;
   }

   public void setResultList(List var1) {
      this.e = var1;
   }
}
