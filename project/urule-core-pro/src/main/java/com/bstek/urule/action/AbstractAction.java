package com.bstek.urule.action;

public abstract class AbstractAction implements Action {
   private int priority;
   protected boolean debug;

   public int compareTo(Action action) {
      return action.getPriority() - this.priority;
   }

   @Override
   public int getPriority() {
      return this.priority;
   }

   @Override
   public void setDebug(boolean debug) {
      this.debug = debug;
   }

   public void setPriority(int priority) {
      this.priority = priority;
   }
}
