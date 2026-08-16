package com.bstek.urule.model.scorecard.runtime;

import com.bstek.urule.Utils;
import com.bstek.urule.runtime.rete.Context;
import java.math.BigDecimal;
import java.util.List;

public class ScorecardImpl implements Scorecard {
   private String name;
   private boolean debug;
   private List<RowItem> rowItems;

   public ScorecardImpl(String var1, List<RowItem> var2, boolean var3) {
      this.name = var1;
      this.rowItems = var2;
      this.debug = var3;
   }

   public BigDecimal executeSum(Context var1) {
      BigDecimal var2 = null;

      for (RowItem var4 : this.rowItems) {
         BigDecimal var5 = Utils.toBigDecimal(var4.getScore());
         var4.setActualScore(var5);
         if (var2 == null) {
            var2 = var5;
         } else {
            var2 = var2.add(var5);
         }
      }

      if (this.debug) {
         var1.getLogger().logScoreCardSum(this.name, var2);
      }

      return var2 == null ? new BigDecimal(0) : var2;
   }

   public BigDecimal executeWeightSum(Context var1) {
      BigDecimal var2 = new BigDecimal(0);

      for (RowItem var4 : this.rowItems) {
         BigDecimal var5 = Utils.toBigDecimal(var4.getScore());
         BigDecimal var6 = Utils.toBigDecimal(var4.getWeight());
         BigDecimal var7 = var5.multiply(var6);
         var4.setActualScore(var7);
         var2 = var2.add(var7);
      }

      if (this.debug) {
         var1.getLogger().logScoreCardSum(this.name, var2);
      }

      return var2;
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
