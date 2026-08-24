package com.bstek.urule.model.library;

import com.bstek.urule.model.library.action.ActionData;
import com.bstek.urule.model.library.action.ActionLibrary;
import com.bstek.urule.model.library.action.Method;
import com.bstek.urule.model.library.action.SpringBean;
import com.bstek.urule.model.library.constant.Constant;
import com.bstek.urule.model.library.constant.ConstantCategory;
import com.bstek.urule.model.library.constant.ConstantData;
import com.bstek.urule.model.library.constant.ConstantLibrary;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.library.variable.VariableData;
import com.bstek.urule.model.library.variable.VariableLibrary;
import com.bstek.urule.model.rule.Predefine;
import com.bstek.urule.model.template.ActionTemplate;
import com.bstek.urule.model.template.ActionTemplateUnit;
import com.bstek.urule.model.template.ConditionTemplate;
import com.bstek.urule.model.template.ConditionTemplateUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class ResourceLibrary {
   private List<ConditionTemplateUnit> conditionTemplateUnits;
   private List<ActionTemplateUnit> actionTemplateUnits;
   private List<ConstantCategory> constantCategories;
   private List<ActionLibrary> actionLibraries;
   private List<VariableCategory> variableCategories;
   private Map<String, VariableCategory> variableCategoryNameMap = new HashMap<>();
   private Map<String, VariableCategory> variableCategoryUuidMap = new HashMap<>();
   private Map<String, VariableData> variableDataMap = new HashMap<>();
   private Map<String, VariableData> variableDataUuidMap = new HashMap<>();
   private Map<String, ConstantData> constantDataUuidMap = new HashMap<>();
   private Map<String, ActionData> actionDataMap = new HashMap<>();
   private Map<String, ActionData> actionDataUuidMap = new HashMap<>();
   private Map<String, ActionData> actionDataLabelMap = new HashMap<>();
   private Map<String, Predefine> predefineMap = new HashMap<>();

   public ResourceLibrary() {
   }

   public ResourceLibrary(List<VariableCategory> vcs) {
      this.variableCategories = vcs;
      this.actionLibraries = new ArrayList<>();
      this.constantCategories = new ArrayList<>();
      this.conditionTemplateUnits = new ArrayList<>();
      this.actionTemplateUnits = new ArrayList<>();
   }

   public ResourceLibrary(
      List<VariableLibrary> variableLibraries,
      List<ActionLibrary> actionLibraries,
      List<ConstantLibrary> constantLibraries,
      List<ConditionTemplate> conditionTemplates,
      List<ActionTemplate> actionTemplates,
      List<Predefine> predefines
   ) {
      this.variableCategories = new ArrayList<>();
      this.actionLibraries = new ArrayList<>();
      this.constantCategories = new ArrayList<>();
      this.conditionTemplateUnits = new ArrayList<>();
      this.actionTemplateUnits = new ArrayList<>();

      for (VariableLibrary variableLibrary : variableLibraries) {
         for (VariableCategory variableCategory : variableLibrary.getVariableCategories()) {
            this.variableCategoryNameMap.put(variableCategory.getName(), variableCategory);
            this.variableCategoryUuidMap.put(variableCategory.getUuid(), variableCategory);
            this.variableCategories.add(variableCategory);

            for (Variable variable : variableCategory.getVariables()) {
               VariableData variableData = new VariableData(variableCategory, variable);
               this.variableDataMap.put(variableCategory.getName() + "," + variable.getName(), variableData);
               this.variableDataUuidMap.put(variableCategory.getUuid() + "," + variable.getUuid(), variableData);
            }
         }
      }

      this.actionLibraries.addAll(actionLibraries);

      for (ActionLibrary actionLibrary : this.actionLibraries) {
         for (SpringBean springBean : actionLibrary.getSpringBeans()) {
            for (Method method : springBean.getMethods()) {
               ActionData actionData = new ActionData(springBean, method);
               this.actionDataMap.put(springBean.getId() + "," + method.getMethodName(), actionData);
               this.actionDataUuidMap.put(springBean.getUuid() + "," + method.getUuid(), actionData);
               this.actionDataLabelMap.put(springBean.getName() + "," + method.getName(), actionData);
            }
         }
      }

      for (ConstantLibrary constantLibrary : constantLibraries) {
         for (ConstantCategory constantCategory : constantLibrary.getCategories()) {
            this.constantCategories.add(constantCategory);

            for (Constant constant : constantCategory.getConstants()) {
               this.constantDataUuidMap.put(constantCategory.getUuid() + "," + constant.getUuid(), new ConstantData(constantCategory, constant));
            }
         }
      }

      for (ConditionTemplate conditionTemplate : conditionTemplates) {
         this.conditionTemplateUnits.addAll(conditionTemplate.getTemplates());
      }

      for (ActionTemplate actionTemplate : actionTemplates) {
         this.actionTemplateUnits.addAll(actionTemplate.getTemplates());
      }

      if (predefines != null) {
         for (Predefine predefine : predefines) {
            this.predefineMap.put(predefine.getUuid(), predefine);
         }
      }
   }

   public void addVariableCategory(VariableCategory vc) {
      this.variableCategoryNameMap.put(vc.getName(), vc);
      this.variableCategoryUuidMap.put(vc.getUuid(), vc);

      for (Variable variable : vc.getVariables()) {
         VariableData variableData = new VariableData(vc, variable);
         this.variableDataMap.put(vc.getName() + "," + variable.getName(), variableData);
         this.variableDataUuidMap.put(vc.getUuid() + "," + variable.getUuid(), variableData);
      }

      this.variableCategories.add(vc);
   }

   public Predefine getPredefine(String uuid) {
      return this.predefineMap.get(uuid);
   }

   public ConditionTemplateUnit getConditionTemplateUnit(String id) {
      for (ConditionTemplateUnit conditionTemplateUnit : this.conditionTemplateUnits) {
         if (conditionTemplateUnit.getId().equals(id)) {
            return conditionTemplateUnit;
         }
      }

      return null;
   }

   public ActionTemplateUnit getActionTemplateUnit(String id) {
      for (ActionTemplateUnit actionTemplateUnit : this.actionTemplateUnits) {
         if (actionTemplateUnit.getId().equals(id)) {
            return actionTemplateUnit;
         }
      }

      return null;
   }

   public Variable getParameterByUuid(String parameterUuid, String parameterName, String parameterLabel) {
      VariableCategory variableCategoryByUuid = this.getVariableCategoryByUuid("参数");
      List variables = variableCategoryByUuid.getVariables();
      Variable variable = null;
      Variable variable2 = null;

      for (Variable variable3 : (Iterable<Variable>)(Iterable<?>)(variables)) {
         if (variable3.getUuid().equals(parameterUuid)) {
            variable = variable3;
         }

         if (StringUtils.isNotBlank(variable3.getName()) && StringUtils.isNotBlank(variable3.getLabel()) && variable3.getName().equals(parameterName) && variable3.getLabel().equals(parameterLabel)) {
            variable2 = variable3;
         }
      }

      if (variable == null && variable2 != null) {
         variable = variable2;
      }

      return variable;
   }

   public VariableCategory getVariableCategoryByUuid(String categoryUuid) {
      if (this.variableCategoryUuidMap.containsKey(categoryUuid)) {
         return this.variableCategoryUuidMap.get(categoryUuid);
      }

      for (VariableCategory variableCategory : this.variableCategories) {
         if (variableCategory.getUuid().equals(categoryUuid)) {
            return variableCategory;
         }
      }

      return null;
   }

   public VariableCategory getVariableCategoryByCategoryName(String categoryName) {
      if (this.variableCategoryNameMap.containsKey(categoryName)) {
         return this.variableCategoryNameMap.get(categoryName);
      }

      for (VariableCategory variableCategory : this.variableCategories) {
         if (variableCategory.getName().equals(categoryName)) {
            return variableCategory;
         }
      }

      return null;
   }

   public VariableData getVariableByName(String category, String name) {
      String text = category + "," + name;
      if (this.variableDataMap.containsKey(text)) {
         return this.variableDataMap.get(text);
      }

      for (VariableCategory variableCategory : this.variableCategories) {
         if (variableCategory.getName().equals(category)) {
            for (Variable variable : variableCategory.getVariables()) {
               if (variable.getName().equals(name)) {
                  return new VariableData(variableCategory, variable);
               }
            }
         }
      }

      return null;
   }

   public VariableData getVariableByUuid(String categoryUuid, String uuid) {
      String text = categoryUuid + "," + uuid;
      if (this.variableDataUuidMap.containsKey(text)) {
         return this.variableDataUuidMap.get(text);
      }

      for (VariableCategory variableCategory : this.variableCategories) {
         if (variableCategory.getUuid().equals(categoryUuid)) {
            for (Variable variable : variableCategory.getVariables()) {
               if (variable.getUuid().equals(uuid)) {
                  return new VariableData(variableCategory, variable);
               }
            }
         }
      }

      return null;
   }

   public ActionData getActionByUuid(String categoryUuid, String uuid) {
      String text = categoryUuid + "," + uuid;
      if (this.actionDataUuidMap.containsKey(text)) {
         return this.actionDataUuidMap.get(text);
      }

      for (ActionLibrary actionLibrary : this.actionLibraries) {
         for (SpringBean springBean : actionLibrary.getSpringBeans()) {
            if (categoryUuid.equals(springBean.getUuid())) {
               for (Method method : springBean.getMethods()) {
                  if (method.getUuid().contentEquals(uuid)) {
                     return new ActionData(springBean, method);
                  }
               }
            }
         }
      }

      return null;
   }

   public ActionData getActionByName(String beanLabel, String methodName) {
      String text = beanLabel + "," + methodName;
      if (this.actionDataLabelMap.containsKey(text)) {
         return this.actionDataLabelMap.get(text);
      }

      for (ActionLibrary actionLibrary : this.actionLibraries) {
         for (SpringBean springBean : actionLibrary.getSpringBeans()) {
            if (beanLabel.equals(springBean.getName())) {
               for (Method method : springBean.getMethods()) {
                  if (method.getName().contentEquals(methodName)) {
                     return new ActionData(springBean, method);
                  }
               }
            }
         }
      }

      return null;
   }

   public ActionData getActionByBean(String beanId, String methodName) {
      String text = beanId + "," + methodName;
      if (this.actionDataMap.containsKey(text)) {
         return this.actionDataMap.get(text);
      }

      for (ActionLibrary actionLibrary : this.actionLibraries) {
         for (SpringBean springBean : actionLibrary.getSpringBeans()) {
            if (beanId.equals(springBean.getId())) {
               for (Method method : springBean.getMethods()) {
                  if (method.getMethodName().contentEquals(methodName)) {
                     return new ActionData(springBean, method);
                  }
               }
            }
         }
      }

      return null;
   }

   public ConstantData getConstantByUuid(String categoryUuid, String uuid) {
      String text = categoryUuid + "," + uuid;
      if (this.constantDataUuidMap.containsKey(text)) {
         return this.constantDataUuidMap.get(text);
      }

      for (ConstantCategory constantCategory : this.constantCategories) {
         if (constantCategory.getUuid().equals(categoryUuid)) {
            for (Constant constant : constantCategory.getConstants()) {
               if (constant.getUuid().equals(uuid)) {
                  return new ConstantData(constantCategory, constant);
               }
            }
         }
      }

      return null;
   }

   public List<ActionLibrary> getActionLibraries() {
      return this.actionLibraries;
   }

   public List<VariableCategory> getVariableCategories() {
      return this.variableCategories;
   }

   public List<ConstantCategory> getConstantCategories() {
      return this.constantCategories;
   }
}
