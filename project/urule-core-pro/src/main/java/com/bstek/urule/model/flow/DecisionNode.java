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
   private List<DecisionItem> decisionNodeItems;
   private FlowNodeType type = FlowNodeType.Decision;
   private DecisionType decisionType = DecisionType.Criteria;
   private PercentScope percentScope;

   public DecisionNode() {
   }

   public DecisionNode(String name) {
      super(name);
   }

   @Override
   public void enterNode(Exception ex, FlowContext context, FlowInstance instance) {
      instance.setCurrentNode(this);
      if (this.decisionType.equals(DecisionType.Criteria)) {
         this.doCriteria(context, instance);
      } else {
         this.doPercent(context, instance);
      }

      this.executeNodeEvent(EventType.enter, context, instance);
   }

   private void doPercent(FlowContext flowContext, FlowInstance flowInstance) {
      if (this.percentScope != null && !this.percentScope.equals(PercentScope.batch)) {
         this.doGlobalScope(flowContext, flowInstance);
      } else {
         this.doBatchScope(flowContext, flowInstance);
      }
   }

   private void doBatchScope(FlowContext flowContext, FlowInstance flowInstance) {
      Exception exception2 = null;
      PercentItem percentItem = null;

      try {
         String text = flowInstance.getProcessDefinition().getId() + "_" + this.name;
         long longValue = this.getAmount(text, flowContext) + 1L;
         ArrayList items = new ArrayList();

         for (DecisionItem decisionItem : this.decisionNodeItems) {
            PercentItem percentItem2 = new PercentItem();
            percentItem2.setName(decisionItem.getTo());
            percentItem2.setPercent(decisionItem.getPercent());
            String text2 = text + "." + decisionItem.getTo();
            long amount = this.getAmount(text2, flowContext);
            percentItem2.setTotal(amount);
            items.add(percentItem2);
         }

         percentItem = this.computePercent(items, longValue);
         this.setAmount(text, longValue, flowContext);
         this.setAmount(text + "." + percentItem.getName(), percentItem.getTotal() + 1L, flowContext);
         this.executeNodeEvent(EventType.leave, flowContext, flowInstance);
      } catch (Exception exception) {
         exception2 = exception;
      } finally {
         if (!Objects.isNull(percentItem)) {
            this.leave(percentItem.getName(), flowContext, flowInstance, exception2);
         }
      }
   }

   private void doGlobalScope(FlowContext flowContext, FlowInstance flowInstance) {
      Exception exception2 = null;
      PercentItem percentItem = null;

      try {
         PercentDataStore percentDataStore = (PercentDataStore)Utils.getApplicationContext().getBean("urule.percentDataStore");
         PercentUnit decisionNodePercent = percentDataStore.getDecisionNodePercent(flowInstance.getProcessDefinition(), this.decisionNodeItems, this.name);
         ArrayList items = new ArrayList();

         for (DecisionItem decisionItem : this.decisionNodeItems) {
            PercentItem percentItem2 = new PercentItem();
            percentItem2.setName(decisionItem.getTo());
            percentItem2.setPercent(decisionItem.getPercent());
            long total = decisionNodePercent.getBranch(decisionItem).getTotal();
            percentItem2.setTotal(total);
            percentItem2.setItem(decisionItem);
            items.add(percentItem2);
         }

         long longValue = decisionNodePercent.getTotal() + 1L;
         percentItem = this.computePercent(items, longValue);
         decisionNodePercent.setTotal(longValue);
         Branch branch = decisionNodePercent.getBranch(percentItem.getItem());
         branch.setTotal(percentItem.getTotal() + 1L);
         this.executeNodeEvent(EventType.leave, flowContext, flowInstance);
      } catch (Exception exception) {
         exception2 = exception;
      } finally {
         if (!Objects.isNull(percentItem)) {
            this.leave(percentItem.getName(), flowContext, flowInstance, exception2);
         }
      }
   }

   private long getAmount(String text, FlowContext flowContext) {
      Object sessionValue = flowContext.getWorkingMemory().getSessionValue(text);
      return sessionValue == null ? 0L : (Long)sessionValue;
   }

   private void setAmount(String text, long longValue, FlowContext flowContext) {
      flowContext.getWorkingMemory().setSessionValue(text, longValue);
   }

   private void doCriteria(FlowContext flowContext, FlowInstance flowInstance) {
      Object parameter = null;
      Exception exception2 = null;

      try {
         KnowledgeSession knowledgeSession = this.executeKnowledgePackage(flowContext, flowInstance);
         this.executeNodeEvent(EventType.leave, flowContext, flowInstance);
         parameter = knowledgeSession.getParameter("return_to__");
         String file = flowInstance.getProcessDefinition().getFile();
         if (parameter != null) {
            flowContext.getLogger().logDecisionNodeMatch(this, file, parameter.toString());
            knowledgeSession.getParameters().remove("return_to__");
            return;
         }

         flowContext.getLogger().logDecisionNodeMatch(this, file, null);
      } catch (Exception exception) {
         exception2 = exception;
         return;
      } finally {
         this.leave(parameter != null ? parameter.toString() : null, flowContext, flowInstance, exception2);
      }
   }

   private PercentItem computePercent(List<PercentItem> percentItems, long longValue) {
      BigDecimal bigDecimal = new BigDecimal(longValue);

      for (PercentItem percentItem : percentItems) {
         long total = percentItem.getTotal();
         BigDecimal bigDecimal2 = new BigDecimal(total);
         BigDecimal decimalValue = bigDecimal2.divide(bigDecimal, 20, 6);
         BigDecimal bigDecimal3 = new BigDecimal(percentItem.getPercent());
         bigDecimal3 = bigDecimal3.divide(new BigDecimal(100), 2, 6);
         int number = decimalValue.compareTo(bigDecimal3);
         if (number == -1) {
            return percentItem;
         }
      }

      return (PercentItem)percentItems.get(0);
   }

   @Override
   public FlowNodeType getType() {
      return this.type;
   }

   public List<DecisionItem> getItems() {
      return this.decisionNodeItems;
   }

   public void setItems(List<DecisionItem> items) {
      this.decisionNodeItems = items;
   }

   public RuleSet buildRuleSet(List<Library> libraries, FlowDefinition fd) {
      RuleSet ruleSet = new RuleSet();
      ruleSet.setLibraries(libraries);
      ArrayList items = new ArrayList();
      ruleSet.setRules(items);
      int number = 0;

      for (DecisionItem decisionItem : this.decisionNodeItems) {
         number++;
         if (decisionItem.getConditionType() != null && !decisionItem.getConditionType().equals("script")) {
            Rule rule = new Rule();
            items.add(rule);
            rule.setFile(fd.getFile());
            rule.setLhs(decisionItem.getLhs());
            rule.setDebug(fd.isDebug());
            rule.setName("决策节点[" + this.getName() + "]-" + number);
            Rhs rhs = new Rhs();
            rule.setRhs(rhs);
            ArrayList items2 = new ArrayList();
            rhs.setActions(items2);
            VariableAssignAction variableAssignAction = new VariableAssignAction();
            items2.add(variableAssignAction);
            variableAssignAction.setDatatype(Datatype.String);
            variableAssignAction.setDebug(fd.isDebug());
            variableAssignAction.setCategoryUuid("参数");
            variableAssignAction.setVariableCategory("parameter");
            variableAssignAction.setVariableLabel("return_to__");
            variableAssignAction.setVariableName("return_to__");
            variableAssignAction.setType(LeftType.variable);
            SimpleValue simpleValue = new SimpleValue();
            simpleValue.setContent(decisionItem.getTo());
            variableAssignAction.setValue(simpleValue);
         }
      }

      return ruleSet;
   }

   public PercentScope getPercentScope() {
      return this.percentScope;
   }

   public void setPercentScope(PercentScope percentScope) {
      this.percentScope = percentScope;
   }

   public DecisionType getDecisionType() {
      return this.decisionType;
   }

   public void setDecisionType(DecisionType decisionType) {
      this.decisionType = decisionType;
   }
}
