package com.bstek.urule.runtime;

import com.bstek.urule.Utils;
import com.bstek.urule.model.GeneralEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FactManager {
   private FactManager a;
   private List<Object> b = new ArrayList<>();
   private Map<String, Object> c = new HashMap<>();
   private Map<String, Object> d = new HashMap<>();
   private Map<String, Object> e = new HashMap<>();
   private Map<String, Object> f = new HashMap<>();
   private Map<String, List<Object>> g = new HashMap<>();

   public FactManager(KnowledgeSession var1) {
      if (var1 != null) {
         for (Object var3 : var1.getFactList()) {
            if (!var3.getClass().getName().equals(HashMap.class.getName())) {
               this.b.add(var3);
            }
         }

         this.g.putAll(var1.getFactManager().g);
         this.c.putAll(var1.getAllFactsMap());
         this.a = var1.getFactManager();
      }
   }

   public void initKnowledgePackageParameters(KnowledgePackage var1) {
      ParameterManager.getInstance().initKnowledgePackageParameters(var1, this.e);
   }

   public Map<String, Object> buildRuntimeParameters(Map<String, Object> var1) {
      this.d.clear();
      ParameterManager.getInstance().clearInitParameters(this.e);
      this.d.putAll(this.e);
      this.d.putAll(this.f);
      if (var1 != null) {
         for (String var3 : var1.keySet()) {
            if (!var3.equals("_loop_rule_break_tag__")) {
               this.d.put(var3, var1.get(var3));
            }
         }
      }

      this.addToFactsMap(this.d);
      return this.d;
   }

   public boolean insert(Object var1) {
      if (this.a != null) {
         this.a.insert(var1);
      }

      if (!(var1 instanceof GeneralEntity) && var1 instanceof Map) {
         Map var2 = (Map)var1;

         for (Object var4 : var2.keySet()) {
            if (var4 != null) {
               this.f.put(var4.toString(), var2.get(var4));
            }
         }

         return false;
      } else {
         this.addToFactsMap(var1);
         return true;
      }
   }

   public void insertLoopFact(Object var1) {
      if (!(var1 instanceof GeneralEntity) && var1 instanceof Map) {
         Map var2 = (Map)var1;

         for (Object var4 : var2.keySet()) {
            if (var4 != null) {
               this.f.put(var4.toString(), var2.get(var4));
            }
         }
      } else {
         this.addToFactsMap(var1);
      }
   }

   public void addToFactsMap(Object var1) {
      boolean var2 = false;

      for (Object var4 : this.b) {
         if (var4 == var1) {
            var2 = true;
            break;
         }
      }

      if (!var2) {
         this.b.add(var1);
      }

      String var5 = Utils.getClassName(var1);
      this.a(var1, var5);
      this.c.put(var5, var1);
   }

   private void a(Object var1, String var2) {
      List var3 = this.g.get(var2);
      if (var3 == null) {
         var3 = new ArrayList();
         this.g.put(var2, var3);
      }

      var3.add(var1);
   }

   public void clean() {
      this.c.clear();
      this.b.clear();
      this.g.clear();
      this.f.clear();
   }

   public Map<String, Object> getParameters() {
      return this.d;
   }

   public Map<String, Object> getFactMap() {
      return this.c;
   }

   public List<Object> getFacts(String var1) {
      return this.g.get(var1);
   }

   public Map<String, List<Object>> getFactListMap() {
      return this.g;
   }

   public List<Object> getFactList() {
      ArrayList var1 = new ArrayList(this.b.size());
      var1.addAll(this.b);
      return var1;
   }
}
