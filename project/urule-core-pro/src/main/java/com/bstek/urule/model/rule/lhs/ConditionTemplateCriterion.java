package com.bstek.urule.model.rule.lhs;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.runtime.rete.EvaluationContext;

public class ConditionTemplateCriterion extends BaseCriterion {
   private String id;
   private String name;
   private String path;
   private boolean template = true;

   public String getId() {
      return this.id;
   }

   public void setId(String var1) {
      this.id = var1;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   @Override
   public boolean doEval(EvaluationContext var1, boolean var2) {
      throw new RuleException("条件模版不能在N个条件成立节点中使用");
   }

   public String getPath() {
      return this.path;
   }

   public void setPath(String var1) {
      this.path = var1;
   }

   public boolean isTemplate() {
      return this.template;
   }
}
