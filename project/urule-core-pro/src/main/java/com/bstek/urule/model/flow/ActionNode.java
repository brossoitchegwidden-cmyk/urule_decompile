package com.bstek.urule.model.flow;

import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.FlowInstance;

public class ActionNode extends FlowNode {
   private String actionBean;
   private FlowNodeType type = FlowNodeType.Action;

   public ActionNode() {
   }

   public ActionNode(String name) {
      super(name);
   }

   @Override
   public void enterNode(Exception ex, FlowContext context, FlowInstance instance) {
      Exception exception2 = null;
      instance.setCurrentNode(this);

      try {
         this.executeNodeEvent(EventType.enter, context, instance);
         FlowAction flowAction = (FlowAction)context.getApplicationContext().getBean(this.actionBean);
         flowAction.execute(this, context, instance);
         this.executeNodeEvent(EventType.leave, context, instance);
      } catch (Exception exception) {
         exception2 = exception;
      } finally {
         this.leave(null, context, instance, exception2);
      }
   }

   @Override
   public FlowNodeType getType() {
      return this.type;
   }

   public String getActionBean() {
      return this.actionBean;
   }

   public void setActionBean(String actionBean) {
      this.actionBean = actionBean;
   }
}
