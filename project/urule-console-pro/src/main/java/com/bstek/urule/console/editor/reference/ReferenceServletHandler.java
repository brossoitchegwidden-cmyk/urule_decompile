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
   public void file(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("id"));
      String var5 = var1.getParameter("type");
      if (StringUtils.isNotBlank(var5) && var5.contentEquals(ResourceType.Packet.name())) {
         FileReference var12 = KnowledgePacketBuilder.builder.build(var3);
         this.a(var2, var12);
      } else {
         String var6 = FileManager.ins.loadContent(var3);
         RuleFile var7 = FileManager.ins.get(var3);
         RuleFileHolder.resetRuleFile(var7.getPath());
         Element var8 = FileDeserializer.getInstance().parseXml(var6);
         Object var9 = FileDeserializer.getInstance().deserialize(var8);
         RuleFileHolder.clean();
         Reference var10 = Reference.loadReference(var9);
         if (var10 == null) {
            return;
         }

         FileReference var11 = var10.build(var9);
         var11.setId(var3);
         var11.setName(var7.getName());
         var11.setPathInfo(var7.getPath());
         this.a(var2, var11);
      }

   }

   public void uuid(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("projectId"));
      long var5 = Long.valueOf(var1.getParameter("id"));
      String var7 = var1.getParameter("uuid");

      try {
         List var8 = ReferenceService.ins.uuid(var3, var5, var7);
         this.a(var2, var8);
      } catch (DeserializeException var9) {
         throw new ReferenceDeleteException(var9.getMessage());
      }
   }

   public void packet(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("projectId"));
      long var5 = Long.valueOf(var1.getParameter("id"));
      String var7 = var1.getParameter("code");

      try {
         List var8 = ReferenceService.ins.packet(var3, var5, var7);
         this.a(var2, var8);
      } catch (DeserializeException var9) {
         throw new ReferenceDeleteException(var9.getMessage());
      }
   }

   public String url() {
      return "/reference";
   }
}
