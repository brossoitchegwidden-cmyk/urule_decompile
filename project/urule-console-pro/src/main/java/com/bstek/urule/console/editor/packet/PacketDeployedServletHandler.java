package com.bstek.urule.console.editor.packet;

import com.bstek.urule.Utils;
import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.TransactionalInvoke;
import com.bstek.urule.console.admin.log.SystemLogUtils;
import com.bstek.urule.console.cache.packet.PacketCache;
import com.bstek.urule.console.cache.packet.PacketCacheImpl;
import com.bstek.urule.console.cache.packet.PacketConfig;
import com.bstek.urule.console.cache.packet.PacketData;
import com.bstek.urule.console.database.manager.packet.PacketManager;
import com.bstek.urule.console.database.manager.packet.deploy.PacketDeployManager;
import com.bstek.urule.console.database.manager.packet.deploy.PacketDeployQuery;
import com.bstek.urule.console.database.manager.packet.packge.PacketPackageManager;
import com.bstek.urule.console.database.model.ApplyStatus;
import com.bstek.urule.console.database.model.ApplyType;
import com.bstek.urule.console.database.model.Packet;
import com.bstek.urule.console.database.model.PacketDeploy;
import com.bstek.urule.console.database.model.PacketPackage;
import com.bstek.urule.console.database.model.PacketType;
import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.model.UrlType;
import com.bstek.urule.console.database.service.url.UrlService;
import com.bstek.urule.console.editor.todo.PacketPublishListener;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.security.URuleAuthorization;
import com.bstek.urule.console.type.RuleFileType;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.runtime.KnowledgePackageImpl;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public class PacketDeployedServletHandler extends ApiServletHandler {
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
      PacketDeployQuery packetDeployQuery = PacketDeployManager.ins.newQuery();
      packetDeployQuery.versionLike(req.getParameter("version"));
      packetDeployQuery.descLike(req.getParameter("desc"));
      String parameter = req.getParameter("status");
      if (StringUtils.isNotBlank(parameter)) {
         packetDeployQuery.status(ApplyStatus.valueOf(parameter));
      }

      Page page = packetDeployQuery.packetId(Long.valueOf(req.getParameter("packetId"))).paging(number, number2);
      this.writeObjectToJson(resp, page);
   }

   public void loadCompareContent(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("leftId"));
      PacketDeploy packetDeploy = PacketDeployManager.ins.load(longValue);
      HashMap valuesByKey = new HashMap();
      valuesByKey.put("left", packetDeploy.getContent());
      long longValue2 = Long.valueOf(req.getParameter("rightId"));
      PacketDeploy packetDeploy2 = PacketDeployManager.ins.load(longValue2);
      valuesByKey.put("right", packetDeploy2.getContent());
      this.writeObjectToJson(resp, valuesByKey);
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void enable(final HttpServletRequest req, HttpServletResponse resp) throws Exception {
      final long longValue = Long.valueOf(req.getParameter("id"));
      final PacketDeploy packetDeploy = PacketDeployManager.ins.load(longValue);
      final Packet packet = PacketManager.ins.load(packetDeploy.getPacketId());
      this.doInTransactional(new TransactionalInvoke() {
         public void doTransactional() {
            if (PacketDeployedServletHandler.this.packetPublishListener != null) {
               PacketDeployedServletHandler.this.packetPublishListener.beforeActive(packet, packetDeploy.getVersion());
            }

            PacketDeployManager.ins.disableAll(packetDeploy.getPacketId());
            PacketDeployManager.ins.updateEnable(longValue, true);
            packet.setUpdateDate(new Date());
            packet.setUpdateUser(SecurityUtils.getLoginUsername(req));
            PacketManager.ins.update(packet);
            if (PacketDeployedServletHandler.this.packetPublishListener != null) {
               PacketDeployedServletHandler.this.packetPublishListener.afterActive(packet, packetDeploy.getVersion());
            }

            String var1x = "Switch the version of the packet %s[%s] to %s";
            SystemLogUtils.addProjectOperationLog(RuleFileType.Knowledge.name(), ApplyType.enable.name(), packet.getId(), String.format(var1x, packet.getName(), packet.getCode(), packetDeploy.getVersion()));
         }
      });
      List items = PacketCache.ins.refreshPacket(packetDeploy.getPacketId());
      this.writeObjectToJson(resp, items);
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void export(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("id"));
      PacketDeploy packetDeploy = PacketDeployManager.ins.load(longValue);
      String text = packetDeploy.getPacketId() + ".data";
      resp.setHeader("Content-Disposition", "attachment; filename=" + new String(text.getBytes("UTF-8"), "ISO8859-1"));
      byte[] bytes = Utils.compress(packetDeploy.getContent());
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
      ServletOutputStream outputStream = resp.getOutputStream();
      IOUtils.copy(byteArrayInputStream, outputStream);
      IOUtils.closeQuietly(byteArrayInputStream);
      IOUtils.closeQuietly(outputStream);
   }

   public void clients(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("groupId");
      List list = UrlService.ins.load(UrlType.client, parameter).getList();
      this.writeObjectToJson(resp, list);
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void push(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("packetId"));
      String parameter = req.getParameter("id");
      String content = null;
      String version = null;
      if (StringUtils.isNotBlank(parameter)) {
         long longValue2 = Long.valueOf(parameter);
         PacketDeploy packetDeploy = PacketDeployManager.ins.load(longValue2);
         content = packetDeploy.getContent();
         version = packetDeploy.getVersion();
      } else {
         content = this.loadUploadedKnowledgePackage(req);
      }

      KnowledgePackageWrapper knowledgePackageWrapper = Utils.stringToKnowledgePackageWrapper(content);
      KnowledgePackageImpl knowledgePackage = (KnowledgePackageImpl)knowledgePackageWrapper.getKnowledgePackage();
      knowledgePackage.initForActiveVersion();
      Packet packet = PacketManager.ins.load(longValue);
      PacketData packetData = new PacketData(packet, knowledgePackageWrapper);
      PacketConfig packet2 = packetData.getPacket();
      knowledgePackage.setMonitor(packet.isAuditEnable());
      knowledgePackage.setInputData(packet2.getAuditInput());
      knowledgePackage.setOutputData(packet2.getAuditOutput());
      knowledgePackage.setPackageInfo(String.valueOf(longValue));
      knowledgePackage.setVersion(version);
      String parameter2 = req.getParameter("groupId");
      List items = ((PacketCacheImpl)PacketCache.ins).getClientPacketCacheAdapter().pushPacketToClients(parameter2, packetData);
      this.writeObjectToJson(resp, items);
   }

   private String loadUploadedKnowledgePackage(HttpServletRequest httpServletRequest) {
      String parameter = httpServletRequest.getParameter("packetId");
      if (StringUtils.isNotBlank(parameter)) {
         Packet packet = PacketManager.ins.load(Long.valueOf(parameter));
         if (packet.getType().equals(PacketType.upload)) {
            PacketPackage packetPackage = packet.getPacketPackage();
            if (packetPackage != null && packetPackage.getId() != 0L) {
               String content = PacketPackageManager.ins.loadContent(packetPackage.getId());
               if (StringUtils.isBlank(content)) {
                  throw new InfoException("请先上传知识包");
               }

               return content;
            }

            throw new InfoException("请先上传知识包");
         }
      }

      return null;
   }

   public String url() {
      return "/deploy";
   }
}
