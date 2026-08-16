package com.bstek.urule.model.flow;

import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.FlowInstance;
import java.util.List;

public class RuleNode extends BindingNode {
   private FlowNodeType type = FlowNodeType.Rule;
   private List<BindingFile> files;

   public RuleNode() {
   }

   public RuleNode(String var1) {
      super(var1);
   }

   @Override
   public FlowNodeType getType() {
      return this.type;
   }

   @Override
   public void enterNode(Exception var1, FlowContext var2, FlowInstance var3) {
      Exception var4 = null;

      try {
         var3.setCurrentNode(this);
         this.executeNodeEvent(EventType.enter, var2, var3);
         this.executeKnowledgePackage(var2, var3);
         this.executeNodeEvent(EventType.leave, var2, var3);
      } catch (Exception var9) {
         var4 = var9;
      } finally {
         this.leave(null, var2, var3, var4);
      }
   }

   public List<BindingFile> getFiles() {
      return this.files;
   }

   public void setFiles(List<BindingFile> var1) {
      this.files = var1;
   }
}
