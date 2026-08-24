package com.bstek.urule.runtime.log;

import java.util.ArrayList;
import java.util.List;

public class UnitLog implements Log {
   private List<Log> logs = new ArrayList<>();

   public void addLog(Log log) {
      this.logs.add(log);
   }

   public List<Log> getLogs() {
      return this.logs;
   }
}
