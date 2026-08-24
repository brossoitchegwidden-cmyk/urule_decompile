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
   /**获取登录日志*/
   public void login(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("dateBegin");
      String parameter2 = req.getParameter("dateEnd");
      String parameter3 = req.getParameter("ip");
      int number = Integer.valueOf(req.getParameter("pageIndex"));
      int number2 = Integer.valueOf(req.getParameter("pageSize"));
      LoginLogQuery loginLogQuery = LoginLogManager.ins.newQuery();
      loginLogQuery.userIdLike(req.getParameter("userId"));
      loginLogQuery.username(req.getParameter("username"));
      if (StringUtils.isNotBlank(parameter3)) {
         loginLogQuery.ip(parameter3);
      }

      if (StringUtils.isNotBlank(parameter)) {
         loginLogQuery.loginDateBegin(DateFormat.getDateInstance().parse(parameter));
      }

      if (StringUtils.isNotBlank(parameter2)) {
         loginLogQuery.loginDateEnd(DateFormat.getDateInstance().parse(parameter2));
      }

      Page page = loginLogQuery.paging(number, number2);
      this.writeObjectToJson(resp, page);
   }

   /**获取操作日志*/
   public void operation(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("dateBegin");
      String parameter2 = req.getParameter("dateEnd");
      int number = Integer.valueOf(req.getParameter("pageIndex"));
      int number2 = Integer.valueOf(req.getParameter("pageSize"));
      OperationLogQuery operationLogQuery = OperationLogManager.ins.newQuery();
      String parameter3 = req.getParameter("userId");
      String parameter4 = req.getParameter("username");
      String parameter5 = req.getParameter("category");
      String parameter6 = req.getParameter("type");
      if ("project".equals(parameter6) && ContextHolder.getProjectId() == null) {
         throw new ParameterInvaidException();
      } else if (StringUtils.isNotBlank(ContextHolder.getGroupId())) {
         operationLogQuery.groupId(ContextHolder.getGroupId());
         if (StringUtils.isNotBlank(parameter3)) {
            operationLogQuery.userIdLike(parameter3);
         }

         if (StringUtils.isNotBlank(parameter4)) {
            operationLogQuery.username(parameter4);
         }

         if (StringUtils.isNotBlank(parameter5)) {
            operationLogQuery.categoryLike(parameter5);
         }

         if ("project".equals(parameter6) && ContextHolder.getProjectId() != null) {
            operationLogQuery.projectId(ContextHolder.getProjectId());
         }

         if (StringUtils.isNotBlank(parameter)) {
            operationLogQuery.dateBegin(DateFormat.getDateInstance().parse(parameter));
         }

         if (StringUtils.isNotBlank(parameter2)) {
            operationLogQuery.dateEnd(DateFormat.getDateInstance().parse(parameter2));
         }

         Page page = operationLogQuery.paging(number, number2);
         this.writeObjectToJson(resp, page);
      } else {
         throw new ParameterInvaidException();
      }
   }

   /**获取知识包执行日志*/
   public void knowledge(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("dateBegin");
      String parameter2 = req.getParameter("dateEnd");
      int number = Integer.valueOf(req.getParameter("pageIndex"));
      int number2 = Integer.valueOf(req.getParameter("pageSize"));
      KnowledgeLogQuery knowledgeLogQuery = KnowledgeLogManager.ins.newQuery();
      String parameter3 = req.getParameter("user");
      String parameter4 = req.getParameter("ip");
      if (StringUtils.isNotBlank(parameter3)) {
         knowledgeLogQuery.user(parameter3);
      }

      if (StringUtils.isNotBlank(parameter4)) {
         knowledgeLogQuery.ip(parameter4);
      }

      String parameter5 = req.getParameter("groupId");
      if (StringUtils.isNotBlank(parameter5)) {
         knowledgeLogQuery.groupId(parameter5);
      }

      String parameter6 = req.getParameter("projectId");
      if (StringUtils.isNotBlank(parameter6)) {
         knowledgeLogQuery.projectId(Long.parseLong(parameter6));
      }

      String parameter7 = req.getParameter("knowledgeId");
      if (StringUtils.isNotBlank(parameter7)) {
         knowledgeLogQuery.packetId(Long.parseLong(parameter7));
      }

      String parameter8 = req.getParameter("knowledgeName");
      if (StringUtils.isNotBlank(parameter8)) {
         knowledgeLogQuery.packetNameLike(parameter8);
      }

      if (StringUtils.isNotBlank(parameter)) {
         knowledgeLogQuery.dateBegin(DateFormat.getDateInstance().parse(parameter));
      }

      if (StringUtils.isNotBlank(parameter2)) {
         knowledgeLogQuery.dateEnd(DateFormat.getDateInstance().parse(parameter2));
      }

      Page page = knowledgeLogQuery.paging(number, number2);
      this.writeObjectToJson(resp, page);
   }

   /**获取批处理执行日志*/
   public void batch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      int number = Integer.valueOf(req.getParameter("pageIndex"));
      int number2 = Integer.valueOf(req.getParameter("pageSize"));
      BatchLogQuery batchLogQuery = BatchLogManager.ins.newQuery();
      String parameter = req.getParameter("user");
      if (StringUtils.isNotBlank(parameter)) {
         batchLogQuery.user(parameter);
      }

      String parameter2 = req.getParameter("ip");
      if (StringUtils.isNotBlank(parameter2)) {
         batchLogQuery.ip(parameter2);
      }

      String parameter3 = req.getParameter("status");
      if (StringUtils.isNotBlank(parameter3)) {
         batchLogQuery.status(parameter3);
      }

      String parameter4 = req.getParameter("groupId");
      if (StringUtils.isNotBlank(parameter4)) {
         batchLogQuery.groupId(parameter4);
      }

      String parameter5 = req.getParameter("projectId");
      if (StringUtils.isNotBlank(parameter5)) {
         batchLogQuery.projectId(Long.parseLong(parameter5));
      }

      String parameter6 = req.getParameter("batchId");
      if (StringUtils.isNotBlank(parameter6)) {
         batchLogQuery.batchId(Long.parseLong(parameter6));
      }

      String parameter7 = req.getParameter("batchName");
      if (StringUtils.isNotBlank(parameter7)) {
         batchLogQuery.batchNameLike(parameter7);
      }

      String parameter8 = req.getParameter("dateBegin");
      if (StringUtils.isNotBlank(parameter8)) {
         batchLogQuery.dateBegin(DateFormat.getDateInstance().parse(parameter8));
      }

      String parameter9 = req.getParameter("dateEnd");
      if (StringUtils.isNotBlank(parameter9)) {
         batchLogQuery.dateEnd(DateFormat.getDateInstance().parse(parameter9));
      }

      Page page = batchLogQuery.paging(number, number2);
      this.writeObjectToJson(resp, page);
   }

   public void batchSkip(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("logId");
      BatchSkipLogQuery batchSkipLogQuery = BatchSkipLogManager.ins.newQuery();
      this.writeObjectToJson(resp, batchSkipLogQuery.batchLogId(Long.parseLong(parameter)).list());
   }

   /**获取知识包执行日志详情*/
   public void knowledgeDetail(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("id");
      if (StringUtils.isNotBlank(parameter)) {
         KnowledgeLogQuery knowledgeLogQuery = KnowledgeLogManager.ins.newQuery();
         KnowledgeLog knowledgeLog = knowledgeLogQuery.details(Long.parseLong(parameter));
         this.writeObjectToJson(resp, knowledgeLog);
      }

   }

   /**获取批处理执行日志详情*/
   public void batchDetail(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("id");
      if (StringUtils.isNotBlank(parameter)) {
         BatchLogQuery batchLogQuery = BatchLogManager.ins.newQuery();
         BatchLog batchLog = batchLogQuery.details(Long.parseLong(parameter));
         this.writeObjectToJson(resp, batchLog);
      }

   }

   /**获取批处理异常日志详情*/
   public void batchSkipDetail(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("logId");
      if (StringUtils.isNotBlank(parameter)) {
         BatchSkipLogQuery batchSkipLogQuery = BatchSkipLogManager.ins.newQuery();
         BatchSkipLog batchSkipLog = batchSkipLogQuery.details(Long.parseLong(parameter));
         this.writeObjectToJson(resp, batchSkipLog);
      }

   }

   public String url() {
      return "/log";
   }
}
