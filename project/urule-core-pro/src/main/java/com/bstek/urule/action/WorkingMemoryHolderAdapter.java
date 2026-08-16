package com.bstek.urule.action;

import com.bstek.urule.runtime.WorkingMemory;

public class WorkingMemoryHolderAdapter {
   public static void clean() {
      WorkingMemoryHolder.a();
   }

   public static void set(WorkingMemory var0) {
      WorkingMemoryHolder.a(var0);
   }
}
