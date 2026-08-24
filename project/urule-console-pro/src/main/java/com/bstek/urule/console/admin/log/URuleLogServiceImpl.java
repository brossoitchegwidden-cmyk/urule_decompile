package com.bstek.urule.console.admin.log;

import com.bstek.urule.console.RequestHolder;
import com.bstek.urule.console.database.model.KnowledgeLog;
import com.bstek.urule.console.database.model.LoginLog;
import com.bstek.urule.console.database.model.OperationLog;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.util.IPUtils;

public class URuleLogServiceImpl implements URuleLogService {
   public OperationLog getOperationLog() {
      OperationLog operationLog = new OperationLog();
      operationLog.setUserId(SecurityUtils.getLoginUsername(RequestHolder.getRequest()));
      operationLog.setUsername(SecurityUtils.getLoginUser(RequestHolder.getRequest()).getDesc());
      return operationLog;
   }

   public LoginLog getLoginLog() {
      LoginLog loginLog = new LoginLog();
      loginLog.setUserId(SecurityUtils.getLoginUsername(RequestHolder.getRequest()));
      loginLog.setUsername(SecurityUtils.getLoginUser(RequestHolder.getRequest()).getDesc());
      loginLog.setIp(IPUtils.getIpAddress(RequestHolder.getRequest()));
      loginLog.setUserAgent(RequestHolder.getRequest().getHeader("User-Agent"));
      return loginLog;
   }

   public KnowledgeLog getKnowledgeLog() {
      KnowledgeLog knowledgeLog = new KnowledgeLog();
      knowledgeLog.setIp(IPUtils.getIpAddress(RequestHolder.getRequest()));
      knowledgeLog.setUserAgent(RequestHolder.getRequest().getHeader("User-Agent"));
      return knowledgeLog;
   }
}
