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
   private static final Log a = LogFactory.getLog(ProjectExport.class);
   public static final ProjectExport ins = new ProjectExport();

   private ProjectExport() {
   }

   public void doExport(OutputStream var1, Project var2) throws Exception {
      Document var3 = this.a(var2);
      StringWriter var4 = new StringWriter();
      XMLWriter var5 = new XMLWriter(var4, new OutputFormat());
      var5.write(var3);
      String var6 = var4.toString();
      byte[] var7 = Utils.compress(var6);
      IOUtils.write(var7, var1);
   }

   private Document a(Project var1) {
      Document var2 = DocumentHelper.createDocument();
      Element var3 = var2.addElement("project");
      var3.addAttribute("name", var1.getName());
      var3.addAttribute("id", String.valueOf(var1.getId()));
      var3.addAttribute("type", var1.getType());
      var3.addAttribute("viewModel", var1.getViewModel().name());
      this.a(var3, var1.getDesc(), "desc");
      this.b(var1, var3);
      this.a(var1, var3);
      return var2;
   }

   private void a(Project var1, Element var2) {
      PacketQuery var3 = PacketManager.ins.newQuery();

      for(Packet var6 : (Iterable<Packet>)(Iterable<?>)(var3.projectId(var1.getId()).list())) {
         Element var7 = var2.addElement("packet");
         var7.addAttribute("name", var6.getName());
         var7.addAttribute("code", var6.getCode());
         var7.addAttribute("desc", var6.getDesc());
         var7.addAttribute("id", String.valueOf(var6.getId()));
         if (var6.getType() != null) {
            var7.addAttribute("type", var6.getType().name());
         }

         var7.addAttribute("enable", String.valueOf(var6.isEnable()));
         var7.addAttribute("audit-enable", String.valueOf(var6.isAuditEnable()));
         var7.addAttribute("rest-enable", String.valueOf(var6.isRestEnable()));
         var7.addAttribute("rest-security-enable", String.valueOf(var6.isRestSecurityEnable()));
         if (var6.isRestSecurityEnable()) {
            var7.addAttribute("rest-security-user", var6.getRestSecurityUser());
            var7.addAttribute("rest-security-password", var6.getRestSecurityPassword());
         }

         this.a(var7, var6.getAuditInput(), "audit-input");
         this.a(var7, var6.getAuditOutput(), "audit-output");
         this.a(var7, var6.getRestInput(), "rest-input");
         this.a(var7, var6.getRestOutput(), "rest-output");
         this.a(var7, var6.getInputData(), "input-data");
         this.a(var7, var6.getOutputData(), "output-data");
         a.debug("buildPacket:" + var6.getName());
         this.a(var6.getFiles(), var7);
      }

   }

   private void a(List var1, Element var2) {
      for(PacketFile var4 : (Iterable<PacketFile>)(Iterable<?>)(var1)) {
         a.debug("buildPacketFile:" + var4.getFileId());
         Element var5 = var2.addElement("file");
         var5.addAttribute("id", String.valueOf(var4.getFileId()));
         var5.addAttribute("desc", var4.getDesc());
         var5.addAttribute("path", var4.getPath());
         var5.addAttribute("version", var4.getVersion());
      }

   }

   private void b(Project var1, Element var2) {
      FileQuery var3 = FileManager.ins.newQuery();

      for(RuleFile var6 : (Iterable<RuleFile>)(Iterable<?>)(var3.list(var1.getId()))) {
         if (!var6.isDeleted()) {
            a.debug("buildFile:" + var6.getName());
            Element var7 = var2.addElement("file");
            var7.addAttribute("name", var6.getName());
            var7.addAttribute("deleted", String.valueOf(var6.isDeleted()));
            var7.addAttribute("id", String.valueOf(var6.getId()));
            var7.addAttribute("path", var6.getPath());
            var7.addAttribute("fileSet", String.valueOf(var6.isFileSet()));
            var7.addAttribute("digest", var6.getDigest());
            var7.addAttribute("type", var6.getType());
            var7.addAttribute("latest-version", var6.getLatestVersion());
            String var8 = FileManager.ins.loadContent(var6.getId());
            this.a(var7, var8, "content");
            this.a(var6, var7);
         }
      }

   }

   private void a(RuleFile var1, Element var2) {
      for(VersionFile var5 : (Iterable<VersionFile>)(Iterable<?>)(VersionFileManager.ins.loadFiles(var1.getId()))) {
         Element var6 = var2.addElement("version");
         var6.addAttribute("version", var5.getVersion());
         var6.addAttribute("digest", var5.getDigest());
         this.a(var6, var5.getNote(), "note");
         var6.addAttribute("id", String.valueOf(var5.getId()));
         String var7 = VersionFileManager.ins.loadFileContent(var5.getId());
         this.a(var6, var7, "content");
      }

   }

   private void a(Element var1, String var2, String var3) {
      if (!StringUtils.isBlank(var2)) {
         try {
            var2 = Base64.getEncoder().encodeToString(var2.getBytes("utf-8"));
         } catch (UnsupportedEncodingException var5) {
            throw new RuleException(var5);
         }

         Element var4 = var1.addElement(var3);
         var4.add(new DefaultCDATA(var2));
      }
   }
}
