package com.bstek.urule.model.library.action;

import java.util.ArrayList;
import java.util.List;

public class ActionLibrary {
   private List<SpringBean> springBeans;

   public List<SpringBean> getSpringBeans() {
      return this.springBeans;
   }

   public void setSpringBeans(List<SpringBean> springBeans) {
      this.springBeans = springBeans;
   }

   public void addSpringBean(SpringBean springBean) {
      if (this.springBeans == null) {
         this.springBeans = new ArrayList<>();
      }

      this.springBeans.add(springBean);
   }
}
