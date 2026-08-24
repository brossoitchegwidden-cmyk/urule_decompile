package com.bstek.urule.model.flow;

import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.ProcessInstance;

public interface FlowAction {
   void execute(ActionNode node, FlowContext context, ProcessInstance instance);
}
