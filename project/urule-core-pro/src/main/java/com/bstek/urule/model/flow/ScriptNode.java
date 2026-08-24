package com.bstek.urule.model.flow;

import com.bstek.urule.Utils;
import com.bstek.urule.action.Action;
import com.bstek.urule.action.ExecuteMethodAction;
import com.bstek.urule.action.InvokeFile;
import com.bstek.urule.builder.KnowledgeBase;
import com.bstek.urule.builder.KnowledgeBuilder;
import com.bstek.urule.builder.ResourceBase;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.FlowInstance;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.Rhs;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.RuleSet;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScriptNode extends BindingNode {
   private String actionXml;
   private List<Action> actionsData = new ArrayList<>();
   private FlowNodeType type = FlowNodeType.Script;

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

   @Override
   public FlowNodeType getType() {
      return this.type;
   }

   public RuleSet buildRuleSet(List<Library> libraries, FlowDefinition fd) {
      RuleSet ruleSet = new RuleSet();
      Rule rule = new Rule();
      rule.setDebug(fd.isDebug());
      rule.setFile(fd.getFile());
      rule.setName("脚本节点[" + this.name + "]规则");
      Rhs rhs = new Rhs();
      rule.setRhs(rhs);
      this.buildActions();
      rhs.setActions(this.actionsData);
      if (libraries != null) {
         for (Library library : libraries) {
            ruleSet.addLibrary(library);
         }
      }

      ArrayList items = new ArrayList();
      items.add(rule);
      ruleSet.setRules(items);
      return ruleSet;
   }

   private void buildActions() {
      for (Action action : this.actionsData) {
         if (action instanceof ExecuteMethodAction) {
            ExecuteMethodAction executeMethodAction = (ExecuteMethodAction)action;
            InvokeFile invokeFile = executeMethodAction.getInvokeFile();
            if (invokeFile != null) {
               KnowledgeBuilder knowledgeBuilder = (KnowledgeBuilder)Utils.getApplicationContext().getBean("urule.knowledgeBuilder");
               ResourceBase resourceBase = knowledgeBuilder.newResourceBase();
               resourceBase.addResource(invokeFile.getId(), invokeFile.getVersion());

               try {
                  KnowledgeBase knowledgeBase = knowledgeBuilder.buildKnowledgeBase(resourceBase);
                  KnowledgePackage knowledgePackage = knowledgeBase.getKnowledgePackage();
                  invokeFile.setKnowledgePackageWrapper(new KnowledgePackageWrapper(knowledgePackage));
               } catch (IOException iOException) {
                  throw new RuleException(iOException);
               }
            }
         }
      }
   }

   public String getActionXml() {
      return this.actionXml;
   }

   public void setActionXml(String actionXml) {
      this.actionXml = actionXml;
   }

   public List<Action> getActionsData() {
      return this.actionsData;
   }

   public void setActionsData(List<Action> actions) {
      this.actionsData = actions;
   }
}
