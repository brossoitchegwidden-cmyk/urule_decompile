package com.bstek.urule.model.flow;

import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.ProcessInstance;

public class DefaultExceptionHandler implements ExceptionHandler {
   @Override
   public void handle(Exception var1, FlowContext var2, ProcessInstance var3) {
      var1.printStackTrace();
   }
}
