package com.bstek.urule.console.anonymous.rest;

import java.util.ArrayList;
import java.util.List;

public class ExecutionResult {
   private long a = 0L;
   private List b = new ArrayList();

   public long getDuration() {
      return this.a;
   }

   public void addDuration(long var1) {
      this.a = var1;
   }

   public List getOutput() {
      return this.b;
   }

   public void addOutput(List var1) {
      this.b.add(var1);
   }
}
