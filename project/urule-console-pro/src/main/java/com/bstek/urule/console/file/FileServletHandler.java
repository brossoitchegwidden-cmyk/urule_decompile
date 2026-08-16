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
   private Map e = new HashMap();

   public void load(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("id"));
      RuleFile var5 = FileManager.ins.get(var3);
      this.a(var2, var5);
   }

   public void list(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      this.loadFiles("list", var1, var2);
   }

   public void searchRuleName(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = "<rule\\s+name=\\\"([^\\\"]*)";
      String var4 = "<loop-rule\\s+name=\\\"([^\\\"]*)";
      Pattern var5 = Pattern.compile(var3);
      Pattern var6 = Pattern.compile(var4);
      ArrayList var7 = new ArrayList();
      var7.add(var5);
      var7.add(var6);
      String var8 = var1.getParameter("projectId");
      String var9 = var1.getParameter("nameLike");
      ArrayList var10 = new ArrayList();
      var10.add(ResourceType.RuleSet.name());
      List var11 = this.a(var7, var10, var8, var9);
      this.a(var2, var11);
   }

   public void searchRemark(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = "<remark><!\\[CDATA\\[([^\\]]*)";
      Pattern var4 = Pattern.compile(var3);
      ArrayList var5 = new ArrayList();
      var5.add(var4);
      String var6 = var1.getParameter("projectId");
      String var7 = var1.getParameter("nameLike");
      ArrayList var8 = new ArrayList();
      var8.add(ResourceType.RuleSet.name());
      var8.add(ResourceType.DecisionTree.name());
      var8.add(ResourceType.DecisionTable.name());
      var8.add(ResourceType.CrossDecisionTable.name());
      var8.add(ResourceType.Scorecard.name());
      var8.add(ResourceType.ComplexScorecard.name());
      var8.add(ResourceType.ActionTemplate.name());
      var8.add(ResourceType.ConditionTemplate.name());
      List var9 = this.a(var5, var8, var6, var7);
      this.a(var2, var9);
   }

   public void searchNodeName(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      ArrayList var3 = new ArrayList();
      var3.add(Pattern.compile("<start\\s+name=\\\"([^\\\"]*)"));
      var3.add(Pattern.compile("<rule\\s+name=\\\"([^\\\"]*)"));
      var3.add(Pattern.compile("<rule-package\\s+name=\\\"([^\\\"]*)"));
      var3.add(Pattern.compile("<action\\s+name=\\\"([^\\\"]*)"));
      var3.add(Pattern.compile("<script\\s+name=\\\"([^\\\"]*)"));
      var3.add(Pattern.compile("<decision\\s+name=\\\"([^\\\"]*)"));
      var3.add(Pattern.compile("<join\\s+name=\\\"([^\\\"]*)"));
      var3.add(Pattern.compile("<fork\\s+name=\\\"([^\\\"]*)"));
      String var4 = var1.getParameter("projectId");
      String var5 = var1.getParameter("nameLike");
      ArrayList var6 = new ArrayList();
      var6.add(ResourceType.Flow.name());
      List var7 = this.a(var3, var6, var4, var5);
      this.a(var2, var7);
   }

   private List a(List var1, List var2, String var3, String var4) throws Exception {
      return this.a(var1, var2, var3, var4, 1);
   }

   private List a(List var1, List var2, String var3, String var4, int var5) throws Exception {
      FileQuery var6 = FileManager.ins.newQuery();
      if (var2.size() > 0) {
         String[] var7 = new String[var2.size()];
         var6.types((String[])var2.toArray(var7));
      }

      var6.asc("NAME_");
      var6.deleted(false);
      var6.containCommonProject(true);
      ArrayList var17 = new ArrayList();

      for(RuleFile var10 : (Iterable<RuleFile>)(Iterable<?>)(var6.list(Long.valueOf(var3)))) {
         String var11 = FileManager.ins.loadContent(var10.getId());
         boolean var12 = false;

         for(Pattern var14 : (Iterable<Pattern>)(Iterable<?>)(var1)) {
            Matcher var15 = var14.matcher(var11);

            while(var15.find()) {
               String var16 = var15.group(var5);
               if (var16.indexOf(var4) > -1) {
                  var17.add(var10);
                  var12 = true;
                  break;
               }
            }

            if (var12) {
               break;
            }
         }
      }

      return var17;
   }

   public void tree(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      this.loadFiles("tree", var1, var2);
   }

   public void content(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("fileId");
      String var4 = var1.getParameter("versionFileId");
      if (StringUtils.isNotBlank(var3)) {
         long var5 = Long.valueOf(var3);
         String var7 = FileManager.ins.loadContent(var5);
         this.a(var2, FileUtils.formatXml(var7));
      } else if (StringUtils.isNotBlank(var4)) {
         long var8 = Long.valueOf(var4);
         String var9 = VersionFileManager.ins.loadFileContent(var8);
         this.a(var2, FileUtils.formatXml(var9));
      }

   }

   public void info(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      HashMap var3 = new HashMap();
      String var4 = var1.getParameter("fileId");
      long var5 = Long.valueOf(var4);
      String var7 = FileManager.ins.loadContent(var5);
      var3.put("size", var7.length());
      RuleFile var8 = FileManager.ins.get(var5);
      var3.put("updateUser", var8.getUpdateUser());
      var3.put("createDate", var8.getCreateDate());
      var3.put("path", var8.getPath());
      var3.put("fileSet", var8.isFileSet());
      var3.put("latestVersion", var8.getLatestVersion());
      this.a(var2, var3);
   }

   public void versionFiles(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("fileId"));
      VersionFileQuery var5 = VersionFileManager.ins.newQuery().fileId(var3);
      var5.versionLike(var1.getParameter("version")).noteLike(var1.getParameter("note"));
      int var6 = Integer.valueOf(var1.getParameter("pageIndex"));
      int var7 = Integer.valueOf(var1.getParameter("pageSize"));
      this.a(var2, var5.paging(var6, var7));
   }

   public void loadFiles(String var1, HttpServletRequest var2, HttpServletResponse var3) throws Exception {
      FileQuery var4 = FileManager.ins.newQuery();
      String var5 = var2.getParameter("projectId");
      var4.name(var2.getParameter("name"));
      String var6 = var2.getParameter("nameLike");
      if (StringUtils.isNotBlank(var6)) {
         var4.nameLike(var6);
      }

      var4.type(var2.getParameter("type"));
      String var7 = var2.getParameter("types");
      String[] var8 = var2.getParameterValues("types");
      if (var7 != null) {
         var8 = var7.split(",");
         var4.types(var8);
      }

      var4.lockedUser(var2.getParameter("lockedUser"));
      var4.asc("NAME_");
      var4.deleted(false);
      String var9 = var2.getParameter("removeEmpty");
      if (StringUtils.isNotBlank(var9)) {
         var4.removeEmpty(Boolean.valueOf(var9));
      }

      var4.containCommonProject(true);
      new ArrayList();
      List var10;
      if (var1.contentEquals("list")) {
         var10 = var4.list(Long.valueOf(var5));
      } else {
         var10 = var4.tree(Long.valueOf(var5));
      }

      Map var11 = this.a(SecurityUtils.getLoginUser(var2), (long)Integer.parseInt(var5));
      this.a(var10, var8, var11);
      this.a(var3, var10);
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

   private void a(List var1, String[] var2, Map var3) {
      ArrayList var4 = new ArrayList();

      for(RuleFile var6 : (Iterable<RuleFile>)(Iterable<?>)(var1)) {
         if ("custom".equals(var6.getType())) {
            this.a(var6.getChildren(), var2, var3);
         } else if (RuleFileType.General.name().equals(var6.getType())) {
            this.a(var6.getChildren(), var2, var3);
         } else if (var6.isDirectory()) {
            this.a(var6.getChildren(), var2, var3);
         } else if (var2 == null) {
            boolean var14 = false;
            RuleFileType var15 = RuleFileType.getRuleFileType(var6.getType());
            if (var3.containsKey(var15.name())) {
               var14 = (Boolean)var3.get(var6.getType());
            }

            if (!var14) {
               var4.add(var6);
            }
         } else {
            boolean var7 = false;

            for(String var11 : var2) {
               if (var11.equals(var6.getType())) {
                  var7 = true;
               }
            }

            if (!var7) {
               var4.add(var6);
            }
         }
      }

      for(RuleFile var13 : (Iterable<RuleFile>)(Iterable<?>)(var4)) {
         var1.remove(var13);
      }

   }

   private void a(String var1, long var2, long var4, String var6) {
      if (StringUtils.isBlank(var1)) {
         throw new InfoException("文件名称不能为空！");
      } else if (var1.indexOf("/") > 0) {
         throw new InfoException("文件名称不能使用“/”！");
      } else if (FileManager.ins.checkExist(var4, var2, var6, var1)) {
         throw new InfoException("文件【" + var1 + "】已存在！");
      }
   }

   @URuleAuthorization(
      authType = "project",
      code = "add",
      ruleFile = true
   )
   public void add(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("parentId"));
      long var5 = ContextHolder.getProjectId();
      String var7 = var1.getParameter("type");
      String var8 = var1.getParameter("name");
      if (StringUtils.isNotBlank(var8)) {
         var8 = var8.trim();
      }

      this.a(var8, var3, var5, var7);
      String var9 = this.a(var7);
      RuleFile var10 = new RuleFile();
      var10.setDirectory(false);
      var10.setParentId(var3);
      var10.setProjectId(var5);
      var10.setName(var8);
      var10.setType(var7);
      var10.setContent(var9);
      var10.setCreateUser(SecurityUtils.getLoginUsername(var1));
      FileManager.ins.add(var10);
      this.a(var2, var10);
      SystemLogUtils.addRuleFileOperationLog(var10.getType(), "add", var10.getId(), String.format("Create a new %s type file %s[%s]", var7, var8, var10.getId()));
   }

   @URuleAuthorization(
      authType = "project",
      code = "update",
      ruleFile = true
   )
   public void saveFile(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("id"));
      String var5 = var1.getParameter("xml");
      if (StringUtils.isBlank(var5)) {
         throw new RuleException("文件内容不能为空,无法保存!<br>The file content is empty, can't save!");
      } else {
         var5 = URLDecoder.decode(var5, "utf-8");
         RuleFile var6 = FileManager.ins.get(var3);
         if (var6 != null) {
            String var7 = SecurityUtils.getLoginUsername(var1);
            if (!StringUtils.isBlank(var6.getLockedUser()) && !var6.getLockedUser().equals(var7)) {
               throw new FileFixedException("文件已被[" + var6.getLockedUser() + "]锁定,无法保存,请先解锁!<br>The file has been locked by [" + var6.getLockedUser() + "]. Please unlock it first!");
            }

            FileManager.ins.updateContent(var3, var7, var5);
            FileManager.ins.lock(var3, var7);
            SystemLogUtils.addRuleFileOperationLog(var6.getType(), "update", var6.getId(), String.format("Save file %s[%s] of type %s", var6.getName(), var6.getId(), var6.getType()));
         }

      }
   }

   @URuleAuthorization(
      authType = "project",
      code = "update",
      ruleFile = true
   )
   public void removeUUID(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("id"));
      String var5 = var1.getParameter("uuid");
      boolean var6 = Boolean.valueOf(var1.getParameter("force"));
      RuleFile var7 = FileManager.ins.get(var3);
      String var8 = SecurityUtils.getLoginUsername(var1);
      if ((StringUtils.isBlank(var7.getLockedUser()) || var7.getLockedUser().equals(var8)) && !var6) {
         try {
            List var9 = ReferenceService.ins.uuid(var7.getProjectId(), var3, var5);
            if (var9.size() > 0) {
               throw new ReferenceDeleteException(var9.size());
            }
         } catch (DeserializeException var10) {
            throw new ReferenceDeleteException(var10.getMessage());
         }
      }

   }

   public void copy(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("id");
      long var4 = ContextHolder.getProjectId();
      Object var6 = StoreTools.getAttribute("urule_file_copy");
      if (var6 == null) {
         var6 = new HashMap();
      }

      ((Map)var6).put(String.valueOf(var4), var3);
      StoreTools.setAttribute("urule_file_copy", var6);
   }

   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "add",
      ruleDir = true
   )
   public void paste(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = SecurityUtils.getLoginUsername(var1);
      long var4 = ContextHolder.getProjectId();
      long var6 = Long.valueOf(var1.getParameter("id"));
      String var8 = var1.getParameter("name");
      Object var9 = StoreTools.getAttribute("urule_file_copy");
      if (var9 == null) {
         throw new InfoException("No File for Paste!");
      } else {
         Map var10 = (Map)var9;
         String var11 = (String)var10.get(String.valueOf(var4));
         String[] var12 = var11.split(",");
         ArrayList var13 = new ArrayList();

         for(String var17 : var12) {
            var13.add(Long.parseLong(var17));
         }

         Object var19 = new ArrayList();
         if (var13.size() == 1 && StringUtils.isNotBlank(var8)) {
            long var20 = (Long)var13.get(0);
            RuleFile var21 = DirectoryManager.ins.get(var20);
            if (var21 != null) {
               RuleFile var18 = FileService.ins.copyDir(var4, var6, var20, var8, var3);
               ((List)var19).add(var18);
            } else {
               RuleFile var22 = FileService.ins.copyFile(var4, var6, var20, var8, var3);
               ((List)var19).add(var22);
            }
         } else {
            var19 = FileService.ins.copyFiles(var4, var6, var13, var3);
         }

         this.a(var2, var19);
      }
   }

   public void remove(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      User var3 = SecurityUtils.getLoginUser(var1);
      boolean var4 = Boolean.valueOf(var1.getParameter("force"));
      String var5 = var1.getParameter("id");
      String[] var6 = var5.split(",");

      for(String var10 : var6) {
         Long var11 = Long.valueOf(var10);
         RuleFile var12 = FileManager.ins.get(var11);
         RuleFileType var13 = RuleFileType.getRuleFileType(var12.getType());
         boolean var14 = AuthenticationManager.decide(var3, RoleCategory.project, var13.getModel(), "remove");
         if (!var14) {
            throw new PermissionDeniedException();
         }

         if (var12 != null) {
            String var15 = SecurityUtils.getLoginUsername(var1);
            if (!StringUtils.isBlank(var12.getLockedUser()) && !var12.getLockedUser().equals(var15)) {
               throw new FileFixedException("文件已被[" + var12.getLockedUser() + "]锁定,无法删除,请先解锁!<br>The file has been locked by [" + var12.getLockedUser() + "]. Please unlock it first!");
            }

            if (!var4) {
               try {
                  List var16 = ReferenceService.ins.uuid(var12.getProjectId(), var11, (String)null);
                  if (var16.size() > 0) {
                     throw new ReferenceDeleteException(var16.size());
                  }
               } catch (DeserializeException var17) {
                  throw new ReferenceDeleteException(var17.getMessage());
               }
            }

            FileManager.ins.updateDeleteFlag(var11, true, SecurityUtils.getLoginUsername(var1));
            SystemLogUtils.addRuleFileOperationLog(var12.getType(), "remove", var12.getId(), String.format("Remove file %s[%s] of type %s", var12.getName(), var12.getId(), var12.getType()));
         }
      }

   }

   @URuleAuthorization(
      authType = "project",
      code = "update",
      ruleFile = true
   )
   @Transactional
   public void move(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("parentId"));
      String var5 = var1.getParameter("id");
      String[] var6 = var5.split(",");

      for(String var10 : var6) {
         Long var11 = Long.valueOf(var10);
         RuleFile var12 = FileManager.ins.get(var11);
         RuleFile var13 = DirectoryManager.ins.get(var3);
         RuleFile var14 = DirectoryManager.ins.get(var3);
         if (var14 != null && var13 != null) {
            if (!var12.getType().equals(var14.getType()) && !var14.getType().equals(ResourceType.General.name())) {
               throw new RuleException("文件类型限制，不支持此操作!<br>File type restrictions, this operation is not supported!");
            }

            String var15 = SecurityUtils.getLoginUsername(var1);
            if (!StringUtils.isBlank(var12.getLockedUser()) && !var12.getLockedUser().equals(var15)) {
               throw new FileFixedException("文件已被[" + var12.getLockedUser() + "]锁定,无法移动,请先解锁!<br>The file has been locked by [" + var12.getLockedUser() + "]. Please unlock it first!");
            }

            FileManager.ins.changeParent(var11, var3);
            SystemLogUtils.addRuleFileOperationLog(var12.getType(), "update", var12.getId(), String.format("File %s[%s] moved from directory %s to directory %s", var12.getName(), var12.getId(), var12.getType(), var13.getName(), var14.getName()));
         }
      }

   }

   @URuleAuthorization(
      authType = "project",
      code = "update",
      ruleFile = true
   )
   public void rename(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("newName");
      if (StringUtils.isNotBlank(var3)) {
         var3 = var3.trim();
      }

      long var4 = Long.valueOf(var1.getParameter("id"));
      RuleFile var6 = FileManager.ins.get(var4);
      if (!var6.getName().equals(var3)) {
         this.a(var3, var6.getParentId(), var6.getProjectId(), var6.getType());
         String var7 = SecurityUtils.getLoginUsername(var1);
         if (!StringUtils.isBlank(var6.getLockedUser()) && !var6.getLockedUser().equals(var7)) {
            throw new FileFixedException("文件已被[" + var6.getLockedUser() + "]锁定,无法重命名,请先解锁!<br>The file has been locked by [" + var6.getLockedUser() + "]. Please unlock it first!");
         } else {
            FileManager.ins.rename(var4, SecurityUtils.getLoginUsername(var1), var3);
            SystemLogUtils.addRuleFileOperationLog(var6.getType(), "rename", var6.getId(), String.format("Rename file %s of %s type to %s", var6.getName(), var6.getType(), var3));
         }
      }
   }

   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "update",
      ruleFile = true
   )
   public void unlock(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("id"));
      String var5 = var1.getParameter("note");
      Project var6 = ProjectManager.ins.get(ContextHolder.getProjectId());
      String var7 = SecurityUtils.getLoginUsername(var1);
      List var8 = ProjectRoleManager.ins.loadUserRoles(ContextHolder.getProjectId(), var7);
      boolean var9 = var6.getCreateUser().equals(var7);
      if (!var9) {
         for(Role var11 : (Iterable<Role>)(Iterable<?>)(var8)) {
            if (var11.getName().equals(ProjectRoleEnum.Manager.name())) {
               var9 = true;
               break;
            }
         }
      }

      RuleFile var13 = FileManager.ins.get(var3);
      if (!StringUtils.isNotBlank(var13.getLockedUser()) || !var13.getLockedUser().equals(var7) && !var9) {
         throw new ParameterInvaidException();
      } else {
         String var14 = FileUtils.getMaxVersion(var13.getLatestVersion());
         VersionFile var12 = new VersionFile();
         var12.setContent(var13.getContent());
         var12.setFileId(var3);
         var12.setProjectId(var13.getProjectId());
         var12.setName(var13.getName());
         var12.setNote(var5);
         var12.setVersion(var14);
         var12.setCreateUser(SecurityUtils.getLoginUsername(var1));
         var12.setContent(FileManager.ins.loadContent(var3));
         VersionFileManager.ins.saveFile(var12);
         FileManager.ins.unlock(var3, var14, SecurityUtils.getLoginUsername(var1));
         SystemLogUtils.addRuleFileOperationLog(var13.getType(), "unlock", var13.getId(), String.format("Unlock file %s[%s] of type %s", var13.getName(), var13.getId(), var13.getType()));
      }
   }

   private String a(String var1) {
      if (this.e.containsKey(var1)) {
         return (String)this.e.get(var1);
      } else {
         String var2 = null;
         InputStream var3 = null;

         try {
            var3 = this.getClass().getClassLoader().getResourceAsStream("com/bstek/urule/console/file/template/" + var1 + ".xml");
            var2 = IOUtils.toString(var3, "utf-8");
         } catch (Exception var8) {
            throw new RuleException(var8);
         } finally {
            IOUtils.closeQuietly(var3);
         }

         this.e.put(var1, var2);
         return var2;
      }
   }

   @URuleAuthorization(
      authType = "project",
      code = "update",
      ruleFile = true
   )
   public void newVersion(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.parseLong(var1.getParameter("id"));
      RuleFile var5 = FileManager.ins.get(var3);
      String var6 = FileUtils.getMaxVersion(var5.getLatestVersion());
      this.a(var2, var6);
   }

   public void validate(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      Long var3 = ContextHolder.getProjectId();
      this.a(var2, this.a(var3));
   }

   private List a(long var1) {
      ArrayList var3 = new ArrayList();

      for(RuleFile var6 : (Iterable<RuleFile>)(Iterable<?>)(FileManager.ins.newQuery().deleted(false).asc("NAME_").list(var1))) {
         Element var7 = null;

         try {
            String var8 = FileManager.ins.loadContent(var6.getId());
            RuleFileHolder.resetRuleFile(var6.getPath());

            try {
               var7 = FileDeserializer.getInstance().parseXml(var8);
               FileDeserializer.getInstance().deserialize(var7);
            } catch (DeserializeException var11) {
               HashMap var10 = new HashMap();
               var10.put("id", var6.getId());
               var10.put("name", var6.getName());
               var10.put("path", var6.getPath());
               var10.put("error", var11.getMessage());
               var3.add(var10);
            }

            RuleFileHolder.clean();
         } catch (Exception var12) {
            if (var12 instanceof RuleException) {
               throw (RuleException)var12;
            }

            throw new RuleException(var12);
         }
      }

      return var3;
   }

   public String url() {
      return "/file";
   }
}
