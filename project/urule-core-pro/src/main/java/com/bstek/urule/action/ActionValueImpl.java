package com.bstek.urule.action;

public class ActionValueImpl implements ActionValue {
   private String a;
   private Object b;

   public ActionValueImpl(String var1, Object var2) {
      this.a = var1;
      this.b = var2;
   }

   @Override
   public String getActionId() {
      return this.a;
   }

   @Override
   public Object getValue() {
      return this.b;
   }
}
