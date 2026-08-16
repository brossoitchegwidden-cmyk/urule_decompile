package com.bstek.urule.console.editor.packet;

import com.bstek.urule.Utils;
import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.ContextHolder;
import com.bstek.urule.console.Transactional;
import com.bstek.urule.console.admin.log.SystemLogUtils;
import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.manager.packet.PacketBuilder;
import com.bstek.urule.console.database.manager.packet.PacketManager;
import com.bstek.urule.console.database.manager.packet.apply.PacketApplyManager;
import com.bstek.urule.console.database.manager.packet.apply.PacketApplyQuery;
import com.bstek.urule.console.database.manager.packet.deploy.PacketDeployManager;
import com.bstek.urule.console.database.manager.packet.deploy.file.PacketDeployFileManager;
import com.bstek.urule.console.database.manager.project.ProjectManager;
import com.bstek.urule.console.database.model.ApplyStatus;
import com.bstek.urule.console.database.model.ApplyType;
import com.bstek.urule.console.database.model.Packet;
import com.bstek.urule.console.database.model.PacketApply;
import com.bstek.urule.console.database.model.PacketDeploy;
import com.bstek.urule.console.database.model.PacketDeployFile;
import com.bstek.urule.console.database.model.PacketFile;
import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.security.SecurityUtils;
import com.bstek.urule.console.security.URuleAuthorization;
import com.bstek.urule.console.type.RuleFileType;
import com.bstek.urule.console.util.MD5Utils;
import com.bstek.urule.exception.RuleAssertException;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.runtime.KnowledgePackage;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PacketApplyServletHandler extends ApiServletHandler {
   public void get(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("applyId"));
      this.a(var2, PacketApplyManager.ins.load(var3));
   }

   public void load(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("packetId"));
      ApplyType var5 = ApplyType.valueOf(var1.getParameter("type"));
      int var6 = Integer.valueOf(var1.getParameter("pageIndex"));
      int var7 = Integer.valueOf(var1.getParameter("pageSize"));
      PacketApplyQuery var8 = PacketApplyManager.ins.newQuery();
      var8.titleLike(var1.getParameter("title"));
      var8.createUserLike(var1.getParameter("createUser"));
      Page var9 = var8.packetId(var3).type(var5).projectId(ContextHolder.getProjectId()).paging(var6, var7);
      List var10 = PacketApplyManager.ins.newQuery().packetId(var3).projectId(ContextHolder.getProjectId()).type(var5).statusIn(new ApplyStatus[]{ApplyStatus.pending, ApplyStatus.reject}).list();
      HashMap var11 = new HashMap();
      var11.put("page", var9);
      if (var10.size() == 0) {
         var11.put("add", true);
      } else {
         var11.put("add", false);
      }

      this.a(var2, var11);
   }

   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void add(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("packetId"));
      Packet var5 = PacketManager.ins.load(var3);
      PacketApply var6 = new PacketApply();
      ApplyType var7 = ApplyType.valueOf(var1.getParameter("type"));
      var6.setType(var7);
      var6.setTitle(var1.getParameter("title"));
      var6.setDesc(var1.getParameter("desc"));
      var6.setPacketId(var3);
      var6.setProjectId(var5.getProjectId());
      long var8 = IDGenerator.getInstance().nextId(IDType.DEPLOYED_PACKET);
      var6.setDeployedPacketId(var8);
      var6.setCreateUser(SecurityUtils.getLoginUsername(var1));
      var6.setApprover(ProjectManager.ins.getApproveUser(PacketManager.ins.load(var3).getProjectId(), var7));
      PacketApplyManager.ins.add(var6);
      if (var7.equals(ApplyType.deploy)) {
         KnowledgePackage var10 = PacketBuilder.ins.buildKnowledgePackage(var3);
         String var11 = Utils.knowledgePackageToString(var10);

         try {
            Utils.stringToKnowledgePackageWrapper(var11);
         } catch (RuleException var18) {
            Exception var13 = (Exception)var18.getCause();
            throw new RuleAssertException("知识包[" + var3 + "]对应的规则格式非法!", var13);
         }

         PacketDeploy var12 = new PacketDeploy();
         var12.setId(var8);
         var12.setApplyId(var6.getId());
         var12.setDigest(MD5Utils.stringToMD5(var11));
         var12.setContent(var11);
         var12.setCreateUser(var6.getCreateUser());
         var12.setDesc(var1.getParameter("deployDesc"));
         var12.setPacketId(var3);
         var12.setStatus(ApplyStatus.pending);
         var12.setProjectId(var5.getProjectId());
         PacketDeployManager.ins.add(var12);

         for(PacketFile var14 : (Iterable<PacketFile>)(Iterable<?>)(var5.getFiles())) {
            RuleFile var15 = FileManager.ins.get(var14.getFileId());
            PacketDeployFile var16 = new PacketDeployFile();
            var16.setPacketDeployId(var12.getId());
            var16.setFileId(var14.getFileId());
            var16.setProjectId(var5.getProjectId());
            var16.setDigest(var15.getDigest());
            var16.setPath(var14.getPath());
            var16.setVersion(var14.getVersion());
            String var17 = FileManager.ins.loadContent(var14.getFileId());
            var16.setContent(var17);
            var16.setCreateUser(var6.getCreateUser());
            var16.setDigest(MD5Utils.stringToMD5(var17));
            PacketDeployFileManager.ins.add(var16);
         }
      }

      String var19 = "";
      if (var7.equals(ApplyType.deploy)) {
         var19 = "Apply for release of packet %s[%s]";
      } else if (var7.equals(ApplyType.disable)) {
         var19 = "Request to disable packet %s[%s]";
      } else if (var7.equals(ApplyType.enable)) {
         var19 = "Request to enable packet %s[%s]";
      }

      SystemLogUtils.addProjectOperationLog(RuleFileType.Knowledge.name(), var7.name(), var5.getId(), String.format(var19, var5.getName(), var5.getCode()));
      this.a(var2, var6);
   }

   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void delete(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("id"));
      PacketApply var5 = PacketApplyManager.ins.load(var3);
      if (!var5.getStatus().equals(ApplyStatus.pending)) {
         throw new RuleException("当前申请【" + var5.getTitle() + "】不处于【待审批】状态，不能被删除！");
      } else {
         PacketApplyManager.ins.delete(var3);
         if (var5.getType().equals(ApplyType.deploy)) {
            PacketDeployManager.ins.deleteByApplyId(var3);
         }

      }
   }

   public String url() {
      return "/apply";
   }
}
