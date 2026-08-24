package com.bstek.urule.console.editor.ruleset;

import java.util.Map;

class LibInfo {
   private String type;
   private Map libraryDefinition;

   public LibInfo(String text, Map valuesByKey) {
      this.type = text;
      this.libraryDefinition = valuesByKey;
   }

   public String getType() {
      return this.type;
   }

   public Map getMap() {
      return this.libraryDefinition;
   }
}
