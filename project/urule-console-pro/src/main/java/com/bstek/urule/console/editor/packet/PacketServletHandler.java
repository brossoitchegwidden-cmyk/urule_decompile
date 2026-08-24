package com.bstek.urule.console.editor.packet;

import com.bstek.urule.Utils;
import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.ContextHolder;
import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.Transactional;
import com.bstek.urule.console.TransactionalInvoke;
import com.bstek.urule.console.admin.log.SystemLogUtils;
import com.bstek.urule.console.cache.packet.PacketCache;
import com.bstek.urule.console.cache.packet.PacketCacheImpl;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.manager.packet.PacketManager;
import com.bstek.urule.console.database.manager.packet.PacketQuery;
import com.bstek.urule.console.database.manager.packet.file.PacketFileManager;
import com.bstek.urule.console.database.manager.packet.packge.PacketPackageManager;
import com.bstek.urule.console.database.manager.project.ProjectManager;
import com.bstek.urule.console.database.model.Packet;
import com.bstek.urule.console.database.model.PacketFile;
import com.bstek.urule.console.database.model.PacketPackage;
import com.bstek.urule.console.database.model.PacketType;
import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.service.reference.ReferenceService;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.security.URuleAuthorization;
import com.bstek.urule.console.type.RuleFileType;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.DeserializeException;
import com.bstek.urule.exception.ReferenceDeleteException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

