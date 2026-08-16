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

   @Override
   public FlowNodeType getType() {
      return this.type;
   }

   public RuleSet buildRuleSet(List<Library> var1, FlowDefinition var2) {
      RuleSet var3 = new RuleSet();
      Rule var4 = new Rule();
      var4.setDebug(var2.isDebug());
      var4.setFile(var2.getFile());
      var4.setName("脚本节点[" + this.name + "]规则");
      Rhs var5 = new Rhs();
      var4.setRhs(var5);
      this.buildActions();
      var5.setActions(this.actionsData);
      if (var1 != null) {
         for (Library var7 : var1) {
            var3.addLibrary(var7);
         }
      }

      ArrayList var8 = new ArrayList();
      var8.add(var4);
      var3.setRules(var8);
      return var3;
   }

   private void buildActions() {
      for (Action var2 : this.actionsData) {
         if (var2 instanceof ExecuteMethodAction) {
            ExecuteMethodAction var3 = (ExecuteMethodAction)var2;
            InvokeFile var4 = var3.getInvokeFile();
            if (var4 != null) {
               KnowledgeBuilder var5 = (KnowledgeBuilder)Utils.getApplicationContext().getBean("urule.knowledgeBuilder");
               ResourceBase var6 = var5.newResourceBase();
               var6.addResource(var4.getId(), var4.getVersion());

               try {
                  KnowledgeBase var7 = var5.buildKnowledgeBase(var6);
                  KnowledgePackage var8 = var7.getKnowledgePackage();
                  var4.setKnowledgePackageWrapper(new KnowledgePackageWrapper(var8));
               } catch (IOException var9) {
                  throw new RuleException(var9);
               }
            }
         }
      }
   }

   public String getActionXml() {
      return this.actionXml;
   }

   public void setActionXml(String var1) {
      this.actionXml = var1;
   }

   public List<Action> getActionsData() {
      return this.actionsData;
   }

   public void setActionsData(List<Action> var1) {
      this.actionsData = var1;
   }
}
