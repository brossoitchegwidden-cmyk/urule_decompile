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

   public RulePackageNode(String var1) {
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

   @JsonIgnore
   @Override
   public KnowledgePackageWrapper getKnowledgePackageWrapper() {
      return super.getKnowledgePackageWrapper();
   }

   public String getProject() {
      return this.project;
   }

   public void setProject(String var1) {
      this.project = var1;
   }

   public String getPackageId() {
      return this.packageId;
   }

   public void setPackageId(String var1) {
      this.packageId = var1;
   }

   public String getCode() {
      return this.code == null ? this.packageId : this.code;
   }

   public void setCode(String var1) {
      this.code = var1;
   }
}
