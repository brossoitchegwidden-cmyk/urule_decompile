package com.bstek.urule.action;

import com.bstek.urule.Utils;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.runtime.rete.Context;
import com.bstek.urule.runtime.rete.ValueCompute;
import java.math.BigDecimal;
import java.util.Map;

public class ConsolePrintAction extends AbstractAction {
   private Value b;
   private ActionType c = ActionType.ConsolePrint;

   @Override
   public ActionValue execute(Context var1, Map<String, Object> var2) {
      if (!Utils.isDebug()) {
         return null;
      }

      ValueCompute var3 = (ValueCompute)var1.getApplicationContext().getBean("urule.valueCompute");
      Object var4 = var3.complexValueCompute(this.b, var1, var2);
      if (var4 != null && var4 instanceof Number) {
         BigDecimal var5 = Utils.toBigDecimal(var4);
         var4 = var5;
      }

      if (var4 instanceof BigDecimal) {
         BigDecimal var6 = (BigDecimal)var4;
         var1.getLogger().logConsoleOutput(var6.stripTrailingZeros().toPlainString());
      } else {
         var1.getLogger().logConsoleOutput(var4);
      }

      return null;
   }

   public Value getValue() {
      return this.b;
   }

   public void setValue(Value var1) {
      this.b = var1;
   }

   @Override
   public ActionType getActionType() {
      return this.c;
   }
}
