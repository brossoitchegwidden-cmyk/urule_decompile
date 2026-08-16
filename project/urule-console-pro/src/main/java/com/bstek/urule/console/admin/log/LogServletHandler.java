package com.bstek.urule.console.admin.log;

import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.ContextHolder;
import com.bstek.urule.console.ParameterInvaidException;
import com.bstek.urule.console.database.manager.log.KnowledgeLogManager;
import com.bstek.urule.console.database.manager.log.KnowledgeLogQuery;
import com.bstek.urule.console.database.manager.log.LoginLogManager;
import com.bstek.urule.console.database.manager.log.LoginLogQuery;
import com.bstek.urule.console.database.manager.log.OperationLogManager;
import com.bstek.urule.console.database.manager.log.OperationLogQuery;
import com.bstek.urule.console.database.manager.log.batch.BatchLogManager;
import com.bstek.urule.console.database.manager.log.batch.BatchLogQuery;
import com.bstek.urule.console.database.manager.log.batch.BatchSkipLogManager;
import com.bstek.urule.console.database.manager.log.batch.BatchSkipLogQuery;
import com.bstek.urule.console.database.model.KnowledgeLog;
import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.model.batch.BatchLog;
import com.bstek.urule.console.database.model.batch.BatchSkipLog;
import com.bstek.urule.console.util.StringUtils;
import java.text.DateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogServletHandler extends ApiServletHandler {
   public void login(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("dateBegin");
      String var4 = var1.getParameter("dateEnd");
      String var5 = var1.getParameter("ip");
      int var6 = Integer.valueOf(var1.getParameter("pageIndex"));
      int var7 = Integer.valueOf(var1.getParameter("pageSize"));
      LoginLogQuery var8 = LoginLogManager.ins.newQuery();
      var8.userIdLike(var1.getParameter("userId"));
      var8.username(var1.getParameter("username"));
      if (StringUtils.isNotBlank(var5)) {
         var8.ip(var5);
      }

      if (StringUtils.isNotBlank(var3)) {
         var8.loginDateBegin(DateFormat.getDateInstance().parse(var3));
      }

      if (StringUtils.isNotBlank(var4)) {
         var8.loginDateEnd(DateFormat.getDateInstance().parse(var4));
      }

      Page var9 = var8.paging(var6, var7);
      this.a(var2, var9);
   }

   public void operation(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("dateBegin");
      String var4 = var1.getParameter("dateEnd");
      int var5 = Integer.valueOf(var1.getParameter("pageIndex"));
      int var6 = Integer.valueOf(var1.getParameter("pageSize"));
      OperationLogQuery var7 = OperationLogManager.ins.newQuery();
      String var8 = var1.getParameter("userId");
      String var9 = var1.getParameter("username");
      String var10 = var1.getParameter("category");
      String var11 = var1.getParameter("type");
      if ("project".equals(var11) && ContextHolder.getProjectId() == null) {
         throw new ParameterInvaidException();
      } else if (StringUtils.isNotBlank(ContextHolder.getGroupId())) {
         var7.groupId(ContextHolder.getGroupId());
         if (StringUtils.isNotBlank(var8)) {
            var7.userIdLike(var8);
         }

         if (StringUtils.isNotBlank(var9)) {
            var7.username(var9);
         }

         if (StringUtils.isNotBlank(var10)) {
            var7.categoryLike(var10);
         }

         if ("project".equals(var11) && ContextHolder.getProjectId() != null) {
            var7.projectId(ContextHolder.getProjectId());
         }

         if (StringUtils.isNotBlank(var3)) {
            var7.dateBegin(DateFormat.getDateInstance().parse(var3));
         }

         if (StringUtils.isNotBlank(var4)) {
            var7.dateEnd(DateFormat.getDateInstance().parse(var4));
         }

         Page var12 = var7.paging(var5, var6);
         this.a(var2, var12);
      } else {
         throw new ParameterInvaidException();
      }
   }

   public void knowledge(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("dateBegin");
      String var4 = var1.getParameter("dateEnd");
      int var5 = Integer.valueOf(var1.getParameter("pageIndex"));
      int var6 = Integer.valueOf(var1.getParameter("pageSize"));
      KnowledgeLogQuery var7 = KnowledgeLogManager.ins.newQuery();
      String var8 = var1.getParameter("user");
      String var9 = var1.getParameter("ip");
      if (StringUtils.isNotBlank(var8)) {
         var7.user(var8);
      }

      if (StringUtils.isNotBlank(var9)) {
         var7.ip(var9);
      }

      String var10 = var1.getParameter("groupId");
      if (StringUtils.isNotBlank(var10)) {
         var7.groupId(var10);
      }

      String var11 = var1.getParameter("projectId");
      if (StringUtils.isNotBlank(var11)) {
         var7.projectId(Long.parseLong(var11));
      }

      String var12 = var1.getParameter("knowledgeId");
      if (StringUtils.isNotBlank(var12)) {
         var7.packetId(Long.parseLong(var12));
      }

      String var13 = var1.getParameter("knowledgeName");
      if (StringUtils.isNotBlank(var13)) {
         var7.packetNameLike(var13);
      }

      if (StringUtils.isNotBlank(var3)) {
         var7.dateBegin(DateFormat.getDateInstance().parse(var3));
      }

      if (StringUtils.isNotBlank(var4)) {
         var7.dateEnd(DateFormat.getDateInstance().parse(var4));
      }

      Page var14 = var7.paging(var5, var6);
      this.a(var2, var14);
   }

   public void batch(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      int var3 = Integer.valueOf(var1.getParameter("pageIndex"));
      int var4 = Integer.valueOf(var1.getParameter("pageSize"));
      BatchLogQuery var5 = BatchLogManager.ins.newQuery();
      String var6 = var1.getParameter("user");
      if (StringUtils.isNotBlank(var6)) {
         var5.user(var6);
      }

      String var7 = var1.getParameter("ip");
      if (StringUtils.isNotBlank(var7)) {
         var5.ip(var7);
      }

      String var8 = var1.getParameter("status");
      if (StringUtils.isNotBlank(var8)) {
         var5.status(var8);
      }

      String var9 = var1.getParameter("groupId");
      if (StringUtils.isNotBlank(var9)) {
         var5.groupId(var9);
      }

      String var10 = var1.getParameter("projectId");
      if (StringUtils.isNotBlank(var10)) {
         var5.projectId(Long.parseLong(var10));
      }

      String var11 = var1.getParameter("batchId");
      if (StringUtils.isNotBlank(var11)) {
         var5.batchId(Long.parseLong(var11));
      }

      String var12 = var1.getParameter("batchName");
      if (StringUtils.isNotBlank(var12)) {
         var5.batchNameLike(var12);
      }

      String var13 = var1.getParameter("dateBegin");
      if (StringUtils.isNotBlank(var13)) {
         var5.dateBegin(DateFormat.getDateInstance().parse(var13));
      }

      String var14 = var1.getParameter("dateEnd");
      if (StringUtils.isNotBlank(var14)) {
         var5.dateEnd(DateFormat.getDateInstance().parse(var14));
      }

      Page var15 = var5.paging(var3, var4);
      this.a(var2, var15);
   }

   public void batchSkip(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("logId");
      BatchSkipLogQuery var4 = BatchSkipLogManager.ins.newQuery();
      this.a(var2, var4.batchLogId(Long.parseLong(var3)).list());
   }

   public void knowledgeDetail(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("id");
      if (StringUtils.isNotBlank(var3)) {
         KnowledgeLogQuery var4 = KnowledgeLogManager.ins.newQuery();
         KnowledgeLog var5 = var4.details(Long.parseLong(var3));
         this.a(var2, var5);
      }

   }

   public void batchDetail(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("id");
      if (StringUtils.isNotBlank(var3)) {
         BatchLogQuery var4 = BatchLogManager.ins.newQuery();
         BatchLog var5 = var4.details(Long.parseLong(var3));
         this.a(var2, var5);
      }

   }

   public void batchSkipDetail(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("logId");
      if (StringUtils.isNotBlank(var3)) {
         BatchSkipLogQuery var4 = BatchSkipLogManager.ins.newQuery();
         BatchSkipLog var5 = var4.details(Long.parseLong(var3));
         this.a(var2, var5);
      }

   }

   public String url() {
      return "/log";
   }
}
