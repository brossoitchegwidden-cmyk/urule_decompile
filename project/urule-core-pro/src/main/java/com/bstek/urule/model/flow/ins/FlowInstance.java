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

   public FlowInstance(ProcessDefinition flowDefinition, boolean debug) {
      this.flowDefinition = flowDefinition;
      this.id = UUID.randomUUID().toString();
      this.debug = debug;
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

   public void setCurrentNode(FlowNode currentNode) {
      this.currentNode = currentNode;
   }

   public void setParent(FlowInstance parent) {
      this.parent = parent;
   }

   public FlowInstance getParent() {
      return this.parent;
   }

   public FlowInstance newChildInstance() {
      FlowInstance flowInstance = new FlowInstance(this.flowDefinition, this.debug);
      flowInstance.setParent(this);
      return flowInstance;
   }
}
