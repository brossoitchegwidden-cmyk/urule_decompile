package com.bstek.urule.model.flow;

import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.FlowInstance;
import java.util.List;

public class RuleNode extends BindingNode {
   private FlowNodeType type = FlowNodeType.Rule;
   private List<BindingFile> files;

   public RuleNode() {
   }

   public RuleNode(String name) {
      super(name);
   }

   @Override
   public FlowNodeType getType() {
      return this.type;
   }

   @Override
   public void enterNode(Exception ex, FlowContext context, FlowInstance instance) {
      Exception exception2 = null;

      try {
         instance.setCurrentNode(this);
         this.executeNodeEvent(EventType.enter, context, instance);
         this.executeKnowledgePackage(context, instance);
         this.executeNodeEvent(EventType.leave, context, instance);
      } catch (Exception exception) {
         exception2 = exception;
      } finally {
         this.leave(null, context, instance, exception2);
      }
   }

   public List<BindingFile> getFiles() {
      return this.files;
   }

   public void setFiles(List<BindingFile> files) {
      this.files = files;
   }
}
