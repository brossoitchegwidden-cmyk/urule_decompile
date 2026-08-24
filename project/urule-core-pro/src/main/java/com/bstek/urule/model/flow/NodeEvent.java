package com.bstek.urule.model.flow;

import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.ProcessInstance;

public interface NodeEvent {
   /**规则流流入当前节点触发的方法*/
   void enter(FlowNode node, ProcessInstance instance, FlowContext context);

   /**规则流流出当前节点触发的方法*/
   void leave(FlowNode node, ProcessInstance instance, FlowContext context);
}
