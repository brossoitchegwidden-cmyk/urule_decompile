package com.bstek.urule.action;

import com.bstek.urule.runtime.WorkingMemory;
import java.util.Stack;

public class WorkingMemoryHolder {
   private static final ThreadLocal<Stack<WorkingMemory>> WORKING_MEMORY_STACK = new ThreadLocal<>();

   public static WorkingMemory getCurrentWorkingMemory() {
      Stack stack = WORKING_MEMORY_STACK.get();
      return stack != null && !stack.isEmpty() ? (WorkingMemory)stack.peek() : null;
   }

   protected static void popWorkingMemory() {
      Stack stack = WORKING_MEMORY_STACK.get();
      if (stack != null) {
         if (stack.isEmpty()) {
            WORKING_MEMORY_STACK.remove();
         } else {
            stack.pop();
            if (stack.isEmpty()) {
               WORKING_MEMORY_STACK.remove();
            }
         }
      }
   }

   protected static void set(WorkingMemory workingMemory) {
      Stack stack = WORKING_MEMORY_STACK.get();
      if (stack == null) {
         stack = new Stack();
         WORKING_MEMORY_STACK.set(stack);
      }

      stack.push(workingMemory);
   }
}
