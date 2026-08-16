package com.bstek.urule.console.admin.project.in;

import com.bstek.urule.Utils;
import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.RequestHolder;
import com.bstek.urule.console.database.manager.file.DirectoryManager;
import com.bstek.urule.console.database.manager.file.DirectoryManagerImpl;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.manager.file.version.VersionFileManager;
import com.bstek.urule.console.database.manager.file.version.VersionFileManagerImpl;
import com.bstek.urule.console.database.manager.packet.PacketManager;
import com.bstek.urule.console.database.manager.packet.file.PacketFileManager;
import com.bstek.urule.console.database.manager.project.ProjectManager;
import com.bstek.urule.console.database.model.Group;
import com.bstek.urule.console.database.model.Packet;
import com.bstek.urule.console.database.model.PacketFile;
import com.bstek.urule.console.database.model.PacketType;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.ProjectViewModel;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.model.VersionFile;
import com.bstek.urule.console.database.service.project.ProjectService;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.security.entity.User;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.console.xml.DocumentHelper;
import com.bstek.urule.exception.RuleException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;

public class ProjectImport {
   private static final Log a = LogFactory.getLog(ProjectImport.class);
   private Map b = new HashMap();
   private Map c = new HashMap();
   private Map d = new HashMap();
   private List e = new ArrayList();
   private Map f = new HashMap();
   private Map g = new HashMap();
   private Map h = new HashMap();

   public void doImport(InputStream var1, Group var2, ConfigInfo var3) throws Exception {
      this.b(var2.getId());
      byte[] var4 = IOUtils.toByteArray(var1);
      String var5 = Utils.uncompress(var4);
      Document var6 = DocumentHelper.parseText(var5);
      Element var7 = var6.getRootElement();
      if (!var7.getName().contentEquals("project")) {
         throw new InfoException("文件不合法，不能导入，请选择一个URule Pro4+项目导出的备份文件");
      } else {
         Long var8 = Long.parseLong(var7.attributeValue("id"));
         String var9 = var7.attributeValue("name");
         String var10 = var7.attributeValue("type");
         String var11 = var7.attributeValue("viewModel");
         ProjectViewModel var12 = ProjectViewModel.category;
         if (StringUtils.isNotBlank(var11)) {
            var12 = ProjectViewModel.valueOf(var11);
         }

         if (!StringUtils.isBlank(var9) && !StringUtils.isBlank(var10)) {
            Project var13 = null;
            User var14 = SecurityUtils.getLoginUser(RequestHolder.getRequest());
            List var15 = ProjectManager.ins.newQuery().groupId(var2.getId()).name(var9).list();
            if (var3.isReplace() && var15.size() > 0) {
               var13 = (Project)var15.get(0);
               var13.setType(var10);
               var13.setViewModel(var12);
               var13.setUpdateUser(var14.getName());
               var13.setUpdateDate(new Date());
               String var25 = this.a(var7, "desc");
               var13.setDesc(var25);
               ProjectManager.ins.update(var13);
               a.debug("processProject(replace):" + var13.getName());

               for(RuleFile var19 : (Iterable<RuleFile>)(Iterable<?>)(FileManager.ins.newQuery().list(var13.getId()))) {
                  if (!var19.isDeleted()) {
                     this.d.put(var19.getPath(), var19);
                  }
               }
            } else {
               var13 = new Project();
               var9 = this.a(var2.getId(), var9);
               var13.setName(var9);
               var13.setType(var10);
               var13.setViewModel(var12);
               var13.setCreateUser(var14.getName());
               var13.setId(0L);
               var13.setGroupId(var2.getId());
               var13.setDeployApproveUser(var14.getName());
               var13.setDisableApproveUser(var14.getName());
               var13.setEnableApproveUser(var14.getName());
               var13.setUpdateUser(var14.getName());
               String var16 = this.a(var7, "desc");
               var13.setDesc(var16);
               ProjectService.ins.add(var13);
               a.debug("processProject(add):" + var13.getName());
            }

            if (var8 != var13.getId()) {
               String var26 = "project=\"" + var8 + "\"";
               String var30 = "project=\"" + var13.getId() + "\"";
               this.b.put(var26, var30);
            }

            for(Object var31 : var7.elements()) {
               if (var31 instanceof Element) {
                  Element var34 = (Element)var31;
                  if (!this.b(var34, var13, var3) && this.a(var34, var13, var3)) {
                  }
               }
            }

            for(RuleFile var32 : (Iterable<RuleFile>)(Iterable<?>)(this.c.values())) {
               String var35 = var32.getType();
               if (!var35.contentEquals(ResourceType.ActionLibrary.name()) && !var35.contentEquals(ResourceType.ConstantLibrary.name()) && !var35.contentEquals(ResourceType.ParameterLibrary.name()) && !var35.contentEquals(ResourceType.VariableLibrary.name())) {
                  String var37 = var32.getContent();

                  for(String var21 : (Iterable<String>)(Iterable<?>)(this.b.keySet())) {
                     String var22 = (String)this.b.get(var21);
                     if (var21.startsWith("file=")) {
                        if (var35.contentEquals(ResourceType.Flow.name())) {
                           var37 = var37.replace(var21, var22);
                        }
                     } else {
                        var37 = var37.replace(var21, var22);
                     }
                  }

                  for(String var43 : (Iterable<String>)(Iterable<?>)(this.g.keySet())) {
                     String var46 = (String)this.g.get(var43);
                     var37 = this.a(var43, var37, var46);
                  }

                  for(String var44 : (Iterable<String>)(Iterable<?>)(this.f.keySet())) {
                     String var47 = (String)this.f.get(var44);
                     var37 = this.a(var44, var37, var47);
                  }

                  FileManager.ins.updateContent(var32.getId(), var32.getCreateUser(), var37);
               }
            }

            for(VersionFile var33 : (Iterable<VersionFile>)(Iterable<?>)(this.e)) {
               String var36 = var33.getContent();

               for(String var42 : (Iterable<String>)(Iterable<?>)(this.b.keySet())) {
                  String var45 = (String)this.b.get(var42);
                  var36 = var36.replace(var42, var45);
               }

               VersionFileManagerImpl var39 = (VersionFileManagerImpl)VersionFileManager.ins;
               var39.updateContent(var33.getId(), var36);
            }

         } else {
            throw new InfoException("文件不合法，不能导入，请选择一个URule Pro4+项目导出的备份文件");
         }
      }
   }

