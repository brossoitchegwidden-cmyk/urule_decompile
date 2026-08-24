package com.bstek.urule.parse.scorecard;

import com.bstek.urule.model.table.Row;
import java.util.Comparator;

/** Orders complex-scorecard rows by their declared position. */
final class ComplexScorecardRowComparator implements Comparator<Row> {
   @Override
   public int compare(Row left, Row right) {
      return left.getNum() - right.getNum();
   }
}
