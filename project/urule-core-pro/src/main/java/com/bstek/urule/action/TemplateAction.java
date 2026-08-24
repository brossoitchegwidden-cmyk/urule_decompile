package com.bstek.urule.action;

import com.bstek.urule.runtime.rete.Context;
import java.util.Map;

public class TemplateAction extends AbstractAction {
   private String id;
   private String name;
   private String path;

   @Override
   public ActionValue execute(Context context, Map<String, Object> factMap) {
      return null;
   }

   @Override
   public ActionType getActionType() {
      return ActionType.TemplateAction;
   }

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
}
