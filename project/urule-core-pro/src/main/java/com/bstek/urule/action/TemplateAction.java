package com.bstek.urule.action;

import com.bstek.urule.runtime.rete.Context;
import java.util.Map;

public class TemplateAction extends AbstractAction {
   private String b;
   private String c;
   private String d;

   @Override
   public ActionValue execute(Context var1, Map<String, Object> var2) {
      return null;
   }

   @Override
   public ActionType getActionType() {
      return ActionType.TemplateAction;
   }

   public String getId() {
      return this.b;
   }

   public void setId(String var1) {
      this.b = var1;
   }

   public String getName() {
      return this.c;
   }

   public void setName(String var1) {
      this.c = var1;
   }

   public String getPath() {
      return this.d;
   }

   public void setPath(String var1) {
      this.d = var1;
   }
}
