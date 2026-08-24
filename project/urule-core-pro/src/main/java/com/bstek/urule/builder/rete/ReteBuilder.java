package com.bstek.urule.builder.rete;

import com.bstek.urule.Utils;
import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.Node;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.rete.AndNode;
import com.bstek.urule.model.rete.BaseReteNode;
import com.bstek.urule.model.rete.CriteriaNode;
import com.bstek.urule.model.rete.JunctionNode;
import com.bstek.urule.model.rete.Line;
import com.bstek.urule.model.rete.MutexReteUnit;
import com.bstek.urule.model.rete.ObjectTypeNode;
import com.bstek.urule.model.rete.OrNode;
import com.bstek.urule.model.rete.Rete;
import com.bstek.urule.model.rete.ReteUnit;
import com.bstek.urule.model.rete.RuleData;
import com.bstek.urule.model.rete.TerminalNode;
import com.bstek.urule.model.rule.Other;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.lhs.BaseCriterion;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Lhs;
import com.bstek.urule.model.rule.lhs.Met;
import com.bstek.urule.model.rule.loop.LoopRule;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ReteBuilder implements ApplicationContextAware {
   public static final String BEAN_ID = "urule.reteBuilder";
   private static Collection<CriterionBuilder> criterionBuilders;

   public Rete buildRete(List<Rule> rules, ResourceLibrary resourceLibrary) {
      return this.buildReteInternal(rules, resourceLibrary);
   }

   public Rete buildRete(Rule rule, ResourceLibrary resourceLibrary) {
      ArrayList items = new ArrayList();
      items.add(rule);
      return this.buildReteInternal(items, resourceLibrary);
   }

   private Rete buildReteInternal(List<Rule> rules, ResourceLibrary resourceLibrary) {
      ArrayList items = new ArrayList();
      Rete rete = new Rete(items, resourceLibrary);
      BuildContextImpl buildContextImpl = new BuildContextImpl(resourceLibrary, items);
      HashMap valuesByKey = new HashMap();
      HashMap valuesByKey2 = new HashMap();
      ArrayList items2 = new ArrayList();
      rete.setAllRuleData(items2);

      for (Rule rule : rules) {
         if (!this.shouldSkipRule(rule)) {
            if (StringUtils.isNotBlank(rule.getPendedGroup())) {
               List items3 = (List)valuesByKey2.get(rule.getPendedGroup());
               if (items3 == null) {
                  items3 = new ArrayList();
                  valuesByKey2.put(rule.getPendedGroup(), items3);
               }

               items3.add(rule);
            } else if (StringUtils.isNotBlank(rule.getMutexGroup())) {
               List items4 = (List)valuesByKey.get(rule.getMutexGroup());
               if (items4 == null) {
                  items4 = new ArrayList();
                  valuesByKey.put(rule.getMutexGroup(), items4);
               }

               items4.add(rule);
            } else {
               if (!rule.isTargetResource(ResourceType.Flow)) {
                  items2.add(new RuleData(rule));
               }

               TerminalNode terminalNode = new TerminalNode(rule, buildContextImpl.nextId());
               this.buildBranch(rule, buildContextImpl, terminalNode);
               rule.setLhs(null);
            }
         }
      }

      rete.setPendedGroupRetesMap(this.buildGroupedRetes(valuesByKey2, buildContextImpl, true));
      rete.setMutexGroupRetesMap(this.buildGroupedRetes(valuesByKey, buildContextImpl, false));
      return rete;
   }

   private boolean shouldSkipRule(Rule rule) {
      if (rule.getEnabled() != null && !rule.getEnabled()) {
         return true;
      }

      Date expiresDate = rule.getExpiresDate();
      if (expiresDate != null) {
         Date date = new Date();
         if (expiresDate.compareTo(date) < 0) {
            return true;
         }
      }

      return false;
   }

   private Map<String, List<ReteUnit>> buildGroupedRetes(Map<String, List<Rule>> valuesByKey, BuildContext buildContext, boolean pendedGroup) {
      if (valuesByKey.size() == 0) {
         return null;
      }

      ResourceLibrary resourceLibrary = buildContext.getResourceLibrary();
      HashMap groupedRetes = new HashMap();

      for (String text : valuesByKey.keySet()) {
         List items = (List)valuesByKey.get(text);
         this.sortRules(items);
         HashMap valuesByKey2 = new HashMap();

         for (Rule rule : (Iterable<Rule>)(Iterable<?>)(items)) {
            String mutexGroup = rule.getMutexGroup();
            if (pendedGroup && StringUtils.isNotBlank(mutexGroup)) {
               List items2 = (List)valuesByKey2.get(mutexGroup);
               if (items2 == null) {
                  items2 = new ArrayList();
                  valuesByKey2.put(mutexGroup, items2);
               }

               items2.add(rule);
            } else {
               List items3 = (List)groupedRetes.get(text);
               if (items3 == null) {
                  items3 = new ArrayList();
                  groupedRetes.put(text, items3);
               }

               ArrayList items4 = new ArrayList();
               Rete rete = new Rete(items4, resourceLibrary);
               BuildContextImpl buildContextImpl = new BuildContextImpl(items4, buildContext);
               TerminalNode terminalNode = new TerminalNode(rule, buildContextImpl.nextId());
               this.buildBranch(rule, buildContextImpl, terminalNode);
               ReteUnit reteUnit = new ReteUnit(rete, rule.getName());
               reteUnit.setEffectiveDate(rule.getEffectiveDate());
               reteUnit.setExpiresDate(rule.getExpiresDate());
               items3.add(reteUnit);
               rule.setLhs(null);
               buildContext = buildContextImpl;
            }
         }

         Map mutexRetes = this.buildMutexRetes(buildContext, resourceLibrary, valuesByKey2);
         List items5 = (List)groupedRetes.get(text);
         if (items5 == null) {
            items5 = new ArrayList();
            groupedRetes.put(text, items5);
         }

         for (String text2 : (Iterable<String>)(Iterable<?>)(mutexRetes.keySet())) {
            List items6 = (List)mutexRetes.get(text2);
            items5.add(new MutexReteUnit(text2, items6));
         }
      }

      return groupedRetes;
   }

   private Map<String, List<ReteUnit>> buildMutexRetes(BuildContext buildContext, ResourceLibrary resourceLibrary, Map<String, List<Rule>> valuesByKey) {
      HashMap mutexRetes = new HashMap();

      for (String text : valuesByKey.keySet()) {
         List items = (List)mutexRetes.get(text);
         if (items == null) {
            items = new ArrayList();
            mutexRetes.put(text, items);
         }

         for (Rule rule : (Iterable<Rule>)(Iterable<?>)((List)valuesByKey.get(text))) {
            ArrayList items2 = new ArrayList();
            Rete rete = new Rete(items2, resourceLibrary);
            BuildContextImpl buildContextImpl = new BuildContextImpl(items2, buildContext);
            TerminalNode terminalNode = new TerminalNode(rule, buildContextImpl.nextId());
            this.buildBranch(rule, buildContextImpl, terminalNode);
            ReteUnit reteUnit = new ReteUnit(rete, rule.getName());
            items.add(reteUnit);
            rule.setLhs(null);
            buildContext = buildContextImpl;
         }
      }

      return mutexRetes;
   }

   private void sortRules(List<Rule> rules) {
      Collections.sort(rules, new RuleSalienceComparator());
   }

   private void buildBranch(Rule rule, BuildContext buildContext, TerminalNode terminalNode) {
      buildContext.setCurrentRule(rule);
      Lhs lhs = rule.getLhs();
      if (!(rule instanceof LoopRule) && lhs != null && lhs.getCriterion() != null) {
         Criterion criterion = lhs.getCriterion();

         for (BaseReteNode baseReteNode : buildCriterion(buildContext, criterion)) {
            if (baseReteNode instanceof AndNode || baseReteNode instanceof OrNode) {
               JunctionNode junctionNode = (JunctionNode)baseReteNode;
               List toConnections = junctionNode.getToConnections();
               if (toConnections.size() == 1) {
                  Line line = (Line)toConnections.get(0);
                  Node from = line.getFrom();
                  if (from instanceof CriteriaNode) {
                     CriteriaNode criteriaNode = (CriteriaNode)from;
                     criteriaNode.getLines().remove(line);
                     baseReteNode = criteriaNode;
                  }
               }
            }

            baseReteNode.addLine(terminalNode);
         }

         Other other = rule.getOther();
         if (other != null && other.getActions() != null && other.getActions().size() > 0) {
            rule.setWithElse(true);
            Utils.buildElseRule(rule);
            ObjectTypeNode objectTypeNode = buildContext.buildObjectTypeNode("__*__");
            objectTypeNode.addLine(terminalNode);
         }
      } else {
         ObjectTypeNode objectTypeNode2 = buildContext.buildObjectTypeNode("__*__");
         objectTypeNode2.addLine(terminalNode);
      }
   }

   public static List<BaseReteNode> buildCriterion(BuildContext context, Criterion criterion) {
      if (criterion instanceof Met) {
         return MetBuilder.ins.buildCriterion((BaseCriterion)criterion, null, context);
      }

      for (CriterionBuilder criterionBuilder : ReteBuilder.criterionBuilders) {
         if (criterionBuilder.support(criterion)) {
            return criterionBuilder.buildCriterion((BaseCriterion)criterion, context);
         }
      }

      throw new RuleException("Unknow criterion : " + criterion);
   }

   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      ReteBuilder.criterionBuilders = applicationContext.getBeansOfType(CriterionBuilder.class).values();
   }
}
