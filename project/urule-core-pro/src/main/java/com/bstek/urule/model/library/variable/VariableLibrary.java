package com.bstek.urule.model.library.variable;

import java.util.ArrayList;
import java.util.List;

public class VariableLibrary {
   private List<VariableCategory> variableCategories;

   public List<VariableCategory> getVariableCategories() {
      return this.variableCategories;
   }

   public void setVariableCategories(List<VariableCategory> variableCategories) {
      this.variableCategories = variableCategories;
   }

   public void addVariableCategory(VariableCategory category) {
      if (this.variableCategories == null) {
         this.variableCategories = new ArrayList<>();
      }

      this.variableCategories.add(category);
   }
}
