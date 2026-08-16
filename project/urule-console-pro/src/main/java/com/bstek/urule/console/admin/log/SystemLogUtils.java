package com.bstek.urule.console.admin.log;

import com.bstek.urule.console.ContextHolder;
import com.bstek.urule.console.database.model.KnowledgeLog;
import com.bstek.urule.console.database.model.LoginLog;
import com.bstek.urule.console.database.model.OperationLog;
import com.bstek.urule.console.database.model.URuleLog;
import com.bstek.urule.console.type.RuleFileType;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SystemLogUtils {
   private static final Log a = LogFactory.getLog(SystemLogUtils.class);
   private static LogAppenderManager b;

   public static void addGroupOperationLog(String var0, String var1, Long var2, String var3) {
      addGroupOperationLog(var0, var1, var2.toString(), var3);
   }

   public static void addGroupOperationLog(String var0, String var1, String var2, String var3) {
      OperationLog var4 = URuleLogService.ins.getOperationLog();
      var4.setGroupId(ContextHolder.getGroupId());
      var4.setCategory(var0);
      var4.setAction(var1);
      var4.setContent(var3);
      var4.setItemId(var2);

      try {
         a(var4);
      } catch (InterruptedException var6) {
         var6.printStackTrace();
      }

   }

   public static void addRuleFileOperationLog(String var0, String var1, Long var2, String var3) {
      RuleFileType var4 = RuleFileType.getRuleFileType(var0);
      addProjectOperationLog(var4.name(), var1, var2, var3.replace("{type}", var4.getLabel()));
   }

   public static void addProjectOperationLog(String var0, String var1, Long var2, String var3) {
      addProjectOperationLog(var0, var1, var2.toString(), var3);
   }

   public static void addProjectOperationLog(String var0, String var1, String var2, String var3) {
      OperationLog var4 = URuleLogService.ins.getOperationLog();
      var4.setGroupId(ContextHolder.getGroupId());
      var4.setProjectId(ContextHolder.getProjectId());
      var4.setCategory(var0);
      var4.setAction(var1);
      if (var2 != null) {
         var4.setItemId(var2.toString());
      }

      var4.setContent(var3);

      try {
         a(var4);
      } catch (InterruptedException var6) {
         var6.printStackTrace();
      }

   }

   public static void addKnowledgeLog(KnowledgeLog var0) {
      try {
         a(var0);
      } catch (InterruptedException var2) {
         var2.printStackTrace();
      }

   }

   public static void addLoginLog(HttpServletRequest var0) {
      LoginLog var1 = URuleLogService.ins.getLoginLog();

      try {
         a(var1);
      } catch (InterruptedException var3) {
         var3.printStackTrace();
      }

   }

   private static void a(URuleLog var0) throws InterruptedException {
      if (a.isDebugEnabled()) {
         a.debug(var0.toString());
      }

      if (b == null) {
         b = new LogAppenderManager();
      }

      b.putLog(var0);
   }

   static {
      String var0 = "LogWorkerJob";
      Thread var1 = new Thread(new LogWorkerJob(), var0);
      var1.setDaemon(true);
      var1.start();
      System.out.println("Thread [" + var0 + "] is started...");
   }
}
