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
   public final void execute(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = this.a(var1);
      if (var3 == null) {
         this.a("login", var1, var2);
      } else {
         User var4 = SecurityUtils.getLoginUser(var1);
         if (var4 != null) {
            this.b(var1);
            String var5 = this.a(var1, var3);
            this.a(var5, var1, var2);
         } else {
            if (!var3.equals("register") && !var3.equals("invite") && !var3.equals("iforget")) {
               this.a("login", var1, var2);
            } else {
               this.a(var3, var1, var2);
            }

         }
      }
   }

   private void a(String var1, HttpServletRequest var2, HttpServletResponse var3) throws IOException {
      VelocityContext var4 = new VelocityContext();
      var4.put("chunkName", var1);
      var4.put("contextPath", var2.getContextPath());
      var3.setContentType("text/html");
      var3.setCharacterEncoding("utf-8");
      String var5 = "template.html";
      if ("project_summary".equals(var1) || "group_dashboard".equals(var1) || "project_dashboard".equals(var1)) {
         var5 = "template_chart.html";
      }

      Template var6 = this.c.getTemplate("asserts/urule/html/" + var5, "utf-8");
      PrintWriter var7 = var3.getWriter();
      var6.merge(var4, var7);
      var7.close();
   }

   private String a(HttpServletRequest var1, String var2) {
      String var3 = null;
      boolean var4 = true;
      User var5 = SecurityUtils.getLoginUser(var1);

      try {
         Module var6 = PermissionProvider.getGroupModule(var2);
         if (var6 != null) {
            var4 = AuthenticationManager.decide(var5, RoleCategory.group, var6.getCode(), "view");
         } else {
            var6 = PermissionProvider.getProjectModule(var2);
            if (var6 != null) {
               var4 = AuthenticationManager.decide(var5, RoleCategory.project, var6.getCode(), "view");
            }
         }

         if (var4) {
            var3 = var2.replace("/", "_");
         } else {
            var3 = "login";
         }
      } catch (Exception var7) {
         var7.printStackTrace();
         var3 = "login";
      }

      return var3;
   }

   public String url() {
      return "/page";
   }
}
