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

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public CategoryType getType() {
      return this.type;
   }

   public void setType(CategoryType type) {
      this.type = type;
   }

   public Act getAct() {
      return this.act;
   }

   public void setAct(Act act) {
      this.act = act;
   }

   public String getClazz() {
      return this.clazz;
   }

   public void setClazz(String clazz) {
      this.clazz = clazz;
   }

   public String getFile() {
      return this.file;
   }

   public void setFile(String file) {
      this.file = file;
   }

   public List<Variable> getVariables() {
      return this.variables;
   }

   public VariableCategory newVariableCategoryWithDefaultValue() {
      VariableCategory variableCategory = new VariableCategory();
      variableCategory.setName(this.name);
      variableCategory.setAct(this.act);
      variableCategory.setClazz(this.clazz);
      variableCategory.setFile(this.file);
      variableCategory.setType(this.type);

      for (Variable variable : this.variables) {
         if (variable.getDefaultValue() != null) {
            variableCategory.addVariable(variable);
         }
      }

      return variableCategory;
   }

   public void setVariables(List<Variable> variables) {
      this.variables = variables;
      this.variableNames.clear();
      this.variableLabels.clear();

      for (Variable variable : variables) {
         this.variableNames.put(variable.getName(), variable);
         this.variableLabels.put(variable.getLabel(), variable);
      }
   }

   public void addVariable(Variable variable) {
      this.variables.add(variable);
      if (StringUtils.isNotBlank(variable.getName())) {
         this.variableNames.put(variable.getName(), variable);
      }

      if (StringUtils.isNotBlank(variable.getLabel())) {
         this.variableLabels.put(variable.getLabel(), variable);
      }
   }

   public Map<String, Variable> getVariableNames() {
      return this.variableNames;
   }

   public void setVariableNames(Map<String, Variable> variableNames) {
      this.variableNames = variableNames;
   }

   public Map<String, Variable> getVariableLabels() {
      return this.variableLabels;
   }

   public void setVariableLabels(Map<String, Variable> variableLabels) {
      this.variableLabels = variableLabels;
   }
}
