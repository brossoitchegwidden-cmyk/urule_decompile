package com.bstek.urule.model.scorecard.runtime;

import java.util.List;

public interface Scorecard {
   String getName();

   List<RowItem> getRowItems();
}
