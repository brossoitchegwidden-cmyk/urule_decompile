package com.bstek.urule.parse.scorecard;

import com.bstek.urule.model.scorecard.ComplexColumn;
import java.util.Comparator;

class ComplexScorecardParser$1 implements Comparator<ComplexColumn> {
   final ComplexScorecardParser a;

   ComplexScorecardParser$1(ComplexScorecardParser var1) {
      this.a = var1;
   }

   public int compare(ComplexColumn var1, ComplexColumn var2) {
      return var1.getNum() - var2.getNum();
   }
}
