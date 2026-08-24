package com.bstek.urule.console;

import com.bstek.urule.console.security.AuthenticationManager;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.security.entity.Module;
import com.bstek.urule.console.security.entity.User;
import com.bstek.urule.console.security.provider.PermissionProvider;
import com.bstek.urule.console.type.RoleCategory;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

public class PageServletHandler extends BaseServletHandler {
   public final void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String text = this.resolveRequestedMethod(req);
      if (text == null) {
         this.renderConsolePage("login", req, resp);
      } else {
         User loginUser = SecurityUtils.getLoginUser(req);
         if (loginUser != null) {
            this.initializeRequestContext(req);
            String text2 = this.resolveAuthorizedPage(req, text);
            this.renderConsolePage(text2, req, resp);
         } else {
            if (!text.equals("register") && !text.equals("invite") && !text.equals("iforget")) {
               this.renderConsolePage("login", req, resp);
            } else {
               this.renderConsolePage(text, req, resp);
            }

         }
      }
   }

   private void renderConsolePage(String text, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
      VelocityContext velocityContext = new VelocityContext();
      velocityContext.put("chunkName", text);
      velocityContext.put("contextPath", httpServletRequest.getContextPath());
      httpServletResponse.setContentType("text/html");
      httpServletResponse.setCharacterEncoding("utf-8");
      String text2 = "template.html";
      if ("project_summary".equals(text) || "group_dashboard".equals(text) || "project_dashboard".equals(text)) {
         text2 = "template_chart.html";
      }

      Template template = this.velocityEngine.getTemplate("asserts/urule/html/" + text2, "utf-8");
      PrintWriter writer = httpServletResponse.getWriter();
      template.merge(velocityContext, writer);
      writer.close();
   }

   private String resolveAuthorizedPage(HttpServletRequest httpServletRequest, String text) {
      String replacedText = null;
      boolean flag = true;
      User loginUser = SecurityUtils.getLoginUser(httpServletRequest);

      try {
         Module groupModule = PermissionProvider.getGroupModule(text);
         if (groupModule != null) {
            flag = AuthenticationManager.decide(loginUser, RoleCategory.group, groupModule.getCode(), "view");
         } else {
            groupModule = PermissionProvider.getProjectModule(text);
            if (groupModule != null) {
               flag = AuthenticationManager.decide(loginUser, RoleCategory.project, groupModule.getCode(), "view");
            }
         }

         if (flag) {
            replacedText = text.replace("/", "_");
         } else {
            replacedText = "login";
         }
      } catch (Exception exception) {
         java.util.logging.Logger.getLogger(PageServletHandler.class.getName()).log(java.util.logging.Level.SEVERE, exception.getMessage(), exception);
         replacedText = "login";
      }

      return replacedText;
   }

   public String url() {
      return "/page";
   }
}
