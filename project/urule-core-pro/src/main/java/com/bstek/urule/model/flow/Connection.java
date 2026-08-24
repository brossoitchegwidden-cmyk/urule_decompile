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
   private String text;
   private KnowledgePackageWrapper knowledgePackageWrapper;
   @JsonIgnore
   private FlowNode to;

   public boolean evaluate(FlowContext context) {
      if (this.knowledgePackageWrapper == null) {
         return true;
      }

      KnowledgeSession workingMemory = (KnowledgeSession)context.getWorkingMemory();
      KnowledgeSession knowledgeSession = KnowledgeSessionFactory.newKnowledgeSession(this.knowledgePackageWrapper, context, workingMemory);
      knowledgeSession.fireRules(context.getVariables());
      context.addRuleData(knowledgeSession.getLogManager().getRuleData());
      Object parameter = knowledgeSession.getParameter("return_value__");
      return parameter == null ? false : Boolean.valueOf(parameter.toString());
   }

   public void buildDeserialize() {
      if (this.knowledgePackageWrapper != null) {
         this.knowledgePackageWrapper.buildDeserialize();
      }
   }

   public void execute(Exception ex, FlowContext context, FlowInstance instance) {
      this.to.enter(ex, context, instance);
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getToName() {
      return this.toName;
   }

   public void setToName(String toName) {
      this.toName = toName;
   }

   public FlowNode getTo() {
      return this.to;
   }

   public void setTo(FlowNode to) {
      this.to = to;
   }

   public String getScript() {
      return this.script;
   }

   public void setScript(String script) {
      this.script = script;
   }

   public KnowledgePackageWrapper getKnowledgePackageWrapper() {
      return this.knowledgePackageWrapper;
   }

   public void setKnowledgePackageWrapper(KnowledgePackageWrapper knowledgePackageWrapper) {
      this.knowledgePackageWrapper = knowledgePackageWrapper;
   }

   public String getG() {
      return this.text;
   }

   public void setG(String text) {
      this.text = text;
   }
}
