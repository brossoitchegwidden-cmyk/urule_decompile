package com.bstek.urule.console.batch.inspector;

import com.bstek.urule.console.batch.BatchContext;

public interface BatchListener {
   void beforeExecute(BatchContext batchContext);

   void onExecute(BatchContext batchContext);
}
