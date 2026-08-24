package com.bstek.urule.action;

import com.bstek.urule.runtime.WorkingMemory;

public class WorkingMemoryHolderAdapter {
   public static void clean() {
      WorkingMemoryHolder.popWorkingMemory();
   }

   public static void set(WorkingMemory workingMemory) {
      WorkingMemoryHolder.set(workingMemory);
   }
}
