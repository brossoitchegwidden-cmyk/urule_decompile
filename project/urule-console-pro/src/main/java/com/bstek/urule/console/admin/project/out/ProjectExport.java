package com.bstek.urule.console.admin.project.out;

import com.bstek.urule.Utils;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.manager.file.FileQuery;
import com.bstek.urule.console.database.manager.file.version.VersionFileManager;
import com.bstek.urule.console.database.manager.packet.PacketManager;
import com.bstek.urule.console.database.manager.packet.PacketQuery;
import com.bstek.urule.console.database.model.Packet;
import com.bstek.urule.console.database.model.PacketFile;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.model.VersionFile;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultCDATA;

public class ProjectExport {
   private static final Log logger = LogFactory.getLog(ProjectExport.class);
   public static final ProjectExport ins = new ProjectExport();

   private ProjectExport() {
   }

   public void doExport(OutputStream output, Project project) throws Exception {
      Document document = this.buildProjectDocument(project);
      StringWriter stringWriter = new StringWriter();
      XMLWriter xMLWriter = new XMLWriter(stringWriter, new OutputFormat());
      xMLWriter.write(document);
      String text = stringWriter.toString();
      byte[] bytes = Utils.compress(text);
      IOUtils.write(bytes, output);
   }

   private Document buildProjectDocument(Project project) {
      Document document = DocumentHelper.createDocument();
      Element element = document.addElement("project");
      element.addAttribute("name", project.getName());
      element.addAttribute("id", String.valueOf(project.getId()));
      element.addAttribute("type", project.getType());
      element.addAttribute("viewModel", project.getViewModel().name());
      this.appendEncodedChild(element, project.getDesc(), "desc");
      this.appendRuleFiles(project, element);
      this.appendPackets(project, element);
      return document;
   }

   private void appendPackets(Project project, Element element) {
      PacketQuery packetQuery = PacketManager.ins.newQuery();

      for(Packet packet : (Iterable<Packet>)(Iterable<?>)(packetQuery.projectId(project.getId()).list())) {
         Element element2 = element.addElement("packet");
         element2.addAttribute("name", packet.getName());
         element2.addAttribute("code", packet.getCode());
         element2.addAttribute("desc", packet.getDesc());
         element2.addAttribute("id", String.valueOf(packet.getId()));
         if (packet.getType() != null) {
            element2.addAttribute("type", packet.getType().name());
         }

         element2.addAttribute("enable", String.valueOf(packet.isEnable()));
         element2.addAttribute("audit-enable", String.valueOf(packet.isAuditEnable()));
         element2.addAttribute("rest-enable", String.valueOf(packet.isRestEnable()));
         element2.addAttribute("rest-security-enable", String.valueOf(packet.isRestSecurityEnable()));
         if (packet.isRestSecurityEnable()) {
            element2.addAttribute("rest-security-user", packet.getRestSecurityUser());
            element2.addAttribute("rest-security-password", packet.getRestSecurityPassword());
         }

         this.appendEncodedChild(element2, packet.getAuditInput(), "audit-input");
         this.appendEncodedChild(element2, packet.getAuditOutput(), "audit-output");
         this.appendEncodedChild(element2, packet.getRestInput(), "rest-input");
         this.appendEncodedChild(element2, packet.getRestOutput(), "rest-output");
         this.appendEncodedChild(element2, packet.getInputData(), "input-data");
         this.appendEncodedChild(element2, packet.getOutputData(), "output-data");
         ProjectExport.logger.debug("buildPacket:" + packet.getName());
         this.appendPacketFiles(packet.getFiles(), element2);
      }

   }

   private void appendPacketFiles(List items, Element element) {
      for(PacketFile packetFile : (Iterable<PacketFile>)(Iterable<?>)(items)) {
         ProjectExport.logger.debug("buildPacketFile:" + packetFile.getFileId());
         Element element2 = element.addElement("file");
         element2.addAttribute("id", String.valueOf(packetFile.getFileId()));
         element2.addAttribute("desc", packetFile.getDesc());
         element2.addAttribute("path", packetFile.getPath());
         element2.addAttribute("version", packetFile.getVersion());
      }

   }

   private void appendRuleFiles(Project project, Element element) {
      FileQuery fileQuery = FileManager.ins.newQuery();

      for(RuleFile ruleFile : (Iterable<RuleFile>)(Iterable<?>)(fileQuery.list(project.getId()))) {
         if (!ruleFile.isDeleted()) {
            ProjectExport.logger.debug("buildFile:" + ruleFile.getName());
            Element element2 = element.addElement("file");
            element2.addAttribute("name", ruleFile.getName());
            element2.addAttribute("deleted", String.valueOf(ruleFile.isDeleted()));
            element2.addAttribute("id", String.valueOf(ruleFile.getId()));
            element2.addAttribute("path", ruleFile.getPath());
            element2.addAttribute("fileSet", String.valueOf(ruleFile.isFileSet()));
            element2.addAttribute("digest", ruleFile.getDigest());
            element2.addAttribute("type", ruleFile.getType());
            element2.addAttribute("latest-version", ruleFile.getLatestVersion());
            String content = FileManager.ins.loadContent(ruleFile.getId());
            this.appendEncodedChild(element2, content, "content");
            this.appendVersionFiles(ruleFile, element2);
         }
      }

   }

   private void appendVersionFiles(RuleFile ruleFile, Element element) {
      for(VersionFile versionFile : (Iterable<VersionFile>)(Iterable<?>)(VersionFileManager.ins.loadFiles(ruleFile.getId()))) {
         Element element2 = element.addElement("version");
         element2.addAttribute("version", versionFile.getVersion());
         element2.addAttribute("digest", versionFile.getDigest());
         this.appendEncodedChild(element2, versionFile.getNote(), "note");
         element2.addAttribute("id", String.valueOf(versionFile.getId()));
         String fileContent = VersionFileManager.ins.loadFileContent(versionFile.getId());
         this.appendEncodedChild(element2, fileContent, "content");
      }

   }

   private void appendEncodedChild(Element element, String text, String text2) {
      if (!StringUtils.isBlank(text)) {
         try {
            text = Base64.getEncoder().encodeToString(text.getBytes("utf-8"));
         } catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new RuleException(unsupportedEncodingException);
         }

         Element element2 = element.addElement(text2);
         element2.add(new DefaultCDATA(text));
      }
   }
}
