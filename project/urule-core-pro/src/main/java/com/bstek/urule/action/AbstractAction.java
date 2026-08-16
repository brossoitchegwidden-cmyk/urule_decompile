package com.bstek.urule.action;

public abstract class AbstractAction implements Action {
   private int b;
   protected boolean a;

   public int compareTo(Action var1) {
      return var1.getPriority() - this.b;
   }

   @Override
   public int getPriority() {
      return this.b;
   }

   @Override
   public void setDebug(boolean var1) {
      this.a = var1;
   }

   public void setPriority(int var1) {
      this.b = var1;
   }
}
