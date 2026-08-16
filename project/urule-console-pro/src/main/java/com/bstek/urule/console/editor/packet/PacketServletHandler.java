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
   public void load(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      int var3 = Integer.valueOf(var1.getParameter("pageIndex"));
      int var4 = Integer.valueOf(var1.getParameter("pageSize"));
      PacketQuery var5 = PacketManager.ins.newQuery();
      var5.nameLike(var1.getParameter("name"));
      var5.descLike(var1.getParameter("desc"));
      var5.createUserLike(var1.getParameter("createUser"));
      var5.projectId(Long.valueOf(var1.getParameter("projectId")));
      var5.typeLike(var1.getParameter("type"));
      String var6 = var1.getParameter("enable");
      if (StringUtils.isNotBlank(var6)) {
         var5.enable(Boolean.valueOf(var6));
      }

      String var7 = var1.getParameter("id");
      if (StringUtils.isNotBlank(var7)) {
         var5.idLike(var7);
      }

      String var8 = var1.getParameter("code");
      if (StringUtils.isNotBlank(var8)) {
         var5.codeLike(var8);
      }

      String var9 = var1.getParameter("restEnable");
      if (StringUtils.isNotBlank(var9)) {
         var5.restEnable(Boolean.valueOf(var9));
      }

      String var10 = var1.getParameter("auditEnable");
      if (StringUtils.isNotBlank(var10)) {
         var5.auditEnable(Boolean.valueOf(var10));
      }

      Page var11 = var5.paging(var3, var4);
      this.a(var2, var11);
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void add(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      Packet var3 = new Packet();
      String var4 = var1.getParameter("code");
      if (StringUtils.isNotBlank(var4)) {
         var4 = var4.trim();
         if (StringUtils.hasChineseChar(var4)) {
            throw new InfoException("编码不能包含中文字符<br>The code cannot contain Chinese characters.");
         }

         if (StringUtils.hasSpecialChar(var4)) {
            throw new InfoException("编码不能包含特殊字符.<br/>The code cannot contain special characters.");
         }

         var3.setCode(var4);
         List var5 = PacketManager.ins.newQuery().code(var4).list();
         if (var5.size() > 0) {
            Project var6 = ProjectManager.ins.get(((Packet)var5.get(0)).getProjectId());
            String var7 = "Duplicate packet code " + var4 + "!<br/>The code under " + var6.getName() + "(" + var3.getProjectId() + ") of " + var6.getGroupId() + " repeated";
            throw new InfoException(var7);
         }
      }

      var3.setCreateUser(SecurityUtils.getLoginUsername(var1));
      var3.setProjectId(ContextHolder.getProjectId());
      var3.setName(var1.getParameter("name"));
      var3.setDesc(var1.getParameter("desc"));
      PacketType var9 = PacketType.valueOf(var1.getParameter("type"));
      var3.setType(var9);
      var3.setInputData(var1.getParameter("inputData"));
      var3.setOutputData(var1.getParameter("outputData"));
      var3.setEnable(Boolean.valueOf(var1.getParameter("enable")));
      var3.setProjectId(Long.valueOf(var1.getParameter("projectId")));
      PacketManager.ins.add(var3);
      SystemLogUtils.addProjectOperationLog(RuleFileType.Knowledge.name(), "add", var3.getId(), String.format("Add packet %s[%s]", var3.getName(), var3.getCode()));
      this.a(var2, var3);
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void update(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      Packet var3 = new Packet();
      Long var4 = Long.valueOf(var1.getParameter("id"));
      String var5 = var1.getParameter("code");
      Packet var6 = PacketManager.ins.load(var4);
      var3.setId(var4);
      var3.setUpdateUser(SecurityUtils.getLoginUsername(var1));
      var3.setName(var1.getParameter("name"));
      var3.setCode(var5);
      var3.setDesc(var1.getParameter("desc"));
      var3.setInputData(var1.getParameter("inputData"));
      var3.setOutputData(var1.getParameter("outputData"));
      var3.setEnable(Boolean.valueOf(var1.getParameter("enable")));
      var3.setUpdateDate(new Date());
      if (var6 != null) {
         PacketManager.ins.update(var3);
         if (StringUtils.isNotBlank(var6.getCode()) && !var6.getCode().equals(var5)) {
            String var7 = var6.getCode();
            ((PacketCacheImpl)PacketCache.ins).removePacket(var7);
            PacketCache.ins.refreshPacket(var4);
         }
      }

      var3 = PacketManager.ins.load(var3.getId());
      SystemLogUtils.addProjectOperationLog(RuleFileType.Knowledge.name(), "update", var3.getId(), String.format("Update packet %s[%s]", var3.getName(), var3.getCode()));
      this.a(var2, var3);
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void updateAuditConfig(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      Packet var3 = new Packet();
      var3.setId(Long.valueOf(var1.getParameter("id")));
      var3.setUpdateUser(SecurityUtils.getLoginUsername(var1));
      var3.setAuditEnable(Boolean.valueOf(var1.getParameter("auditEnable")));
      if (var3.isAuditEnable()) {
         var3.setAuditInput(var1.getParameter("auditInput"));
         var3.setAuditOutput(var1.getParameter("auditOutput"));
      }

      var3.setUpdateDate(new Date());
      PacketManager.ins.updateAuditConfig(var3);
      PacketCache.ins.refreshPacketConfig(var3.getId());
      this.a(var2, var3);
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void delete(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("id"));
      boolean var5 = Boolean.valueOf(var1.getParameter("force"));
      Packet var6 = PacketManager.ins.load(var3);
      if (var6 != null) {
         if (!var5) {
            try {
               List var7 = ReferenceService.ins.packet(var6.getProjectId(), var3, var6.getCode());
               if (var7.size() > 0) {
                  throw new ReferenceDeleteException(var7.size());
               }
            } catch (DeserializeException var8) {
               throw new ReferenceDeleteException(var8.getMessage());
            }
         }

         PacketManager.ins.delete(var3);
         ((PacketCacheImpl)PacketCache.ins).removePacket(var3);
         ((PacketCacheImpl)PacketCache.ins).removePacket(var6.getCode());
         SystemLogUtils.addProjectOperationLog(RuleFileType.Knowledge.name(), "delete", var6.getId(), String.format("Remove packet %s[%s]", var6.getName(), var6.getCode()));
      }

   }

   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void addFile(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      PacketFile var3 = new PacketFile();
      var3.setCreateUser(SecurityUtils.getLoginUsername(var1));
      var3.setFileId(Long.valueOf(var1.getParameter("fileId")));
      var3.setPacketId(Long.valueOf(var1.getParameter("packetId")));
      var3.setDesc(var1.getParameter("desc"));
      var3.setPath(var1.getParameter("path"));
      var3.setVersion(var1.getParameter("version"));
      RuleFile var4 = FileManager.ins.get(var3.getFileId());
      Packet var5 = PacketManager.ins.load(var3.getPacketId());
      if (var4 != null && var5 != null) {
         var3.setProjectId(var5.getProjectId());
         PacketFileManager.ins.add(var3);
         var5.setUpdateDate(new Date());
         var5.setUpdateUser(SecurityUtils.getLoginUsername(var1));
         PacketManager.ins.update(var5);
         String var6 = String.format("Add rule file %s[%s] to packet %s[%s]", var4.getName(), var4.getId(), var5.getName(), var5.getCode());
         SystemLogUtils.addProjectOperationLog(RuleFileType.Knowledge.name(), "update", var5.getId(), var6);
         this.a(var2, var3);
      }

   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void uploadPackage(final HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      final PacketPackage var3 = new PacketPackage();
      var3.setCreateUser(SecurityUtils.getLoginUsername(var1));
      var3.setUpdateUser(SecurityUtils.getLoginUsername(var1));
      var3.setPacketId(Long.valueOf(var1.getParameter("packetId")));
      var3.setUpdateDate(new Date());
      final Packet var4 = PacketManager.ins.load(var3.getPacketId());
      var3.setProjectId(var4.getProjectId());
      DiskFileItemFactory var5 = new DiskFileItemFactory();
      ServletFileUpload var6 = new ServletFileUpload(var5);
      var6.setHeaderEncoding("UTF-8");

      for(FileItem var9 : var6.parseRequest(var1)) {
         String var10 = var9.getFieldName();
         if (var10.equals("file")) {
            InputStream var11 = var9.getInputStream();
            String var12 = Utils.uncompress(IOUtils.toByteArray(var11));
            var3.setContent(var12);
         } else if (var10.contentEquals("desc")) {
            String var15 = var9.getString("utf-8");
            var3.setDesc(var15);
         }
      }

      if (StringUtils.isBlank(var3.getContent())) {
         throw new InfoException("请上传导出的知识包文件");
      } else {
         final String var13 = var1.getParameter("id");
         this.a(new TransactionalInvoke() {
            public void doTransactional() {
               if (StringUtils.isBlank(var13)) {
                  PacketPackageManager.ins.add(var3);
               } else {
                  var3.setId(Long.valueOf(var13));
                  PacketPackageManager.ins.update(var3);
               }

               var4.setUpdateDate(new Date());
               var4.setUpdateUser(SecurityUtils.getLoginUsername(var1));
               PacketManager.ins.update(var4);
            }
         });
         String var14 = String.format("Pocket File Additional Packet %s[%s]", var4.getName(), var4.getCode());
         SystemLogUtils.addProjectOperationLog(RuleFileType.Knowledge.name(), "update", var4.getId(), var14);
         var3.setContent((String)null);
         HashMap var16 = new HashMap();
         if (var4.isEnable()) {
            PacketCache.ins.cacheUploadPacketPackage(var4.getId());
            List var17 = PacketCache.ins.refreshPacket(var4.getId());
            var16.put("sendResult", var17);
         } else {
            var16.put("pk", var3);
         }

         this.a(var2, var16);
      }
   }

   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void updateFile(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("id"));
      PacketFile var5 = PacketFileManager.ins.load(var3);
      var5.setUpdateUser(SecurityUtils.getLoginUsername(var1));
      var5.setDesc(var1.getParameter("desc"));
      var5.setFileId(Long.valueOf(var1.getParameter("fileId")));
      var5.setPath(var1.getParameter("path"));
      var5.setVersion(var1.getParameter("version"));
      var5.setUpdateDate(new Date());
      PacketFileManager.ins.update(var5);
      Packet var6 = PacketManager.ins.load(var5.getPacketId());
      var6.setUpdateDate(new Date());
      var6.setUpdateUser(SecurityUtils.getLoginUsername(var1));
      PacketManager.ins.update(var6);
      this.a(var2, var5);
   }

   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void deleteFile(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("id"));
      PacketFile var5 = PacketFileManager.ins.load(var3);
      if (var5 != null) {
         PacketFileManager.ins.delete(var3);
         Packet var6 = PacketManager.ins.load(var5.getPacketId());
         var6.setUpdateDate(new Date());
         var6.setUpdateUser(SecurityUtils.getLoginUsername(var1));
         PacketManager.ins.update(var6);
      }

   }

   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void updateRestConfig(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      Packet var3 = new Packet();
      var3.setId(Long.valueOf(var1.getParameter("id")));
      var3.setRestEnable(Boolean.valueOf(var1.getParameter("restEnable")));
      if (var3.isRestEnable()) {
         var3.setRestInput(var1.getParameter("restInput"));
         var3.setRestOutput(var1.getParameter("restOutput"));
         var3.setRestSecurityEnable(Boolean.valueOf(var1.getParameter("restSecurityEnable")));
         var3.setRestSecurityUser(var1.getParameter("restSecurityUser"));
         var3.setRestSecurityPassword(var1.getParameter("restSecurityPassword"));
      }

      var3.setUpdateDate(new Date());
      var3.setUpdateUser(SecurityUtils.getLoginUsername(var1));
      Packet var4 = PacketManager.ins.load(var3.getId());
      boolean var5 = false;
      if (var4.isRestEnable() != var3.isRestEnable()) {
         var5 = true;
      }

      if (var3.isRestEnable()) {
         if (!StringUtils.trimToEmpty(var3.getRestInput()).equals(StringUtils.trimToEmpty(var4.getRestInput()))) {
            var5 = true;
         }

         if (!StringUtils.trimToEmpty(var3.getRestOutput()).equals(StringUtils.trimToEmpty(var4.getRestOutput()))) {
            var5 = true;
         }

         if (var3.isRestSecurityEnable() != var4.isRestSecurityEnable()) {
            var5 = true;
         }

         if (!StringUtils.trimToEmpty(var3.getRestSecurityUser()).equals(StringUtils.trimToEmpty(var4.getRestSecurityUser()))) {
            var5 = true;
         }

         if (!StringUtils.trimToEmpty(var3.getRestSecurityPassword()).equals(StringUtils.trimToEmpty(var4.getRestSecurityPassword()))) {
            var5 = true;
         }
      }

      if (var5) {
         PacketManager.ins.updateRestConfig(var3);
         PacketCache.ins.refreshPacketConfig(var3.getId());
         SystemLogUtils.addProjectOperationLog(RuleFileType.Knowledge.name(), "update", var4.getId(), String.format("Update packet %s[%s]", var4.getName(), var4.getCode()));
      }

   }

   public String url() {
      return "/packet";
   }
}
