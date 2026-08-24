package com.bstek.urule.action;

public class ActionValueImpl implements ActionValue {
   private String actionId;
   private Object value;

   public ActionValueImpl(String actionId, Object value) {
      this.actionId = actionId;
      this.value = value;
   }

   @Override
   public String getActionId() {
      return this.actionId;
   }

   @Override
   public Object getValue() {
      return this.value;
   }
}
