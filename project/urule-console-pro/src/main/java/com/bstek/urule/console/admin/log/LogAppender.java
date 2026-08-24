package com.bstek.urule.console.admin.log;

import com.bstek.urule.console.database.model.URuleLog;

public interface LogAppender {
   void putLog(URuleLog log);
}
