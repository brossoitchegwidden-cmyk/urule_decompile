package com.bstek.urule.console.editor.reference.file;

import com.bstek.urule.action.Action;
import com.bstek.urule.action.ExecuteMethodAction;
import com.bstek.urule.action.InvokeFile;
import com.bstek.urule.action.InvokeKnowledgePackage;
import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.editor.reference.FileReference;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.table.Cell;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Reference {
   public abstract FileReference build(Object var1);

   public abstract boolean exist(Object var1, Long var2);

   public abstract boolean support(Object var1);

   public static Reference loadReference(Object var0) {
      for(Reference var2 : (Iterable<Reference>)(Iterable<?>)(Reference.ReferenceInstances.a)) {
         if (var2.support(var0)) {
            return var2;
         }
      }

      return null;
   }

   protected List a(Map var1) {
      if (var1 == null) {
         return null;
      } else {
         ArrayList var2 = new ArrayList();

         for(Cell var4 : (Iterable<Cell>)(Iterable<?>)(var1.values())) {
            Action var5 = var4.getAction();
            FileReference var6 = this.a(var5);
            if (var6 != null) {
               var2.add(var6);
            }
         }

         return var2;
      }
   }

   protected List a(List var1) {
      ArrayList var2 = new ArrayList();
      if (var1 == null) {
         return var2;
      } else {
         for(Action var4 : (Iterable<Action>)(Iterable<?>)(var1)) {
            FileReference var5 = this.a(var4);
            if (var5 != null) {
               var2.add(var5);
            }
         }

         return var2;
      }
   }

   protected FileReference a(Action var1) {
      if (!(var1 instanceof ExecuteMethodAction)) {
         return null;
      } else {
         ExecuteMethodAction var2 = (ExecuteMethodAction)var1;
         InvokeFile var3 = var2.getInvokeFile();
         InvokeKnowledgePackage var4 = var2.getInvokeKnowledgePackage();
         if (var4 != null) {
            FileReference var7 = new FileReference();
            var7.setType(ResourceType.Packet);
            var7.setId(var4.getId());
            var7.setName(var4.getName());
            var7.setPathInfo(ResourceType.Packet + ":" + var4.getProject() + "/" + var4.getName());
            return var7;
         } else if (var3 != null) {
            FileReference var5 = new FileReference();
            RuleFile var6 = FileManager.ins.get(var3.getId());
            var5.setId(var6.getId());
            var5.setType(ResourceType.valueOf(var6.getType()));
            var5.setName(var6.getName());
            var5.setPathInfo(var6.getPath());
            return var5;
         } else {
            return null;
         }
      }
   }

   protected FileReference b(List var1) {
      if (var1 == null) {
         return null;
      } else {
         FileReference var2 = new FileReference();
         var2.setPathInfo("libs");
         var2.setName("libs");
         var2.setType(ResourceType.Library);
         ArrayList var3 = new ArrayList();
         var2.setChildren(var3);

         for(Library var5 : (Iterable<Library>)(Iterable<?>)(var1)) {
            RuleFile var6 = FileManager.ins.get(var5.getId());
            FileReference var7 = this.a(var6);
            var7.setVersion(var5.getVersion());
            var3.add(var7);
         }

         return var2;
      }
   }

   protected FileReference a(RuleFile var1) {
      FileReference var2 = new FileReference();
      var2.setId(var1.getId());
      ResourceType var3 = ResourceType.valueOf(var1.getType());
      var2.setType(var3);
      var2.setName(var1.getName());
      var2.setPathInfo(var1.getPath());
      return var2;
   }

   protected boolean a(List var1, Long var2) {
      if (var1 == null) {
         return false;
      } else {
         for(Action var4 : (Iterable<Action>)(Iterable<?>)(var1)) {
            if (var4 instanceof ExecuteMethodAction) {
               ExecuteMethodAction var5 = (ExecuteMethodAction)var4;
               InvokeFile var6 = var5.getInvokeFile();
               if (var6 != null && var6.getId() == var2) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public static class ReferenceInstances {
      private static List a = new ArrayList();

      static {
         a.add(new RuleSetReference());
         a.add(new DecisionTableReference());
         a.add(new CrosstableReference());
         a.add(new ComplexScorecardReference());
         a.add(new DecisionTreeReference());
         a.add(new RuleflowReference());
         a.add(new ScorecardReference());
         a.add(new ConditionTemplateReference());
         a.add(new ActionTemplateReference());
      }
   }
}