   private String a(String var1, String var2, String var3) {
      Pattern var4 = Pattern.compile(var1);
      Matcher var5 = var4.matcher(var2);
      StringBuffer var6 = new StringBuffer();

      while(var5.find()) {
         var5.appendReplacement(var6, var3);
      }

      var5.appendTail(var6);
      return var6.toString();
   }

   private String a(String var1, String var2) {
      List var3 = ProjectManager.ins.newQuery().groupId(var1).list();

      for(int var4 = 0; var4 < 10000; ++var4) {
         String var5 = var2;
         if (var4 > 0) {
            var5 = var2 + var4;
         }

         boolean var6 = false;

         for(Project var8 : (Iterable<Project>)(Iterable<?>)(var3)) {
            if (var8.getName().contentEquals(var5)) {
               var6 = true;
               break;
            }
         }

         if (!var6) {
            var2 = var5;
            break;
         }
      }

      return var2;
   }

   private void a(String var1) {
      try {
         List var2 = PacketManager.ins.newQuery().code(var1).list();
         if (var2.size() > 0) {
            Packet var3 = (Packet)var2.get(0);
            long var4 = var3.getProjectId();
            Project var6 = ProjectManager.ins.get(var4);
            String var7 = var6.getGroupId();
            String var8 = "Duplicate packet code " + var1 + "!<br/>The code under " + var6.getName() + "(" + var4 + ") of " + var7 + " repeated";
            throw new DuplicatePacketCodeException(var8);
         }
      } catch (Exception var9) {
         if (var9 instanceof DuplicatePacketCodeException) {
            throw var9;
         } else {
            throw new DuplicatePacketCodeException("Duplicate packet code " + var1 + "!<br/>" + var9.getMessage());
         }
      }
   }

