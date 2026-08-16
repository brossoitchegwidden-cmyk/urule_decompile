package com.bstek.urule.builder;

import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.rete.Rete;
import com.bstek.urule.model.rule.PredefineExecutionUnit;
import com.bstek.urule.model.rule.PredefineGroup;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgePackageImpl;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KnowledgeBase {
   private Rete a;
   private List<Rete> b;
   private ResourceLibrary c;
   private Map<String, FlowDefinition> d;
   private List<PredefineExecutionUnit> e;
   private KnowledgePackageImpl f;

   public KnowledgeBase(Rete var1) {
      this(var1, null, null, null);
   }

   public KnowledgeBase(Rete var1, List<Rete> var2, Map<String, FlowDefinition> var3, List<PredefineExecutionUnit> var4) {
      this.a = var1;
      this.d = var3;
      this.b = var2;
      this.c = var1.getResourceLibrary();
      if (var4 != null) {
         Collections.sort(var4);
      }

      this.e = var4;
   }

   public KnowledgePackage getKnowledgePackage() {
      if (this.f != null) {
         return this.f;
      }

      this.f = new KnowledgePackageImpl();
      this.f.setRete(this.a);
      this.f.setAloneRetes(this.b);
      this.f.setFlowMap(this.d);
      this.f.setPredefineExecutionUnits(this.e);
      HashMap var1 = new HashMap();
      HashMap var2 = new HashMap();
      this.f.setVariableCategoryMap(var2);
      List var3 = this.c.getVariableCategories();
      this.f.setVariableCategories(var3);
      this.f.setVariableCategoryWithDefaultValueClassMap(var1);
      HashMap var4 = new HashMap();
      this.f.setParameters(var4);

      for (VariableCategory var6 : (Iterable<VariableCategory>)(Iterable<?>)(var3)) {
         String var7 = var6.getName();
         String var8 = var6.getClazz();
         var1.put(var8, var6.newVariableCategoryWithDefaultValue());
         var2.put(var7, var6.getClazz());
         if (var7.equals("参数")) {
            List var9 = var6.getVariables();
            if (var9 != null && var9.size() != 0) {
               for (Variable var11 : (Iterable<Variable>)(Iterable<?>)(var9)) {
                  var4.put(var11.getName(), var11.getType().name());
               }
            }
         }
      }

      return this.f;
   }

   public Rete getPredefineRete() {
      if (this.e != null && this.e.size() != 0) {
         PredefineExecutionUnit var1 = this.e.get(0);
         PredefineGroup var2 = var1.getGroup();
         if (var2 == null) {
            return null;
         }

         PredefineGroup var3;
         for (var3 = var2; var2.getNextGroup() != null; var3 = var2) {
            var2 = var2.getNextGroup();
         }

         return var3 == null ? null : var3.getKnowledgePackageWrapper().getKnowledgePackage().getRete();
      } else {
         return null;
      }
   }

   public Rete getRete() {
      return this.a;
   }

   public ResourceLibrary getResourceLibrary() {
      return this.c;
   }

   public Map<String, FlowDefinition> getFlowMap() {
      return this.d;
   }
}
