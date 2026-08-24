package com.bstek.urule.parse.table;

import com.bstek.urule.model.table.Row;
import java.util.Comparator;

/** Orders decision-table rows by their declared position. */
final class DecisionTableRowComparator implements Comparator<Row> {
   @Override
   public int compare(Row left, Row right) {
      return left.getNum() - right.getNum();
   }
}
