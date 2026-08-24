package com.bstek.urule.console.security.provider;

import com.bstek.urule.console.security.entity.Module;
import com.bstek.urule.console.security.entity.Permission;
import com.bstek.urule.console.type.GroupModule;
import com.bstek.urule.console.type.GroupRoleEnum;
import com.bstek.urule.console.type.ModuleType;
import com.bstek.urule.console.type.PermissionType;
import com.bstek.urule.console.type.ProjectModule;
import com.bstek.urule.console.type.ProjectRoleEnum;
import com.bstek.urule.console.type.RoleCategory;
import com.bstek.urule.console.type.RuleFileType;
import java.util.ArrayList;
import java.util.List;

public class PermissionProvider {
   public static final String VIEW_CODE = "view";

   public static List getGroupModules() {
      ArrayList groupModules = new ArrayList();
      buildPermissions(groupModules);
      return groupModules;
   }

   private static List buildPermissions(boolean flag, boolean flag2, boolean flag3) {
      ArrayList items = new ArrayList();
      if (flag) {
         items.add(GroupRoleEnum.Owner.name());
      }

      if (flag2) {
         items.add(GroupRoleEnum.Manager.name());
      }

      if (flag3) {
         items.add(GroupRoleEnum.User.name());
      }

      return items;
   }

   private static void buildPermissions(List items) {
      Module module = new Module(GroupModule.projects.name(), GroupModule.projects.getLabel(), RoleCategory.group);
      module.getItems().add(new Permission("view", "查看页面", buildPermissions(true, true, true), PermissionType.view));
      module.getItems().add(new Permission("add", "创建项目", buildPermissions(true, true, false), PermissionType.fun));
      module.getItems().add(new Permission("update", "编辑项目", buildPermissions(true, true, false), PermissionType.fun));
      module.getItems().add(new Permission("remove", "删除项目", buildPermissions(true, true, false), PermissionType.fun));
      module.getItems().add(new Permission("import", "项目导入", buildPermissions(true, true, false), PermissionType.fun));
      module.getItems().add(new Permission("export", "项目导出", buildPermissions(true, true, false), PermissionType.fun));
      items.add(module);
      module = new Module(GroupModule.members.name(), GroupModule.members.getLabel(), RoleCategory.group);
      module.getItems().add(new Permission("view", "查看页面", buildPermissions(true, true, false), PermissionType.view));
      module.getItems().add(new Permission("join", "邀请成员", buildPermissions(true, true, false), PermissionType.fun));
      module.getItems().add(new Permission("add", "添加成员", buildPermissions(true, true, false), PermissionType.fun));
      module.getItems().add(new Permission("remove", "删除成员", buildPermissions(true, true, false), PermissionType.fun));
      module.getItems().add(new Permission("userrole", "调整成员角色", buildPermissions(true, true, false), PermissionType.fun));
      items.add(module);
      module = new Module(GroupModule.permissions.name(), GroupModule.permissions.getLabel(), RoleCategory.group);
      module.getItems().add(new Permission("view", "查看页面", buildPermissions(true, true, false), PermissionType.view));
      module.getItems().add(new Permission("manager", "管理权限", buildPermissions(true, true, false), PermissionType.fun));
      items.add(module);
      module = new Module(GroupModule.setting.name(), GroupModule.setting.getLabel(), RoleCategory.group);
      module.getItems().add(new Permission("view", "查看页面", buildPermissions(true, true, false), PermissionType.view));
      module.getItems().add(new Permission("update", "编辑", buildPermissions(true, true, false), PermissionType.fun));
      items.add(module);
      module = new Module(GroupModule.logs.name(), "日志", RoleCategory.group);
      module.getItems().add(new Permission("view", "查看页面", buildPermissions(true, true, true), PermissionType.view));
      module.getItems().add(new Permission("export", "导出日志", buildPermissions(true, true, false), PermissionType.fun));
      items.add(module);
      module = new Module(GroupModule.clusterUrls.name(), "集群URL配置", "group/cluster", RoleCategory.group);
      module.getItems().add(new Permission("view", "查看页面", buildPermissions(true, true, false), PermissionType.view));
      module.getItems().add(new Permission("manager", "管理权限", buildPermissions(true, true, false), PermissionType.fun));
      items.add(module);
      module = new Module(GroupModule.clientUrls.name(), "客户端URL配置", "group/client", RoleCategory.group);
      module.getItems().add(new Permission("view", "查看页面", buildPermissions(true, true, false), PermissionType.view));
      module.getItems().add(new Permission("manager", "管理权限", buildPermissions(true, true, false), PermissionType.fun));
      items.add(module);
      module = new Module(GroupModule.dynamicJar.name(), "Jar文件热部署", "group/jar", RoleCategory.group);
      module.getItems().add(new Permission("view", "查看页面", buildPermissions(true, true, false), PermissionType.view));
      module.getItems().add(new Permission("manager", "管理权限", buildPermissions(true, true, false), PermissionType.fun));
      items.add(module);
      module = new Module(GroupModule.datasource.name(), "数据源配置", "group/datasource", RoleCategory.group);
      module.getItems().add(new Permission("view", "查看页面", buildPermissions(true, true, false), PermissionType.view));
      module.getItems().add(new Permission("manager", "管理权限", buildPermissions(true, true, false), PermissionType.fun));
      items.add(module);
   }

