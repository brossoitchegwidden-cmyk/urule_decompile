package com.bstek.urule.action;

import com.bstek.urule.Utils;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.runtime.rete.Context;
import com.bstek.urule.runtime.rete.ValueCompute;
import java.math.BigDecimal;
import java.util.Map;

public class ConsolePrintAction extends AbstractAction {
   private Value value;
   private ActionType actionType = ActionType.ConsolePrint;

   @Override
   public ActionValue execute(Context context, Map<String, Object> factMap) {
      if (!Utils.isDebug()) {
         return null;
      }

      ValueCompute valueCompute = (ValueCompute)context.getApplicationContext().getBean("urule.valueCompute");
      Object objectValue = valueCompute.complexValueCompute(this.value, context, factMap);
      if (objectValue != null && objectValue instanceof Number) {
         BigDecimal decimalValue = Utils.toBigDecimal(objectValue);
         objectValue = decimalValue;
      }

      if (objectValue instanceof BigDecimal) {
         BigDecimal objectValue2 = (BigDecimal)objectValue;
         context.getLogger().logConsoleOutput(objectValue2.stripTrailingZeros().toPlainString());
      } else {
         context.getLogger().logConsoleOutput(objectValue);
      }

      return null;
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value value) {
      this.value = value;
   }

   @Override
   public ActionType getActionType() {
      return this.actionType;
   }
}
