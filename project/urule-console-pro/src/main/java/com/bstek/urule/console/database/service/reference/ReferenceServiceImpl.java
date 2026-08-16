package com.bstek.urule.console.database.service.reference;

import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.manager.project.ProjectManager;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.ProjectType;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.editor.FileDeserializer;
import com.bstek.urule.console.editor.reference.KnowledgePacketBuilder;
import com.bstek.urule.console.editor.reference.file.PacketSupportReference;
import com.bstek.urule.console.editor.reference.file.Reference;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.DeserializeException;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.parse.RuleFileHolder;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;

public class ReferenceServiceImpl implements ReferenceService {
   public List uuid(long var1, long var3, String var5) {
      ArrayList var6 = new ArrayList();
      Project var7 = ProjectManager.ins.get(var1);
      List var8 = this.a(var7, var3, var5);
      var6.addAll(var8);
      if (var7.getType().contentEquals(ProjectType.common.name())) {
         for(Project var11 : (Iterable<Project>)(Iterable<?>)(ProjectManager.ins.newQuery().type(ProjectType.custom.name()).groupId(var7.getGroupId()).list())) {
            var8 = this.a(var11, var3, var5);
            var6.addAll(var8);
         }
      }

      return var6;
   }

   public List packet(long var1, long var3, String var5) {
      ArrayList var6 = new ArrayList();
      Project var7 = ProjectManager.ins.get(var1);
      List var8 = this.b(var7, var3, var5);
      var6.addAll(var8);
      if (var7.getType().contentEquals(ProjectType.common.name())) {
         for(Project var11 : (Iterable<Project>)(Iterable<?>)(ProjectManager.ins.newQuery().type(ProjectType.custom.name()).groupId(var7.getGroupId()).list())) {
            var8 = this.b(var11, var3, var5);
            var6.addAll(var8);
         }
      }

      return var6;
   }

   private List a(Project var1, long var2, String var4) {
      long var5 = var1.getId();
      ArrayList var7 = new ArrayList();

      for(RuleFile var10 : (Iterable<RuleFile>)(Iterable<?>)(FileManager.ins.newQuery().deleted(false).asc("NAME_").list(var5))) {
         if (var10.getId() != var2) {
            Element var11 = null;

            try {
               String var12 = FileManager.ins.loadContent(var10.getId());
               if (StringUtils.isBlank(var4)) {
                  RuleFileHolder.resetRuleFile(var10.getPath());

                  try {
                     var11 = FileDeserializer.getInstance().parseXml(var12);
                     Object var13 = FileDeserializer.getInstance().deserialize(var11);
                     Reference var14 = Reference.loadReference(var13);
                     if (var14 != null && var14.exist(var13, var2)) {
                        var7.add(var10);
                     }
                  } catch (DeserializeException var15) {
                  }

                  RuleFileHolder.clean();
               } else if (var12.indexOf(var4) > -1) {
                  var7.add(var10);
               }
            } catch (Exception var16) {
               if (var16 instanceof RuleException) {
                  throw (RuleException)var16;
               }

               throw new RuleException(var16);
            }
         }
      }

      if (StringUtils.isBlank(var4)) {
         List var17 = KnowledgePacketBuilder.builder.referenceFiles(var1, var2);
         var7.addAll(var17);
      }

      return var7;
   }

   private List b(Project var1, long var2, String var4) {
      long var5 = var1.getId();
      ArrayList var7 = new ArrayList();

      for(RuleFile var10 : (Iterable<RuleFile>)(Iterable<?>)(FileManager.ins.newQuery().deleted(false).asc("NAME_").list(var5))) {
         if (var10.getId() != var2) {
            RuleFileHolder.resetRuleFile(var10.getPath());
            String var11 = FileManager.ins.loadContent(var10.getId());
            Element var12 = null;

            try {
               var12 = FileDeserializer.getInstance().parseXml(var11);
               Object var13 = FileDeserializer.getInstance().deserialize(var12);
               RuleFileHolder.clean();
               Reference var14 = Reference.loadReference(var13);
               if (var14 != null && var14 instanceof PacketSupportReference) {
                  PacketSupportReference var15 = (PacketSupportReference)var14;
                  if (var15.existPacket(var13, var2, var4)) {
                     var7.add(var10);
                  }
               }
            } catch (Exception var16) {
               if (var16 instanceof DeserializeException) {
                  throw (DeserializeException)var16;
               }

               throw new RuleException(var16);
            }
         }
      }

      return var7;
   }
}
