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
   public void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("applyId"));
      this.writeObjectToJson(resp, PacketApplyManager.ins.load(longValue));
   }

   public void load(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("packetId"));
      ApplyType applyType = ApplyType.valueOf(req.getParameter("type"));
      int number = Integer.valueOf(req.getParameter("pageIndex"));
      int number2 = Integer.valueOf(req.getParameter("pageSize"));
      PacketApplyQuery packetApplyQuery = PacketApplyManager.ins.newQuery();
      packetApplyQuery.titleLike(req.getParameter("title"));
      packetApplyQuery.createUserLike(req.getParameter("createUser"));
      Page page = packetApplyQuery.packetId(longValue).type(applyType).projectId(ContextHolder.getProjectId()).paging(number, number2);
      List items = PacketApplyManager.ins.newQuery().packetId(longValue).projectId(ContextHolder.getProjectId()).type(applyType).statusIn(new ApplyStatus[]{ApplyStatus.pending, ApplyStatus.reject}).list();
      HashMap valuesByKey = new HashMap();
      valuesByKey.put("page", page);
      if (items.size() == 0) {
         valuesByKey.put("add", true);
      } else {
         valuesByKey.put("add", false);
      }

      this.writeObjectToJson(resp, valuesByKey);
   }

   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void add(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("packetId"));
      Packet packet = PacketManager.ins.load(longValue);
      PacketApply packetApply = new PacketApply();
      ApplyType applyType = ApplyType.valueOf(req.getParameter("type"));
      packetApply.setType(applyType);
      packetApply.setTitle(req.getParameter("title"));
      packetApply.setDesc(req.getParameter("desc"));
      packetApply.setPacketId(longValue);
      packetApply.setProjectId(packet.getProjectId());
      long longValue2 = IDGenerator.getInstance().nextId(IDType.DEPLOYED_PACKET);
      packetApply.setDeployedPacketId(longValue2);
      packetApply.setCreateUser(SecurityUtils.getLoginUsername(req));
      packetApply.setApprover(ProjectManager.ins.getApproveUser(PacketManager.ins.load(longValue).getProjectId(), applyType));
      PacketApplyManager.ins.add(packetApply);
      if (applyType.equals(ApplyType.deploy)) {
         KnowledgePackage knowledgePackage = PacketBuilder.ins.buildKnowledgePackage(longValue);
         String text = Utils.knowledgePackageToString(knowledgePackage);

         try {
            Utils.stringToKnowledgePackageWrapper(text);
         } catch (RuleException ruleException) {
            Exception cause = (Exception)ruleException.getCause();
            throw new RuleAssertException("知识包[" + longValue + "]对应的规则格式非法!", cause);
         }

         PacketDeploy packetDeploy = new PacketDeploy();
         packetDeploy.setId(longValue2);
         packetDeploy.setApplyId(packetApply.getId());
         packetDeploy.setDigest(MD5Utils.stringToMD5(text));
         packetDeploy.setContent(text);
         packetDeploy.setCreateUser(packetApply.getCreateUser());
         packetDeploy.setDesc(req.getParameter("deployDesc"));
         packetDeploy.setPacketId(longValue);
         packetDeploy.setStatus(ApplyStatus.pending);
         packetDeploy.setProjectId(packet.getProjectId());
         PacketDeployManager.ins.add(packetDeploy);

         for(PacketFile packetFile : (Iterable<PacketFile>)(Iterable<?>)(packet.getFiles())) {
            RuleFile ruleFile = FileManager.ins.get(packetFile.getFileId());
            PacketDeployFile packetDeployFile = new PacketDeployFile();
            packetDeployFile.setPacketDeployId(packetDeploy.getId());
            packetDeployFile.setFileId(packetFile.getFileId());
            packetDeployFile.setProjectId(packet.getProjectId());
            packetDeployFile.setDigest(ruleFile.getDigest());
            packetDeployFile.setPath(packetFile.getPath());
            packetDeployFile.setVersion(packetFile.getVersion());
            String content = FileManager.ins.loadContent(packetFile.getFileId());
            packetDeployFile.setContent(content);
            packetDeployFile.setCreateUser(packetApply.getCreateUser());
            packetDeployFile.setDigest(MD5Utils.stringToMD5(content));
            PacketDeployFileManager.ins.add(packetDeployFile);
         }
      }

      String text2 = "";
      if (applyType.equals(ApplyType.deploy)) {
         text2 = "Apply for release of packet %s[%s]";
      } else if (applyType.equals(ApplyType.disable)) {
         text2 = "Request to disable packet %s[%s]";
      } else if (applyType.equals(ApplyType.enable)) {
         text2 = "Request to enable packet %s[%s]";
      }

      SystemLogUtils.addProjectOperationLog(RuleFileType.Knowledge.name(), applyType.name(), packet.getId(), String.format(text2, packet.getName(), packet.getCode()));
      this.writeObjectToJson(resp, packetApply);
   }

   @Transactional
   @URuleAuthorization(
      authType = "project",
      code = "manager",
      model = "rule_knowledge"
   )
   public void delete(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("id"));
      PacketApply packetApply = PacketApplyManager.ins.load(longValue);
      if (!packetApply.getStatus().equals(ApplyStatus.pending)) {
         throw new RuleException("当前申请【" + packetApply.getTitle() + "】不处于【待审批】状态，不能被删除！");
      } else {
         PacketApplyManager.ins.delete(longValue);
         if (packetApply.getType().equals(ApplyType.deploy)) {
            PacketDeployManager.ins.deleteByApplyId(longValue);
         }

      }
   }

   public String url() {
      return "/apply";
   }
}
