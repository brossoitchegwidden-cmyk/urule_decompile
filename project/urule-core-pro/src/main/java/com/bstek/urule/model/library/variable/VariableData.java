package com.bstek.urule.model.library.variable;

public class VariableData {
   private VariableCategory category;
   private Variable variable;

   public VariableData(VariableCategory category, Variable variable) {
      this.category = category;
      this.variable = variable;
   }

   public VariableCategory getCategory() {
      return this.category;
   }

   public Variable getVariable() {
      return this.variable;
   }
}
