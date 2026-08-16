package com.bstek.urule.console.editor.todo;

import com.bstek.urule.Utils;
import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.ContextHolder;
import com.bstek.urule.console.Transactional;
import com.bstek.urule.console.TransactionalInvoke;
import com.bstek.urule.console.admin.log.SystemLogUtils;
import com.bstek.urule.console.cache.packet.PacketCache;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.manager.packet.PacketBuilder;
import com.bstek.urule.console.database.manager.packet.PacketManager;
import com.bstek.urule.console.database.manager.packet.apply.PacketApplyManager;
import com.bstek.urule.console.database.manager.packet.apply.PacketApplyQuery;
import com.bstek.urule.console.database.manager.packet.apply.detail.PacketApplyDetailManager;
import com.bstek.urule.console.database.manager.packet.deploy.PacketDeployManager;
import com.bstek.urule.console.database.manager.packet.deploy.file.PacketDeployFileManager;
import com.bstek.urule.console.database.model.ApplyStatus;
import com.bstek.urule.console.database.model.ApplyType;
import com.bstek.urule.console.database.model.Packet;
import com.bstek.urule.console.database.model.PacketApply;
import com.bstek.urule.console.database.model.PacketApplyDetail;
import com.bstek.urule.console.database.model.PacketDeploy;
import com.bstek.urule.console.database.model.PacketDeployFile;
import com.bstek.urule.console.database.model.PacketFile;
import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.type.RuleFileType;
import com.bstek.urule.console.util.MD5Utils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.runtime.KnowledgePackage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public class TodoServletHandler extends ApiServletHandler {
   PacketPublishListener e = null;

   public void init() {
      super.init();
      if (this.e == null) {
         try {
            this.e = (PacketPublishListener)Utils.getApplicationContext().getBean("urule.packetPublishListener");
         } catch (NoSuchBeanDefinitionException var2) {
         }
      }

   }

   public void load(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      int var3 = Integer.valueOf(var1.getParameter("pageIndex"));
      int var4 = Integer.valueOf(var1.getParameter("pageSize"));
      PacketApplyQuery var5 = PacketApplyManager.ins.newQuery();
      var5.projectId(ContextHolder.getProjectId());
      var5.titleLike(var1.getParameter("title"));
      var5.descLike(var1.getParameter("desc"));
      var5.createUserLike(var1.getParameter("createUser"));
      String var6 = var1.getParameter("currentType");
      if (var6.contentEquals("pending")) {
         var5.approver(SecurityUtils.getLoginUsername(var1));
         var5.status(ApplyStatus.pending);
      } else if (var6.contentEquals("my")) {
         var5.createUser(SecurityUtils.getLoginUsername(var1));
      } else if (var6.contentEquals("processed")) {
         var5.approver(SecurityUtils.getLoginUsername(var1));
         var5.notStatus(ApplyStatus.pending);
      }

      String var7 = var1.getParameter("approver");
      if (StringUtils.isNotBlank(var7)) {
         var5.approver(var7);
      }

      String var8 = var1.getParameter("type");
      if (StringUtils.isNotBlank(var8)) {
         var5.type(ApplyType.valueOf(var8));
      }

      String var9 = var1.getParameter("status");
      if (StringUtils.isNotBlank(var9)) {
         ApplyStatus var10 = ApplyStatus.valueOf(var9);
         var5.status(var10);
      }

      Page var11 = var5.paging(var3, var4);
      this.a(var2, var11);
   }

   public void detail(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      ApplyType var3 = ApplyType.valueOf(var1.getParameter("type"));
      long var4 = Long.valueOf(var1.getParameter("packetId"));
      long var6 = -1L;
      if (var3.equals(ApplyType.deploy)) {
         var6 = Long.valueOf(var1.getParameter("deployedPacketId"));
      }

      HashMap var8 = new HashMap();
      Packet var9 = PacketManager.ins.load(var4);
      var8.put("name", var9.getName());
      var8.put("desc", var9.getDesc());
      if (var6 > -1L) {
         PacketDeploy var10 = PacketDeployManager.ins.load(var6);
         var8.put("files", var10.getFiles());
      } else {
         var8.put("files", var9.getFiles());
      }

      this.a(var2, var8);
   }

   public void submit(final HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      final long var3 = Long.valueOf(var1.getParameter("applyId"));
      final ApplyStatus var5 = ApplyStatus.valueOf(var1.getParameter("result"));
      final String var6 = var1.getParameter("desc");
      String var7 = var1.getParameter("groupId");
      final PacketApplyDetail var8 = new PacketApplyDetail();
      var8.setApplyId(var3);
      var8.setProjectId(ContextHolder.getProjectId());
      var8.setDesc(var6);
      var8.setCreateUser(SecurityUtils.getLoginUsername(var1));
      final ApplyType var9 = ApplyType.valueOf(var1.getParameter("applyType"));
      final PacketApply var10 = PacketApplyManager.ins.load(var3);
      final Packet var11 = PacketManager.ins.load(var10.getPacketId());
      this.a(new TransactionalInvoke() {
         public void doTransactional() {
            PacketApplyDetailManager.ins.add(var8);
            PacketApplyManager.ins.update(var3, var5);
            String var1x = null;
            if (var5.equals(ApplyStatus.pass)) {
               long var2 = Long.valueOf(var1.getParameter("packetId"));
               if (var9.equals(ApplyType.enable)) {
                  var1x = "Enable packet %s[%s] Approved!";
                  if (TodoServletHandler.this.e != null) {
                     TodoServletHandler.this.e.beforeEnable(var11, var6);
                  }

                  PacketManager.ins.update(var2, true);
                  if (TodoServletHandler.this.e != null) {
                     TodoServletHandler.this.e.afterEnable(var11, var6);
                  }
               } else if (var9.equals(ApplyType.disable)) {
                  var1x = "Desiable packet %s[%s] Approved!";
                  if (TodoServletHandler.this.e != null) {
                     TodoServletHandler.this.e.beforeDisable(var11, var6);
                  }

                  PacketManager.ins.update(var2, false);
                  if (TodoServletHandler.this.e != null) {
                     TodoServletHandler.this.e.afterDisable(var11, var6);
                  }
               } else if (var9.equals(ApplyType.deploy)) {
                  var1x = "Release packet %s[%s] Approved!";
                  if (TodoServletHandler.this.e != null) {
                     TodoServletHandler.this.e.beforePublish(var11, var6);
                  }

                  List var4 = PacketDeployManager.ins.newQuery().enable(true).packetId(var2).list();
                  if (var4.size() == 0) {
                     PacketDeployManager.ins.updateEnable(var10.getDeployedPacketId(), true);
                  }

                  PacketDeployManager.ins.updateStatus(var10.getDeployedPacketId(), var5);
                  if (TodoServletHandler.this.e != null) {
                     TodoServletHandler.this.e.afterPublish(var11, var6);
                  }
               }

               if (StringUtils.isNotBlank(var1x)) {
                  String var5x = String.format(var1x, var11.getName(), var11.getCode());
                  SystemLogUtils.addProjectOperationLog(RuleFileType.Knowledge.name(), var9.name(), var11.getId(), var5x);
               }
            } else {
               PacketDeployManager.ins.updateStatus(var10.getDeployedPacketId(), var5);
            }

         }
      });
      HashMap var12 = new HashMap();
      ArrayList var13 = new ArrayList();
      boolean var14 = false;
      boolean var15 = false;
      if (var5.equals(ApplyStatus.pass)) {
         long var16 = Long.valueOf(var1.getParameter("packetId"));
         if (var9.equals(ApplyType.enable)) {
            List var18 = PacketCache.ins.refreshPacket(var16);
            List var19 = PacketCache.ins.enableClientsPacket(var7, var16);
            if (var18.size() > 0) {
               var15 = true;
            }

            if (var19.size() > 0) {
               var14 = true;
            }

            var13.addAll(var18);
            var13.addAll(var19);
         } else if (var9.equals(ApplyType.disable)) {
            List var22 = PacketCache.ins.refreshPacket(var16);
            List var24 = PacketCache.ins.disableClientsPacket(var7, var16);
            if (var22.size() > 0) {
               var15 = true;
            }

            if (var24.size() > 0) {
               var14 = true;
            }

            var13.addAll(var22);
            var13.addAll(var24);
         } else if (var9.equals(ApplyType.deploy)) {
            List var23 = PacketDeployManager.ins.newQuery().packetId(var16).list();
            if (var23.size() == 1) {
               List var25 = PacketCache.ins.refreshPacket(var16);
               if (var25.size() > 0) {
                  var15 = true;
               }

               var13.addAll(var25);
            }
         }
      }

      if (var13.size() > 0) {
         var12.put("result", var13);
         String var20 = "";
         if (var15) {
            var20 = "集群服务器";
         }

         if (var14) {
            if (var20.length() > 0) {
               var20 = var20 + "及";
            }

            var20 = var20 + "客户端";
         }

         var20 = var20 + "同步结果";
         var12.put("title", var20);
      }

      this.a(var2, var12);
   }

   @Transactional
   public void reapply(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("applyId"));
      String var5 = var1.getParameter("desc");
      PacketApplyDetail var6 = new PacketApplyDetail();
      var6.setApplyId(var3);
      var6.setProjectId(ContextHolder.getProjectId());
      var6.setDesc(var5);
      var6.setCreateUser(SecurityUtils.getLoginUsername(var1));
      PacketApplyDetailManager.ins.add(var6);
      PacketApplyManager.ins.update(var3, ApplyStatus.pending);
      ApplyType var7 = ApplyType.valueOf(var1.getParameter("type"));
      if (var7.equals(ApplyType.deploy)) {
         long var8 = Long.valueOf(var1.getParameter("deployedPacketId"));
         PacketDeploy var10 = PacketDeployManager.ins.load(var8);
         long var11 = Long.valueOf(var1.getParameter("packetId"));
         KnowledgePackage var13 = PacketBuilder.ins.buildKnowledgePackage(var11);
         String var14 = Utils.knowledgePackageToString(var13);
         PacketDeploy var15 = new PacketDeploy();
         var15.setContent(var14);
         var15.setDigest(MD5Utils.stringToMD5(var14));
         var15.setStatus(ApplyStatus.pending);
         var15.setCreateUser(SecurityUtils.getLoginUsername(var1));
         var15.setApplyId(var3);
         var15.setVersion(var10.getVersion());
         var15.setDesc(var10.getDesc());
         var15.setPacketId(var11);
         var15.setProjectId(ContextHolder.getProjectId());
         PacketDeployManager.ins.add(var15);
         Packet var16 = PacketManager.ins.load(var11);

         for(PacketFile var18 : (Iterable<PacketFile>)(Iterable<?>)(var16.getFiles())) {
            RuleFile var19 = FileManager.ins.get(var18.getFileId());
            PacketDeployFile var20 = new PacketDeployFile();
            var20.setFileId(var18.getFileId());
            var20.setProjectId(ContextHolder.getProjectId());
            var20.setDigest(var19.getDigest());
            var20.setPath(var18.getPath());
            var20.setVersion(var18.getVersion());
            var20.setPacketDeployId(var15.getId());
            var20.setContent(FileManager.ins.loadContent(var18.getFileId()));
            var20.setCreateUser(SecurityUtils.getLoginUsername(var1));
            PacketDeployFileManager.ins.add(var20);
         }

         PacketDeployManager.ins.delete(var8);
         PacketApplyManager.ins.updateDeployedPacketId(var3, var15.getId());
      }

   }

   public String url() {
      return "/todo";
   }
}
