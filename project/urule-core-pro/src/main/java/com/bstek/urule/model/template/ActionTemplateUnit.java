package com.bstek.urule.model.template;

import com.bstek.urule.action.Action;
import java.util.List;

public class ActionTemplateUnit {
   private String id;
   private String name;
   private String path;
   private List<Action> actions;

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getPath() {
      return this.path;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public List<Action> getActions() {
      return this.actions;
   }

   public void setActions(List<Action> actions) {
      this.actions = actions;
   }
}
