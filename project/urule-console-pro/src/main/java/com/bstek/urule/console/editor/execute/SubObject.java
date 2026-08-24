package com.bstek.urule.console.editor.execute;

import java.util.Map;

public class SubObject {
   private String name;
   private Map properties;

   public SubObject(String name, Map map) {
      this.name = name;
      this.properties = map;
   }

   public Map getMap() {
      return this.properties;
   }

   public String getName() {
      return this.name;
   }
}
