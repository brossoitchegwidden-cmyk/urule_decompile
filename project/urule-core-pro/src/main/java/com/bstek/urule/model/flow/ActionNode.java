package com.bstek.urule.model.flow;

import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.FlowInstance;

public class ActionNode extends FlowNode {
   private String actionBean;
   private FlowNodeType type = FlowNodeType.Action;

   public ActionNode() {
   }

   public ActionNode(String var1) {
      super(var1);
   }

   @Override
   public void enterNode(Exception var1, FlowContext var2, FlowInstance var3) {
      Exception var4 = null;
      var3.setCurrentNode(this);

      try {
         this.executeNodeEvent(EventType.enter, var2, var3);
         FlowAction var5 = (FlowAction)var2.getApplicationContext().getBean(this.actionBean);
         var5.execute(this, var2, var3);
         this.executeNodeEvent(EventType.leave, var2, var3);
      } catch (Exception var9) {
         var4 = var9;
      } finally {
         this.leave(null, var2, var3, var4);
      }
   }

   @Override
   public FlowNodeType getType() {
      return this.type;
   }

   public String getActionBean() {
      return this.actionBean;
   }

   public void setActionBean(String var1) {
      this.actionBean = var1;
   }
}
