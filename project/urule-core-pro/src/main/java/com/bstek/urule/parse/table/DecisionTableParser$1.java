package com.bstek.urule.parse.table;

import com.bstek.urule.model.table.Column;
import java.util.Comparator;

class DecisionTableParser$1 implements Comparator<Column> {
   final DecisionTableParser a;

   DecisionTableParser$1(DecisionTableParser var1) {
      this.a = var1;
   }

   public int compare(Column var1, Column var2) {
      return var1.getNum() - var2.getNum();
   }
}
