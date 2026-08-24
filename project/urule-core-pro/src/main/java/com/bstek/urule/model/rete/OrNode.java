package com.bstek.urule.model.rete;

import com.bstek.urule.runtime.rete.Activity;
import com.bstek.urule.runtime.rete.OrActivity;
import java.util.Map;

public class OrNode extends JunctionNode {
   private NodeType nodeType = NodeType.or;

   public OrNode() {
      super(0);
   }

   public OrNode(int id) {
      super(id);
   }

   @Override
   public NodeType getNodeType() {
      return this.nodeType;
   }

   @Override
   public Activity newActivity(Map<Object, Object> context) {
      if (context.containsKey(this)) {
         return (OrActivity)context.get(this);
      }

      OrActivity orActivity = new OrActivity();

      for (Line line : this.lines) {
         orActivity.addPath(line.newPath(context));
      }

      context.put(this, orActivity);
      return orActivity;
   }
}
