package com.bstek.urule.console.admin.log;

import com.bstek.urule.console.database.model.KnowledgeLog;
import com.bstek.urule.console.database.model.LoginLog;
import com.bstek.urule.console.database.model.OperationLog;

public interface URuleLogService {
   URuleLogService ins = new URuleLogServiceImpl();

   OperationLog getOperationLog();

   KnowledgeLog getKnowledgeLog();

   LoginLog getLoginLog();
}
