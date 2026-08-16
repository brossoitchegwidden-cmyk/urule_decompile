package com.bstek.urule.console.editor.reference.file;

import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.editor.reference.FileReference;
import com.bstek.urule.console.editor.reference.KnowledgePacketBuilder;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.model.flow.BindingFile;
import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.model.flow.FlowNode;
import com.bstek.urule.model.flow.RuleNode;
import com.bstek.urule.model.flow.RulePackageNode;
import com.bstek.urule.model.flow.ScriptNode;
import com.bstek.urule.model.rule.Library;
import java.util.ArrayList;
import java.util.List;

public class RuleflowReference extends PacketSupportReference {
   protected RuleflowReference() {
   }

   public boolean exist(Object var1, Long var2) {
      FlowDefinition var3 = (FlowDefinition)var1;
      List var4 = var3.getLibraries();
      if (var4 != null) {
         for(Library var6 : (Iterable<Library>)(Iterable<?>)(var4)) {
            if (var6.getId() == var2) {
               return true;
            }
         }
      }

      for(FlowNode var7 : var3.getNodes()) {
         if (var7 instanceof RuleNode) {
            RuleNode var14 = (RuleNode)var7;

            for(BindingFile var11 : var14.getFiles()) {
               if (var11.getId() == var2) {
                  return true;
               }
            }
         } else if (var7 instanceof ScriptNode) {
            ScriptNode var8 = (ScriptNode)var7;
            if (this.a(var8.getActionsData(), var2)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean existPacket(Object var1, Long var2, String var3) {
      FlowDefinition var4 = (FlowDefinition)var1;

      for(FlowNode var7 : var4.getNodes()) {
         if (var7 instanceof RulePackageNode) {
            RulePackageNode var8 = (RulePackageNode)var7;
            if (var8.getCode() != null && StringUtils.isNotBlank(var3) && var8.getCode().contentEquals(var3)) {
               return true;
            }

            if (var8.getPackageId() != null && var2 != null && var8.getPackageId().contentEquals(String.valueOf(var2))) {
               return true;
            }
         }
      }

      return false;
   }

   public FileReference build(Object var1) {
      FlowDefinition var2 = (FlowDefinition)var1;
      FileReference var3 = new FileReference();
      var3.setType(ResourceType.Flow);
      ArrayList var4 = new ArrayList();
      var3.setChildren(var4);
      FileReference var5 = this.b(var2.getLibraries());
      if (var5 != null) {
         var4.add(var5);
      }

      for(FlowNode var8 : var2.getNodes()) {
         if (var8 instanceof RuleNode) {
            RuleNode var16 = (RuleNode)var8;

            for(BindingFile var19 : var16.getFiles()) {
               RuleFile var13 = FileManager.ins.get(var19.getId());
               FileReference var14 = this.a(var13);
               var4.add(var14);
            }
         } else if (var8 instanceof RulePackageNode) {
            RulePackageNode var9 = (RulePackageNode)var8;
            long var10 = Long.valueOf(var9.getPackageId());
            FileReference var12 = KnowledgePacketBuilder.builder.build(var10);
            var4.add(var12);
         } else if (var8 instanceof ScriptNode) {
            ScriptNode var15 = (ScriptNode)var8;
            List var17 = var15.getActionsData();
            var4.addAll(this.a(var17));
         }
      }

      return var3;
   }

   public boolean support(Object var1) {
      return var1 instanceof FlowDefinition;
   }
}
