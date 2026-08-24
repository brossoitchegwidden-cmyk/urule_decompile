package com.bstek.urule.model.rete;

import com.bstek.urule.runtime.rete.Activity;
import com.bstek.urule.runtime.rete.AndActivity;
import java.util.Map;

public class AndNode extends JunctionNode {
   private NodeType nodeType = NodeType.and;

   public AndNode() {
      super(0);
   }

   public AndNode(int id) {
      super(id);
   }

   @Override
   public NodeType getNodeType() {
      return this.nodeType;
   }

   public void setToLineCount(int toLineCount) {
      this.toLineCount = toLineCount;
   }

   @Override
   public Activity newActivity(Map<Object, Object> context) {
      if (context.containsKey(this)) {
         return (AndActivity)context.get(this);
      }

      AndActivity andActivity = new AndActivity();

      for (Line line : this.lines) {
         andActivity.addPath(line.newPath(context));
      }

      context.put(this, andActivity);
      return andActivity;
   }
}
