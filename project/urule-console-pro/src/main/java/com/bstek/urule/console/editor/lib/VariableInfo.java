package com.bstek.urule.console.editor.lib;

import java.util.List;

public class VariableInfo {
   private long id;
   private String path;
   private String type;
   private List variableCategories;

   public VariableInfo(long id, String path, String type, List variableCategories) {
      this.id = id;
      this.path = path;
      this.type = type;
      this.variableCategories = variableCategories;
   }

   public long getId() {
      return this.id;
   }

   public String getPath() {
      return this.path;
   }

   public String getType() {
      return this.type;
   }

   public List getVariableCategories() {
      return this.variableCategories;
   }
}