   public static Module getGroupModule(String url) {
      return getModule(RoleCategory.group, getGroupModules(), url);
   }

   public static Module getProjectModule(String url) {
      return getModule(RoleCategory.project, getProjectModules(), url);
   }

   public static Module getModule(RoleCategory roleCategory, List models, String url) {
      Module module = null;

      for(Module module2 : (Iterable<Module>)(Iterable<?>)(models)) {
         String text = roleCategory + "/" + module2.getCode();
         if (url.indexOf(text) > -1) {
            module = module2;
            break;
         }

         if (null != module2.getUrls()) {
            for(String text2 : (Iterable<String>)(Iterable<?>)(module2.getUrls())) {
               if (url.indexOf(text2) > -1) {
                  module = module2;
                  break;
               }
            }
         }
      }

      return module;
   }

   public static List getProjectModules() {
      ArrayList projectModules = new ArrayList();
      populateProjectModules(projectModules);
      return projectModules;
   }

   private static List buildPermissions(boolean flag, boolean flag2, ProjectRoleEnum projectRoleEnum) {
      ArrayList items = new ArrayList();
      if (flag) {
         items.add(ProjectRoleEnum.Manager.name());
      }

      if (flag2) {
         items.add(ProjectRoleEnum.User.name());
      }

      if (null != projectRoleEnum) {
         items.add(projectRoleEnum.name());
      }

      return items;
   }

