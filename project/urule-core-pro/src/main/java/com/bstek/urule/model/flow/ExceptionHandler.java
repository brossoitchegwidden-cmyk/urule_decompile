package com.bstek.urule.model.flow;

import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.ProcessInstance;

public interface ExceptionHandler {
   void handle(Exception var1, FlowContext var2, ProcessInstance var3);
}
