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

   public ExceptionNode(String name, String exception) {
      super(name);
      this.exception = exception;
   }

   @Override
   public void enterNode(Exception ex, FlowContext context, FlowInstance instance) {
      if (ex != null) {
         Class targetException = this.buildTargetException();
         Class actualExceptionClass = ex.getClass();
         if (targetException.isAssignableFrom(actualExceptionClass)) {
            if (instance.isDebug()) {
               context.getLogger().logExceptionNode(this, ex, instance.getProcessDefinition().getFile());
            }

            instance.setCurrentNode(this);
            this.executeNodeEvent(EventType.enter, context, instance);
            if (!StringUtils.isEmpty(this.exceptionBean)) {
               ExceptionHandler exceptionHandler = (ExceptionHandler)context.getApplicationContext().getBean(this.exceptionBean);
               exceptionHandler.handle(ex, context, instance);
            }

            this.executeNodeEvent(EventType.leave, context, instance);
            this.leave(null, context, instance, null);
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
      } catch (Exception exception) {
         throw new RuleException(exception);
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

   public void setException(String exception) {
      this.exception = exception;
   }

   public String getExceptionBean() {
      return this.exceptionBean;
   }

   public void setExceptionBean(String exceptionBean) {
      this.exceptionBean = exceptionBean;
   }
}
