package com.bstek.urule.model.rete;

import com.bstek.urule.runtime.rete.Activity;
import com.bstek.urule.runtime.rete.AndActivity;
import java.util.Map;

public class AndNode extends JunctionNode {
   private NodeType nodeType = NodeType.and;

   public AndNode() {
      super(0);
   }

   public AndNode(int var1) {
      super(var1);
   }

   @Override
   public NodeType getNodeType() {
      return this.nodeType;
   }

   public void setToLineCount(int var1) {
      this.toLineCount = var1;
   }

   @Override
   public Activity newActivity(Map<Object, Object> var1) {
      if (var1.containsKey(this)) {
         return (AndActivity)var1.get(this);
      }

      AndActivity var2 = new AndActivity();

      for (Line var4 : this.lines) {
         var2.addPath(var4.newPath(var1));
      }

      var1.put(this, var2);
      return var2;
   }
}
