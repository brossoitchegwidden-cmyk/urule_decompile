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

   public CriteriaNode(Criteria criteria, int id, boolean debug) {
      super(id);
      this.criteria = criteria;
      this.setCriteriaInfo(criteria.getId());
      this.debug = debug;
   }

   @Override
   public NodeType getNodeType() {
      return this.nodeType;
   }

   public Criteria getCriteria() {
      return this.criteria;
   }

   public void setCriteria(Criteria criteria) {
      this.criteria = criteria;
   }

   @Override
   public String getCriteriaInfo() {
      return this.criteriaInfo;
   }

   public void setCriteriaInfo(String criteriaInfo) {
      this.criteriaInfo = criteriaInfo;
   }

   public boolean isDebug() {
      return this.debug;
   }

   public void setDebug(boolean debug) {
      this.debug = debug;
   }

   @Override
   public Activity newActivity(Map<Object, Object> context) {
      if (context.containsKey(this)) {
         return (CriteriaActivity)context.get(this);
      }

      CriteriaActivity criteriaActivity = new CriteriaActivity(this.criteria, this.debug);

      for (Line line : this.lines) {
         criteriaActivity.addPath(line.newPath(context));
      }

      context.put(this, criteriaActivity);
      return criteriaActivity;
   }
}
