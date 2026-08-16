package com.bstek.urule.model.library.variable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class VariableCategory {
   public static final String PARAM_CATEGORY = "参数";
   public static final String EN_PARAM_CATEGORY = "Parameter";
   private String uuid;
   private String name;
   private CategoryType type;
   private String clazz;
   private String file;
   private Act act = Act.InOut;
   private List<Variable> variables = new ArrayList<>();
   @JsonIgnore
   private Map<String, Variable> variableNames = new HashMap<>();
   @JsonIgnore
   private Map<String, Variable> variableLabels = new HashMap<>();

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String var1) {
      this.uuid = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public CategoryType getType() {
      return this.type;
   }

   public void setType(CategoryType var1) {
      this.type = var1;
   }

   public Act getAct() {
      return this.act;
   }

   public void setAct(Act var1) {
      this.act = var1;
   }

   public String getClazz() {
      return this.clazz;
   }

   public void setClazz(String var1) {
      this.clazz = var1;
   }

   public String getFile() {
      return this.file;
   }

   public void setFile(String var1) {
      this.file = var1;
   }

   public List<Variable> getVariables() {
      return this.variables;
   }

   public VariableCategory newVariableCategoryWithDefaultValue() {
      VariableCategory var1 = new VariableCategory();
      var1.setName(this.name);
      var1.setAct(this.act);
      var1.setClazz(this.clazz);
      var1.setFile(this.file);
      var1.setType(this.type);

      for (Variable var3 : this.variables) {
         if (var3.getDefaultValue() != null) {
            var1.addVariable(var3);
         }
      }

      return var1;
   }

   public void setVariables(List<Variable> var1) {
      this.variables = var1;
      this.variableNames.clear();
      this.variableLabels.clear();

      for (Variable var3 : var1) {
         this.variableNames.put(var3.getName(), var3);
         this.variableLabels.put(var3.getLabel(), var3);
      }
   }

   public void addVariable(Variable var1) {
      this.variables.add(var1);
      if (StringUtils.isNotBlank(var1.getName())) {
         this.variableNames.put(var1.getName(), var1);
      }

      if (StringUtils.isNotBlank(var1.getLabel())) {
         this.variableLabels.put(var1.getLabel(), var1);
      }
   }

   public Map<String, Variable> getVariableNames() {
      return this.variableNames;
   }

   public void setVariableNames(Map<String, Variable> var1) {
      this.variableNames = var1;
   }

   public Map<String, Variable> getVariableLabels() {
      return this.variableLabels;
   }

   public void setVariableLabels(Map<String, Variable> var1) {
      this.variableLabels = var1;
   }
}
