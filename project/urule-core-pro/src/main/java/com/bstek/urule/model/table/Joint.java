package com.bstek.urule.model.table;

import com.bstek.urule.model.rule.lhs.And;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.rule.lhs.Or;
import java.util.ArrayList;
import java.util.List;

public class Joint {
   private List<Condition> conditions;
   private List<Joint> joints;
   private JointType type;

   public Junction getJunction() {
      return this.type.equals(JointType.and) ? new And() : new Or();
   }

   public List<Condition> getConditions() {
      return this.conditions;
   }

   public void setConditions(List<Condition> conditions) {
      this.conditions = conditions;
   }

   public void addJoint(Joint joint) {
      if (this.joints == null) {
         this.joints = new ArrayList<>();
      }

      this.joints.add(joint);
   }

   public void addCondition(Condition condition) {
      if (this.conditions == null) {
         this.conditions = new ArrayList<>();
      }

      this.conditions.add(condition);
   }

   public List<Joint> getJoints() {
      return this.joints;
   }

   public void setJoints(List<Joint> joints) {
      this.joints = joints;
   }

   public JointType getType() {
      return this.type;
   }

   public void setType(JointType type) {
      this.type = type;
   }
}
