package com.bstek.urule.model.flow;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.FlowInstance;
import org.springframework.util.StringUtils;

public class ExceptionNode extends FlowNode {
   private String exception;
   private String exceptionBean;
   private Class<?> targetException;

   public ExceptionNode() {
   }

   public ExceptionNode(String var1, String var2) {
      super(var1);
      this.exception = var2;
   }

   @Override
   public void enterNode(Exception var1, FlowContext var2, FlowInstance var3) {
      if (var1 != null) {
         Class var4 = this.buildTargetException();
         Class var5 = var1.getClass();
         if (var4.isAssignableFrom(var5)) {
            if (var3.isDebug()) {
               var2.getLogger().logExceptionNode(this, var1, var3.getProcessDefinition().getFile());
            }

            var3.setCurrentNode(this);
            this.executeNodeEvent(EventType.enter, var2, var3);
            if (!StringUtils.isEmpty(this.exceptionBean)) {
               ExceptionHandler var6 = (ExceptionHandler)var2.getApplicationContext().getBean(this.exceptionBean);
               var6.handle(var1, var2, var3);
            }

            this.executeNodeEvent(EventType.leave, var2, var3);
            this.leave(null, var2, var3, null);
         }
      }
   }

   private synchronized Class<?> buildTargetException() {
      if (this.targetException != null) {
         return this.targetException;
      }

      if (StringUtils.isEmpty(this.exception)) {
         throw new RuleException("决策流节点【" + this.name + "】上未指定具体的异常类路径");
      }

      try {
         this.targetException = Class.forName(this.exception);
      } catch (Exception var2) {
         throw new RuleException(var2);
      }

      return this.targetException;
   }

   @Override
   public FlowNodeType getType() {
      return FlowNodeType.Exception;
   }

   public String getException() {
      return this.exception;
   }

   public void setException(String var1) {
      this.exception = var1;
   }

   public String getExceptionBean() {
      return this.exceptionBean;
   }

   public void setExceptionBean(String var1) {
      this.exceptionBean = var1;
   }
}
