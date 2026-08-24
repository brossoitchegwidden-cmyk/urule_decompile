package com.bstek.urule.builder;

import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.rete.Rete;
import com.bstek.urule.model.rule.PredefineExecutionUnit;
import com.bstek.urule.model.rule.PredefineGroup;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgePackageImpl;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KnowledgeBase {
   private Rete rete;
   private List<Rete> aloneRetes;
   private ResourceLibrary resourceLibrary;
   private Map<String, FlowDefinition> flowMap;
   private List<PredefineExecutionUnit> predefineExecutionUnits;
   private KnowledgePackageImpl knowledgePackageImpl;

   public KnowledgeBase(Rete rete) {
      this(rete, null, null, null);
   }

   public KnowledgeBase(Rete rete, List<Rete> aloneRetes, Map<String, FlowDefinition> flowMap, List<PredefineExecutionUnit> predefineExecutionUnits) {
      this.rete = rete;
      this.flowMap = flowMap;
      this.aloneRetes = aloneRetes;
      this.resourceLibrary = rete.getResourceLibrary();
      if (predefineExecutionUnits != null) {
         Collections.sort(predefineExecutionUnits);
      }

      this.predefineExecutionUnits = predefineExecutionUnits;
   }

   public KnowledgePackage getKnowledgePackage() {
      if (this.knowledgePackageImpl != null) {
         return this.knowledgePackageImpl;
      }

      this.knowledgePackageImpl = new KnowledgePackageImpl();
      this.knowledgePackageImpl.setRete(this.rete);
      this.knowledgePackageImpl.setAloneRetes(this.aloneRetes);
      this.knowledgePackageImpl.setFlowMap(this.flowMap);
      this.knowledgePackageImpl.setPredefineExecutionUnits(this.predefineExecutionUnits);
      HashMap valuesByKey = new HashMap();
      HashMap valuesByKey2 = new HashMap();
      this.knowledgePackageImpl.setVariableCategoryMap(valuesByKey2);
      List variableCategories = this.resourceLibrary.getVariableCategories();
      this.knowledgePackageImpl.setVariableCategories(variableCategories);
      this.knowledgePackageImpl.setVariableCategoryWithDefaultValueClassMap(valuesByKey);
      HashMap valuesByKey3 = new HashMap();
      this.knowledgePackageImpl.setParameters(valuesByKey3);

      for (VariableCategory variableCategory : (Iterable<VariableCategory>)(Iterable<?>)(variableCategories)) {
         String name = variableCategory.getName();
         String clazz = variableCategory.getClazz();
         valuesByKey.put(clazz, variableCategory.newVariableCategoryWithDefaultValue());
         valuesByKey2.put(name, variableCategory.getClazz());
         if (name.equals("参数")) {
            List variables = variableCategory.getVariables();
            if (variables != null && variables.size() != 0) {
               for (Variable variable : (Iterable<Variable>)(Iterable<?>)(variables)) {
                  valuesByKey3.put(variable.getName(), variable.getType().name());
               }
            }
         }
      }

      return this.knowledgePackageImpl;
   }

   public Rete getPredefineRete() {
      if (this.predefineExecutionUnits != null && this.predefineExecutionUnits.size() != 0) {
         PredefineExecutionUnit predefineExecutionUnit = this.predefineExecutionUnits.get(0);
         PredefineGroup group = predefineExecutionUnit.getGroup();
         if (group == null) {
            return null;
         }

         PredefineGroup predefineGroup;
         for (predefineGroup = group; group.getNextGroup() != null; predefineGroup = group) {
            group = group.getNextGroup();
         }

         return predefineGroup == null ? null : predefineGroup.getKnowledgePackageWrapper().getKnowledgePackage().getRete();
      } else {
         return null;
      }
   }

   public Rete getRete() {
      return this.rete;
   }

   public ResourceLibrary getResourceLibrary() {
      return this.resourceLibrary;
   }

   public Map<String, FlowDefinition> getFlowMap() {
      return this.flowMap;
   }
}
