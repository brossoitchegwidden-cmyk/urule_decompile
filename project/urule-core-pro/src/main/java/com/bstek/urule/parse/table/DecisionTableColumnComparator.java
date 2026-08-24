package com.bstek.urule.parse.table;

import com.bstek.urule.model.table.Column;
import java.util.Comparator;

/** Orders decision-table columns by their declared position. */
final class DecisionTableColumnComparator implements Comparator<Column> {
   @Override
   public int compare(Column left, Column right) {
      return left.getNum() - right.getNum();
   }
}
