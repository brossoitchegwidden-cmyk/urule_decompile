package com.bstek.urule.parse.scorecard;

import com.bstek.urule.model.scorecard.ComplexColumn;
import java.util.Comparator;

/** Orders complex-scorecard columns by their declared position. */
final class ComplexScorecardColumnComparator implements Comparator<ComplexColumn> {
   @Override
   public int compare(ComplexColumn left, ComplexColumn right) {
      return left.getNum() - right.getNum();
   }
}
