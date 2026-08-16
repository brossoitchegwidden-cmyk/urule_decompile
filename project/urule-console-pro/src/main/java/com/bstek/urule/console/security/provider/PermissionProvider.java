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
      ArrayList var0 = new ArrayList();
      a(var0);
      return var0;
   }

   private static List a(boolean var0, boolean var1, boolean var2) {
      ArrayList var3 = new ArrayList();
      if (var0) {
         var3.add(GroupRoleEnum.Owner.name());
      }

      if (var1) {
         var3.add(GroupRoleEnum.Manager.name());
      }

      if (var2) {
         var3.add(GroupRoleEnum.User.name());
      }

      return var3;
   }

   private static void a(List var0) {
      Module var1 = new Module(GroupModule.projects.name(), GroupModule.projects.getLabel(), RoleCategory.group);
      var1.getItems().add(new Permission("view", "查看页面", a(true, true, true), PermissionType.view));
      var1.getItems().add(new Permission("add", "创建项目", a(true, true, false), PermissionType.fun));
      var1.getItems().add(new Permission("update", "编辑项目", a(true, true, false), PermissionType.fun));
      var1.getItems().add(new Permission("remove", "删除项目", a(true, true, false), PermissionType.fun));
      var1.getItems().add(new Permission("import", "项目导入", a(true, true, false), PermissionType.fun));
      var1.getItems().add(new Permission("export", "项目导出", a(true, true, false), PermissionType.fun));
      var0.add(var1);
      var1 = new Module(GroupModule.members.name(), GroupModule.members.getLabel(), RoleCategory.group);
      var1.getItems().add(new Permission("view", "查看页面", a(true, true, false), PermissionType.view));
      var1.getItems().add(new Permission("join", "邀请成员", a(true, true, false), PermissionType.fun));
      var1.getItems().add(new Permission("add", "添加成员", a(true, true, false), PermissionType.fun));
      var1.getItems().add(new Permission("remove", "删除成员", a(true, true, false), PermissionType.fun));
      var1.getItems().add(new Permission("userrole", "调整成员角色", a(true, true, false), PermissionType.fun));
      var0.add(var1);
      var1 = new Module(GroupModule.permissions.name(), GroupModule.permissions.getLabel(), RoleCategory.group);
      var1.getItems().add(new Permission("view", "查看页面", a(true, true, false), PermissionType.view));
      var1.getItems().add(new Permission("manager", "管理权限", a(true, true, false), PermissionType.fun));
      var0.add(var1);
      var1 = new Module(GroupModule.setting.name(), GroupModule.setting.getLabel(), RoleCategory.group);
      var1.getItems().add(new Permission("view", "查看页面", a(true, true, false), PermissionType.view));
      var1.getItems().add(new Permission("update", "编辑", a(true, true, false), PermissionType.fun));
      var0.add(var1);
      var1 = new Module(GroupModule.logs.name(), "日志", RoleCategory.group);
      var1.getItems().add(new Permission("view", "查看页面", a(true, true, true), PermissionType.view));
      var1.getItems().add(new Permission("export", "导出日志", a(true, true, false), PermissionType.fun));
      var0.add(var1);
      var1 = new Module(GroupModule.clusterUrls.name(), "集群URL配置", "group/cluster", RoleCategory.group);
      var1.getItems().add(new Permission("view", "查看页面", a(true, true, false), PermissionType.view));
      var1.getItems().add(new Permission("manager", "管理权限", a(true, true, false), PermissionType.fun));
      var0.add(var1);
      var1 = new Module(GroupModule.clientUrls.name(), "客户端URL配置", "group/client", RoleCategory.group);
      var1.getItems().add(new Permission("view", "查看页面", a(true, true, false), PermissionType.view));
      var1.getItems().add(new Permission("manager", "管理权限", a(true, true, false), PermissionType.fun));
      var0.add(var1);
      var1 = new Module(GroupModule.dynamicJar.name(), "Jar文件热部署", "group/jar", RoleCategory.group);
      var1.getItems().add(new Permission("view", "查看页面", a(true, true, false), PermissionType.view));
      var1.getItems().add(new Permission("manager", "管理权限", a(true, true, false), PermissionType.fun));
      var0.add(var1);
      var1 = new Module(GroupModule.datasource.name(), "数据源配置", "group/datasource", RoleCategory.group);
      var1.getItems().add(new Permission("view", "查看页面", a(true, true, false), PermissionType.view));
      var1.getItems().add(new Permission("manager", "管理权限", a(true, true, false), PermissionType.fun));
      var0.add(var1);
   }

   public static Module getGroupModule(String var0) {
      return getModule(RoleCategory.group, getGroupModules(), var0);
   }

   public static Module getProjectModule(String var0) {
      return getModule(RoleCategory.project, getProjectModules(), var0);
   }

   public static Module getModule(RoleCategory var0, List var1, String var2) {
      Module var3 = null;

      for(Module var5 : (Iterable<Module>)(Iterable<?>)(var1)) {
         String var6 = var0 + "/" + var5.getCode();
         if (var2.indexOf(var6) > -1) {
            var3 = var5;
            break;
         }

         if (null != var5.getUrls()) {
            for(String var8 : (Iterable<String>)(Iterable<?>)(var5.getUrls())) {
               if (var2.indexOf(var8) > -1) {
                  var3 = var5;
                  break;
               }
            }
         }
      }

      return var3;
   }

   public static List getProjectModules() {
      ArrayList var0 = new ArrayList();
      b(var0);
      return var0;
   }

   private static List a(boolean var0, boolean var1, ProjectRoleEnum var2) {
      ArrayList var3 = new ArrayList();
      if (var0) {
         var3.add(ProjectRoleEnum.Manager.name());
      }

      if (var1) {
         var3.add(ProjectRoleEnum.User.name());
      }

      if (null != var2) {
         var3.add(var2.name());
      }

      return var3;
   }

   private static void b(List var0) {
      Module var1 = new Module(ProjectModule.project.name(), ProjectModule.members.getLabel(), RoleCategory.project);
      var1.getItems().add(new Permission("remove", "删除项目", a(true, false, (ProjectRoleEnum)null), PermissionType.fun));
      var1.getItems().add(new Permission("export", "导出", a(true, false, (ProjectRoleEnum)null), PermissionType.fun));
      var0.add(var1);
      var1 = new Module(ProjectModule.members.name(), ProjectModule.members.getLabel(), RoleCategory.project);
      var1.getItems().add(new Permission("view", "查看页面", a(true, false, (ProjectRoleEnum)null), PermissionType.view));
      var1.getItems().add(new Permission("add", "添加", a(true, false, (ProjectRoleEnum)null), PermissionType.fun));
      var1.getItems().add(new Permission("remove", "删除", a(true, false, (ProjectRoleEnum)null), PermissionType.fun));
      var1.getItems().add(new Permission("userrole", "调整成员角色", a(true, false, (ProjectRoleEnum)null), PermissionType.fun));
      var0.add(var1);
      var1 = new Module(ProjectModule.permissions.name(), ProjectModule.permissions.getLabel(), RoleCategory.project);
      var1.getItems().add(new Permission("view", "查看页面", a(true, false, (ProjectRoleEnum)null), PermissionType.view));
      var1.getItems().add(new Permission("manager", "管理权限", a(true, false, (ProjectRoleEnum)null), PermissionType.fun));
      var0.add(var1);
      var1 = new Module(ProjectModule.setting.name(), ProjectModule.setting.getLabel(), RoleCategory.project);
      var1.getItems().add(new Permission("view", "查看页面", a(true, false, (ProjectRoleEnum)null), PermissionType.view));
      var1.getItems().add(new Permission("update", "编辑", a(true, false, (ProjectRoleEnum)null), PermissionType.fun));
      var1.getItems().add(new Permission("remove", "删除", a(true, false, (ProjectRoleEnum)null), PermissionType.fun));
      var1.getItems().add(new Permission("approveUser", "规则审批人设置", a(true, false, (ProjectRoleEnum)null), PermissionType.fun));
      var0.add(var1);
      var1 = new Module(RuleFileType.Knowledge.getModel(), RuleFileType.Knowledge.getLabel() + "管理", RoleCategory.project);
      var1.setCategory(ModuleType.rule);
      var1.getItems().add(new Permission("view", "查看页面", a(true, true, ProjectRoleEnum.Knowledge), PermissionType.view));
      var1.getItems().add(new Permission("manager", "管理", a(true, false, ProjectRoleEnum.Knowledge), PermissionType.fun));
      var0.add(var1);
      var1 = new Module(RuleFileType.Batch.name(), RuleFileType.Batch.getLabel() + "管理", RoleCategory.project);
      var1.setCategory(ModuleType.rule);
      var1.getItems().add(new Permission("view", "查看页面", a(true, true, ProjectRoleEnum.Batch), PermissionType.view));
      var1.getItems().add(new Permission("manager", "管理", a(true, false, ProjectRoleEnum.Batch), PermissionType.fun));
      var0.add(var1);
      var0.add(a(RuleFileType.Library, "variable,parameter,constant,action"));
      var0.add(a(RuleFileType.RuleSet, "ruleset"));
      var0.add(a(RuleFileType.DecisionTable, "decision/table,decision/crosstab"));
      var0.add(a(RuleFileType.DecisionTree, "decision/tree/modern,decision/tree/tradition"));
      var0.add(a(RuleFileType.Scorecard, "scorecard/simple,scorecard/complex"));
      var0.add(a(RuleFileType.Flow, "flow"));
      var0.add(a(RuleFileType.ConditionTemplate, "template/condition"));
      var0.add(a(RuleFileType.ActionTemplate, "template/action"));
   }

   private static Module a(RuleFileType var0, String var1) {
      String var2 = "管理";
      Module var3 = new Module(var0.getModel(), var0.getLabel() + var2, var1, RoleCategory.project);
      var3.setCategory(ModuleType.rule);
      var3.getItems().add(new Permission("view", "查看页面", a(true, true, ProjectRoleEnum.valueOf(var0.name())), PermissionType.view));
      var3.getItems().add(new Permission("add", "添加", a(true, false, ProjectRoleEnum.valueOf(var0.name())), PermissionType.fun));
      var3.getItems().add(new Permission("update", "编辑", a(true, false, ProjectRoleEnum.valueOf(var0.name())), PermissionType.fun));
      var3.getItems().add(new Permission("remove", "删除", a(true, false, ProjectRoleEnum.valueOf(var0.name())), PermissionType.fun));
      return var3;
   }
}
