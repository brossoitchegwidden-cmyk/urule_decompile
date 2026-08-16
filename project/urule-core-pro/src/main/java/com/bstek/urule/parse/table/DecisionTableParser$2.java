package com.bstek.urule.parse.table;

import com.bstek.urule.model.table.Row;
import java.util.Comparator;

class DecisionTableParser$2 implements Comparator<Row> {
   final DecisionTableParser a;

   DecisionTableParser$2(DecisionTableParser var1) {
      this.a = var1;
   }

   public int compare(Row var1, Row var2) {
      return var1.getNum() - var2.getNum();
   }
}
