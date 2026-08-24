package com.bstek.urule.model.flow;

import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.ProcessInstance;

/**用户添加异常捕获节点，默认为这个Handler，它将直接向控制台打印异常堆栈*/
public class DefaultExceptionHandler implements ExceptionHandler {
   @Override
   public void handle(Exception ex, FlowContext context, ProcessInstance instance) {
      java.util.logging.Logger.getLogger(DefaultExceptionHandler.class.getName()).log(java.util.logging.Level.SEVERE, ex.getMessage(), ex);
   }
}
