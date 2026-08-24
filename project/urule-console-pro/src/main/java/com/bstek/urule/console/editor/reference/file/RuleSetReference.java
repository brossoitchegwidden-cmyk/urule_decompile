package com.bstek.urule.console.editor.reference.file;

import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.editor.reference.FileReference;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.Other;
import com.bstek.urule.model.rule.ParentFile;
import com.bstek.urule.model.rule.Rhs;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.RuleSet;
import com.bstek.urule.model.rule.loop.LoopRule;
import com.bstek.urule.model.rule.loop.LoopRuleUnit;
import java.util.ArrayList;
import java.util.List;

public class RuleSetReference extends PacketSupportReference {
   protected RuleSetReference() {
   }

   public boolean exist(Object obj, Long fileId) {
      RuleSet ruleSet = (RuleSet)obj;
      List libraries = ruleSet.getLibraries();
      if (libraries != null) {
         for(Library library : (Iterable<Library>)(Iterable<?>)(libraries)) {
            if (library.getId() == fileId) {
               return true;
            }
         }
      }

      for(ParentFile parentFile : ruleSet.getParents()) {
         if (parentFile.getId() == fileId) {
            return true;
         }
      }

      List rules = ruleSet.getRules();
      if (rules != null) {
         for(Rule rule : (Iterable<Rule>)(Iterable<?>)(rules)) {
            Rhs rhs = rule.getRhs();
            if (rhs != null && this.containsFileReference(rhs.getActions(), fileId)) {
               return true;
            }

            Other other = rule.getOther();
            if (other != null && this.containsFileReference(other.getActions(), fileId)) {
               return true;
            }

            if (rule instanceof LoopRule) {
               LoopRule loopRule = (LoopRule)rule;
               List units = loopRule.getUnits();
               if (units != null) {
                  for(LoopRuleUnit loopRuleUnit : (Iterable<LoopRuleUnit>)(Iterable<?>)(units)) {
                     rhs = loopRuleUnit.getRhs();
                     if (rhs != null && this.containsFileReference(rhs.getActions(), fileId)) {
                        return true;
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   public boolean existPacket(Object obj, Long packetId, String code) {
      RuleSet ruleSet = (RuleSet)obj;
      List rules = ruleSet.getRules();
      if (rules != null) {
         for(Rule rule : (Iterable<Rule>)(Iterable<?>)(rules)) {
            Rhs rhs = rule.getRhs();
            if (rhs != null && this.containsPacketReference(rhs.getActions(), packetId, code)) {
               return true;
            }

            Other other = rule.getOther();
            if (other != null && this.containsPacketReference(other.getActions(), packetId, code)) {
               return true;
            }

            if (rule instanceof LoopRule) {
               LoopRule loopRule = (LoopRule)rule;
               List units = loopRule.getUnits();
               if (units != null) {
                  for(LoopRuleUnit loopRuleUnit : (Iterable<LoopRuleUnit>)(Iterable<?>)(units)) {
                     rhs = loopRuleUnit.getRhs();
                     if (rhs != null && this.containsPacketReference(rhs.getActions(), packetId, code)) {
                        return true;
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   public FileReference build(Object obj) {
      RuleSet ruleSet = (RuleSet)obj;
      FileReference fileReference = new FileReference();
      fileReference.setType(ResourceType.RuleSet);
      ArrayList items = new ArrayList();
      fileReference.setChildren(items);
      FileReference fileReference2 = this.buildLibraryReferences(ruleSet.getLibraries());
      if (fileReference2 != null) {
         items.add(fileReference2);
      }

      FileReference fileReference3 = new FileReference();
      fileReference3.setPathInfo("Parents");
      ArrayList items2 = new ArrayList();
      fileReference3.setChildren(items2);

      for(ParentFile parentFile : ruleSet.getParents()) {
         RuleFile ruleFile = FileManager.ins.get(parentFile.getId());
         FileReference file = this.buildFile(ruleFile);
         items2.add(file);
      }

      if (items2.size() > 0) {
         items.add(fileReference3);
      }

      for(Rule rule : ruleSet.getRules()) {
         if (rule instanceof LoopRule) {
            LoopRule loopRule = (LoopRule)rule;

            for(LoopRuleUnit loopRuleUnit : loopRule.getUnits()) {
               this.appendRhsActionReferences(items, loopRuleUnit.getRhs());
               Other other = loopRuleUnit.getOther();
               if (other != null) {
                  items.addAll(this.buildActionReferences(other.getActions()));
               }
            }
         } else {
            Rhs rhs = rule.getRhs();
            this.appendRhsActionReferences(items, rhs);
            Other other2 = rule.getOther();
            if (other2 != null) {
               items.addAll(this.buildActionReferences(other2.getActions()));
            }
         }
      }

      return fileReference;
   }

   private void appendRhsActionReferences(List items, Rhs rhs) {
      if (rhs != null) {
         List actions = rhs.getActions();
         items.addAll(this.buildActionReferences(actions));
      }
   }

   public boolean support(Object obj) {
      return obj instanceof RuleSet;
   }
}
