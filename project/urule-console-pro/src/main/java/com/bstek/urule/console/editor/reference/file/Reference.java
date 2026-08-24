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
   public abstract FileReference build(Object obj);

   public abstract boolean exist(Object obj, Long fileId);

   public abstract boolean support(Object obj);

   public static Reference loadReference(Object obj) {
      for(Reference reference : (Iterable<Reference>)(Iterable<?>)(Reference.ReferenceInstances.INSTANCES)) {
         if (reference.support(obj)) {
            return reference;
         }
      }

      return null;
   }

   protected List buildActionReferences(Map valuesByKey) {
      if (valuesByKey == null) {
         return null;
      } else {
         ArrayList items = new ArrayList();

         for(Cell cell : (Iterable<Cell>)(Iterable<?>)(valuesByKey.values())) {
            Action action = cell.getAction();
            FileReference fileReference = this.buildAction(action);
            if (fileReference != null) {
               items.add(fileReference);
            }
         }

         return items;
      }
   }

   protected List buildActionReferences(List items2) {
      ArrayList items = new ArrayList();
      if (items2 == null) {
         return items;
      } else {
         for(Action action : (Iterable<Action>)(Iterable<?>)(items2)) {
            FileReference fileReference = this.buildAction(action);
            if (fileReference != null) {
               items.add(fileReference);
            }
         }

         return items;
      }
   }

   protected FileReference buildAction(Action action) {
      if (!(action instanceof ExecuteMethodAction)) {
         return null;
      } else {
         ExecuteMethodAction executeMethodAction = (ExecuteMethodAction)action;
         InvokeFile invokeFile = executeMethodAction.getInvokeFile();
         InvokeKnowledgePackage invokeKnowledgePackage = executeMethodAction.getInvokeKnowledgePackage();
         if (invokeKnowledgePackage != null) {
            FileReference fileReference = new FileReference();
            fileReference.setType(ResourceType.Packet);
            fileReference.setId(invokeKnowledgePackage.getId());
            fileReference.setName(invokeKnowledgePackage.getName());
            fileReference.setPathInfo(ResourceType.Packet + ":" + invokeKnowledgePackage.getProject() + "/" + invokeKnowledgePackage.getName());
            return fileReference;
         } else if (invokeFile != null) {
            FileReference action2 = new FileReference();
            RuleFile ruleFile = FileManager.ins.get(invokeFile.getId());
            action2.setId(ruleFile.getId());
            action2.setType(ResourceType.valueOf(ruleFile.getType()));
            action2.setName(ruleFile.getName());
            action2.setPathInfo(ruleFile.getPath());
            return action2;
         } else {
            return null;
         }
      }
   }

   protected FileReference buildLibraryReferences(List items2) {
      if (items2 == null) {
         return null;
      } else {
         FileReference fileReference = new FileReference();
         fileReference.setPathInfo("libs");
         fileReference.setName("libs");
         fileReference.setType(ResourceType.Library);
         ArrayList items = new ArrayList();
         fileReference.setChildren(items);

         for(Library library : (Iterable<Library>)(Iterable<?>)(items2)) {
            RuleFile ruleFile = FileManager.ins.get(library.getId());
            FileReference file = this.buildFile(ruleFile);
            file.setVersion(library.getVersion());
            items.add(file);
         }

         return fileReference;
      }
   }

   protected FileReference buildFile(RuleFile ruleFile) {
      FileReference fileReference = new FileReference();
      fileReference.setId(ruleFile.getId());
      ResourceType resourceType = ResourceType.valueOf(ruleFile.getType());
      fileReference.setType(resourceType);
      fileReference.setName(ruleFile.getName());
      fileReference.setPathInfo(ruleFile.getPath());
      return fileReference;
   }

   protected boolean containsFileReference(List items, Long longValue) {
      if (items == null) {
         return false;
      } else {
         for(Action action : (Iterable<Action>)(Iterable<?>)(items)) {
            if (action instanceof ExecuteMethodAction) {
               ExecuteMethodAction executeMethodAction = (ExecuteMethodAction)action;
               InvokeFile invokeFile = executeMethodAction.getInvokeFile();
               if (invokeFile != null && invokeFile.getId() == longValue) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public static class ReferenceInstances {
      private static final List INSTANCES = new ArrayList();

      static {
         INSTANCES.add(new RuleSetReference());
         INSTANCES.add(new DecisionTableReference());
         INSTANCES.add(new CrosstableReference());
         INSTANCES.add(new ComplexScorecardReference());
         INSTANCES.add(new DecisionTreeReference());
         INSTANCES.add(new RuleflowReference());
         INSTANCES.add(new ScorecardReference());
         INSTANCES.add(new ConditionTemplateReference());
         INSTANCES.add(new ActionTemplateReference());
      }
   }
}