   private static void populateProjectModules(List items) {
      Module module = new Module(ProjectModule.project.name(), ProjectModule.members.getLabel(), RoleCategory.project);
      module.getItems().add(new Permission("remove", "删除项目", buildPermissions(true, false, (ProjectRoleEnum)null), PermissionType.fun));
      module.getItems().add(new Permission("export", "导出", buildPermissions(true, false, (ProjectRoleEnum)null), PermissionType.fun));
      items.add(module);
      module = new Module(ProjectModule.members.name(), ProjectModule.members.getLabel(), RoleCategory.project);
      module.getItems().add(new Permission("view", "查看页面", buildPermissions(true, false, (ProjectRoleEnum)null), PermissionType.view));
      module.getItems().add(new Permission("add", "添加", buildPermissions(true, false, (ProjectRoleEnum)null), PermissionType.fun));
      module.getItems().add(new Permission("remove", "删除", buildPermissions(true, false, (ProjectRoleEnum)null), PermissionType.fun));
      module.getItems().add(new Permission("userrole", "调整成员角色", buildPermissions(true, false, (ProjectRoleEnum)null), PermissionType.fun));
      items.add(module);
      module = new Module(ProjectModule.permissions.name(), ProjectModule.permissions.getLabel(), RoleCategory.project);
      module.getItems().add(new Permission("view", "查看页面", buildPermissions(true, false, (ProjectRoleEnum)null), PermissionType.view));
      module.getItems().add(new Permission("manager", "管理权限", buildPermissions(true, false, (ProjectRoleEnum)null), PermissionType.fun));
      items.add(module);
      module = new Module(ProjectModule.setting.name(), ProjectModule.setting.getLabel(), RoleCategory.project);
      module.getItems().add(new Permission("view", "查看页面", buildPermissions(true, false, (ProjectRoleEnum)null), PermissionType.view));
      module.getItems().add(new Permission("update", "编辑", buildPermissions(true, false, (ProjectRoleEnum)null), PermissionType.fun));
      module.getItems().add(new Permission("remove", "删除", buildPermissions(true, false, (ProjectRoleEnum)null), PermissionType.fun));
      module.getItems().add(new Permission("approveUser", "规则审批人设置", buildPermissions(true, false, (ProjectRoleEnum)null), PermissionType.fun));
      items.add(module);
      module = new Module(RuleFileType.Knowledge.getModel(), RuleFileType.Knowledge.getLabel() + "管理", RoleCategory.project);
      module.setCategory(ModuleType.rule);
      module.getItems().add(new Permission("view", "查看页面", buildPermissions(true, true, ProjectRoleEnum.Knowledge), PermissionType.view));
      module.getItems().add(new Permission("manager", "管理", buildPermissions(true, false, ProjectRoleEnum.Knowledge), PermissionType.fun));
      items.add(module);
      module = new Module(RuleFileType.Batch.name(), RuleFileType.Batch.getLabel() + "管理", RoleCategory.project);
      module.setCategory(ModuleType.rule);
      module.getItems().add(new Permission("view", "查看页面", buildPermissions(true, true, ProjectRoleEnum.Batch), PermissionType.view));
      module.getItems().add(new Permission("manager", "管理", buildPermissions(true, false, ProjectRoleEnum.Batch), PermissionType.fun));
      items.add(module);
      items.add(buildPermissions(RuleFileType.Library, "variable,parameter,constant,action"));
      items.add(buildPermissions(RuleFileType.RuleSet, "ruleset"));
      items.add(buildPermissions(RuleFileType.DecisionTable, "decision/table,decision/crosstab"));
      items.add(buildPermissions(RuleFileType.DecisionTree, "decision/tree/modern,decision/tree/tradition"));
      items.add(buildPermissions(RuleFileType.Scorecard, "scorecard/simple,scorecard/complex"));
      items.add(buildPermissions(RuleFileType.Flow, "flow"));
      items.add(buildPermissions(RuleFileType.ConditionTemplate, "template/condition"));
      items.add(buildPermissions(RuleFileType.ActionTemplate, "template/action"));
   }

   private static Module buildPermissions(RuleFileType ruleFileType, String text) {
      String text2 = "管理";
      Module module = new Module(ruleFileType.getModel(), ruleFileType.getLabel() + text2, text, RoleCategory.project);
      module.setCategory(ModuleType.rule);
      module.getItems().add(new Permission("view", "查看页面", buildPermissions(true, true, ProjectRoleEnum.valueOf(ruleFileType.name())), PermissionType.view));
      module.getItems().add(new Permission("add", "添加", buildPermissions(true, false, ProjectRoleEnum.valueOf(ruleFileType.name())), PermissionType.fun));
      module.getItems().add(new Permission("update", "编辑", buildPermissions(true, false, ProjectRoleEnum.valueOf(ruleFileType.name())), PermissionType.fun));
      module.getItems().add(new Permission("remove", "删除", buildPermissions(true, false, ProjectRoleEnum.valueOf(ruleFileType.name())), PermissionType.fun));
      return module;
   }
}
