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
      PacketDeployQuery var5 = PacketDeployManager.ins.newQuery();
      var5.versionLike(var1.getParameter("version"));
      var5.descLike(var1.getParameter("desc"));
      String var6 = var1.getParameter("status");
      if (StringUtils.isNotBlank(var6)) {
         var5.status(ApplyStatus.valueOf(var6));
      }

      Page var7 = var5.packetId(Long.valueOf(var1.getParameter("packetId"))).paging(var3, var4);
      this.a(var2, var7);
   }

   public void loadCompareContent(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("leftId"));
      PacketDeploy var5 = PacketDeployManager.ins.load(var3);
      HashMap var6 = new HashMap();
      var6.put("left", var5.getContent());
      long var7 = Long.valueOf(var1.getParameter("rightId"));
      PacketDeploy var9 = PacketDeployManager.ins.load(var7);
      var6.put("right", var9.getContent());
      this.a(var2, var6);
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void enable(final HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      final long var3 = Long.valueOf(var1.getParameter("id"));
      final PacketDeploy var5 = PacketDeployManager.ins.load(var3);
      final Packet var6 = PacketManager.ins.load(var5.getPacketId());
      this.a(new TransactionalInvoke() {
         public void doTransactional() {
            if (PacketDeployedServletHandler.this.e != null) {
               PacketDeployedServletHandler.this.e.beforeActive(var6, var5.getVersion());
            }

            PacketDeployManager.ins.disableAll(var5.getPacketId());
            PacketDeployManager.ins.updateEnable(var3, true);
            var6.setUpdateDate(new Date());
            var6.setUpdateUser(SecurityUtils.getLoginUsername(var1));
            PacketManager.ins.update(var6);
            if (PacketDeployedServletHandler.this.e != null) {
               PacketDeployedServletHandler.this.e.afterActive(var6, var5.getVersion());
            }

            String var1x = "Switch the version of the packet %s[%s] to %s";
            SystemLogUtils.addProjectOperationLog(RuleFileType.Knowledge.name(), ApplyType.enable.name(), var6.getId(), String.format(var1x, var6.getName(), var6.getCode(), var5.getVersion()));
         }
      });
      List var7 = PacketCache.ins.refreshPacket(var5.getPacketId());
      this.a(var2, var7);
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void export(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("id"));
      PacketDeploy var5 = PacketDeployManager.ins.load(var3);
      String var6 = var5.getPacketId() + ".data";
      var2.setHeader("Content-Disposition", "attachment; filename=" + new String(var6.getBytes("UTF-8"), "ISO8859-1"));
      byte[] var7 = Utils.compress(var5.getContent());
      ByteArrayInputStream var8 = new ByteArrayInputStream(var7);
      ServletOutputStream var9 = var2.getOutputStream();
      IOUtils.copy(var8, var9);
      IOUtils.closeQuietly(var8);
      IOUtils.closeQuietly(var9);
   }

   public void clients(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("groupId");
      List var4 = UrlService.ins.load(UrlType.client, var3).getList();
      this.a(var2, var4);
   }

   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void push(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("packetId"));
      String var5 = var1.getParameter("id");
      String var6 = null;
      String var7 = null;
      if (StringUtils.isNotBlank(var5)) {
         long var8 = Long.valueOf(var5);
         PacketDeploy var10 = PacketDeployManager.ins.load(var8);
         var6 = var10.getContent();
         var7 = var10.getVersion();
      } else {
         var6 = this.c(var1);
      }

      KnowledgePackageWrapper var16 = Utils.stringToKnowledgePackageWrapper(var6);
      KnowledgePackageImpl var9 = (KnowledgePackageImpl)var16.getKnowledgePackage();
      var9.initForActiveVersion();
      Packet var17 = PacketManager.ins.load(var3);
      PacketData var11 = new PacketData(var17, var16);
      PacketConfig var12 = var11.getPacket();
      var9.setMonitor(var17.isAuditEnable());
      var9.setInputData(var12.getAuditInput());
      var9.setOutputData(var12.getAuditOutput());
      var9.setPackageInfo(String.valueOf(var3));
      var9.setVersion(var7);
      String var13 = var1.getParameter("groupId");
      List var14 = ((PacketCacheImpl)PacketCache.ins).getClientPacketCacheAdapter().pushPacketToClients(var13, var11);
      this.a(var2, var14);
   }

   private String c(HttpServletRequest var1) {
      String var2 = var1.getParameter("packetId");
      if (StringUtils.isNotBlank(var2)) {
         Packet var3 = PacketManager.ins.load(Long.valueOf(var2));
         if (var3.getType().equals(PacketType.upload)) {
            PacketPackage var4 = var3.getPacketPackage();
            if (var4 != null && var4.getId() != 0L) {
               String var5 = PacketPackageManager.ins.loadContent(var4.getId());
               if (StringUtils.isBlank(var5)) {
                  throw new InfoException("请先上传知识包");
               }

               return var5;
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
