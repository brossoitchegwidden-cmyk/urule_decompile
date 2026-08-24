package com.bstek.urule.model.template;

import com.bstek.urule.model.rule.lhs.Criterion;

public class ConditionTemplateUnit {
   private String id;
   private String name;
   private String path;
   private Criterion criterion;

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getPath() {
      return this.path;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public Criterion getCriterion() {
      return this.criterion;
   }

   public void setCriterion(Criterion criterion) {
      this.criterion = criterion;
   }
}
