package com.bstek.urule.action;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;
import com.bstek.urule.runtime.rete.Context;
import java.util.Map;

public class ExecuteCommonFunctionAction extends AbstractAction {
   private String b;
   private String c;
   private CommonFunctionParameter d;

   @Override
   public ActionValue execute(Context var1, Map<String, Object> var2) {
      FunctionDescriptor var3 = null;
      if (Utils.getFunctionDescriptorMap().containsKey(this.b)) {
         var3 = Utils.findFunctionDescriptor(this.b);
      } else if (Utils.getFunctionDescriptorLabelMap().containsKey(this.c)) {
         var3 = Utils.getFunctionDescriptorLabelMap().get(this.c);
      }

      if (var3 == null) {
         throw new RuleException("Function[" + this.b + "] not exist.");
      }

      String var4 = LocaleHolder.isEnglish() ? this.b : (this.c == null ? this.b : this.c);
      Value var5 = null;
      Object var6 = null;
      if (this.d != null) {
         var5 = this.d.getObjectParameter();
         var6 = var1.getValueCompute().complexValueCompute(var5, var1, var2);
      }

      String var7 = null;
      if (var3.getArgument() != null && var3.getArgument().isNeedProperty()) {
         var7 = this.d.getProperty();
      }

      if (this.a) {
         var1.getLogger().logExecuteFunction(var4, var6);
      }

      Object var8 = var3.doFunction(var6, var7, var1.getWorkingMemory());
      return var8 == null ? null : new ActionValueImpl(this.b, var8);
   }

   @Override
   public ActionType getActionType() {
      return ActionType.ExecuteCommonFunction;
   }

   public String getName() {
      return this.b;
   }

   public void setName(String var1) {
      this.b = var1;
   }

   public String getLabel() {
      return this.c;
   }

   public void setLabel(String var1) {
      this.c = var1;
   }

   public CommonFunctionParameter getParameter() {
      return this.d;
   }

   public void setParameter(CommonFunctionParameter var1) {
      this.d = var1;
   }
}
