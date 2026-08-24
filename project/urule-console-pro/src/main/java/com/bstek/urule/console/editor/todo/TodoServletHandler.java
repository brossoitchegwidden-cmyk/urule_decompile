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
   PacketPublishListener packetPublishListener = null;

   public void init() {
      super.init();
      if (this.packetPublishListener == null) {
         try {
            this.packetPublishListener = (PacketPublishListener)Utils.getApplicationContext().getBean("urule.packetPublishListener");
         } catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {
         }
      }

   }

   public void load(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      int number = Integer.valueOf(req.getParameter("pageIndex"));
      int number2 = Integer.valueOf(req.getParameter("pageSize"));
      PacketApplyQuery packetApplyQuery = PacketApplyManager.ins.newQuery();
      packetApplyQuery.projectId(ContextHolder.getProjectId());
      packetApplyQuery.titleLike(req.getParameter("title"));
      packetApplyQuery.descLike(req.getParameter("desc"));
      packetApplyQuery.createUserLike(req.getParameter("createUser"));
      String parameter = req.getParameter("currentType");
      if (parameter.contentEquals("pending")) {
         packetApplyQuery.approver(SecurityUtils.getLoginUsername(req));
         packetApplyQuery.status(ApplyStatus.pending);
      } else if (parameter.contentEquals("my")) {
         packetApplyQuery.createUser(SecurityUtils.getLoginUsername(req));
      } else if (parameter.contentEquals("processed")) {
         packetApplyQuery.approver(SecurityUtils.getLoginUsername(req));
         packetApplyQuery.notStatus(ApplyStatus.pending);
      }

      String parameter2 = req.getParameter("approver");
      if (StringUtils.isNotBlank(parameter2)) {
         packetApplyQuery.approver(parameter2);
      }

      String parameter3 = req.getParameter("type");
      if (StringUtils.isNotBlank(parameter3)) {
         packetApplyQuery.type(ApplyType.valueOf(parameter3));
      }

      String parameter4 = req.getParameter("status");
      if (StringUtils.isNotBlank(parameter4)) {
         ApplyStatus applyStatus = ApplyStatus.valueOf(parameter4);
         packetApplyQuery.status(applyStatus);
      }

      Page page = packetApplyQuery.paging(number, number2);
      this.writeObjectToJson(resp, page);
   }

   public void detail(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      ApplyType applyType = ApplyType.valueOf(req.getParameter("type"));
      long longValue = Long.valueOf(req.getParameter("packetId"));
      long longValue2 = -1L;
      if (applyType.equals(ApplyType.deploy)) {
         longValue2 = Long.valueOf(req.getParameter("deployedPacketId"));
      }

      HashMap valuesByKey = new HashMap();
      Packet packet = PacketManager.ins.load(longValue);
      valuesByKey.put("name", packet.getName());
      valuesByKey.put("desc", packet.getDesc());
      if (longValue2 > -1L) {
         PacketDeploy packetDeploy = PacketDeployManager.ins.load(longValue2);
         valuesByKey.put("files", packetDeploy.getFiles());
      } else {
         valuesByKey.put("files", packet.getFiles());
      }

      this.writeObjectToJson(resp, valuesByKey);
   }

   public void submit(final HttpServletRequest req, HttpServletResponse resp) throws Exception {
      final long longValue = Long.valueOf(req.getParameter("applyId"));
      final ApplyStatus applyStatus = ApplyStatus.valueOf(req.getParameter("result"));
      final String parameter = req.getParameter("desc");
      String parameter2 = req.getParameter("groupId");
      final PacketApplyDetail packetApplyDetail = new PacketApplyDetail();
      packetApplyDetail.setApplyId(longValue);
      packetApplyDetail.setProjectId(ContextHolder.getProjectId());
      packetApplyDetail.setDesc(parameter);
      packetApplyDetail.setCreateUser(SecurityUtils.getLoginUsername(req));
      final ApplyType applyType = ApplyType.valueOf(req.getParameter("applyType"));
      final PacketApply packetApply = PacketApplyManager.ins.load(longValue);
      final Packet packet = PacketManager.ins.load(packetApply.getPacketId());
      this.doInTransactional(new TransactionalInvoke() {
         public void doTransactional() {
            PacketApplyDetailManager.ins.add(packetApplyDetail);
            PacketApplyManager.ins.update(longValue, applyStatus);
            String var1x = null;
            if (applyStatus.equals(ApplyStatus.pass)) {
               long longValue = Long.valueOf(req.getParameter("packetId"));
               if (applyType.equals(ApplyType.enable)) {
                  var1x = "Enable packet %s[%s] Approved!";
                  if (TodoServletHandler.this.packetPublishListener != null) {
                     TodoServletHandler.this.packetPublishListener.beforeEnable(packet, parameter);
                  }

                  PacketManager.ins.update(longValue, true);
                  if (TodoServletHandler.this.packetPublishListener != null) {
                     TodoServletHandler.this.packetPublishListener.afterEnable(packet, parameter);
                  }
               } else if (applyType.equals(ApplyType.disable)) {
                  var1x = "Desiable packet %s[%s] Approved!";
                  if (TodoServletHandler.this.packetPublishListener != null) {
                     TodoServletHandler.this.packetPublishListener.beforeDisable(packet, parameter);
                  }

                  PacketManager.ins.update(longValue, false);
                  if (TodoServletHandler.this.packetPublishListener != null) {
                     TodoServletHandler.this.packetPublishListener.afterDisable(packet, parameter);
                  }
               } else if (applyType.equals(ApplyType.deploy)) {
                  var1x = "Release packet %s[%s] Approved!";
                  if (TodoServletHandler.this.packetPublishListener != null) {
                     TodoServletHandler.this.packetPublishListener.beforePublish(packet, parameter);
                  }

                  List items = PacketDeployManager.ins.newQuery().enable(true).packetId(longValue).list();
                  if (items.size() == 0) {
                  PacketDeployManager.ins.updateEnable(packetApply.getDeployedPacketId(), true);
                  }

                  PacketDeployManager.ins.updateStatus(packetApply.getDeployedPacketId(), applyStatus);
                  if (TodoServletHandler.this.packetPublishListener != null) {
                     TodoServletHandler.this.packetPublishListener.afterPublish(packet, parameter);
                  }
               }

               if (StringUtils.isNotBlank(var1x)) {
                  String var5x = String.format(var1x, packet.getName(), packet.getCode());
                  SystemLogUtils.addProjectOperationLog(RuleFileType.Knowledge.name(), applyType.name(), packet.getId(), var5x);
               }
            } else {
               PacketDeployManager.ins.updateStatus(packetApply.getDeployedPacketId(), applyStatus);
            }

         }
      });
      HashMap valuesByKey = new HashMap();
      ArrayList items = new ArrayList();
      boolean flag = false;
      boolean flag2 = false;
      if (applyStatus.equals(ApplyStatus.pass)) {
         long longValue2 = Long.valueOf(req.getParameter("packetId"));
         if (applyType.equals(ApplyType.enable)) {
            List items2 = PacketCache.ins.refreshPacket(longValue2);
            List items3 = PacketCache.ins.enableClientsPacket(parameter2, longValue2);
            if (items2.size() > 0) {
               flag2 = true;
            }

            if (items3.size() > 0) {
               flag = true;
            }

            items.addAll(items2);
            items.addAll(items3);
         } else if (applyType.equals(ApplyType.disable)) {
            List items4 = PacketCache.ins.refreshPacket(longValue2);
            List items5 = PacketCache.ins.disableClientsPacket(parameter2, longValue2);
            if (items4.size() > 0) {
               flag2 = true;
            }

            if (items5.size() > 0) {
               flag = true;
            }

            items.addAll(items4);
            items.addAll(items5);
         } else if (applyType.equals(ApplyType.deploy)) {
            List items6 = PacketDeployManager.ins.newQuery().packetId(longValue2).list();
            if (items6.size() == 1) {
               List items7 = PacketCache.ins.refreshPacket(longValue2);
               if (items7.size() > 0) {
                  flag2 = true;
               }

               items.addAll(items7);
            }
         }
      }

      if (items.size() > 0) {
         valuesByKey.put("result", items);
         String text = "";
         if (flag2) {
            text = "集群服务器";
         }

         if (flag) {
            if (text.length() > 0) {
               text = text + "及";
            }

            text = text + "客户端";
         }

         text = text + "同步结果";
         valuesByKey.put("title", text);
      }

      this.writeObjectToJson(resp, valuesByKey);
   }

   @Transactional
   public void reapply(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("applyId"));
      String parameter = req.getParameter("desc");
      PacketApplyDetail packetApplyDetail = new PacketApplyDetail();
      packetApplyDetail.setApplyId(longValue);
      packetApplyDetail.setProjectId(ContextHolder.getProjectId());
      packetApplyDetail.setDesc(parameter);
      packetApplyDetail.setCreateUser(SecurityUtils.getLoginUsername(req));
      PacketApplyDetailManager.ins.add(packetApplyDetail);
      PacketApplyManager.ins.update(longValue, ApplyStatus.pending);
      ApplyType applyType = ApplyType.valueOf(req.getParameter("type"));
      if (applyType.equals(ApplyType.deploy)) {
         long longValue2 = Long.valueOf(req.getParameter("deployedPacketId"));
         PacketDeploy packetDeploy = PacketDeployManager.ins.load(longValue2);
         long longValue3 = Long.valueOf(req.getParameter("packetId"));
         KnowledgePackage knowledgePackage = PacketBuilder.ins.buildKnowledgePackage(longValue3);
         String text = Utils.knowledgePackageToString(knowledgePackage);
         PacketDeploy packetDeploy2 = new PacketDeploy();
         packetDeploy2.setContent(text);
         packetDeploy2.setDigest(MD5Utils.stringToMD5(text));
         packetDeploy2.setStatus(ApplyStatus.pending);
         packetDeploy2.setCreateUser(SecurityUtils.getLoginUsername(req));
         packetDeploy2.setApplyId(longValue);
         packetDeploy2.setVersion(packetDeploy.getVersion());
         packetDeploy2.setDesc(packetDeploy.getDesc());
         packetDeploy2.setPacketId(longValue3);
         packetDeploy2.setProjectId(ContextHolder.getProjectId());
         PacketDeployManager.ins.add(packetDeploy2);
         Packet packet = PacketManager.ins.load(longValue3);

         for(PacketFile packetFile : (Iterable<PacketFile>)(Iterable<?>)(packet.getFiles())) {
            RuleFile ruleFile = FileManager.ins.get(packetFile.getFileId());
            PacketDeployFile packetDeployFile = new PacketDeployFile();
            packetDeployFile.setFileId(packetFile.getFileId());
            packetDeployFile.setProjectId(ContextHolder.getProjectId());
            packetDeployFile.setDigest(ruleFile.getDigest());
            packetDeployFile.setPath(packetFile.getPath());
            packetDeployFile.setVersion(packetFile.getVersion());
            packetDeployFile.setPacketDeployId(packetDeploy2.getId());
            packetDeployFile.setContent(FileManager.ins.loadContent(packetFile.getFileId()));
            packetDeployFile.setCreateUser(SecurityUtils.getLoginUsername(req));
            PacketDeployFileManager.ins.add(packetDeployFile);
         }

         PacketDeployManager.ins.delete(longValue2);
         PacketApplyManager.ins.updateDeployedPacketId(longValue, packetDeploy2.getId());
      }

   }

   public String url() {
      return "/todo";
   }
}