   private boolean a(Element var1, Project var2, ConfigInfo var3) {
      boolean var4 = var3.isReplace();
      boolean var5 = var3.isNewPacketCode();
      if (!var1.getName().contentEquals("packet")) {
         return false;
      } else {
         long var6 = Long.valueOf(var1.attributeValue("id"));
         String var8 = var1.attributeValue("code");
         String var9 = var1.attributeValue("name");
         boolean var10 = false;
         Packet var11 = new Packet();
         if (StringUtils.isNotBlank(var8)) {
            if (var4) {
               List var24 = PacketManager.ins.newQuery().code(var8).list();
               if (var24.size() > 0) {
                  var11 = (Packet)var24.get(0);
                  PacketFileManager.ins.deleteByPacketId(var11.getId());
                  var10 = true;
               }
            }

            if (!var10) {
               try {
                  this.a(var8);
               } catch (DuplicatePacketCodeException var23) {
                  if (!var5) {
                     throw var23;
                  }

                  var8 = "";
               }
            }
         } else if (var4) {
            List var12 = PacketManager.ins.newQuery().name(var9).list();
            if (var12.size() > 0) {
               var11 = (Packet)var12.get(0);
               PacketFileManager.ins.deleteByPacketId(var11.getId());
               var10 = true;
            }
         }

         String var25 = var1.attributeValue("type");
         PacketType var13 = PacketType.file;
         if (var25 != null) {
            var13 = PacketType.valueOf(var25);
         }

         var11.setType(var13);
         var11.setName(var9);
         var11.setCode(var8);
         var11.setDesc(var1.attributeValue("desc"));
         var11.setProjectId(var2.getId());
         var11.setCreateUser(var2.getCreateUser());
         var11.setEnable(Boolean.valueOf(var1.attributeValue("enable")));
         var11.setAuditEnable(Boolean.valueOf(var1.attributeValue("audit-enable")));
         var11.setRestEnable(Boolean.valueOf(var1.attributeValue("rest-enable")));
         var11.setRestSecurityEnable(Boolean.valueOf(var1.attributeValue("rest-security-enable")));
         if (var11.isRestSecurityEnable()) {
            var11.setRestSecurityUser(var1.attributeValue("rest-security-user"));
            var11.setRestSecurityPassword(var1.attributeValue("rest-security-password"));
         }

         String var14 = this.a(var1, "audit-input");
         String var15 = this.a(var1, "audit-output");
         String var16 = this.a(var1, "rest-input");
         String var17 = this.a(var1, "rest-output");
         String var18 = this.a(var1, "input-data");
         String var19 = this.a(var1, "output-data");
         var11.setAuditInput(var14);
         var11.setAuditOutput(var15);
         var11.setRestInput(var16);
         var11.setRestOutput(var17);
         var11.setInputData(var18);
         var11.setOutputData(var19);
         if (var10) {
            PacketManager.ins.update(var11);
            a.debug("processPacket(replace):" + var11.getName());
         } else {
            PacketManager.ins.add(var11);
            a.debug("processPacket(add):" + var11.getName());
         }

         if (var6 != var11.getId()) {
            String var20 = "package-id=\"" + var6 + "\"";
            String var21 = "package-id=\"" + var11.getId() + "\"";
            this.b.put(var20, var21);
         }

         for(Object var27 : var1.elements()) {
            if (var27 instanceof Element) {
               Element var22 = (Element)var27;
               if (var22.getName().contentEquals("file")) {
                  this.a(var22, var11);
               }
            }
         }

         return true;
      }
   }

   private void a(Element var1, Packet var2) {
      PacketFile var3 = new PacketFile();
      long var4 = Long.valueOf(var1.attributeValue("id"));
      String var6 = var1.attributeValue("path");
      RuleFile var7 = (RuleFile)this.c.get(var4);
      if (var7 != null) {
         var3.setFileId(var7.getId());
      } else if (this.h.containsKey(var6)) {
         var3.setFileId((Long)this.h.get(var6));
      } else {
         var3.setFileId(0L);
      }

      var3.setProjectId(var2.getProjectId());
      var3.setCreateUser(var2.getCreateUser());
      var3.setDesc(var1.attributeValue("desc"));
      var3.setPath(var6);
      var3.setPacketId(var2.getId());
      var3.setVersion(var1.attributeValue("version"));
      a.debug("processPacketFile:" + var3.getFileId());
      PacketFileManager.ins.add(var3);
   }

