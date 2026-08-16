package com.bstek.urule.model.library.variable;

import java.util.ArrayList;
import java.util.List;

public class VariableLibrary {
   private List<VariableCategory> variableCategories;

   public List<VariableCategory> getVariableCategories() {
      return this.variableCategories;
   }

   public void setVariableCategories(List<VariableCategory> var1) {
      this.variableCategories = var1;
   }

   public void addVariableCategory(VariableCategory var1) {
      if (this.variableCategories == null) {
         this.variableCategories = new ArrayList<>();
      }

      this.variableCategories.add(var1);
   }
}