public class PacketServletHandler extends ApiServletHandler {
   public void load(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      int number = Integer.valueOf(req.getParameter("pageIndex"));
      int number2 = Integer.valueOf(req.getParameter("pageSize"));
      PacketQuery packetQuery = PacketManager.ins.newQuery();
      packetQuery.nameLike(req.getParameter("name"));
      packetQuery.descLike(req.getParameter("desc"));
      packetQuery.createUserLike(req.getParameter("createUser"));
      packetQuery.projectId(Long.valueOf(req.getParameter("projectId")));
      packetQuery.typeLike(req.getParameter("type"));
      String parameter = req.getParameter("enable");
      if (StringUtils.isNotBlank(parameter)) {
         packetQuery.enable(Boolean.valueOf(parameter));
      }

      String parameter2 = req.getParameter("id");
      if (StringUtils.isNotBlank(parameter2)) {
         packetQuery.idLike(parameter2);
      }

      String parameter3 = req.getParameter("code");
      if (StringUtils.isNotBlank(parameter3)) {
         packetQuery.codeLike(parameter3);
      }

      String parameter4 = req.getParameter("restEnable");
      if (StringUtils.isNotBlank(parameter4)) {
         packetQuery.restEnable(Boolean.valueOf(parameter4));
      }

      String parameter5 = req.getParameter("auditEnable");
      if (StringUtils.isNotBlank(parameter5)) {
         packetQuery.auditEnable(Boolean.valueOf(parameter5));
      }

      Page page = packetQuery.paging(number, number2);
      this.writeObjectToJson(resp, page);
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void add(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      Packet packet = new Packet();
      String parameter = req.getParameter("code");
      if (StringUtils.isNotBlank(parameter)) {
         parameter = parameter.trim();
         if (StringUtils.hasChineseChar(parameter)) {
            throw new InfoException("编码不能包含中文字符<br>The code cannot contain Chinese characters.");
         }

         if (StringUtils.hasSpecialChar(parameter)) {
            throw new InfoException("编码不能包含特殊字符.<br/>The code cannot contain special characters.");
         }

         packet.setCode(parameter);
         List items = PacketManager.ins.newQuery().code(parameter).list();
         if (items.size() > 0) {
            Project project = ProjectManager.ins.get(((Packet)items.get(0)).getProjectId());
            String text = "Duplicate packet code " + parameter + "!<br/>The code under " + project.getName() + "(" + packet.getProjectId() + ") of " + project.getGroupId() + " repeated";
            throw new InfoException(text);
         }
      }

      packet.setCreateUser(SecurityUtils.getLoginUsername(req));
      packet.setProjectId(ContextHolder.getProjectId());
      packet.setName(req.getParameter("name"));
      packet.setDesc(req.getParameter("desc"));
      PacketType packetType = PacketType.valueOf(req.getParameter("type"));
      packet.setType(packetType);
      packet.setInputData(req.getParameter("inputData"));
      packet.setOutputData(req.getParameter("outputData"));
      packet.setEnable(Boolean.valueOf(req.getParameter("enable")));
      packet.setProjectId(Long.valueOf(req.getParameter("projectId")));
      PacketManager.ins.add(packet);
      SystemLogUtils.addProjectOperationLog(RuleFileType.Knowledge.name(), "add", packet.getId(), String.format("Add packet %s[%s]", packet.getName(), packet.getCode()));
      this.writeObjectToJson(resp, packet);
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void update(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      Packet packet = new Packet();
      Long longValue = Long.valueOf(req.getParameter("id"));
      String parameter = req.getParameter("code");
      Packet packet2 = PacketManager.ins.load(longValue);
      packet.setId(longValue);
      packet.setUpdateUser(SecurityUtils.getLoginUsername(req));
      packet.setName(req.getParameter("name"));
      packet.setCode(parameter);
      packet.setDesc(req.getParameter("desc"));
      packet.setInputData(req.getParameter("inputData"));
      packet.setOutputData(req.getParameter("outputData"));
      packet.setEnable(Boolean.valueOf(req.getParameter("enable")));
      packet.setUpdateDate(new Date());
      if (packet2 != null) {
         PacketManager.ins.update(packet);
         if (StringUtils.isNotBlank(packet2.getCode()) && !packet2.getCode().equals(parameter)) {
            String code = packet2.getCode();
            ((PacketCacheImpl)PacketCache.ins).removePacket(code);
            PacketCache.ins.refreshPacket(longValue);
         }
      }

      packet = PacketManager.ins.load(packet.getId());
      SystemLogUtils.addProjectOperationLog(RuleFileType.Knowledge.name(), "update", packet.getId(), String.format("Update packet %s[%s]", packet.getName(), packet.getCode()));
      this.writeObjectToJson(resp, packet);
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void updateAuditConfig(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      Packet packet = new Packet();
      packet.setId(Long.valueOf(req.getParameter("id")));
      packet.setUpdateUser(SecurityUtils.getLoginUsername(req));
      packet.setAuditEnable(Boolean.valueOf(req.getParameter("auditEnable")));
      if (packet.isAuditEnable()) {
         packet.setAuditInput(req.getParameter("auditInput"));
         packet.setAuditOutput(req.getParameter("auditOutput"));
      }

      packet.setUpdateDate(new Date());
      PacketManager.ins.updateAuditConfig(packet);
      PacketCache.ins.refreshPacketConfig(packet.getId());
      this.writeObjectToJson(resp, packet);
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void delete(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("id"));
      boolean flag = Boolean.valueOf(req.getParameter("force"));
      Packet packet = PacketManager.ins.load(longValue);
      if (packet != null) {
         if (!flag) {
            try {
               List items = ReferenceService.ins.packet(packet.getProjectId(), longValue, packet.getCode());
               if (items.size() > 0) {
                  throw new ReferenceDeleteException(items.size());
               }
            } catch (DeserializeException deserializeException) {
               throw new ReferenceDeleteException(deserializeException.getMessage());
            }
         }

         PacketManager.ins.delete(longValue);
         ((PacketCacheImpl)PacketCache.ins).removePacket(longValue);
         ((PacketCacheImpl)PacketCache.ins).removePacket(packet.getCode());
         SystemLogUtils.addProjectOperationLog(RuleFileType.Knowledge.name(), "delete", packet.getId(), String.format("Remove packet %s[%s]", packet.getName(), packet.getCode()));
      }

   }

   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void addFile(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      PacketFile packetFile = new PacketFile();
      packetFile.setCreateUser(SecurityUtils.getLoginUsername(req));
      packetFile.setFileId(Long.valueOf(req.getParameter("fileId")));
      packetFile.setPacketId(Long.valueOf(req.getParameter("packetId")));
      packetFile.setDesc(req.getParameter("desc"));
      packetFile.setPath(req.getParameter("path"));
      packetFile.setVersion(req.getParameter("version"));
      RuleFile ruleFile = FileManager.ins.get(packetFile.getFileId());
      Packet packet = PacketManager.ins.load(packetFile.getPacketId());
      if (ruleFile != null && packet != null) {
         packetFile.setProjectId(packet.getProjectId());
         PacketFileManager.ins.add(packetFile);
         packet.setUpdateDate(new Date());
         packet.setUpdateUser(SecurityUtils.getLoginUsername(req));
         PacketManager.ins.update(packet);
         String text = String.format("Add rule file %s[%s] to packet %s[%s]", ruleFile.getName(), ruleFile.getId(), packet.getName(), packet.getCode());
         SystemLogUtils.addProjectOperationLog(RuleFileType.Knowledge.name(), "update", packet.getId(), text);
         this.writeObjectToJson(resp, packetFile);
      }

   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void uploadPackage(final HttpServletRequest req, HttpServletResponse resp) throws Exception {
      final PacketPackage packetPackage = new PacketPackage();
      packetPackage.setCreateUser(SecurityUtils.getLoginUsername(req));
      packetPackage.setUpdateUser(SecurityUtils.getLoginUsername(req));
      packetPackage.setPacketId(Long.valueOf(req.getParameter("packetId")));
      packetPackage.setUpdateDate(new Date());
      final Packet packet = PacketManager.ins.load(packetPackage.getPacketId());
      packetPackage.setProjectId(packet.getProjectId());
      DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
      ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
      servletFileUpload.setHeaderEncoding("UTF-8");

      for(FileItem fileItem : servletFileUpload.parseRequest(req)) {
         String fieldName = fileItem.getFieldName();
         if (fieldName.equals("file")) {
            InputStream inputStream = fileItem.getInputStream();
            String text = Utils.uncompress(IOUtils.toByteArray(inputStream));
            packetPackage.setContent(text);
         } else if (fieldName.contentEquals("desc")) {
            String string = fileItem.getString("utf-8");
            packetPackage.setDesc(string);
         }
      }

      if (StringUtils.isBlank(packetPackage.getContent())) {
         throw new InfoException("请上传导出的知识包文件");
      } else {
         final String parameter = req.getParameter("id");
         this.doInTransactional(new TransactionalInvoke() {
            public void doTransactional() {
               if (StringUtils.isBlank(parameter)) {
                  PacketPackageManager.ins.add(packetPackage);
               } else {
                  packetPackage.setId(Long.valueOf(parameter));
                  PacketPackageManager.ins.update(packetPackage);
               }

               packet.setUpdateDate(new Date());
               packet.setUpdateUser(SecurityUtils.getLoginUsername(req));
               PacketManager.ins.update(packet);
            }
         });
         String text2 = String.format("Pocket File Additional Packet %s[%s]", packet.getName(), packet.getCode());
         SystemLogUtils.addProjectOperationLog(RuleFileType.Knowledge.name(), "update", packet.getId(), text2);
         packetPackage.setContent((String)null);
         HashMap valuesByKey = new HashMap();
         if (packet.isEnable()) {
            PacketCache.ins.cacheUploadPacketPackage(packet.getId());
            List items = PacketCache.ins.refreshPacket(packet.getId());
            valuesByKey.put("sendResult", items);
         } else {
            valuesByKey.put("pk", packetPackage);
         }

         this.writeObjectToJson(resp, valuesByKey);
      }
   }

   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void updateFile(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("id"));
      PacketFile packetFile = PacketFileManager.ins.load(longValue);
      packetFile.setUpdateUser(SecurityUtils.getLoginUsername(req));
      packetFile.setDesc(req.getParameter("desc"));
      packetFile.setFileId(Long.valueOf(req.getParameter("fileId")));
      packetFile.setPath(req.getParameter("path"));
      packetFile.setVersion(req.getParameter("version"));
      packetFile.setUpdateDate(new Date());
      PacketFileManager.ins.update(packetFile);
      Packet packet = PacketManager.ins.load(packetFile.getPacketId());
      packet.setUpdateDate(new Date());
      packet.setUpdateUser(SecurityUtils.getLoginUsername(req));
      PacketManager.ins.update(packet);
      this.writeObjectToJson(resp, packetFile);
   }

   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void deleteFile(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("id"));
      PacketFile packetFile = PacketFileManager.ins.load(longValue);
      if (packetFile != null) {
         PacketFileManager.ins.delete(longValue);
         Packet packet = PacketManager.ins.load(packetFile.getPacketId());
         packet.setUpdateDate(new Date());
         packet.setUpdateUser(SecurityUtils.getLoginUsername(req));
         PacketManager.ins.update(packet);
      }

   }

   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void updateRestConfig(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      Packet packet = new Packet();
      packet.setId(Long.valueOf(req.getParameter("id")));
      packet.setRestEnable(Boolean.valueOf(req.getParameter("restEnable")));
      if (packet.isRestEnable()) {
         packet.setRestInput(req.getParameter("restInput"));
         packet.setRestOutput(req.getParameter("restOutput"));
         packet.setRestSecurityEnable(Boolean.valueOf(req.getParameter("restSecurityEnable")));
         packet.setRestSecurityUser(req.getParameter("restSecurityUser"));
         packet.setRestSecurityPassword(req.getParameter("restSecurityPassword"));
      }

      packet.setUpdateDate(new Date());
      packet.setUpdateUser(SecurityUtils.getLoginUsername(req));
      Packet packet2 = PacketManager.ins.load(packet.getId());
      boolean flag = false;
      if (packet2.isRestEnable() != packet.isRestEnable()) {
         flag = true;
      }

      if (packet.isRestEnable()) {
         if (!StringUtils.trimToEmpty(packet.getRestInput()).equals(StringUtils.trimToEmpty(packet2.getRestInput()))) {
            flag = true;
         }

         if (!StringUtils.trimToEmpty(packet.getRestOutput()).equals(StringUtils.trimToEmpty(packet2.getRestOutput()))) {
            flag = true;
         }

         if (packet.isRestSecurityEnable() != packet2.isRestSecurityEnable()) {
            flag = true;
         }

         if (!StringUtils.trimToEmpty(packet.getRestSecurityUser()).equals(StringUtils.trimToEmpty(packet2.getRestSecurityUser()))) {
            flag = true;
         }

         if (!StringUtils.trimToEmpty(packet.getRestSecurityPassword()).equals(StringUtils.trimToEmpty(packet2.getRestSecurityPassword()))) {
            flag = true;
         }
      }

      if (flag) {
         PacketManager.ins.updateRestConfig(packet);
         PacketCache.ins.refreshPacketConfig(packet.getId());
         SystemLogUtils.addProjectOperationLog(RuleFileType.Knowledge.name(), "update", packet2.getId(), String.format("Update packet %s[%s]", packet2.getName(), packet2.getCode()));
      }

   }

   public String url() {
      return "/packet";
   }
}
