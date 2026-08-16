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
   protected static final String a = "/api";

   public final void execute(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      this.b(var1);
      String var3 = var1.getContextPath() + "/urule";
      String var4 = var1.getRequestURI();
      String var5 = var4.substring(var3.length());
      int var6 = var5.lastIndexOf("/");
      String var7 = var5.substring(var6 + 1, var5.length());
      this.a(var7, var1, var2);
   }

   public void info(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      StringBuilder var3 = new StringBuilder();
      if (StringUtils.isNotBlank(DynamicSpringConfigLoaderImpl.getAuthInfo())) {
         var3.append("Authorized to " + DynamicSpringConfigLoaderImpl.getAuthInfo() + "（授权给【" + DynamicSpringConfigLoaderImpl.getAuthInfo() + "】使用）");
      } else {
         var3.append("You are using a trial version,please purchase the commercial license.（当前为试用版，请购买商业授权）");
      }

      this.a(var2, var3.toString());
   }

   protected void a(String var1, HttpServletRequest var2, HttpServletResponse var3) throws Exception {
      Method var4 = this.getClass().getMethod(var1, HttpServletRequest.class, HttpServletResponse.class);
      URuleAuthAnonymous var5 = (URuleAuthAnonymous)var4.getAnnotation(URuleAuthAnonymous.class);
      if (var5 != null) {
         this.a(var4, var2, var3);
      } else if (SecurityUtils.getLoginUser(var2) == null) {
         throw new InfoException("请先登录<br/>Please Login first! ");
      } else {
         User var6 = SecurityUtils.getLoginUser(var2);
         URuleAuthorization var7 = (URuleAuthorization)var4.getAnnotation(URuleAuthorization.class);
         if (var7 != null) {
            boolean var8 = this.a(var2, var3, var4, var6, var7);
            if (!var8) {
               throw new PermissionDeniedException();
            }

            this.a(var4, var2, var3);
         } else {
            this.a(var4, var2, var3);
         }

      }
   }

   private boolean a(HttpServletRequest var1, HttpServletResponse var2, Method var3, User var4, URuleAuthorization var5) {
      boolean var6 = true;
      if (!var5.ruleFile() && !var5.ruleDir()) {
         var6 = AuthenticationManager.decide(var4, RoleCategory.valueOf(var5.authType()), var5.model(), var5.code());
      } else {
         String var7 = var1.getParameter("id");
         String var8 = var5.code();
         String var9 = "";
         if ("add".equals(var8.toLowerCase())) {
            var9 = var1.getParameter("type");
         } else {
            Object var10 = null;
            RuleFile var13;
            if (var5.ruleFile()) {
               var13 = FileManager.ins.get(Long.parseLong(var7));
               ContextHolder.setProjectId(var13.getProjectId());
            } else {
               var13 = DirectoryManager.ins.get(Long.parseLong(var7));
               if (var13 != null) {
                  ContextHolder.setProjectId(var13.getProjectId());
               }
            }

            if (var13 == null) {
               throw new RuleException("URule File Object " + var7 + " not exist.");
            }

            var9 = var13.getType();
         }

         RuleFileType var14 = RuleFileType.getRuleFileType(var9);
         var6 = AuthenticationManager.decide(var4, RoleCategory.project, var14.getModel(), var5.code());
      }

      return var6;
   }

   protected void a(TransactionalInvoke var1) throws Exception {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var2.setAutoCommit(false);
         var1.doTransactional();
         var2.commit();
      } catch (Exception var7) {
         var2.rollback();
         throw var7;
      } finally {
         var2.setAutoCommit(true);
         JdbcUtils.closeConnection(var2);
      }

   }

   private void a(Method var1, HttpServletRequest var2, HttpServletResponse var3) throws Exception {
      Transactional var4 = (Transactional)var1.getAnnotation(Transactional.class);
      if (var4 == null) {
         var1.invoke(this, var2, var3);
      } else {
         Connection var5 = JdbcUtils.getConnection();

         try {
            var5.setAutoCommit(false);
            var1.invoke(this, var2, var3);
            var5.commit();
         } catch (Exception var10) {
            var5.rollback();
            throw var10;
         } finally {
            var5.setAutoCommit(true);
            JdbcUtils.closeConnection(var5);
         }
      }

   }
}
