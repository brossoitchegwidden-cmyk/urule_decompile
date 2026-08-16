package com.bstek.urule.model.library.action;

public class ActionData {
   private SpringBean bean;
   private Method method;

   public ActionData(SpringBean var1, Method var2) {
      this.bean = var1;
      this.method = var2;
   }

   public SpringBean getBean() {
      return this.bean;
   }

   public Method getMethod() {
      return this.method;
   }
}
