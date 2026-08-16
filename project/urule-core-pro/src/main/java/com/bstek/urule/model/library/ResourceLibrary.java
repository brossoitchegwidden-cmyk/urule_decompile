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

   public ResourceLibrary(List<VariableCategory> var1) {
      this.variableCategories = var1;
      this.actionLibraries = new ArrayList<>();
      this.constantCategories = new ArrayList<>();
      this.conditionTemplateUnits = new ArrayList<>();
      this.actionTemplateUnits = new ArrayList<>();
   }

   public ResourceLibrary(
      List<VariableLibrary> var1,
      List<ActionLibrary> var2,
      List<ConstantLibrary> var3,
      List<ConditionTemplate> var4,
      List<ActionTemplate> var5,
      List<Predefine> var6
   ) {
      this.variableCategories = new ArrayList<>();
      this.actionLibraries = new ArrayList<>();
      this.constantCategories = new ArrayList<>();
      this.conditionTemplateUnits = new ArrayList<>();
      this.actionTemplateUnits = new ArrayList<>();

      for (VariableLibrary var8 : var1) {
         for (VariableCategory var10 : var8.getVariableCategories()) {
            this.variableCategoryNameMap.put(var10.getName(), var10);
            this.variableCategoryUuidMap.put(var10.getUuid(), var10);
            this.variableCategories.add(var10);

            for (Variable var12 : var10.getVariables()) {
               VariableData var13 = new VariableData(var10, var12);
               this.variableDataMap.put(var10.getName() + "," + var12.getName(), var13);
               this.variableDataUuidMap.put(var10.getUuid() + "," + var12.getUuid(), var13);
            }
         }
      }

      this.actionLibraries.addAll(var2);

      for (ActionLibrary var19 : this.actionLibraries) {
         for (SpringBean var26 : var19.getSpringBeans()) {
            for (Method var30 : var26.getMethods()) {
               ActionData var32 = new ActionData(var26, var30);
               this.actionDataMap.put(var26.getId() + "," + var30.getMethodName(), var32);
               this.actionDataUuidMap.put(var26.getUuid() + "," + var30.getUuid(), var32);
               this.actionDataLabelMap.put(var26.getName() + "," + var30.getName(), var32);
            }
         }
      }

      for (ConstantLibrary var20 : var3) {
         for (ConstantCategory var27 : var20.getCategories()) {
            this.constantCategories.add(var27);

            for (Constant var31 : var27.getConstants()) {
               this.constantDataUuidMap.put(var27.getUuid() + "," + var31.getUuid(), new ConstantData(var27, var31));
            }
         }
      }

      for (ConditionTemplate var21 : var4) {
         this.conditionTemplateUnits.addAll(var21.getTemplates());
      }

      for (ActionTemplate var22 : var5) {
         this.actionTemplateUnits.addAll(var22.getTemplates());
      }

      if (var6 != null) {
         for (Predefine var23 : var6) {
            this.predefineMap.put(var23.getUuid(), var23);
         }
      }
   }

   public void addVariableCategory(VariableCategory var1) {
      this.variableCategoryNameMap.put(var1.getName(), var1);
      this.variableCategoryUuidMap.put(var1.getUuid(), var1);

      for (Variable var3 : var1.getVariables()) {
         VariableData var4 = new VariableData(var1, var3);
         this.variableDataMap.put(var1.getName() + "," + var3.getName(), var4);
         this.variableDataUuidMap.put(var1.getUuid() + "," + var3.getUuid(), var4);
      }

      this.variableCategories.add(var1);
   }

   public Predefine getPredefine(String var1) {
      return this.predefineMap.get(var1);
   }

   public ConditionTemplateUnit getConditionTemplateUnit(String var1) {
      for (ConditionTemplateUnit var3 : this.conditionTemplateUnits) {
         if (var3.getId().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public ActionTemplateUnit getActionTemplateUnit(String var1) {
      for (ActionTemplateUnit var3 : this.actionTemplateUnits) {
         if (var3.getId().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public Variable getParameterByUuid(String var1, String var2, String var3) {
      VariableCategory var4 = this.getVariableCategoryByUuid("参数");
      List var5 = var4.getVariables();
      Variable var6 = null;
      Variable var7 = null;

      for (Variable var9 : (Iterable<Variable>)(Iterable<?>)(var5)) {
         if (var9.getUuid().equals(var1)) {
            var6 = var9;
         }

         if (StringUtils.isNotBlank(var9.getName()) && StringUtils.isNotBlank(var9.getLabel()) && var9.getName().equals(var2) && var9.getLabel().equals(var3)) {
            var7 = var9;
         }
      }

      if (var6 == null && var7 != null) {
         var6 = var7;
      }

      return var6;
   }

   public VariableCategory getVariableCategoryByUuid(String var1) {
      if (this.variableCategoryUuidMap.containsKey(var1)) {
         return this.variableCategoryUuidMap.get(var1);
      }

      for (VariableCategory var3 : this.variableCategories) {
         if (var3.getUuid().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public VariableCategory getVariableCategoryByCategoryName(String var1) {
      if (this.variableCategoryNameMap.containsKey(var1)) {
         return this.variableCategoryNameMap.get(var1);
      }

      for (VariableCategory var3 : this.variableCategories) {
         if (var3.getName().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public VariableData getVariableByName(String var1, String var2) {
      String var3 = var1 + "," + var2;
      if (this.variableDataMap.containsKey(var3)) {
         return this.variableDataMap.get(var3);
      }

      for (VariableCategory var5 : this.variableCategories) {
         if (var5.getName().equals(var1)) {
            for (Variable var7 : var5.getVariables()) {
               if (var7.getName().equals(var2)) {
                  return new VariableData(var5, var7);
               }
            }
         }
      }

      return null;
   }

   public VariableData getVariableByUuid(String var1, String var2) {
      String var3 = var1 + "," + var2;
      if (this.variableDataUuidMap.containsKey(var3)) {
         return this.variableDataUuidMap.get(var3);
      }

      for (VariableCategory var5 : this.variableCategories) {
         if (var5.getUuid().equals(var1)) {
            for (Variable var7 : var5.getVariables()) {
               if (var7.getUuid().equals(var2)) {
                  return new VariableData(var5, var7);
               }
            }
         }
      }

      return null;
   }

   public ActionData getActionByUuid(String var1, String var2) {
      String var3 = var1 + "," + var2;
      if (this.actionDataUuidMap.containsKey(var3)) {
         return this.actionDataUuidMap.get(var3);
      }

      for (ActionLibrary var5 : this.actionLibraries) {
         for (SpringBean var8 : var5.getSpringBeans()) {
            if (var1.equals(var8.getUuid())) {
               for (Method var10 : var8.getMethods()) {
                  if (var10.getUuid().contentEquals(var2)) {
                     return new ActionData(var8, var10);
                  }
               }
            }
         }
      }

      return null;
   }

   public ActionData getActionByName(String var1, String var2) {
      String var3 = var1 + "," + var2;
      if (this.actionDataLabelMap.containsKey(var3)) {
         return this.actionDataLabelMap.get(var3);
      }

      for (ActionLibrary var5 : this.actionLibraries) {
         for (SpringBean var8 : var5.getSpringBeans()) {
            if (var1.equals(var8.getName())) {
               for (Method var10 : var8.getMethods()) {
                  if (var10.getName().contentEquals(var2)) {
                     return new ActionData(var8, var10);
                  }
               }
            }
         }
      }

      return null;
   }

   public ActionData getActionByBean(String var1, String var2) {
      String var3 = var1 + "," + var2;
      if (this.actionDataMap.containsKey(var3)) {
         return this.actionDataMap.get(var3);
      }

      for (ActionLibrary var5 : this.actionLibraries) {
         for (SpringBean var8 : var5.getSpringBeans()) {
            if (var1.equals(var8.getId())) {
               for (Method var10 : var8.getMethods()) {
                  if (var10.getMethodName().contentEquals(var2)) {
                     return new ActionData(var8, var10);
                  }
               }
            }
         }
      }

      return null;
   }

   public ConstantData getConstantByUuid(String var1, String var2) {
      String var3 = var1 + "," + var2;
      if (this.constantDataUuidMap.containsKey(var3)) {
         return this.constantDataUuidMap.get(var3);
      }

      for (ConstantCategory var5 : this.constantCategories) {
         if (var5.getUuid().equals(var1)) {
            for (Constant var7 : var5.getConstants()) {
               if (var7.getUuid().equals(var2)) {
                  return new ConstantData(var5, var7);
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
