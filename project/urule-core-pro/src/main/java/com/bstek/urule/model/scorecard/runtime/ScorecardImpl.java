package com.bstek.urule.model.scorecard.runtime;

import com.bstek.urule.Utils;
import com.bstek.urule.runtime.rete.Context;
import java.math.BigDecimal;
import java.util.List;

public class ScorecardImpl implements Scorecard {
   private String name;
   private boolean debug;
   private List<RowItem> rowItems;

   public ScorecardImpl(String name, List<RowItem> rowItems, boolean debug) {
      this.name = name;
      this.rowItems = rowItems;
      this.debug = debug;
   }

   public BigDecimal executeSum(Context context) {
      BigDecimal decimalValue = null;

      for (RowItem rowItem : this.rowItems) {
         BigDecimal decimalValue2 = Utils.toBigDecimal(rowItem.getScore());
         rowItem.setActualScore(decimalValue2);
         if (decimalValue == null) {
            decimalValue = decimalValue2;
         } else {
            decimalValue = decimalValue.add(decimalValue2);
         }
      }

      if (this.debug) {
         context.getLogger().logScoreCardSum(this.name, decimalValue);
      }

      return decimalValue == null ? new BigDecimal(0) : decimalValue;
   }

   public BigDecimal executeWeightSum(Context context) {
      BigDecimal bigDecimal = new BigDecimal(0);

      for (RowItem rowItem : this.rowItems) {
         BigDecimal decimalValue = Utils.toBigDecimal(rowItem.getScore());
         BigDecimal decimalValue2 = Utils.toBigDecimal(rowItem.getWeight());
         BigDecimal decimalValue3 = decimalValue.multiply(decimalValue2);
         rowItem.setActualScore(decimalValue3);
         bigDecimal = bigDecimal.add(decimalValue3);
      }

      if (this.debug) {
         context.getLogger().logScoreCardSum(this.name, bigDecimal);
      }

      return bigDecimal;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public List<RowItem> getRowItems() {
      return this.rowItems;
   }
}
