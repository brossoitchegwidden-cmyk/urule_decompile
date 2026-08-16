package com.bstek.urule.model.library.variable;

public class VariableData {
   private VariableCategory category;
   private Variable variable;

   public VariableData(VariableCategory var1, Variable var2) {
      this.category = var1;
      this.variable = var2;
   }

   public VariableCategory getCategory() {
      return this.category;
   }

   public Variable getVariable() {
      return this.variable;
   }
}
