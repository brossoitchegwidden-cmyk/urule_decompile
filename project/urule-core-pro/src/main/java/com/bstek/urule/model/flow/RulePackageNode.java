package com.bstek.urule.model.flow;

import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.FlowInstance;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class RulePackageNode extends BindingNode {
   private FlowNodeType type = FlowNodeType.RulePackage;
   private String packageId;
   private String code;
   private String project;

   public RulePackageNode() {
   }

   public RulePackageNode(String name) {
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

   @JsonIgnore
   @Override
   public KnowledgePackageWrapper getKnowledgePackageWrapper() {
      return super.getKnowledgePackageWrapper();
   }

   public String getProject() {
      return this.project;
   }

   public void setProject(String project) {
      this.project = project;
   }

   public String getPackageId() {
      return this.packageId;
   }

   public void setPackageId(String packageId) {
      this.packageId = packageId;
   }

   public String getCode() {
      return this.code == null ? this.packageId : this.code;
   }

   public void setCode(String code) {
      this.code = code;
   }
}
