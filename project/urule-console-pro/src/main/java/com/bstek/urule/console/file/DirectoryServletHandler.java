package com.bstek.urule.console.file;

import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.ContextHolder;
import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.PermissionDeniedException;
import com.bstek.urule.console.Transactional;
import com.bstek.urule.console.admin.log.SystemLogUtils;
import com.bstek.urule.console.database.manager.file.DirectoryManager;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.service.file.FileService;
import com.bstek.urule.console.security.AuthenticationManager;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.security.URuleAuthorization;
import com.bstek.urule.console.security.entity.User;
import com.bstek.urule.console.type.RoleCategory;
import com.bstek.urule.console.type.RuleFileType;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DirectoryServletHandler extends ApiServletHandler {
   @URuleAuthorization(
      authType = "project",
      code = "add",
      ruleDir = true
   )
   @Transactional
   public void add(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("name");
      String var4 = var1.getParameter("type");
      long var5 = Long.valueOf(var1.getParameter("parentId"));
      long var7 = ContextHolder.getProjectId();
      if (var5 > 0L) {
         RuleFile var9 = DirectoryManager.ins.get(var5);
         if (null == var9) {
            throw new InfoException("父目录不存在!");
         }

         var7 = var9.getProjectId();
      }

      if (!StringUtils.isBlank(var3) && !StringUtils.isBlank(var4)) {
         var3 = var3.trim();
         String[] var20 = var3.split("\\.");
         if (var20.length == 1) {
            var20 = var3.split("/");
         }

         RuleFile var10 = null;
         RuleFile var11 = null;

         for(String var15 : var20) {
            boolean var16 = DirectoryManager.ins.checkExist(var7, var5, var4, var15);
            if (var16) {
               throw new InfoException("目录【" + var3 + "】已存在！");
            }

            if (var11 != null) {
               var5 = var11.getId();
            }

            if (!StringUtils.isLetterDigitOrChinese(var15)) {
               throw new InfoException("无效名称,只能使用中英文字母,数字和下划线");
            }

            RuleFile var17 = new RuleFile();
            var17.setDirectory(true);
            var17.setProjectId(var7);
            var17.setParentId(var5);
            var17.setName(var15);
            var17.setType(var4);
            var17.setCreateUser(SecurityUtils.getLoginUsername(var1));
            DirectoryManager.ins.add(var17);
            SystemLogUtils.addRuleFileOperationLog(var4, "add", var17.getId(), String.format("Create a new %s type folder %s[%s]", var4, var3, var17.getId()));
            if (var10 == null) {
               var10 = var17;
               var11 = var17;
            } else {
               ArrayList var18 = new ArrayList();
               var18.add(var17);
               if (var11 != null) {
                  var11.setChildren(var18);
               }

               var11 = var17;
            }
         }

         this.a(var2, var10);
      } else {
         throw new InfoException("文件名称或类型不能为空!");
      }
   }

   @Transactional
   public void remove(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      User var3 = SecurityUtils.getLoginUser(var1);
      String var4 = var1.getParameter("id");
      boolean var5 = Boolean.valueOf(var1.getParameter("force"));
      String[] var6 = var4.split(",");

      for(String var10 : var6) {
         Long var11 = Long.valueOf(var10);
         RuleFile var12 = DirectoryManager.ins.get(var11);
         RuleFileType var13 = RuleFileType.getRuleFileType(var12.getType());
         boolean var14 = AuthenticationManager.decide(var3, RoleCategory.project, var13.getModel(), "remove");
         if (!var14) {
            throw new PermissionDeniedException();
         }

         if (var12 != null) {
            FileService.ins.removeDir(var12, var5);
            SystemLogUtils.addRuleFileOperationLog(var12.getType(), "remove", var12.getId(), String.format("Remove %s type file %s[%s]", var12.getType(), var12.getName(), var12.getId()));
         }
      }

   }

   @URuleAuthorization(
      authType = "project",
      code = "update",
      ruleDir = true
   )
   @Transactional
   public void move(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("parentId"));
      String var5 = var1.getParameter("id");
      String[] var6 = var5.split(",");

      for(String var10 : var6) {
         Long var11 = Long.valueOf(var10);
         RuleFile var12 = DirectoryManager.ins.get(var11);
         RuleFile var13 = DirectoryManager.ins.get(var3);
         if (var12 != null && var13 != null) {
            if (!var12.getType().equals(var13.getType())) {
               throw new RuleException("文件类型限制，不支持此操作!<br>File type restrictions, this operation is not supported!");
            }

            DirectoryManager.ins.changeParent(var11, var3);
         }
      }

   }

   @URuleAuthorization(
      authType = "project",
      code = "update",
      ruleDir = true
   )
   public void rename(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("newName");
      long var4 = Long.valueOf(var1.getParameter("id"));
      RuleFile var6 = DirectoryManager.ins.get(var4);
      if (var6 != null) {
         boolean var7 = DirectoryManager.ins.checkExist(ContextHolder.getProjectId(), var4, var6.getType(), var3);
         if (var7) {
            throw new InfoException("文件【" + var3 + "】已存在！");
         }

         DirectoryManager.ins.changeName(var4, var3, SecurityUtils.getLoginUsername(var1));
         SystemLogUtils.addRuleFileOperationLog(var6.getType(), "rename", var4, String.format("Rename directory %s of %s type to %s", var6.getName(), var6.getType(), var3));
      }

   }

   public String url() {
      return "/directory";
   }
}