   private boolean b(Element var1, Project var2, ConfigInfo var3) {
      boolean var4 = var3.isReplace();
      boolean var5 = var3.isForceLock();
      if (!var1.getName().contentEquals("file")) {
         return false;
      } else {
         String var6 = var1.attributeValue("path");
         long var7 = Long.valueOf(var1.attributeValue("id"));
         RuleFile var9 = new RuleFile();
         RuleFile var10 = (RuleFile)this.d.get(var6);
         boolean var11 = false;
         if (var10 != null && var4) {
            var9 = var10;
            var11 = true;
         }

         String var12 = var1.attributeValue("deleted");
         if (StringUtils.isNotBlank(var12)) {
            var9.setDeleted(Boolean.valueOf(var12));
         }

         if (var9.isDeleted()) {
            return false;
         } else {
            var9.setName(var1.attributeValue("name"));
            var9.setDigest(var1.attributeValue("digest"));
            var9.setPath(var1.attributeValue("path"));
            var9.setLatestVersion(var1.attributeValue("latest-version"));
            var9.setCreateUser(var2.getCreateUser());
            var9.setProjectId(var2.getId());
            var9.setType(var1.attributeValue("type"));
            String var13 = this.a(var1, "content");
            String var14 = var9.getType();
            if (!var14.contentEquals(ResourceType.ActionLibrary.name()) && !var14.contentEquals(ResourceType.ConstantLibrary.name()) && !var14.contentEquals(ResourceType.ParameterLibrary.name()) && !var14.contentEquals(ResourceType.VariableLibrary.name())) {
               if (!var14.contentEquals(ResourceType.Scorecard.name()) && !var14.contentEquals(ResourceType.ComplexScorecard.name())) {
                  if (var14.contentEquals(ResourceType.DecisionTable.name()) || var14.contentEquals(ResourceType.CrossDecisionTable.name())) {
                     var14 = ResourceType.DecisionTable.name();
                  }
               } else {
                  var14 = ResourceType.Scorecard.name();
               }
            } else {
               var14 = ResourceType.Library.name();
            }

            String var15 = var1.attributeValue("fileSet");
            if (StringUtils.isNotBlank(var15) && Boolean.valueOf(var15)) {
               var14 = ResourceType.General.name();
            }

            var9.setContent(var13);
            RuleFile var16 = this.a(var9.getPath(), var2, var14);
            if (var16 != null) {
               var9.setParentId(var16.getId());
            }

            if (var11) {
               VersionFileManager.ins.deleteByFileId(var9.getId());
               var9.setModifyDate(new Date());
               var9.setUpdateUser(var2.getCreateUser());
               FileManager.ins.update(var9);
               a.debug("processFile(replace):" + var9.getName());
            } else {
               FileManager.ins.add(var9);
               a.debug("processFile(add):" + var9.getName());
            }

            if (var7 != var9.getId()) {
               String var17 = "id=\"" + var7 + "\"";
               String var18 = "id=\"" + var9.getId() + "\"";
               this.b.put(var17, var18);
               var17 = "file=\"" + var7 + "\"";
               var18 = "file=\"" + var9.getId() + "\"";
               this.b.put(var17, var18);
            }

            if (var5) {
               FileManager.ins.lock(var9.getId(), var9.getCreateUser());
            }

            this.c.put(var7, var9);

            for(Object var23 : var1.elements()) {
               if (var23 instanceof Element) {
                  Element var19 = (Element)var23;
                  if (var19.getName().contentEquals("version")) {
                     this.a(var19, var9);
                  }
               }
            }

            return true;
         }
      }
   }

   private void b(String var1) {
      for(Project var4 : (Iterable<Project>)(Iterable<?>)(ProjectManager.ins.newQuery().type("common").groupId(var1).list())) {
         for(Packet var7 : (Iterable<Packet>)(Iterable<?>)(PacketManager.ins.newQuery().projectId(var4.getId()).list())) {
            String var8 = "knowledge project=\"" + var4.getName() + "\" name=\"" + var7.getName() + "\" package-id=\"[0-9]+\"";
            String var9 = "knowledge project=\"" + var4.getName() + "\" name=\"" + var7.getName() + "\" package-id=\"" + var7.getId() + "\"";
            this.f.put(var8, var9);
         }

         List var10 = FileManager.ins.newQuery().containCommonProject(false).tree(var4.getId());
         String var11 = "";

         for(RuleFile var13 : (Iterable<RuleFile>)(Iterable<?>)(var10)) {
            this.a(var11, var13);
         }
      }

   }

