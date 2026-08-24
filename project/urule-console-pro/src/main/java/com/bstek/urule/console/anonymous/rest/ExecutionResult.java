package com.bstek.urule.console.anonymous.rest;

import java.util.ArrayList;
import java.util.List;

public class ExecutionResult {
   private long duration = 0L;
   private List output = new ArrayList();

   public long getDuration() {
      return this.duration;
   }

   public void addDuration(long duration) {
      this.duration = duration;
   }

   public List getOutput() {
      return this.output;
   }

   public void addOutput(List data) {
      this.output.add(data);
   }
}
