package com.bstek.urule.model.rule;

import com.bstek.urule.builder.KnowledgeBase;
import com.bstek.urule.builder.KnowledgeBuilder;
import com.bstek.urule.builder.rete.ReteBuilder;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.rete.Rete;
import com.bstek.urule.model.rete.ReteUnit;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PredefineExecutionUnit implements Comparable<PredefineExecutionUnit> {
   private int priority;
   private boolean containsRules;
   private PredefineGroup group;

   public PredefineExecutionUnit() {
   }

   public PredefineExecutionUnit(PredefineGroupDefinition def, List<Rule> rules, boolean alone) {
      this.priority = def.getPriority();
      this.init(def, rules, alone);
   }

   public void buildRete(ResourceLibrary resourceLibrary, KnowledgeBuilder knowledgeBuilder, Map<String, List<ReteUnit>> pendedGroupRetesMap) {
      this.buildGroupRete(resourceLibrary, this.group, knowledgeBuilder, pendedGroupRetesMap);
   }

   private void buildGroupRete(ResourceLibrary resourceLibrary, PredefineGroup predefineGroup, KnowledgeBuilder knowledgeBuilder, Map<String, List<ReteUnit>> valuesByKey) {
      ReteBuilder reteBuilder = knowledgeBuilder.getReteBuilder();
      List rules = predefineGroup.getRules();
      knowledgeBuilder.buildRules(rules);
      knowledgeBuilder.buildLoopRules(rules, resourceLibrary);
      knowledgeBuilder.buildRulesConditionActionTemplate(rules, resourceLibrary);
      ArrayList items = new ArrayList();
      if (rules.size() > 0) {
         if (predefineGroup.isAlone()) {
            Collections.sort(rules);

            for (Rule rule : (Iterable<Rule>)(Iterable<?>)(rules)) {
               Rete rete = reteBuilder.buildRete(rule, resourceLibrary);
               items.add(rete);
            }

            rules.clear();
         }

         Rete rete2 = reteBuilder.buildRete(rules, resourceLibrary);
         if (rete2.getPendedGroupRetesMap() != null) {
            valuesByKey.putAll(rete2.getPendedGroupRetesMap());
         }

         rules.clear();
         KnowledgeBase knowledgeBase = new KnowledgeBase(rete2, items, null, null);
         KnowledgePackageWrapper knowledgePackageWrapper = new KnowledgePackageWrapper(knowledgeBase.getKnowledgePackage());
         predefineGroup.resetKnowledgePackageWrapper(knowledgePackageWrapper);
      }

      for (PredefineGroup nextGroup = predefineGroup.getNextGroup(); nextGroup != null; nextGroup = nextGroup.getNextGroup()) {
         this.buildGroupRete(resourceLibrary, nextGroup, knowledgeBuilder, valuesByKey);
      }
   }

   private void init(PredefineGroupDefinition predefineGroupDefinition, List<Rule> rules, boolean flag) {
      if (predefineGroupDefinition != null && rules.size() != 0) {
         PredefineGroup predefineGroup = null;
         PredefineGroup predefineGroup2 = null;

         for (Predefine predefine : predefineGroupDefinition.getPredefines()) {
            PredefineGroup predefineGroup3 = new PredefineGroup(flag);
            if (predefineGroup == null) {
               predefineGroup = predefineGroup3;
            }

            predefineGroup3.setFilePath(predefineGroupDefinition.getFilePath());
            predefineGroup3.setPredefine(predefine);
            if (predefineGroup2 != null) {
               predefineGroup2.setNextGroup(predefineGroup3);
            }

            predefineGroup2 = predefineGroup3;
         }

         if (predefineGroup2 != null) {
            predefineGroup2.getRules().addAll(rules);
            rules.clear();
            this.containsRules = true;
            this.group = predefineGroup;
         }
      }
   }

   public int compareTo(PredefineExecutionUnit predefineExecutionUnit) {
      int priority = predefineExecutionUnit.getPriority();
      return priority - this.priority;
   }

   public int getPriority() {
      return this.priority;
   }

   public void setPriority(int priority) {
      this.priority = priority;
   }

   public PredefineGroup getGroup() {
      return this.group;
   }

   public void setContainsRules(boolean containsRules) {
      this.containsRules = containsRules;
   }

   public boolean isContainsRules() {
      return this.containsRules;
   }
}
