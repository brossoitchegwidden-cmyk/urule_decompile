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
   /**加载顶级菜单*/
   public void menu(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      Long projectId = ContextHolder.getProjectId();
      User loginUser = SecurityUtils.getLoginUser(req);
      List items = FileService.ins.menus(projectId);
      ArrayList items2 = new ArrayList();
      Project project = ProjectManager.ins.get(projectId);
      if (project != null) {
         for(RuleFile ruleFile : (Iterable<RuleFile>)(Iterable<?>)(items)) {
            RuleFileType ruleFileType = RuleFileType.getRuleFileType(ruleFile.getType());
            if (ruleFileType == RuleFileType.General) {
               if (project.getViewModel() == ProjectViewModel.general) {
                  items2.add(ruleFile);
               }
            } else if (ruleFileType != null && project.getViewModel() != ProjectViewModel.general) {
               boolean flag = AuthenticationManager.decide(loginUser, RoleCategory.project, ruleFileType.getModel(), "view");
               if (flag) {
                  items2.add(ruleFile);
               }
            }
         }
      }

      this.writeObjectToJson(resp, items2);
   }

   /**加载规则文件*/
   public void tree(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      Long projectId = ContextHolder.getProjectId();
      User loginUser = SecurityUtils.getLoginUser(req);
      String parameter = req.getParameter("type");
      RuleFileType ruleFileType = RuleFileType.getRuleFileType(parameter);
      Object objectValue = new ArrayList();
      if (ruleFileType != null) {
         if (ruleFileType == RuleFileType.General) {
            Map valuesByKey = this.buildViewPermissions(loginUser, projectId);
            objectValue = FileService.ins.tree(projectId, ruleFileType);
            this.filterUnauthorizedFiles((List)objectValue, valuesByKey);
         } else {
            boolean flag = AuthenticationManager.decide(loginUser, RoleCategory.project, ruleFileType.getModel(), "view");
            if (flag) {
               objectValue = FileService.ins.tree(projectId, ruleFileType);
            }
         }
      }

      this.writeObjectToJson(resp, objectValue);
   }

   private Map buildViewPermissions(User user, long longValue) {
      HashMap valuesByKey = new HashMap();
      String text = "view";

      for(RuleFile ruleFile : (Iterable<RuleFile>)(Iterable<?>)(FileService.ins.menus(longValue))) {
         RuleFileType ruleFileType = RuleFileType.getRuleFileType(ruleFile.getType());
         if (RuleFileType.General != ruleFileType && ruleFileType != null) {
            boolean flag = AuthenticationManager.decide(user, RoleCategory.project, ruleFileType.getModel(), text);
            valuesByKey.put(ruleFileType.name(), flag);
         }
      }

      valuesByKey.put(RuleFileType.CrossDecisionTable.name(), valuesByKey.get(RuleFileType.DecisionTable.name()));
      valuesByKey.put(RuleFileType.ComplexScorecard.name(), valuesByKey.get(RuleFileType.Scorecard.name()));
      return valuesByKey;
   }

   private void filterUnauthorizedFiles(List items, Map valuesByKey) {
      ArrayList items2 = new ArrayList();

      for(RuleFile ruleFile : (Iterable<RuleFile>)(Iterable<?>)(items)) {
         if (RuleFileType.General.name().equals(ruleFile.getType()) && ruleFile.isDirectory()) {
            this.filterUnauthorizedFiles(ruleFile.getChildren(), valuesByKey);
         } else {
            boolean flag = true;
            RuleFileType ruleFileType = RuleFileType.getRuleFileType(ruleFile.getType());
            if (valuesByKey.containsKey(ruleFileType.name())) {
               flag = (Boolean)valuesByKey.get(ruleFileType.name());
            }

            if (!flag) {
               items2.add(ruleFile);
            } else if (ruleFile.isDirectory()) {
               this.filterUnauthorizedFiles(ruleFile.getChildren(), valuesByKey);
            }
         }
      }

      for(RuleFile ruleFile2 : (Iterable<RuleFile>)(Iterable<?>)(items2)) {
         items.remove(ruleFile2);
      }

   }

   public void validate(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      HashMap valuesByKey = new HashMap();
      valuesByKey.put("reg", DynamicSpringConfigLoaderImpl.getAuthInfo() != null);
      if (DynamicSpringConfigLoaderImpl.getAuthInfo() == null) {
         valuesByKey.put("expired", DynamicSpringConfigLoaderImpl.getTrialExpired());
      } else {
         valuesByKey.put("expired", DynamicSpringConfigLoaderImpl.getLimit());
         valuesByKey.put("authInfo", DynamicSpringConfigLoaderImpl.getAuthInfo());
         valuesByKey.put("limitedDate", DynamicSpringConfigLoaderImpl.getLimitDate());
      }

      this.writeObjectToJson(resp, valuesByKey);
   }

   public String url() {
      return "/studio";
   }
}
