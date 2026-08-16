package com.bstek.urule.console.batch.inspector;

import com.bstek.urule.console.batch.BatchContext;

public interface BatchListener {
   void beforeExecute(BatchContext var1);

   void onExecute(BatchContext var1);
}
