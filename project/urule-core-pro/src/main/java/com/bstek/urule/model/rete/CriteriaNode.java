package com.bstek.urule.model.rete;

import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.runtime.rete.Activity;
import com.bstek.urule.runtime.rete.CriteriaActivity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Map;

public class CriteriaNode extends BaseReteNode implements ConditionNode {
   @JsonIgnore
   private String criteriaInfo;
   private boolean debug;
   private Criteria criteria;
   private NodeType nodeType = NodeType.criteria;

   public CriteriaNode() {
      super(0);
   }

   public CriteriaNode(Criteria var1, int var2, boolean var3) {
      super(var2);
      this.criteria = var1;
      this.setCriteriaInfo(var1.getId());
      this.debug = var3;
   }

   @Override
   public NodeType getNodeType() {
      return this.nodeType;
   }

   public Criteria getCriteria() {
      return this.criteria;
   }

   public void setCriteria(Criteria var1) {
      this.criteria = var1;
   }

   @Override
   public String getCriteriaInfo() {
      return this.criteriaInfo;
   }

   public void setCriteriaInfo(String var1) {
      this.criteriaInfo = var1;
   }

   public boolean isDebug() {
      return this.debug;
   }

   public void setDebug(boolean var1) {
      this.debug = var1;
   }

   @Override
   public Activity newActivity(Map<Object, Object> var1) {
      if (var1.containsKey(this)) {
         return (CriteriaActivity)var1.get(this);
      }

      CriteriaActivity var2 = new CriteriaActivity(this.criteria, this.debug);

      for (Line var4 : this.lines) {
         var2.addPath(var4.newPath(var1));
      }

      var1.put(this, var2);
      return var2;
   }
}
