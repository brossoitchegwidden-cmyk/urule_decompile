package com.bstek.urule.console.editor.reference;

import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.service.reference.ReferenceService;
import com.bstek.urule.console.editor.FileDeserializer;
import com.bstek.urule.console.editor.reference.file.Reference;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.DeserializeException;
import com.bstek.urule.exception.ReferenceDeleteException;
import com.bstek.urule.parse.RuleFileHolder;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dom4j.Element;

public class ReferenceServletHandler extends ApiServletHandler {
   public void file(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("id"));
      String parameter = req.getParameter("type");
      if (StringUtils.isNotBlank(parameter) && parameter.contentEquals(ResourceType.Packet.name())) {
         FileReference fileReference = KnowledgePacketBuilder.builder.build(longValue);
         this.writeObjectToJson(resp, fileReference);
      } else {
         String content = FileManager.ins.loadContent(longValue);
         RuleFile ruleFile = FileManager.ins.get(longValue);
         RuleFileHolder.resetRuleFile(ruleFile.getPath());
         Element xml = FileDeserializer.getInstance().parseXml(content);
         Object objectValue = FileDeserializer.getInstance().deserialize(xml);
         RuleFileHolder.clean();
         Reference reference = Reference.loadReference(objectValue);
         if (reference == null) {
            return;
         }

         FileReference fileReference2 = reference.build(objectValue);
         fileReference2.setId(longValue);
         fileReference2.setName(ruleFile.getName());
         fileReference2.setPathInfo(ruleFile.getPath());
         this.writeObjectToJson(resp, fileReference2);
      }

   }

   public void uuid(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("projectId"));
      long longValue2 = Long.valueOf(req.getParameter("id"));
      String parameter = req.getParameter("uuid");

      try {
         List items = ReferenceService.ins.uuid(longValue, longValue2, parameter);
         this.writeObjectToJson(resp, items);
      } catch (DeserializeException deserializeException) {
         throw new ReferenceDeleteException(deserializeException.getMessage());
      }
   }

   public void packet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("projectId"));
      long longValue2 = Long.valueOf(req.getParameter("id"));
      String parameter = req.getParameter("code");

      try {
         List items = ReferenceService.ins.packet(longValue, longValue2, parameter);
         this.writeObjectToJson(resp, items);
      } catch (DeserializeException deserializeException) {
         throw new ReferenceDeleteException(deserializeException.getMessage());
      }
   }

   public String url() {
      return "/reference";
   }
}
