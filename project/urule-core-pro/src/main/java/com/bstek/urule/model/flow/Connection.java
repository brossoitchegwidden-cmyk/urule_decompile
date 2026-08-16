package com.bstek.urule.model.flow;

import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.FlowInstance;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.KnowledgeSessionFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Connection {
   public static final String RETURN_VALUE_KEY = "return_value__";
   private String name;
   private String toName;
   private String script;
   private String g;
   private KnowledgePackageWrapper knowledgePackageWrapper;
   @JsonIgnore
   private FlowNode to;

   public boolean evaluate(FlowContext var1) {
      if (this.knowledgePackageWrapper == null) {
         return true;
      }

      KnowledgeSession var2 = (KnowledgeSession)var1.getWorkingMemory();
      KnowledgeSession var3 = KnowledgeSessionFactory.newKnowledgeSession(this.knowledgePackageWrapper, var1, var2);
      var3.fireRules(var1.getVariables());
      var1.addRuleData(var3.getLogManager().getRuleData());
      Object var4 = var3.getParameter("return_value__");
      return var4 == null ? false : Boolean.valueOf(var4.toString());
   }

   public void buildDeserialize() {
      if (this.knowledgePackageWrapper != null) {
         this.knowledgePackageWrapper.buildDeserialize();
      }
   }

   public void execute(Exception var1, FlowContext var2, FlowInstance var3) {
      this.to.enter(var1, var2, var3);
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getToName() {
      return this.toName;
   }

   public void setToName(String var1) {
      this.toName = var1;
   }

   public FlowNode getTo() {
      return this.to;
   }

   public void setTo(FlowNode var1) {
      this.to = var1;
   }

   public String getScript() {
      return this.script;
   }

   public void setScript(String var1) {
      this.script = var1;
   }

   public KnowledgePackageWrapper getKnowledgePackageWrapper() {
      return this.knowledgePackageWrapper;
   }

   public void setKnowledgePackageWrapper(KnowledgePackageWrapper var1) {
      this.knowledgePackageWrapper = var1;
   }

   public String getG() {
      return this.g;
   }

   public void setG(String var1) {
      this.g = var1;
   }
}
