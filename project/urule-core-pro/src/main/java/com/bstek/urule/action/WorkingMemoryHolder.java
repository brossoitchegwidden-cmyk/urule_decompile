package com.bstek.urule.action;

import com.bstek.urule.runtime.WorkingMemory;
import java.util.Stack;

public class WorkingMemoryHolder {
   private static final ThreadLocal<Stack<WorkingMemory>> a = new ThreadLocal<>();

   public static WorkingMemory getCurrentWorkingMemory() {
      Stack var0 = a.get();
      return var0 != null && !var0.isEmpty() ? (WorkingMemory)var0.peek() : null;
   }

   protected static void a() {
      Stack var0 = a.get();
      if (var0 != null) {
         if (var0.isEmpty()) {
            a.remove();
         } else {
            var0.pop();
            if (var0.isEmpty()) {
               a.remove();
            }
         }
      }
   }

   protected static void a(WorkingMemory var0) {
      Stack var1 = a.get();
      if (var1 == null) {
         var1 = new Stack();
         a.set(var1);
      }

      var1.push(var0);
   }
}
