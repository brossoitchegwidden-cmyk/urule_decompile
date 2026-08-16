package com.bstek.urule.console.database.manager.log.batch;

import com.bstek.urule.console.database.model.batch.BatchSkipLog;
import java.util.List;

public interface BatchSkipLogQuery {
   BatchSkipLogQuery batchLogId(Long var1);

   List list();

   BatchSkipLog details(Long var1);
}
