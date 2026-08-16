package com.bstek.urule.model.crosstab;

import com.bstek.urule.model.table.Joint;

public class ConditionCrossCell extends CrossCell {
   private Joint joint;

   @Override
   public String getType() {
      return "condition";
   }

   public Joint getJoint() {
      return this.joint;
   }

   public void setJoint(Joint var1) {
      this.joint = var1;
   }
}
