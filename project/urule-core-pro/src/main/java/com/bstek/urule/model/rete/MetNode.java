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

   public MetNode(int var1, boolean var2) {
      super(var1);
      this.debug = var2;
   }

   @Override
   public NodeType getNodeType() {
      return NodeType.met;
   }

   @Override
   public Activity newActivity(Map<Object, Object> var1) {
      if (var1.containsKey(this)) {
         return (MetActivity)var1.get(this);
      }

      MetActivity var2 = new MetActivity(this.met, this.criterions, this.debug, this.only);

      for (Line var4 : this.lines) {
         var2.addPath(var4.newPath(var1));
      }

      var1.put(this, var2);
      return var2;
   }

   public void setMet(int var1) {
      this.met = var1;
   }

   public int getMet() {
      return this.met;
   }

   public boolean isOnly() {
      return this.only;
   }

   public void setOnly(boolean var1) {
      this.only = var1;
   }

   public void setCriterions(List<Criterion> var1) {
      this.criterions = var1;
   }

   public List<Criterion> getCriterions() {
      return this.criterions;
   }

   public boolean isDebug() {
      return this.debug;
   }

   public void setDebug(boolean var1) {
      this.debug = var1;
   }
}
