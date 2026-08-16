package com.bstek.urule.console.admin.studio;

import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.ContextHolder;
import com.bstek.urule.console.database.manager.project.ProjectManager;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.ProjectViewModel;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.service.file.FileService;
import com.bstek.urule.console.security.AuthenticationManager;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.security.entity.User;
import com.bstek.urule.console.type.RoleCategory;
import com.bstek.urule.console.type.RuleFileType;
import com.bstek.urule.runtime.DynamicSpringConfigLoaderImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StudioServletHandler extends ApiServletHandler {
   public void menu(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      Long var3 = ContextHolder.getProjectId();
      User var4 = SecurityUtils.getLoginUser(var1);
      List var5 = FileService.ins.menus(var3);
      ArrayList var6 = new ArrayList();
      Project var7 = ProjectManager.ins.get(var3);
      if (var7 != null) {
         for(RuleFile var9 : (Iterable<RuleFile>)(Iterable<?>)(var5)) {
            RuleFileType var10 = RuleFileType.getRuleFileType(var9.getType());
            if (var10 == RuleFileType.General) {
               if (var7.getViewModel() == ProjectViewModel.general) {
                  var6.add(var9);
               }
            } else if (var10 != null && var7.getViewModel() != ProjectViewModel.general) {
               boolean var11 = AuthenticationManager.decide(var4, RoleCategory.project, var10.getModel(), "view");
               if (var11) {
                  var6.add(var9);
               }
            }
         }
      }

      this.a(var2, var6);
   }

   public void tree(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      Long var3 = ContextHolder.getProjectId();
      User var4 = SecurityUtils.getLoginUser(var1);
      String var5 = var1.getParameter("type");
      RuleFileType var6 = RuleFileType.getRuleFileType(var5);
      Object var7 = new ArrayList();
      if (var6 != null) {
         if (var6 == RuleFileType.General) {
            Map var8 = this.a(var4, var3);
            var7 = FileService.ins.tree(var3, var6);
            this.a((List)var7, var8);
         } else {
            boolean var9 = AuthenticationManager.decide(var4, RoleCategory.project, var6.getModel(), "view");
            if (var9) {
               var7 = FileService.ins.tree(var3, var6);
            }
         }
      }

      this.a(var2, var7);
   }

   private Map a(User var1, long var2) {
      HashMap var4 = new HashMap();
      String var5 = "view";

      for(RuleFile var8 : (Iterable<RuleFile>)(Iterable<?>)(FileService.ins.menus(var2))) {
         RuleFileType var9 = RuleFileType.getRuleFileType(var8.getType());
         if (RuleFileType.General != var9 && var9 != null) {
            boolean var10 = AuthenticationManager.decide(var1, RoleCategory.project, var9.getModel(), var5);
            var4.put(var9.name(), var10);
         }
      }

      var4.put(RuleFileType.CrossDecisionTable.name(), var4.get(RuleFileType.DecisionTable.name()));
      var4.put(RuleFileType.ComplexScorecard.name(), var4.get(RuleFileType.Scorecard.name()));
      return var4;
   }

   private void a(List var1, Map var2) {
      ArrayList var3 = new ArrayList();

      for(RuleFile var5 : (Iterable<RuleFile>)(Iterable<?>)(var1)) {
         if (RuleFileType.General.name().equals(var5.getType()) && var5.isDirectory()) {
            this.a(var5.getChildren(), var2);
         } else {
            boolean var6 = true;
            RuleFileType var7 = RuleFileType.getRuleFileType(var5.getType());
            if (var2.containsKey(var7.name())) {
               var6 = (Boolean)var2.get(var7.name());
            }

            if (!var6) {
               var3.add(var5);
            } else if (var5.isDirectory()) {
               this.a(var5.getChildren(), var2);
            }
         }
      }

      for(RuleFile var9 : (Iterable<RuleFile>)(Iterable<?>)(var3)) {
         var1.remove(var9);
      }

   }

   public void validate(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      HashMap var3 = new HashMap();
      var3.put("reg", DynamicSpringConfigLoaderImpl.getAuthInfo() != null);
      if (DynamicSpringConfigLoaderImpl.getAuthInfo() == null) {
         var3.put("expired", DynamicSpringConfigLoaderImpl.getTrialExpired());
      } else {
         var3.put("expired", DynamicSpringConfigLoaderImpl.getLimit());
         var3.put("authInfo", DynamicSpringConfigLoaderImpl.getAuthInfo());
         var3.put("limitedDate", DynamicSpringConfigLoaderImpl.getLimitDate());
      }

      this.a(var2, var3);
   }

   public String url() {
      return "/studio";
   }
}
