package com.bstek.urule.model.flow.ins;

import com.bstek.urule.model.flow.FlowNode;
import com.bstek.urule.model.flow.ProcessDefinition;

public interface ProcessInstance {
   String getId();

   ProcessDefinition getProcessDefinition();

   FlowNode getCurrentNode();

   ProcessInstance getParent();
}
