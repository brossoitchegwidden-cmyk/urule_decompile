package com.bstek.urule.model.rete;

import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.runtime.rete.Activity;
import com.bstek.urule.runtime.rete.MetActivity;
import java.util.List;
import java.util.Map;

public class MetNode extends JunctionNode {
   private int met;
   private boolean only;
   private boolean debug;
   private List<Criterion> criterions;

   public MetNode() {
      super(0);
   }

   public MetNode(int id, boolean debug) {
      super(id);
      this.debug = debug;
   }

   @Override
   public NodeType getNodeType() {
      return NodeType.met;
   }

   @Override
   public Activity newActivity(Map<Object, Object> context) {
      if (context.containsKey(this)) {
         return (MetActivity)context.get(this);
      }

      MetActivity metActivity = new MetActivity(this.met, this.criterions, this.debug, this.only);

      for (Line line : this.lines) {
         metActivity.addPath(line.newPath(context));
      }

      context.put(this, metActivity);
      return metActivity;
   }

   public void setMet(int met) {
      this.met = met;
   }

   public int getMet() {
      return this.met;
   }

   public boolean isOnly() {
      return this.only;
   }

   public void setOnly(boolean only) {
      this.only = only;
   }

   public void setCriterions(List<Criterion> criterions) {
      this.criterions = criterions;
   }

   public List<Criterion> getCriterions() {
      return this.criterions;
   }

   public boolean isDebug() {
      return this.debug;
   }

   public void setDebug(boolean debug) {
      this.debug = debug;
   }
}