   private void a(String var1, RuleFile var2) {
      if (StringUtils.isBlank(var1)) {
         var1 = "/" + var2.getName();
      } else {
         var1 = var1 + "/" + var2.getName();
      }

      if (var2.isDirectory()) {
         if (var2.getChildren() == null) {
            return;
         }

         for(RuleFile var4 : (Iterable<RuleFile>)(Iterable<?>)(var2.getChildren())) {
            this.a(var1, var4);
         }
      } else {
         String var13 = var2.getType();
         if (var13 == null) {
            return;
         }

         if (!var13.contentEquals(ResourceType.ActionLibrary.name()) && !var13.contentEquals(ResourceType.ConstantLibrary.name()) && !var13.contentEquals(ResourceType.ParameterLibrary.name()) && !var13.contentEquals(ResourceType.VariableLibrary.name()) && !var13.contentEquals(ResourceType.ActionTemplate.name()) && !var13.contentEquals(ResourceType.ConditionTemplate.name())) {
            String var15 = "path=\"" + var13 + ":" + var1 + "\" version=\"([0-9]+(\\.[0-9]+)*)?\" id=\"[0-9]+\"";
            String var18 = "path=\"" + var13 + ":" + var1 + "\" version=\"\" id=\"" + var2.getId() + "\"";
            this.g.put(var15, var18);
            String var19 = var1;
            if (var1.startsWith("/")) {
               var19 = var1.substring(1, var1.length());
            }

            var15 = "path=\"" + var13 + ":" + var19 + "\" version=\"([0-9]+(\\.[0-9]+)*)?\" id=\"[0-9]+\"";
            this.g.put(var15, var18);
            String var20 = "id=\"[0-9]+\" path=\"" + var13 + ":" + var1 + "\"/";
            String var21 = "id=\"" + var2.getId() + "\" path=\"" + var13 + ":" + var1 + "\"/";
            this.g.put(var20, var21);
            String var9 = "id=\"[0-9]+\" path=\"" + var13 + ":" + var19 + "\"/";
            this.g.put(var9, var21);
            String var10 = var13 + ":" + var1;
            String var11 = var13 + ":" + var19;
            this.h.put(var10, var2.getId());
            this.h.put(var11, var2.getId());
         } else {
            String var14 = "path=\"" + var13 + ":" + var1 + "\"";
            String var5 = "id=\"[0-9]+\" " + var14 + "";
            String var6 = "id=\"" + var2.getId() + "\" " + var14 + "";
            this.g.put(var5, var6);
            String var7 = var1;
            if (var1.startsWith("/")) {
               var7 = var1.substring(1, var1.length());
            }

            String var8 = "path=\"" + var13 + ":" + var7 + "\"";
            var5 = "id=\"[0-9]+\" " + var8 + "";
            this.g.put(var5, var6);
         }
      }

   }

   private void a(Element var1, RuleFile var2) {
      VersionFile var3 = new VersionFile();
      var3.setProjectId(var2.getProjectId());
      var3.setFileId(var2.getId());
      var3.setName(var2.getName());
      var3.setDigest(var1.attributeValue("digest"));
      var3.setVersion(var1.attributeValue("version"));
      var3.setCreateUser(var2.getCreateUser());
      String var4 = this.a(var1, "content");
      var3.setContent(var4);
      String var5 = this.a(var1, "note");
      var3.setNote(var5);
      VersionFileManager.ins.saveFile(var3);
      this.e.add(var3);
   }

   protected RuleFile a(String var1, Project var2, String var3) {
      if (var1.startsWith("/")) {
         var1 = var1.substring(1);
      }

      int var4 = var1.indexOf(":");
      if (var4 > -1) {
         var1 = var1.substring(var4 + 1);
      }

      int var5 = var1.indexOf("/");
      var1 = var1.substring(var5 + 1);
      var5 = var1.lastIndexOf("/");
      if (var5 == -1) {
         return null;
      } else {
         String var6 = var1.substring(0, var5);
         String[] var7 = var6.split("/");
         long var8 = 0L;
         RuleFile var10 = null;

         for(String var14 : var7) {
            DirectoryManagerImpl var15 = (DirectoryManagerImpl)DirectoryManager.ins;
            var10 = var15.loadDir(var2.getId(), var8, var14, var3);
            if (var10 == null) {
               var10 = new RuleFile();
               var10.setProjectId(var2.getId());
               var10.setParentId(var8);
               var10.setName(var14);
               var10.setType(var3);
               var10.setCreateUser(var2.getCreateUser());
               var15.add(var10);
            }

            var8 = var10.getId();
         }

         return var10;
      }
   }

   private String a(Element var1, String var2) {
      String var3 = null;

      for(Object var5 : var1.elements()) {
         if (var5 instanceof Element) {
            Element var6 = (Element)var5;
            if (var6.getName().contentEquals(var2)) {
               var3 = var6.getText();
               break;
            }
         }
      }

      if (StringUtils.isNotBlank(var3)) {
         try {
            var3 = IOUtils.toString(Base64.getDecoder().decode(var3), "utf-8");
         } catch (IOException var7) {
            throw new RuleException(var7);
         }
      }

      return var3;
   }
}
