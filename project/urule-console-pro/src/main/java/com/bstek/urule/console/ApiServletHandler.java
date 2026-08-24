package com.bstek.urule.console;

import com.bstek.urule.console.database.manager.file.DirectoryManager;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.security.AuthenticationManager;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.security.URuleAuthAnonymous;
import com.bstek.urule.console.security.URuleAuthorization;
import com.bstek.urule.console.security.entity.User;
import com.bstek.urule.console.type.RoleCategory;
import com.bstek.urule.console.type.RuleFileType;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.runtime.DynamicSpringConfigLoaderImpl;
import java.lang.reflect.Method;
import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ApiServletHandler extends BaseServletHandler {
   protected static final String API_PATH_PREFIX = "/api";

   public boolean useApiPrefix() {
      return true;
   }

   protected boolean handleUnauthenticated(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
      return false;
   }

   public final void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      this.initializeRequestContext(req);
      String text = req.getContextPath() + "/urule";
      String requestURI = req.getRequestURI();
      String substring = requestURI.substring(text.length());
      int number = substring.lastIndexOf("/");
      String substring2 = substring.substring(number + 1, substring.length());
      this.invokeAuthMethod(substring2, req, resp);
   }

   public void info(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      StringBuilder stringBuilder = new StringBuilder();
      if (StringUtils.isNotBlank(DynamicSpringConfigLoaderImpl.getAuthInfo())) {
         stringBuilder.append("Authorized to " + DynamicSpringConfigLoaderImpl.getAuthInfo() + "（授权给【" + DynamicSpringConfigLoaderImpl.getAuthInfo() + "】使用）");
      } else {
         stringBuilder.append("You are using a trial version,please purchase the commercial license.（当前为试用版，请购买商业授权）");
      }

      this.writeObjectToJson(resp, stringBuilder.toString());
   }

   protected void invokeAuthMethod(String methodName, HttpServletRequest req, HttpServletResponse resp) throws Exception {
      Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
      URuleAuthAnonymous annotation = (URuleAuthAnonymous)method.getAnnotation(URuleAuthAnonymous.class);
      if (annotation != null) {
         this.invokeMethodTransactionally(method, req, resp);
      } else if (SecurityUtils.getLoginUser(req) == null) {
         if (!this.handleUnauthenticated(req, resp)) {
            throw new InfoException("请先登录<br/>Please Login first! ");
         }
      } else {
         User loginUser = SecurityUtils.getLoginUser(req);
         URuleAuthorization uRuleAuthorization = (URuleAuthorization)method.getAnnotation(URuleAuthorization.class);
         if (uRuleAuthorization != null) {
            boolean flag = this.authorizeRequest(req, resp, method, loginUser, uRuleAuthorization);
            if (!flag) {
               throw new PermissionDeniedException();
            }

            this.invokeMethodTransactionally(method, req, resp);
         } else {
            this.invokeMethodTransactionally(method, req, resp);
         }

      }
   }

   private boolean authorizeRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Method method, User user, URuleAuthorization uRuleAuthorization) {
      boolean flag = true;
      if (!uRuleAuthorization.ruleFile() && !uRuleAuthorization.ruleDir()) {
         flag = AuthenticationManager.decide(user, RoleCategory.valueOf(uRuleAuthorization.authType()), uRuleAuthorization.model(), uRuleAuthorization.code());
      } else {
         String parameter = httpServletRequest.getParameter("id");
         String text = uRuleAuthorization.code();
         String text2 = "";
         if ("add".equals(text.toLowerCase())) {
            text2 = httpServletRequest.getParameter("type");
         } else {
            Object objectValue = null;
            RuleFile ruleFile;
            if (uRuleAuthorization.ruleFile()) {
               ruleFile = FileManager.ins.get(Long.parseLong(parameter));
               ContextHolder.setProjectId(ruleFile.getProjectId());
            } else {
               ruleFile = DirectoryManager.ins.get(Long.parseLong(parameter));
               if (ruleFile != null) {
                  ContextHolder.setProjectId(ruleFile.getProjectId());
               }
            }

            if (ruleFile == null) {
               throw new RuleException("URule File Object " + parameter + " not exist.");
            }

            text2 = ruleFile.getType();
         }

         RuleFileType ruleFileType = RuleFileType.getRuleFileType(text2);
         flag = AuthenticationManager.decide(user, RoleCategory.project, ruleFileType.getModel(), uRuleAuthorization.code());
      }

      return flag;
   }

   protected void doInTransactional(TransactionalInvoke invoke) throws Exception {
      Connection connection = JdbcUtils.getConnection();

      try {
         connection.setAutoCommit(false);
         invoke.doTransactional();
         connection.commit();
      } catch (Exception exception) {
         connection.rollback();
         throw exception;
      } finally {
         connection.setAutoCommit(true);
         JdbcUtils.closeConnection(connection);
      }

   }

   private void invokeMethodTransactionally(Method method, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
      Transactional annotation = (Transactional)method.getAnnotation(Transactional.class);
      if (annotation == null) {
         method.invoke(this, httpServletRequest, httpServletResponse);
      } else {
         Connection connection = JdbcUtils.getConnection();

         try {
            connection.setAutoCommit(false);
            method.invoke(this, httpServletRequest, httpServletResponse);
            connection.commit();
         } catch (Exception exception) {
            connection.rollback();
            throw exception;
         } finally {
            connection.setAutoCommit(true);
            JdbcUtils.closeConnection(connection);
         }
      }

   }
}
