package com.bstek.urule.console.admin.log;

import com.bstek.urule.console.RequestHolder;
import com.bstek.urule.console.database.model.KnowledgeLog;
import com.bstek.urule.console.database.model.LoginLog;
import com.bstek.urule.console.database.model.OperationLog;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.util.IPUtils;

public class URuleLogServiceImpl implements URuleLogService {
   public OperationLog getOperationLog() {
      OperationLog var1 = new OperationLog();
      var1.setUserId(SecurityUtils.getLoginUsername(RequestHolder.getRequest()));
      var1.setUsername(SecurityUtils.getLoginUser(RequestHolder.getRequest()).getDesc());
      return var1;
   }

   public LoginLog getLoginLog() {
      LoginLog var1 = new LoginLog();
      var1.setUserId(SecurityUtils.getLoginUsername(RequestHolder.getRequest()));
      var1.setUsername(SecurityUtils.getLoginUser(RequestHolder.getRequest()).getDesc());
      var1.setIp(IPUtils.getIpAddress(RequestHolder.getRequest()));
      var1.setUserAgent(RequestHolder.getRequest().getHeader("User-Agent"));
      return var1;
   }

   public KnowledgeLog getKnowledgeLog() {
      KnowledgeLog var1 = new KnowledgeLog();
      var1.setIp(IPUtils.getIpAddress(RequestHolder.getRequest()));
      var1.setUserAgent(RequestHolder.getRequest().getHeader("User-Agent"));
      return var1;
   }
}
