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
   private static final Log logger = LogFactory.getLog(ProjectImport.class);
   private Map idTextReplacements = new HashMap();
   private Map importedFilesByOriginalId = new HashMap();
   private Map existingFilesByPath = new HashMap();
   private List importedVersionFiles = new ArrayList();
   private Map knowledgePackageReferenceReplacements = new HashMap();
   private Map resourceReferenceReplacements = new HashMap();
   private Map importedFileIdsByPath = new HashMap();

   public void doImport(InputStream inputStream, Group group, ConfigInfo info) throws Exception {
      this.importReferencedResources(group.getId());
      byte[] bytes = IOUtils.toByteArray(inputStream);
      String text2 = Utils.uncompress(bytes);
      Document text = DocumentHelper.parseText(text2);
      Element rootElement = text.getRootElement();
      if (!rootElement.getName().contentEquals("project")) {
         throw new InfoException("文件不合法，不能导入，请选择一个URule Pro4+项目导出的备份文件");
      } else {
         Long longValue = Long.parseLong(rootElement.attributeValue("id"));
         String text3 = rootElement.attributeValue("name");
         String text4 = rootElement.attributeValue("type");
         String text5 = rootElement.attributeValue("viewModel");
         ProjectViewModel projectViewModel = ProjectViewModel.category;
         if (StringUtils.isNotBlank(text5)) {
            projectViewModel = ProjectViewModel.valueOf(text5);
         }

         if (!StringUtils.isBlank(text3) && !StringUtils.isBlank(text4)) {
            Project project = null;
            User loginUser = SecurityUtils.getLoginUser(RequestHolder.getRequest());
            List items = ProjectManager.ins.newQuery().groupId(group.getId()).name(text3).list();
            if (info.isReplace() && items.size() > 0) {
               project = (Project)items.get(0);
               project.setType(text4);
               project.setViewModel(projectViewModel);
               project.setUpdateUser(loginUser.getName());
               project.setUpdateDate(new Date());
               String text6 = this.readEncodedChild(rootElement, "desc");
               project.setDesc(text6);
               ProjectManager.ins.update(project);
               ProjectImport.logger.debug("processProject(replace):" + project.getName());

               for(RuleFile ruleFile : (Iterable<RuleFile>)(Iterable<?>)(FileManager.ins.newQuery().list(project.getId()))) {
                  if (!ruleFile.isDeleted()) {
                     this.existingFilesByPath.put(ruleFile.getPath(), ruleFile);
                  }
               }
            } else {
               project = new Project();
               text3 = this.ensureUniqueProjectName(group.getId(), text3);
               project.setName(text3);
               project.setType(text4);
               project.setViewModel(projectViewModel);
               project.setCreateUser(loginUser.getName());
               project.setId(0L);
               project.setGroupId(group.getId());
               project.setDeployApproveUser(loginUser.getName());
               project.setDisableApproveUser(loginUser.getName());
               project.setEnableApproveUser(loginUser.getName());
               project.setUpdateUser(loginUser.getName());
               String text7 = this.readEncodedChild(rootElement, "desc");
               project.setDesc(text7);
               ProjectService.ins.add(project);
               ProjectImport.logger.debug("processProject(add):" + project.getName());
            }

            if (longValue != project.getId()) {
               String text8 = "project=\"" + longValue + "\"";
               String text9 = "project=\"" + project.getId() + "\"";
               this.idTextReplacements.put(text8, text9);
            }

            for(Object objectValue : rootElement.elements()) {
               if (objectValue instanceof Element) {
                  Element element = (Element)objectValue;
                  if (!this.importReferencedResources(element, project, info) && this.importPacket(element, project, info)) {
                  }
               }
            }

            for(RuleFile ruleFile2 : (Iterable<RuleFile>)(Iterable<?>)(this.importedFilesByOriginalId.values())) {
               String type = ruleFile2.getType();
               if (!type.contentEquals(ResourceType.ActionLibrary.name()) && !type.contentEquals(ResourceType.ConstantLibrary.name()) && !type.contentEquals(ResourceType.ParameterLibrary.name()) && !type.contentEquals(ResourceType.VariableLibrary.name())) {
                  String content = ruleFile2.getContent();

                  for(String text10 : (Iterable<String>)(Iterable<?>)(this.idTextReplacements.keySet())) {
                     String text11 = (String)this.idTextReplacements.get(text10);
                     if (text10.startsWith("file=")) {
                        if (type.contentEquals(ResourceType.Flow.name())) {
                           content = content.replace(text10, text11);
                        }
                     } else {
                        content = content.replace(text10, text11);
                     }
                  }

                  for(String text12 : (Iterable<String>)(Iterable<?>)(this.resourceReferenceReplacements.keySet())) {
                     String text13 = (String)this.resourceReferenceReplacements.get(text12);
                     content = this.replaceReferences(text12, content, text13);
                  }

                  for(String text14 : (Iterable<String>)(Iterable<?>)(this.knowledgePackageReferenceReplacements.keySet())) {
                     String text15 = (String)this.knowledgePackageReferenceReplacements.get(text14);
                     content = this.replaceReferences(text14, content, text15);
                  }

                  FileManager.ins.updateContent(ruleFile2.getId(), ruleFile2.getCreateUser(), content);
               }
            }

            for(VersionFile versionFile : (Iterable<VersionFile>)(Iterable<?>)(this.importedVersionFiles)) {
               String replacedText = versionFile.getContent();

               for(String text16 : (Iterable<String>)(Iterable<?>)(this.idTextReplacements.keySet())) {
                  String text17 = (String)this.idTextReplacements.get(text16);
                  replacedText = replacedText.replace(text16, text17);
               }

               VersionFileManagerImpl versionFileManagerImpl = (VersionFileManagerImpl)VersionFileManager.ins;
               versionFileManagerImpl.updateContent(versionFile.getId(), replacedText);
            }

         } else {
            throw new InfoException("文件不合法，不能导入，请选择一个URule Pro4+项目导出的备份文件");
         }
      }
   }

   private String replaceReferences(String text, String text2, String text3) {
      Pattern pattern = Pattern.compile(text);
      Matcher matcher = pattern.matcher(text2);
      StringBuffer stringBuffer = new StringBuffer();

      while(matcher.find()) {
         matcher.appendReplacement(stringBuffer, text3);
      }

      matcher.appendTail(stringBuffer);
      return stringBuffer.toString();
   }

   private String ensureUniqueProjectName(String text, String text2) {
      List items = ProjectManager.ins.newQuery().groupId(text).list();

      for(int index = 0; index < 10000; ++index) {
         String text22 = text2;
         if (index > 0) {
            text22 = text2 + index;
         }

         boolean flag = false;

         for(Project project : (Iterable<Project>)(Iterable<?>)(items)) {
            if (project.getName().contentEquals(text22)) {
               flag = true;
               break;
            }
         }

         if (!flag) {
            text2 = text22;
            break;
         }
      }

      return text2;
   }

   private void verifyPacketCodeAvailable(String text) {
      try {
         List items = PacketManager.ins.newQuery().code(text).list();
         if (items.size() > 0) {
            Packet packet = (Packet)items.get(0);
            long projectId = packet.getProjectId();
            Project project = ProjectManager.ins.get(projectId);
            String groupId = project.getGroupId();
            String text2 = "Duplicate packet code " + text + "!<br/>The code under " + project.getName() + "(" + projectId + ") of " + groupId + " repeated";
            throw new DuplicatePacketCodeException(text2);
         }
      } catch (Exception exception) {
         if (exception instanceof DuplicatePacketCodeException) {
            throw exception;
         } else {
            throw new DuplicatePacketCodeException("Duplicate packet code " + text + "!<br/>" + exception.getMessage());
         }
      }
   }

   private boolean importPacket(Element element, Project project, ConfigInfo configInfo) {
      boolean flag = configInfo.isReplace();
      boolean flag2 = configInfo.isNewPacketCode();
      if (!element.getName().contentEquals("packet")) {
         return false;
      } else {
         long longValue = Long.valueOf(element.attributeValue("id"));
         String text = element.attributeValue("code");
         String text2 = element.attributeValue("name");
         boolean flag3 = false;
         Packet packet = new Packet();
         if (StringUtils.isNotBlank(text)) {
            if (flag) {
               List items = PacketManager.ins.newQuery().code(text).list();
               if (items.size() > 0) {
                  packet = (Packet)items.get(0);
                  PacketFileManager.ins.deleteByPacketId(packet.getId());
                  flag3 = true;
               }
            }

            if (!flag3) {
               try {
                  this.verifyPacketCodeAvailable(text);
               } catch (DuplicatePacketCodeException duplicatePacketCodeException) {
                  if (!flag2) {
                     throw duplicatePacketCodeException;
                  }

                  text = "";
               }
            }
         } else if (flag) {
            List items2 = PacketManager.ins.newQuery().name(text2).list();
            if (items2.size() > 0) {
               packet = (Packet)items2.get(0);
               PacketFileManager.ins.deleteByPacketId(packet.getId());
               flag3 = true;
            }
         }

         String text3 = element.attributeValue("type");
         PacketType packetType = PacketType.file;
         if (text3 != null) {
            packetType = PacketType.valueOf(text3);
         }

         packet.setType(packetType);
         packet.setName(text2);
         packet.setCode(text);
         packet.setDesc(element.attributeValue("desc"));
         packet.setProjectId(project.getId());
         packet.setCreateUser(project.getCreateUser());
         packet.setEnable(Boolean.valueOf(element.attributeValue("enable")));
         packet.setAuditEnable(Boolean.valueOf(element.attributeValue("audit-enable")));
         packet.setRestEnable(Boolean.valueOf(element.attributeValue("rest-enable")));
         packet.setRestSecurityEnable(Boolean.valueOf(element.attributeValue("rest-security-enable")));
         if (packet.isRestSecurityEnable()) {
            packet.setRestSecurityUser(element.attributeValue("rest-security-user"));
            packet.setRestSecurityPassword(element.attributeValue("rest-security-password"));
         }

         String text4 = this.readEncodedChild(element, "audit-input");
         String text5 = this.readEncodedChild(element, "audit-output");
         String text6 = this.readEncodedChild(element, "rest-input");
         String text7 = this.readEncodedChild(element, "rest-output");
         String text8 = this.readEncodedChild(element, "input-data");
         String text9 = this.readEncodedChild(element, "output-data");
         packet.setAuditInput(text4);
         packet.setAuditOutput(text5);
         packet.setRestInput(text6);
         packet.setRestOutput(text7);
         packet.setInputData(text8);
         packet.setOutputData(text9);
         if (flag3) {
            PacketManager.ins.update(packet);
            ProjectImport.logger.debug("processPacket(replace):" + packet.getName());
         } else {
            PacketManager.ins.add(packet);
            ProjectImport.logger.debug("processPacket(add):" + packet.getName());
         }

         if (longValue != packet.getId()) {
            String text10 = "package-id=\"" + longValue + "\"";
            String text11 = "package-id=\"" + packet.getId() + "\"";
            this.idTextReplacements.put(text10, text11);
         }

         for(Object objectValue : element.elements()) {
            if (objectValue instanceof Element) {
               Element element2 = (Element)objectValue;
               if (element2.getName().contentEquals("file")) {
                  this.importPacketFile(element2, packet);
               }
            }
         }

         return true;
      }
   }

   private void importPacketFile(Element element, Packet packet) {
      PacketFile packetFile = new PacketFile();
      long longValue = Long.valueOf(element.attributeValue("id"));
      String text = element.attributeValue("path");
      RuleFile ruleFile = (RuleFile)this.importedFilesByOriginalId.get(longValue);
      if (ruleFile != null) {
         packetFile.setFileId(ruleFile.getId());
      } else if (this.importedFileIdsByPath.containsKey(text)) {
         packetFile.setFileId((Long)this.importedFileIdsByPath.get(text));
      } else {
         packetFile.setFileId(0L);
      }

      packetFile.setProjectId(packet.getProjectId());
      packetFile.setCreateUser(packet.getCreateUser());
      packetFile.setDesc(element.attributeValue("desc"));
      packetFile.setPath(text);
      packetFile.setPacketId(packet.getId());
      packetFile.setVersion(element.attributeValue("version"));
      ProjectImport.logger.debug("processPacketFile:" + packetFile.getFileId());
      PacketFileManager.ins.add(packetFile);
   }

   private boolean importReferencedResources(Element element, Project project, ConfigInfo configInfo) {
      boolean flag = configInfo.isReplace();
      boolean flag2 = configInfo.isForceLock();
      if (!element.getName().contentEquals("file")) {
         return false;
      } else {
         String text = element.attributeValue("path");
         long longValue = Long.valueOf(element.attributeValue("id"));
         RuleFile ruleFile = new RuleFile();
         RuleFile ruleFile2 = (RuleFile)this.existingFilesByPath.get(text);
         boolean flag3 = false;
         if (ruleFile2 != null && flag) {
            ruleFile = ruleFile2;
            flag3 = true;
         }

         String text2 = element.attributeValue("deleted");
         if (StringUtils.isNotBlank(text2)) {
            ruleFile.setDeleted(Boolean.valueOf(text2));
         }

         if (ruleFile.isDeleted()) {
            return false;
         } else {
            ruleFile.setName(element.attributeValue("name"));
            ruleFile.setDigest(element.attributeValue("digest"));
            ruleFile.setPath(element.attributeValue("path"));
            ruleFile.setLatestVersion(element.attributeValue("latest-version"));
            ruleFile.setCreateUser(project.getCreateUser());
            ruleFile.setProjectId(project.getId());
            ruleFile.setType(element.attributeValue("type"));
            String text3 = this.readEncodedChild(element, "content");
            String type = ruleFile.getType();
            if (!type.contentEquals(ResourceType.ActionLibrary.name()) && !type.contentEquals(ResourceType.ConstantLibrary.name()) && !type.contentEquals(ResourceType.ParameterLibrary.name()) && !type.contentEquals(ResourceType.VariableLibrary.name())) {
               if (!type.contentEquals(ResourceType.Scorecard.name()) && !type.contentEquals(ResourceType.ComplexScorecard.name())) {
                  if (type.contentEquals(ResourceType.DecisionTable.name()) || type.contentEquals(ResourceType.CrossDecisionTable.name())) {
                     type = ResourceType.DecisionTable.name();
                  }
               } else {
                  type = ResourceType.Scorecard.name();
               }
            } else {
               type = ResourceType.Library.name();
            }

            String text4 = element.attributeValue("fileSet");
            if (StringUtils.isNotBlank(text4) && Boolean.valueOf(text4)) {
               type = ResourceType.General.name();
            }

            ruleFile.setContent(text3);
            RuleFile dir = this.buildDir(ruleFile.getPath(), project, type);
            if (dir != null) {
               ruleFile.setParentId(dir.getId());
            }

            if (flag3) {
               VersionFileManager.ins.deleteByFileId(ruleFile.getId());
               ruleFile.setModifyDate(new Date());
               ruleFile.setUpdateUser(project.getCreateUser());
               FileManager.ins.update(ruleFile);
               ProjectImport.logger.debug("processFile(replace):" + ruleFile.getName());
            } else {
               FileManager.ins.add(ruleFile);
               ProjectImport.logger.debug("processFile(add):" + ruleFile.getName());
            }

            if (longValue != ruleFile.getId()) {
               String text5 = "id=\"" + longValue + "\"";
               String text6 = "id=\"" + ruleFile.getId() + "\"";
               this.idTextReplacements.put(text5, text6);
               text5 = "file=\"" + longValue + "\"";
               text6 = "file=\"" + ruleFile.getId() + "\"";
               this.idTextReplacements.put(text5, text6);
            }

            if (flag2) {
               FileManager.ins.lock(ruleFile.getId(), ruleFile.getCreateUser());
            }

            this.importedFilesByOriginalId.put(longValue, ruleFile);

            for(Object objectValue : element.elements()) {
               if (objectValue instanceof Element) {
                  Element element2 = (Element)objectValue;
                  if (element2.getName().contentEquals("version")) {
                     this.importVersionFile(element2, ruleFile);
                  }
               }
            }

            return true;
         }
      }
   }

   private void importReferencedResources(String text) {
      for(Project project : (Iterable<Project>)(Iterable<?>)(ProjectManager.ins.newQuery().type("common").groupId(text).list())) {
         for(Packet packet : (Iterable<Packet>)(Iterable<?>)(PacketManager.ins.newQuery().projectId(project.getId()).list())) {
            String text2 = "knowledge project=\"" + project.getName() + "\" name=\"" + packet.getName() + "\" package-id=\"[0-9]+\"";
            String text3 = "knowledge project=\"" + project.getName() + "\" name=\"" + packet.getName() + "\" package-id=\"" + packet.getId() + "\"";
            this.knowledgePackageReferenceReplacements.put(text2, text3);
         }

         List items = FileManager.ins.newQuery().containCommonProject(false).tree(project.getId());
         String text4 = "";

         for(RuleFile ruleFile : (Iterable<RuleFile>)(Iterable<?>)(items)) {
            this.indexFilePaths(text4, ruleFile);
         }
      }

   }

   private void indexFilePaths(String text, RuleFile ruleFile) {
      if (StringUtils.isBlank(text)) {
         text = "/" + ruleFile.getName();
      } else {
         text = text + "/" + ruleFile.getName();
      }

      if (ruleFile.isDirectory()) {
         if (ruleFile.getChildren() == null) {
            return;
         }

         for(RuleFile ruleFile2 : (Iterable<RuleFile>)(Iterable<?>)(ruleFile.getChildren())) {
            this.indexFilePaths(text, ruleFile2);
         }
      } else {
         String type = ruleFile.getType();
         if (type == null) {
            return;
         }

         if (!type.contentEquals(ResourceType.ActionLibrary.name()) && !type.contentEquals(ResourceType.ConstantLibrary.name()) && !type.contentEquals(ResourceType.ParameterLibrary.name()) && !type.contentEquals(ResourceType.VariableLibrary.name()) && !type.contentEquals(ResourceType.ActionTemplate.name()) && !type.contentEquals(ResourceType.ConditionTemplate.name())) {
            String text2 = "path=\"" + type + ":" + text + "\" version=\"([0-9]+(\\.[0-9]+)*)?\" id=\"[0-9]+\"";
            String text3 = "path=\"" + type + ":" + text + "\" version=\"\" id=\"" + ruleFile.getId() + "\"";
            this.resourceReferenceReplacements.put(text2, text3);
            String substring = text;
            if (text.startsWith("/")) {
               substring = text.substring(1, text.length());
            }

            text2 = "path=\"" + type + ":" + substring + "\" version=\"([0-9]+(\\.[0-9]+)*)?\" id=\"[0-9]+\"";
            this.resourceReferenceReplacements.put(text2, text3);
            String text4 = "id=\"[0-9]+\" path=\"" + type + ":" + text + "\"/";
            String text5 = "id=\"" + ruleFile.getId() + "\" path=\"" + type + ":" + text + "\"/";
            this.resourceReferenceReplacements.put(text4, text5);
            String text6 = "id=\"[0-9]+\" path=\"" + type + ":" + substring + "\"/";
            this.resourceReferenceReplacements.put(text6, text5);
            String text7 = type + ":" + text;
            String text8 = type + ":" + substring;
            this.importedFileIdsByPath.put(text7, ruleFile.getId());
            this.importedFileIdsByPath.put(text8, ruleFile.getId());
         } else {
            String text9 = "path=\"" + type + ":" + text + "\"";
            String text10 = "id=\"[0-9]+\" " + text9 + "";
            String text11 = "id=\"" + ruleFile.getId() + "\" " + text9 + "";
            this.resourceReferenceReplacements.put(text10, text11);
            String substring2 = text;
            if (text.startsWith("/")) {
               substring2 = text.substring(1, text.length());
            }

            String text12 = "path=\"" + type + ":" + substring2 + "\"";
            text10 = "id=\"[0-9]+\" " + text12 + "";
            this.resourceReferenceReplacements.put(text10, text11);
         }
      }

   }

   private void importVersionFile(Element element, RuleFile ruleFile) {
      VersionFile versionFile = new VersionFile();
      versionFile.setProjectId(ruleFile.getProjectId());
      versionFile.setFileId(ruleFile.getId());
      versionFile.setName(ruleFile.getName());
      versionFile.setDigest(element.attributeValue("digest"));
      versionFile.setVersion(element.attributeValue("version"));
      versionFile.setCreateUser(ruleFile.getCreateUser());
      String text = this.readEncodedChild(element, "content");
      versionFile.setContent(text);
      String text2 = this.readEncodedChild(element, "note");
      versionFile.setNote(text2);
      VersionFileManager.ins.saveFile(versionFile);
      this.importedVersionFiles.add(versionFile);
   }

   protected RuleFile buildDir(String path, Project project, String type) {
      if (path.startsWith("/")) {
         path = path.substring(1);
      }

      int number = path.indexOf(":");
      if (number > -1) {
         path = path.substring(number + 1);
      }

      int number2 = path.indexOf("/");
      path = path.substring(number2 + 1);
      number2 = path.lastIndexOf("/");
      if (number2 == -1) {
         return null;
      } else {
         String substring = path.substring(0, number2);
         String[] parts = substring.split("/");
         long id = 0L;
         RuleFile ruleFile = null;

         for(String text : parts) {
            DirectoryManagerImpl directoryManagerImpl = (DirectoryManagerImpl)DirectoryManager.ins;
            ruleFile = directoryManagerImpl.loadDir(project.getId(), id, text, type);
            if (ruleFile == null) {
               ruleFile = new RuleFile();
               ruleFile.setProjectId(project.getId());
               ruleFile.setParentId(id);
               ruleFile.setName(text);
               ruleFile.setType(type);
               ruleFile.setCreateUser(project.getCreateUser());
               directoryManagerImpl.add(ruleFile);
            }

            id = ruleFile.getId();
         }

         return ruleFile;
      }
   }

   private String readEncodedChild(Element element, String text) {
      String text2 = null;

      for(Object objectValue : element.elements()) {
         if (objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().contentEquals(text)) {
               text2 = element2.getText();
               break;
            }
         }
      }

      if (StringUtils.isNotBlank(text2)) {
         try {
            text2 = IOUtils.toString(Base64.getDecoder().decode(text2), "utf-8");
         } catch (IOException iOException) {
            throw new RuleException(iOException);
         }
      }

      return text2;
   }
}
