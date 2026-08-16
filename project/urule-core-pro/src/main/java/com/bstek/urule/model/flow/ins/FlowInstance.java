package com.bstek.urule.model.flow.ins;

import com.bstek.urule.model.flow.FlowNode;
import com.bstek.urule.model.flow.ProcessDefinition;
import java.util.UUID;

public class FlowInstance implements ProcessInstance {
   private String id;
   private ProcessDefinition flowDefinition;
   private FlowInstance parent;
   private FlowNode currentNode;
   private boolean debug;

   public FlowInstance(ProcessDefinition var1, boolean var2) {
      this.flowDefinition = var1;
      this.id = UUID.randomUUID().toString();
      this.debug = var2;
   }

   public boolean isDebug() {
      return this.debug;
   }

   @Override
   public ProcessDefinition getProcessDefinition() {
      return this.flowDefinition;
   }

   @Override
   public String getId() {
      return this.id;
   }

   @Override
   public FlowNode getCurrentNode() {
      return this.currentNode;
   }

   public void setCurrentNode(FlowNode var1) {
      this.currentNode = var1;
   }

   public void setParent(FlowInstance var1) {
      this.parent = var1;
   }

   public FlowInstance getParent() {
      return this.parent;
   }

   public FlowInstance newChildInstance() {
      FlowInstance var1 = new FlowInstance(this.flowDefinition, this.debug);
      var1.setParent(this);
      return var1;
   }
}
