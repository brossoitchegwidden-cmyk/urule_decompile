package com.bstek.urule.model.library.action;

import java.util.ArrayList;
import java.util.List;

public class ActionLibrary {
   private List<SpringBean> springBeans;

   public List<SpringBean> getSpringBeans() {
      return this.springBeans;
   }

   public void setSpringBeans(List<SpringBean> var1) {
      this.springBeans = var1;
   }

   public void addSpringBean(SpringBean var1) {
      if (this.springBeans == null) {
         this.springBeans = new ArrayList<>();
      }

      this.springBeans.add(var1);
   }
}
