package com.bstek.urule.console.file;

import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.ContextHolder;
import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.ParameterInvaidException;
import com.bstek.urule.console.PermissionDeniedException;
import com.bstek.urule.console.Transactional;
import com.bstek.urule.console.admin.log.SystemLogUtils;
import com.bstek.urule.console.database.manager.file.DirectoryManager;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.manager.file.FileQuery;
import com.bstek.urule.console.database.manager.file.version.VersionFileManager;
import com.bstek.urule.console.database.manager.file.version.VersionFileQuery;
import com.bstek.urule.console.database.manager.project.ProjectManager;
import com.bstek.urule.console.database.manager.project.role.ProjectRoleManager;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.Role;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.model.VersionFile;
import com.bstek.urule.console.database.service.file.FileService;
import com.bstek.urule.console.database.service.reference.ReferenceService;
import com.bstek.urule.console.editor.FileDeserializer;
import com.bstek.urule.console.editor.store.StoreTools;
import com.bstek.urule.console.security.AuthenticationManager;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.security.URuleAuthorization;
import com.bstek.urule.console.security.entity.User;
import com.bstek.urule.console.type.ProjectRoleEnum;
import com.bstek.urule.console.type.RoleCategory;
import com.bstek.urule.console.type.RuleFileType;
import com.bstek.urule.console.util.FileUtils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.DeserializeException;
import com.bstek.urule.exception.FileFixedException;
import com.bstek.urule.exception.ReferenceDeleteException;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.parse.RuleFileHolder;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.dom4j.Element;

public class FileServletHandler extends ApiServletHandler {
   private Map templatesByType = new HashMap();

