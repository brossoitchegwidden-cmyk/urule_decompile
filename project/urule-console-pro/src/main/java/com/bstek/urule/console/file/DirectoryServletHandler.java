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
   public void add(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("name");
      String parameter2 = req.getParameter("type");
      long id = Long.valueOf(req.getParameter("parentId"));
      long projectId = ContextHolder.getProjectId();
      if (id > 0L) {
         RuleFile ruleFile = DirectoryManager.ins.get(id);
         if (null == ruleFile) {
            throw new InfoException("父目录不存在!");
         }

         projectId = ruleFile.getProjectId();
      }

      if (!StringUtils.isBlank(parameter) && !StringUtils.isBlank(parameter2)) {
         parameter = parameter.trim();
         String[] parts = parameter.split("\\.");
         if (parts.length == 1) {
            parts = parameter.split("/");
         }

         RuleFile ruleFile2 = null;
         RuleFile ruleFile3 = null;

         for(String text : parts) {
            boolean flag = DirectoryManager.ins.checkExist(projectId, id, parameter2, text);
            if (flag) {
               throw new InfoException("目录【" + parameter + "】已存在！");
            }

            if (ruleFile3 != null) {
               id = ruleFile3.getId();
            }

            if (!StringUtils.isLetterDigitOrChinese(text)) {
               throw new InfoException("无效名称,只能使用中英文字母,数字和下划线");
            }

            RuleFile ruleFile4 = new RuleFile();
            ruleFile4.setDirectory(true);
            ruleFile4.setProjectId(projectId);
            ruleFile4.setParentId(id);
            ruleFile4.setName(text);
            ruleFile4.setType(parameter2);
            ruleFile4.setCreateUser(SecurityUtils.getLoginUsername(req));
            DirectoryManager.ins.add(ruleFile4);
            SystemLogUtils.addRuleFileOperationLog(parameter2, "add", ruleFile4.getId(), String.format("Create a new %s type folder %s[%s]", parameter2, parameter, ruleFile4.getId()));
            if (ruleFile2 == null) {
               ruleFile2 = ruleFile4;
               ruleFile3 = ruleFile4;
            } else {
               ArrayList items = new ArrayList();
               items.add(ruleFile4);
               if (ruleFile3 != null) {
                  ruleFile3.setChildren(items);
               }

               ruleFile3 = ruleFile4;
            }
         }

         this.writeObjectToJson(resp, ruleFile2);
      } else {
         throw new InfoException("文件名称或类型不能为空!");
      }
   }

   @Transactional
   public void remove(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      User loginUser = SecurityUtils.getLoginUser(req);
      String parameter = req.getParameter("id");
      boolean flag = Boolean.valueOf(req.getParameter("force"));
      String[] parts = parameter.split(",");

      for(String text : parts) {
         Long longValue = Long.valueOf(text);
         RuleFile ruleFile = DirectoryManager.ins.get(longValue);
         RuleFileType ruleFileType = RuleFileType.getRuleFileType(ruleFile.getType());
         boolean flag2 = AuthenticationManager.decide(loginUser, RoleCategory.project, ruleFileType.getModel(), "remove");
         if (!flag2) {
            throw new PermissionDeniedException();
         }

         if (ruleFile != null) {
            FileService.ins.removeDir(ruleFile, flag);
            SystemLogUtils.addRuleFileOperationLog(ruleFile.getType(), "remove", ruleFile.getId(), String.format("Remove %s type file %s[%s]", ruleFile.getType(), ruleFile.getName(), ruleFile.getId()));
         }
      }

   }

   @URuleAuthorization(
      authType = "project",
      code = "update",
      ruleDir = true
   )
   @Transactional
   public void move(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("parentId"));
      String parameter = req.getParameter("id");
      String[] parts = parameter.split(",");

      for(String text : parts) {
         Long longValue2 = Long.valueOf(text);
         RuleFile ruleFile = DirectoryManager.ins.get(longValue2);
         RuleFile ruleFile2 = DirectoryManager.ins.get(longValue);
         if (ruleFile != null && ruleFile2 != null) {
            if (!ruleFile.getType().equals(ruleFile2.getType())) {
               throw new RuleException("文件类型限制，不支持此操作!<br>File type restrictions, this operation is not supported!");
            }

            DirectoryManager.ins.changeParent(longValue2, longValue);
         }
      }

   }

   @URuleAuthorization(
      authType = "project",
      code = "update",
      ruleDir = true
   )
   public void rename(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("newName");
      long longValue = Long.valueOf(req.getParameter("id"));
      RuleFile ruleFile = DirectoryManager.ins.get(longValue);
      if (ruleFile != null) {
         boolean flag = DirectoryManager.ins.checkExist(ContextHolder.getProjectId(), longValue, ruleFile.getType(), parameter);
         if (flag) {
            throw new InfoException("文件【" + parameter + "】已存在！");
         }

         DirectoryManager.ins.changeName(longValue, parameter, SecurityUtils.getLoginUsername(req));
         SystemLogUtils.addRuleFileOperationLog(ruleFile.getType(), "rename", longValue, String.format("Rename directory %s of %s type to %s", ruleFile.getName(), ruleFile.getType(), parameter));
      }

   }

   public String url() {
      return "/directory";
   }
}
