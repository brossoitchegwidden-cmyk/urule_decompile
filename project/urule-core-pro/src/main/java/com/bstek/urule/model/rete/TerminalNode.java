package com.bstek.urule.model.rete;

import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.runtime.rete.Activity;
import com.bstek.urule.runtime.rete.Context;
import com.bstek.urule.runtime.rete.TerminalActivity;
import java.util.Map;

public class TerminalNode extends ReteNode {
   private Rule rule;
   private NodeType nodeType = NodeType.terminal;

   public TerminalNode() {
      super(0);
   }

   public TerminalNode(Rule var1, int var2) {
      super(var2);
      this.rule = var1;
   }

   @Override
   public NodeType getNodeType() {
      return this.nodeType;
   }

   public Rule[] enter(Context var1, Object var2) {
      return new Rule[]{this.rule};
   }

   public Rule getRule() {
      return this.rule;
   }

   public void setRule(Rule var1) {
      this.rule = var1;
   }

   @Override
   public Activity newActivity(Map<Object, Object> var1) {
      if (var1.containsKey(this)) {
         return (TerminalActivity)var1.get(this);
      }

      TerminalActivity var2 = new TerminalActivity(this.rule);
      var1.put(this, var2);
      return var2;
   }
}