   public void load(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("id"));
      RuleFile ruleFile = FileManager.ins.get(longValue);
      this.writeObjectToJson(resp, ruleFile);
   }

   public void list(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      this.loadFiles("list", req, resp);
   }

   public void searchRuleName(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String text = "<rule\\s+name=\\\"([^\\\"]*)";
      String text2 = "<loop-rule\\s+name=\\\"([^\\\"]*)";
      Pattern pattern = Pattern.compile(text);
      Pattern pattern2 = Pattern.compile(text2);
      ArrayList items = new ArrayList();
      items.add(pattern);
      items.add(pattern2);
      String parameter = req.getParameter("projectId");
      String parameter2 = req.getParameter("nameLike");
      ArrayList items2 = new ArrayList();
      items2.add(ResourceType.RuleSet.name());
      List items3 = this.searchFileContents(items, items2, parameter, parameter2);
      this.writeObjectToJson(resp, items3);
   }

   public void searchRemark(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String text = "<remark><!\\[CDATA\\[([^\\]]*)";
      Pattern pattern = Pattern.compile(text);
      ArrayList items = new ArrayList();
      items.add(pattern);
      String parameter = req.getParameter("projectId");
      String parameter2 = req.getParameter("nameLike");
      ArrayList items2 = new ArrayList();
      items2.add(ResourceType.RuleSet.name());
      items2.add(ResourceType.DecisionTree.name());
      items2.add(ResourceType.DecisionTable.name());
      items2.add(ResourceType.CrossDecisionTable.name());
      items2.add(ResourceType.Scorecard.name());
      items2.add(ResourceType.ComplexScorecard.name());
      items2.add(ResourceType.ActionTemplate.name());
      items2.add(ResourceType.ConditionTemplate.name());
      List items3 = this.searchFileContents(items, items2, parameter, parameter2);
      this.writeObjectToJson(resp, items3);
   }

   public void searchNodeName(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      ArrayList items = new ArrayList();
      items.add(Pattern.compile("<start\\s+name=\\\"([^\\\"]*)"));
      items.add(Pattern.compile("<rule\\s+name=\\\"([^\\\"]*)"));
      items.add(Pattern.compile("<rule-package\\s+name=\\\"([^\\\"]*)"));
      items.add(Pattern.compile("<action\\s+name=\\\"([^\\\"]*)"));
      items.add(Pattern.compile("<script\\s+name=\\\"([^\\\"]*)"));
      items.add(Pattern.compile("<decision\\s+name=\\\"([^\\\"]*)"));
      items.add(Pattern.compile("<join\\s+name=\\\"([^\\\"]*)"));
      items.add(Pattern.compile("<fork\\s+name=\\\"([^\\\"]*)"));
      String parameter = req.getParameter("projectId");
      String parameter2 = req.getParameter("nameLike");
      ArrayList items2 = new ArrayList();
      items2.add(ResourceType.Flow.name());
      List items3 = this.searchFileContents(items, items2, parameter, parameter2);
      this.writeObjectToJson(resp, items3);
   }

   private List searchFileContents(List items, List items2, String text, String text2) throws Exception {
      return this.searchFileContents(items, items2, text, text2, 1);
   }

   private List searchFileContents(List items, List items2, String text, String text2, int number) throws Exception {
      FileQuery fileQuery = FileManager.ins.newQuery();
      if (items2.size() > 0) {
         String[] values = new String[items2.size()];
         fileQuery.types((String[])items2.toArray(values));
      }

      fileQuery.asc("NAME_");
      fileQuery.deleted(false);
      fileQuery.containCommonProject(true);
      ArrayList items3 = new ArrayList();

      for(RuleFile ruleFile : (Iterable<RuleFile>)(Iterable<?>)(fileQuery.list(Long.valueOf(text)))) {
         String content = FileManager.ins.loadContent(ruleFile.getId());
         boolean flag = false;

         for(Pattern pattern : (Iterable<Pattern>)(Iterable<?>)(items)) {
            Matcher matcher = pattern.matcher(content);

            while(matcher.find()) {
               String text3 = matcher.group(number);
               if (text3.indexOf(text2) > -1) {
                  items3.add(ruleFile);
                  flag = true;
                  break;
               }
            }

            if (flag) {
               break;
            }
         }
      }

      return items3;
   }

   public void tree(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      this.loadFiles("tree", req, resp);
   }

   public void content(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("fileId");
      String parameter2 = req.getParameter("versionFileId");
      if (StringUtils.isNotBlank(parameter)) {
         long longValue = Long.valueOf(parameter);
         String content = FileManager.ins.loadContent(longValue);
         this.writeObjectToJson(resp, FileUtils.formatXml(content));
      } else if (StringUtils.isNotBlank(parameter2)) {
         long longValue2 = Long.valueOf(parameter2);
         String fileContent = VersionFileManager.ins.loadFileContent(longValue2);
         this.writeObjectToJson(resp, FileUtils.formatXml(fileContent));
      }

   }

   public void info(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      HashMap valuesByKey = new HashMap();
      String parameter = req.getParameter("fileId");
      long longValue = Long.valueOf(parameter);
      String content = FileManager.ins.loadContent(longValue);
      valuesByKey.put("size", content.length());
      RuleFile ruleFile = FileManager.ins.get(longValue);
      valuesByKey.put("updateUser", ruleFile.getUpdateUser());
      valuesByKey.put("createDate", ruleFile.getCreateDate());
      valuesByKey.put("path", ruleFile.getPath());
      valuesByKey.put("fileSet", ruleFile.isFileSet());
      valuesByKey.put("latestVersion", ruleFile.getLatestVersion());
      this.writeObjectToJson(resp, valuesByKey);
   }

   public void versionFiles(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("fileId"));
      VersionFileQuery versionFileQuery = VersionFileManager.ins.newQuery().fileId(longValue);
      versionFileQuery.versionLike(req.getParameter("version")).noteLike(req.getParameter("note"));
      int number = Integer.valueOf(req.getParameter("pageIndex"));
      int number2 = Integer.valueOf(req.getParameter("pageSize"));
      this.writeObjectToJson(resp, versionFileQuery.paging(number, number2));
   }

   public void loadFiles(String style, HttpServletRequest req, HttpServletResponse resp) throws Exception {
      FileQuery fileQuery = FileManager.ins.newQuery();
      String parameter = req.getParameter("projectId");
      fileQuery.name(req.getParameter("name"));
      String parameter2 = req.getParameter("nameLike");
      if (StringUtils.isNotBlank(parameter2)) {
         fileQuery.nameLike(parameter2);
      }

      fileQuery.type(req.getParameter("type"));
      String parameter3 = req.getParameter("types");
      String[] parameterValues = req.getParameterValues("types");
      if (parameter3 != null) {
         parameterValues = parameter3.split(",");
         fileQuery.types(parameterValues);
      }

      fileQuery.lockedUser(req.getParameter("lockedUser"));
      fileQuery.asc("NAME_");
      fileQuery.deleted(false);
      String parameter4 = req.getParameter("removeEmpty");
      if (StringUtils.isNotBlank(parameter4)) {
         fileQuery.removeEmpty(Boolean.valueOf(parameter4));
      }

      fileQuery.containCommonProject(true);
      new ArrayList();
      List items;
      if (style.contentEquals("list")) {
         items = fileQuery.list(Long.valueOf(parameter));
      } else {
         items = fileQuery.tree(Long.valueOf(parameter));
      }

      Map valuesByKey = this.buildFileTypePermissions(SecurityUtils.getLoginUser(req), (long)Integer.parseInt(parameter));
      this.filterFileTree(items, parameterValues, valuesByKey);
      this.writeObjectToJson(resp, items);
   }

   private Map buildFileTypePermissions(User user, long longValue) {
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

   private void filterFileTree(List items, String[] values, Map valuesByKey) {
      ArrayList items2 = new ArrayList();

      for(RuleFile ruleFile : (Iterable<RuleFile>)(Iterable<?>)(items)) {
         if ("custom".equals(ruleFile.getType())) {
            this.filterFileTree(ruleFile.getChildren(), values, valuesByKey);
         } else if (RuleFileType.General.name().equals(ruleFile.getType())) {
            this.filterFileTree(ruleFile.getChildren(), values, valuesByKey);
         } else if (ruleFile.isDirectory()) {
            this.filterFileTree(ruleFile.getChildren(), values, valuesByKey);
         } else if (values == null) {
            boolean flag = false;
            RuleFileType ruleFileType = RuleFileType.getRuleFileType(ruleFile.getType());
            if (valuesByKey.containsKey(ruleFileType.name())) {
               flag = (Boolean)valuesByKey.get(ruleFile.getType());
            }

            if (!flag) {
               items2.add(ruleFile);
            }
         } else {
            boolean flag2 = false;

            for(String text : values) {
               if (text.equals(ruleFile.getType())) {
                  flag2 = true;
               }
            }

            if (!flag2) {
               items2.add(ruleFile);
            }
         }
      }

      for(RuleFile ruleFile2 : (Iterable<RuleFile>)(Iterable<?>)(items2)) {
         items.remove(ruleFile2);
      }

   }

   private void validateFileName(String text, long longValue, long longValue2, String text2) {
      if (StringUtils.isBlank(text)) {
         throw new InfoException("文件名称不能为空！");
      } else if (text.indexOf("/") > 0) {
         throw new InfoException("文件名称不能使用“/”！");
      } else if (FileManager.ins.checkExist(longValue2, longValue, text2, text)) {
         throw new InfoException("文件【" + text + "】已存在！");
      }
   }

   @URuleAuthorization(
      authType = "project",
      code = "add",
      ruleFile = true
   )
   public void add(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("parentId"));
      long projectId = ContextHolder.getProjectId();
      String parameter = req.getParameter("type");
      String trimmedText = req.getParameter("name");
      if (StringUtils.isNotBlank(trimmedText)) {
         trimmedText = trimmedText.trim();
      }

      this.validateFileName(trimmedText, longValue, projectId, parameter);
      String text = this.loadTemplate(parameter);
      RuleFile ruleFile = new RuleFile();
      ruleFile.setDirectory(false);
      ruleFile.setParentId(longValue);
      ruleFile.setProjectId(projectId);
      ruleFile.setName(trimmedText);
      ruleFile.setType(parameter);
      ruleFile.setContent(text);
      ruleFile.setCreateUser(SecurityUtils.getLoginUsername(req));
      FileManager.ins.add(ruleFile);
      this.writeObjectToJson(resp, ruleFile);
      SystemLogUtils.addRuleFileOperationLog(ruleFile.getType(), "add", ruleFile.getId(), String.format("Create a new %s type file %s[%s]", parameter, trimmedText, ruleFile.getId()));
   }

   @URuleAuthorization(
      authType = "project",
      code = "update",
      ruleFile = true
   )
   public void saveFile(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("id"));
      String parameter = req.getParameter("xml");
      if (StringUtils.isBlank(parameter)) {
         throw new RuleException("文件内容不能为空,无法保存!<br>The file content is empty, can't save!");
      } else {
         parameter = URLDecoder.decode(parameter, "utf-8");
         RuleFile ruleFile = FileManager.ins.get(longValue);
         if (ruleFile != null) {
            String loginUsername = SecurityUtils.getLoginUsername(req);
            if (!StringUtils.isBlank(ruleFile.getLockedUser()) && !ruleFile.getLockedUser().equals(loginUsername)) {
               throw new FileFixedException("文件已被[" + ruleFile.getLockedUser() + "]锁定,无法保存,请先解锁!<br>The file has been locked by [" + ruleFile.getLockedUser() + "]. Please unlock it first!");
            }

            FileManager.ins.updateContent(longValue, loginUsername, parameter);
            FileManager.ins.lock(longValue, loginUsername);
            SystemLogUtils.addRuleFileOperationLog(ruleFile.getType(), "update", ruleFile.getId(), String.format("Save file %s[%s] of type %s", ruleFile.getName(), ruleFile.getId(), ruleFile.getType()));
         }

      }
   }

   @URuleAuthorization(
      authType = "project",
      code = "update",
      ruleFile = true
   )
   public void removeUUID(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("id"));
      String parameter = req.getParameter("uuid");
      boolean flag = Boolean.valueOf(req.getParameter("force"));
      RuleFile ruleFile = FileManager.ins.get(longValue);
      String loginUsername = SecurityUtils.getLoginUsername(req);
      if ((StringUtils.isBlank(ruleFile.getLockedUser()) || ruleFile.getLockedUser().equals(loginUsername)) && !flag) {
         try {
            List items = ReferenceService.ins.uuid(ruleFile.getProjectId(), longValue, parameter);
            if (items.size() > 0) {
               throw new ReferenceDeleteException(items.size());
            }
         } catch (DeserializeException deserializeException) {
            throw new ReferenceDeleteException(deserializeException.getMessage());
         }
      }

   }

   public void copy(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("id");
      long projectId = ContextHolder.getProjectId();
      Object attribute = StoreTools.getAttribute("urule_file_copy");
      if (attribute == null) {
         attribute = new HashMap();
      }

      ((Map)attribute).put(String.valueOf(projectId), parameter);
      StoreTools.setAttribute("urule_file_copy", attribute);
   }

   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "add",
      ruleDir = true
   )
   public void paste(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String loginUsername = SecurityUtils.getLoginUsername(req);
      long projectId = ContextHolder.getProjectId();
      long longValue = Long.valueOf(req.getParameter("id"));
      String parameter = req.getParameter("name");
      Object attribute = StoreTools.getAttribute("urule_file_copy");
      if (attribute == null) {
         throw new InfoException("No File for Paste!");
      } else {
         Map attribute2 = (Map)attribute;
         String text = (String)attribute2.get(String.valueOf(projectId));
         String[] parts = text.split(",");
         ArrayList items = new ArrayList();

         for(String text2 : parts) {
            items.add(Long.parseLong(text2));
         }

         Object objectValue = new ArrayList();
         if (items.size() == 1 && StringUtils.isNotBlank(parameter)) {
            long longValue2 = (Long)items.get(0);
            RuleFile ruleFile = DirectoryManager.ins.get(longValue2);
            if (ruleFile != null) {
               RuleFile ruleFile2 = FileService.ins.copyDir(projectId, longValue, longValue2, parameter, loginUsername);
               ((List)objectValue).add(ruleFile2);
            } else {
               RuleFile ruleFile3 = FileService.ins.copyFile(projectId, longValue, longValue2, parameter, loginUsername);
               ((List)objectValue).add(ruleFile3);
            }
         } else {
            objectValue = FileService.ins.copyFiles(projectId, longValue, items, loginUsername);
         }

         this.writeObjectToJson(resp, objectValue);
      }
   }

   public void remove(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      User loginUser = SecurityUtils.getLoginUser(req);
      boolean flag = Boolean.valueOf(req.getParameter("force"));
      String parameter = req.getParameter("id");
      String[] parts = parameter.split(",");

      for(String text : parts) {
         Long longValue = Long.valueOf(text);
         RuleFile ruleFile = FileManager.ins.get(longValue);
         RuleFileType ruleFileType = RuleFileType.getRuleFileType(ruleFile.getType());
         boolean flag2 = AuthenticationManager.decide(loginUser, RoleCategory.project, ruleFileType.getModel(), "remove");
         if (!flag2) {
            throw new PermissionDeniedException();
         }

         if (ruleFile != null) {
            String loginUsername = SecurityUtils.getLoginUsername(req);
            if (!StringUtils.isBlank(ruleFile.getLockedUser()) && !ruleFile.getLockedUser().equals(loginUsername)) {
               throw new FileFixedException("文件已被[" + ruleFile.getLockedUser() + "]锁定,无法删除,请先解锁!<br>The file has been locked by [" + ruleFile.getLockedUser() + "]. Please unlock it first!");
            }

            if (!flag) {
               try {
                  List items = ReferenceService.ins.uuid(ruleFile.getProjectId(), longValue, (String)null);
                  if (items.size() > 0) {
                     throw new ReferenceDeleteException(items.size());
                  }
               } catch (DeserializeException deserializeException) {
                  throw new ReferenceDeleteException(deserializeException.getMessage());
               }
            }

            FileManager.ins.updateDeleteFlag(longValue, true, SecurityUtils.getLoginUsername(req));
            SystemLogUtils.addRuleFileOperationLog(ruleFile.getType(), "remove", ruleFile.getId(), String.format("Remove file %s[%s] of type %s", ruleFile.getName(), ruleFile.getId(), ruleFile.getType()));
         }
      }

   }

   @URuleAuthorization(
      authType = "project",
      code = "update",
      ruleFile = true
   )
   @Transactional
   public void move(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("parentId"));
      String parameter = req.getParameter("id");
      String[] parts = parameter.split(",");

      for(String text : parts) {
         Long longValue2 = Long.valueOf(text);
         RuleFile ruleFile = FileManager.ins.get(longValue2);
         RuleFile ruleFile2 = DirectoryManager.ins.get(longValue);
         RuleFile ruleFile3 = DirectoryManager.ins.get(longValue);
         if (ruleFile3 != null && ruleFile2 != null) {
            if (!ruleFile.getType().equals(ruleFile3.getType()) && !ruleFile3.getType().equals(ResourceType.General.name())) {
               throw new RuleException("文件类型限制，不支持此操作!<br>File type restrictions, this operation is not supported!");
            }

            String loginUsername = SecurityUtils.getLoginUsername(req);
            if (!StringUtils.isBlank(ruleFile.getLockedUser()) && !ruleFile.getLockedUser().equals(loginUsername)) {
               throw new FileFixedException("文件已被[" + ruleFile.getLockedUser() + "]锁定,无法移动,请先解锁!<br>The file has been locked by [" + ruleFile.getLockedUser() + "]. Please unlock it first!");
            }

            FileManager.ins.changeParent(longValue2, longValue);
            SystemLogUtils.addRuleFileOperationLog(ruleFile.getType(), "update", ruleFile.getId(), String.format("File %s[%s] moved from directory %s to directory %s", ruleFile.getName(), ruleFile.getId(), ruleFile.getType(), ruleFile2.getName(), ruleFile3.getName()));
         }
      }

   }

   @URuleAuthorization(
      authType = "project",
      code = "update",
      ruleFile = true
   )
   public void rename(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("newName");
      if (StringUtils.isNotBlank(parameter)) {
         parameter = parameter.trim();
      }

      long longValue = Long.valueOf(req.getParameter("id"));
      RuleFile ruleFile = FileManager.ins.get(longValue);
      if (!ruleFile.getName().equals(parameter)) {
         this.validateFileName(parameter, ruleFile.getParentId(), ruleFile.getProjectId(), ruleFile.getType());
         String loginUsername = SecurityUtils.getLoginUsername(req);
         if (!StringUtils.isBlank(ruleFile.getLockedUser()) && !ruleFile.getLockedUser().equals(loginUsername)) {
            throw new FileFixedException("文件已被[" + ruleFile.getLockedUser() + "]锁定,无法重命名,请先解锁!<br>The file has been locked by [" + ruleFile.getLockedUser() + "]. Please unlock it first!");
         } else {
            FileManager.ins.rename(longValue, SecurityUtils.getLoginUsername(req), parameter);
            SystemLogUtils.addRuleFileOperationLog(ruleFile.getType(), "rename", ruleFile.getId(), String.format("Rename file %s of %s type to %s", ruleFile.getName(), ruleFile.getType(), parameter));
         }
      }
   }

   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "update",
      ruleFile = true
   )
   public void unlock(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("id"));
      String parameter = req.getParameter("note");
      Project project = ProjectManager.ins.get(ContextHolder.getProjectId());
      String loginUsername = SecurityUtils.getLoginUsername(req);
      List userRoles = ProjectRoleManager.ins.loadUserRoles(ContextHolder.getProjectId(), loginUsername);
      boolean flag = project.getCreateUser().equals(loginUsername);
      if (!flag) {
         for(Role role : (Iterable<Role>)(Iterable<?>)(userRoles)) {
            if (role.getName().equals(ProjectRoleEnum.Manager.name())) {
               flag = true;
               break;
            }
         }
      }

      RuleFile ruleFile = FileManager.ins.get(longValue);
      if (!StringUtils.isNotBlank(ruleFile.getLockedUser()) || !ruleFile.getLockedUser().equals(loginUsername) && !flag) {
         throw new ParameterInvaidException();
      } else {
         String maxVersion = FileUtils.getMaxVersion(ruleFile.getLatestVersion());
         VersionFile versionFile = new VersionFile();
         versionFile.setContent(ruleFile.getContent());
         versionFile.setFileId(longValue);
         versionFile.setProjectId(ruleFile.getProjectId());
         versionFile.setName(ruleFile.getName());
         versionFile.setNote(parameter);
         versionFile.setVersion(maxVersion);
         versionFile.setCreateUser(SecurityUtils.getLoginUsername(req));
         versionFile.setContent(FileManager.ins.loadContent(longValue));
         VersionFileManager.ins.saveFile(versionFile);
         FileManager.ins.unlock(longValue, maxVersion, SecurityUtils.getLoginUsername(req));
         SystemLogUtils.addRuleFileOperationLog(ruleFile.getType(), "unlock", ruleFile.getId(), String.format("Unlock file %s[%s] of type %s", ruleFile.getName(), ruleFile.getId(), ruleFile.getType()));
      }
   }

   private String loadTemplate(String text) {
      if (this.templatesByType.containsKey(text)) {
         return (String)this.templatesByType.get(text);
      } else {
         String text2 = null;
         InputStream inputStream = null;

         try {
            inputStream = this.getClass().getClassLoader().getResourceAsStream("com/bstek/urule/console/file/template/" + text + ".xml");
            text2 = IOUtils.toString(inputStream, "utf-8");
         } catch (Exception exception) {
            throw new RuleException(exception);
         } finally {
            IOUtils.closeQuietly(inputStream);
         }

         this.templatesByType.put(text, text2);
         return text2;
      }
   }

   @URuleAuthorization(
      authType = "project",
      code = "update",
      ruleFile = true
   )
   public void newVersion(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.parseLong(req.getParameter("id"));
      RuleFile ruleFile = FileManager.ins.get(longValue);
      String maxVersion = FileUtils.getMaxVersion(ruleFile.getLatestVersion());
      this.writeObjectToJson(resp, maxVersion);
   }

   public void validate(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      Long projectId = ContextHolder.getProjectId();
      this.writeObjectToJson(resp, this.validateProjectFiles(projectId));
   }

   private List validateProjectFiles(long longValue) {
      ArrayList items = new ArrayList();

      for(RuleFile ruleFile : (Iterable<RuleFile>)(Iterable<?>)(FileManager.ins.newQuery().deleted(false).asc("NAME_").list(longValue))) {
         Element element = null;

         try {
            String content = FileManager.ins.loadContent(ruleFile.getId());
            RuleFileHolder.resetRuleFile(ruleFile.getPath());

            try {
               element = FileDeserializer.getInstance().parseXml(content);
               FileDeserializer.getInstance().deserialize(element);
            } catch (DeserializeException deserializeException) {
               HashMap valuesByKey = new HashMap();
               valuesByKey.put("id", ruleFile.getId());
               valuesByKey.put("name", ruleFile.getName());
               valuesByKey.put("path", ruleFile.getPath());
               valuesByKey.put("error", deserializeException.getMessage());
               items.add(valuesByKey);
            }

            RuleFileHolder.clean();
         } catch (Exception exception) {
            if (exception instanceof RuleException) {
               throw (RuleException)exception;
            }

            throw new RuleException(exception);
         }
      }

      return items;
   }

   public String url() {
      return "/file";
   }
}
