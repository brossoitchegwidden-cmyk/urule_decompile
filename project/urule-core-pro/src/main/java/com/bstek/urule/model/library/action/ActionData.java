package com.bstek.urule.model.library.action;

public class ActionData {
   private SpringBean bean;
   private Method method;

   public ActionData(SpringBean bean, Method method) {
      this.bean = bean;
      this.method = method;
   }

   public SpringBean getBean() {
      return this.bean;
   }

   public Method getMethod() {
      return this.method;
   }
}
