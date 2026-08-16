package com.bstek.urule.model.rete;

import com.bstek.urule.runtime.rete.Activity;
import com.bstek.urule.runtime.rete.OrActivity;
import java.util.Map;

public class OrNode extends JunctionNode {
   private NodeType nodeType = NodeType.or;

   public OrNode() {
      super(0);
   }

   public OrNode(int var1) {
      super(var1);
   }

   @Override
   public NodeType getNodeType() {
      return this.nodeType;
   }

   @Override
   public Activity newActivity(Map<Object, Object> var1) {
      if (var1.containsKey(this)) {
         return (OrActivity)var1.get(this);
      }

      OrActivity var2 = new OrActivity();

      for (Line var4 : this.lines) {
         var2.addPath(var4.newPath(var1));
      }

      var1.put(this, var2);
      return var2;
   }
}
