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

   public boolean exist(Object var1, Long var2) {
      RuleSet var3 = (RuleSet)var1;
      List var4 = var3.getLibraries();
      if (var4 != null) {
         for(Library var6 : (Iterable<Library>)(Iterable<?>)(var4)) {
            if (var6.getId() == var2) {
               return true;
            }
         }
      }

      for(ParentFile var7 : var3.getParents()) {
         if (var7.getId() == var2) {
            return true;
         }
      }

      List var17 = var3.getRules();
      if (var17 != null) {
         for(Rule var8 : (Iterable<Rule>)(Iterable<?>)(var17)) {
            Rhs var9 = var8.getRhs();
            if (var9 != null && this.a(var9.getActions(), var2)) {
               return true;
            }

            Other var10 = var8.getOther();
            if (var10 != null && this.a(var10.getActions(), var2)) {
               return true;
            }

            if (var8 instanceof LoopRule) {
               LoopRule var11 = (LoopRule)var8;
               List var12 = var11.getUnits();
               if (var12 != null) {
                  for(LoopRuleUnit var14 : (Iterable<LoopRuleUnit>)(Iterable<?>)(var12)) {
                     var9 = var14.getRhs();
                     if (var9 != null && this.a(var9.getActions(), var2)) {
                        return true;
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   public boolean existPacket(Object var1, Long var2, String var3) {
      RuleSet var4 = (RuleSet)var1;
      List var5 = var4.getRules();
      if (var5 != null) {
         for(Rule var7 : (Iterable<Rule>)(Iterable<?>)(var5)) {
            Rhs var8 = var7.getRhs();
            if (var8 != null && this.a(var8.getActions(), var2, var3)) {
               return true;
            }

            Other var9 = var7.getOther();
            if (var9 != null && this.a(var9.getActions(), var2, var3)) {
               return true;
            }

            if (var7 instanceof LoopRule) {
               LoopRule var10 = (LoopRule)var7;
               List var11 = var10.getUnits();
               if (var11 != null) {
                  for(LoopRuleUnit var13 : (Iterable<LoopRuleUnit>)(Iterable<?>)(var11)) {
                     var8 = var13.getRhs();
                     if (var8 != null && this.a(var8.getActions(), var2, var3)) {
                        return true;
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   public FileReference build(Object var1) {
      RuleSet var2 = (RuleSet)var1;
      FileReference var3 = new FileReference();
      var3.setType(ResourceType.RuleSet);
      ArrayList var4 = new ArrayList();
      var3.setChildren(var4);
      FileReference var5 = this.b(var2.getLibraries());
      if (var5 != null) {
         var4.add(var5);
      }

      FileReference var6 = new FileReference();
      var6.setPathInfo("Parents");
      ArrayList var7 = new ArrayList();
      var6.setChildren(var7);

      for(ParentFile var10 : var2.getParents()) {
         RuleFile var11 = FileManager.ins.get(var10.getId());
         FileReference var12 = this.a(var11);
         var7.add(var12);
      }

      if (var7.size() > 0) {
         var4.add(var6);
      }

      for(Rule var17 : var2.getRules()) {
         if (var17 instanceof LoopRule) {
            LoopRule var19 = (LoopRule)var17;

            for(LoopRuleUnit var14 : var19.getUnits()) {
               this.a(var4, var14.getRhs());
               Other var15 = var14.getOther();
               if (var15 != null) {
                  var4.addAll(this.a(var15.getActions()));
               }
            }
         } else {
            Rhs var18 = var17.getRhs();
            this.a(var4, var18);
            Other var20 = var17.getOther();
            if (var20 != null) {
               var4.addAll(this.a(var20.getActions()));
            }
         }
      }

      return var3;
   }

   private void a(List var1, Rhs var2) {
      if (var2 != null) {
         List var3 = var2.getActions();
         var1.addAll(this.a(var3));
      }
   }

   public boolean support(Object var1) {
      return var1 instanceof RuleSet;
   }
}
