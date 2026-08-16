package com.bstek.urule.model.flow;

import com.bstek.urule.Utils;
import com.bstek.urule.action.VariableAssignAction;
import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.FlowInstance;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.Rhs;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.RuleSet;
import com.bstek.urule.model.rule.SimpleValue;
import com.bstek.urule.model.rule.lhs.LeftType;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.percent.PercentDataStore;
import com.bstek.urule.runtime.percent.PercentUnit;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DecisionNode extends BindingNode {
   private List<DecisionItem> items;
   private FlowNodeType type = FlowNodeType.Decision;
   private DecisionType decisionType = DecisionType.Criteria;
   private PercentScope percentScope;

   public DecisionNode() {
   }

   public DecisionNode(String var1) {
      super(var1);
   }

   @Override
   public void enterNode(Exception var1, FlowContext var2, FlowInstance var3) {
      var3.setCurrentNode(this);
      if (this.decisionType.equals(DecisionType.Criteria)) {
         this.doCriteria(var2, var3);
      } else {
         this.doPercent(var2, var3);
      }

      this.executeNodeEvent(EventType.enter, var2, var3);
   }

   private void doPercent(FlowContext var1, FlowInstance var2) {
      if (this.percentScope != null && !this.percentScope.equals(PercentScope.batch)) {
         this.doGlobalScope(var1, var2);
      } else {
         this.doBatchScope(var1, var2);
      }
   }

   private void doBatchScope(FlowContext var1, FlowInstance var2) {
      Exception var3 = null;
      PercentItem var4 = null;

      try {
         String var5 = var2.getProcessDefinition().getId() + "_" + this.name;
         long var6 = this.getAmount(var5, var1) + 1L;
         ArrayList var8 = new ArrayList();

         for (DecisionItem var10 : this.items) {
            PercentItem var11 = new PercentItem();
            var11.setName(var10.getTo());
            var11.setPercent(var10.getPercent());
            String var12 = var5 + "." + var10.getTo();
            long var13 = this.getAmount(var12, var1);
            var11.setTotal(var13);
            var8.add(var11);
         }

         var4 = this.computePercent(var8, var6);
         this.setAmount(var5, var6, var1);
         this.setAmount(var5 + "." + var4.getName(), var4.getTotal() + 1L, var1);
         this.executeNodeEvent(EventType.leave, var1, var2);
      } catch (Exception var18) {
         var3 = var18;
      } finally {
         if (!Objects.isNull(var4)) {
            this.leave(var4.getName(), var1, var2, var3);
         }
      }
   }

   private void doGlobalScope(FlowContext var1, FlowInstance var2) {
      Exception var3 = null;
      PercentItem var4 = null;

      try {
         PercentDataStore var5 = (PercentDataStore)Utils.getApplicationContext().getBean("urule.percentDataStore");
         PercentUnit var6 = var5.getDecisionNodePercent(var2.getProcessDefinition(), this.items, this.name);
         ArrayList var7 = new ArrayList();

         for (DecisionItem var9 : this.items) {
            PercentItem var10 = new PercentItem();
            var10.setName(var9.getTo());
            var10.setPercent(var9.getPercent());
            long var11 = var6.getBranch(var9).getTotal();
            var10.setTotal(var11);
            var10.setItem(var9);
            var7.add(var10);
         }

         long var18 = var6.getTotal() + 1L;
         var4 = this.computePercent(var7, var18);
         var6.setTotal(var18);
         Branch var19 = var6.getBranch(var4.getItem());
         var19.setTotal(var4.getTotal() + 1L);
         this.executeNodeEvent(EventType.leave, var1, var2);
      } catch (Exception var16) {
         var3 = var16;
      } finally {
         if (!Objects.isNull(var4)) {
            this.leave(var4.getName(), var1, var2, var3);
         }
      }
   }

   private long getAmount(String var1, FlowContext var2) {
      Object var3 = var2.getWorkingMemory().getSessionValue(var1);
      return var3 == null ? 0L : (Long)var3;
   }

   private void setAmount(String var1, long var2, FlowContext var4) {
      var4.getWorkingMemory().setSessionValue(var1, var2);
   }

   private void doCriteria(FlowContext var1, FlowInstance var2) {
      Object var3 = null;
      Exception var4 = null;

      try {
         KnowledgeSession var5 = this.executeKnowledgePackage(var1, var2);
         this.executeNodeEvent(EventType.leave, var1, var2);
         var3 = var5.getParameter("return_to__");
         String var6 = var2.getProcessDefinition().getFile();
         if (var3 != null) {
            var1.getLogger().logDecisionNodeMatch(this, var6, var3.toString());
            var5.getParameters().remove("return_to__");
            return;
         }

         var1.getLogger().logDecisionNodeMatch(this, var6, null);
      } catch (Exception var10) {
         var4 = var10;
         return;
      } finally {
         this.leave(var3 != null ? var3.toString() : null, var1, var2, var4);
      }
   }

   private PercentItem computePercent(List<PercentItem> var1, long var2) {
      BigDecimal var4 = new BigDecimal(var2);

      for (PercentItem var6 : var1) {
         long var7 = var6.getTotal();
         BigDecimal var9 = new BigDecimal(var7);
         BigDecimal var10 = var9.divide(var4, 20, 6);
         BigDecimal var11 = new BigDecimal(var6.getPercent());
         var11 = var11.divide(new BigDecimal(100), 2, 6);
         int var12 = var10.compareTo(var11);
         if (var12 == -1) {
            return var6;
         }
      }

      return (PercentItem)var1.get(0);
   }

   @Override
   public FlowNodeType getType() {
      return this.type;
   }

   public List<DecisionItem> getItems() {
      return this.items;
   }

   public void setItems(List<DecisionItem> var1) {
      this.items = var1;
   }

   public RuleSet buildRuleSet(List<Library> var1, FlowDefinition var2) {
      RuleSet var3 = new RuleSet();
      var3.setLibraries(var1);
      ArrayList var4 = new ArrayList();
      var3.setRules(var4);
      int var5 = 0;

      for (DecisionItem var7 : this.items) {
         var5++;
         if (var7.getConditionType() != null && !var7.getConditionType().equals("script")) {
            Rule var8 = new Rule();
            var4.add(var8);
            var8.setFile(var2.getFile());
            var8.setLhs(var7.getLhs());
            var8.setDebug(var2.isDebug());
            var8.setName("决策节点[" + this.getName() + "]-" + var5);
            Rhs var9 = new Rhs();
            var8.setRhs(var9);
            ArrayList var10 = new ArrayList();
            var9.setActions(var10);
            VariableAssignAction var11 = new VariableAssignAction();
            var10.add(var11);
            var11.setDatatype(Datatype.String);
            var11.setDebug(var2.isDebug());
            var11.setCategoryUuid("参数");
            var11.setVariableCategory("parameter");
            var11.setVariableLabel("return_to__");
            var11.setVariableName("return_to__");
            var11.setType(LeftType.variable);
            SimpleValue var12 = new SimpleValue();
            var12.setContent(var7.getTo());
            var11.setValue(var12);
         }
      }

      return var3;
   }

   public PercentScope getPercentScope() {
      return this.percentScope;
   }

   public void setPercentScope(PercentScope var1) {
      this.percentScope = var1;
   }

   public DecisionType getDecisionType() {
      return this.decisionType;
   }

   public void setDecisionType(DecisionType var1) {
      this.decisionType = var1;
   }
}
