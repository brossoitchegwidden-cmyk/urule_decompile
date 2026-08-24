package com.bstek.urule.model.scorecard.runtime;

import java.util.List;

public interface RowItem {
   int getRowNumber();

   Object getScore();

   Object getActualScore();

   void setActualScore(Object actualScore);

   String getWeight();

   List<CellItem> getCellItems();
}
