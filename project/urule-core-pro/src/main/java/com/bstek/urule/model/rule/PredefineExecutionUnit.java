package com.bstek.urule.model.rule;

import com.bstek.urule.builder.KnowledgeBase;
import com.bstek.urule.builder.KnowledgeBuilder;
import com.bstek.urule.builder.rete.ReteBuilder;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.rete.Rete;
import com.bstek.urule.model.rete.ReteUnit;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PredefineExecutionUnit implements Comparable<PredefineExecutionUnit> {
   private int priority;
   private boolean containsRules;
   private PredefineGroup group;

   public PredefineExecutionUnit() {
   }

   public PredefineExecutionUnit(PredefineGroupDefinition var1, List<Rule> var2, boolean var3) {
      this.priority = var1.getPriority();
      this.init(var1, var2, var3);
   }

   public void buildRete(ResourceLibrary var1, KnowledgeBuilder var2, Map<String, List<ReteUnit>> var3) {
      this.buildGroupRete(var1, this.group, var2, var3);
   }

   private void buildGroupRete(ResourceLibrary var1, PredefineGroup var2, KnowledgeBuilder var3, Map<String, List<ReteUnit>> var4) {
      ReteBuilder var5 = var3.getReteBuilder();
      List var6 = var2.getRules();
      var3.buildRules(var6);
      var3.buildLoopRules(var6, var1);
      var3.buildRulesConditionActionTemplate(var6, var1);
      ArrayList var7 = new ArrayList();
      if (var6.size() > 0) {
         if (var2.isAlone()) {
            Collections.sort(var6);

            for (Rule var9 : (Iterable<Rule>)(Iterable<?>)(var6)) {
               Rete var10 = var5.buildRete(var9, var1);
               var7.add(var10);
            }

            var6.clear();
         }

         Rete var11 = var5.buildRete(var6, var1);
         if (var11.getPendedGroupRetesMap() != null) {
            var4.putAll(var11.getPendedGroupRetesMap());
         }

         var6.clear();
         KnowledgeBase var13 = new KnowledgeBase(var11, var7, null, null);
         KnowledgePackageWrapper var14 = new KnowledgePackageWrapper(var13.getKnowledgePackage());
         var2.resetKnowledgePackageWrapper(var14);
      }

      for (PredefineGroup var12 = var2.getNextGroup(); var12 != null; var12 = var12.getNextGroup()) {
         this.buildGroupRete(var1, var12, var3, var4);
      }
   }

   private void init(PredefineGroupDefinition var1, List<Rule> var2, boolean var3) {
      if (var1 != null && var2.size() != 0) {
         PredefineGroup var4 = null;
         PredefineGroup var5 = null;

         for (Predefine var7 : var1.getPredefines()) {
            PredefineGroup var8 = new PredefineGroup(var3);
            if (var4 == null) {
               var4 = var8;
            }

            var8.setFilePath(var1.getFilePath());
            var8.setPredefine(var7);
            if (var5 != null) {
               var5.setNextGroup(var8);
            }

            var5 = var8;
         }

         if (var5 != null) {
            var5.getRules().addAll(var2);
            var2.clear();
            this.containsRules = true;
            this.group = var4;
         }
      }
   }

   public int compareTo(PredefineExecutionUnit var1) {
      int var2 = var1.getPriority();
      return var2 - this.priority;
   }

   public int getPriority() {
      return this.priority;
   }

   public void setPriority(int var1) {
      this.priority = var1;
   }

   public PredefineGroup getGroup() {
      return this.group;
   }

   public void setContainsRules(boolean var1) {
      this.containsRules = var1;
   }

   public boolean isContainsRules() {
      return this.containsRules;
   }
}
