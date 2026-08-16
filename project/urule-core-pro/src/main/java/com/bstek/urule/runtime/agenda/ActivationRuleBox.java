package com.bstek.urule.runtime.agenda;

import com.bstek.urule.PropertyConfigurer;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.runtime.rete.Context;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ActivationRuleBox {
   private Context a;
   private Set<Rule> b = new HashSet<>();
   private Set<Rule> c = new HashSet<>();
   private List<Activation> d = new ArrayList<>();

   public ActivationRuleBox(Context var1) {
      this.a = var1;
   }

   public void execute(AgendaFilter var1, int var2) {
      this.c.clear();
      List var3 = this.a();
      int var4 = 0;

      while (var3.size() > 0) {
         for (Activation var6 : (Iterable<Activation>)(Iterable<?>)(var3)) {
            if (var1 == null || var1.accept(var6)) {
               if (var4 >= var2) {
                  break;
               }

               var6.execute(this.a);
               var4++;
            }
         }

         if (var4 >= var2) {
            break;
         }

         var3 = this.a();
      }
   }

   private List<Activation> a() {
      ArrayList var1 = new ArrayList(this.d.size());
      var1.addAll(this.d);
      this.d.clear();
      if (var1.size() > 1) {
         Collections.sort(var1);
      }

      return var1;
   }

   private boolean a(Rule var1, boolean var2) {
      boolean var3 = true;
      if (!var2 && this.b.contains(var1)) {
         if (var1.getLoop() != null && var1.getLoop()) {
            var3 = true;
         } else {
            var3 = false;
         }
      }

      this.a.getLogger().logAddRuleToExecuteQueue(var1, var3);
      return var3;
   }

   public boolean add(Activation var1, boolean var2) {
      Rule var3 = var1.getRule();
      this.c.add(var3);
      if (this.a(var3, var2)) {
         this.b.add(var3);
         return this.d.add(var1);
      } else {
         return false;
      }
   }

   public void addElseRule(Activation var1) {
      Rule var2 = var1.getRule();
      if (!this.c.contains(var2)) {
         boolean var3 = PropertyConfigurer.isEnabledActiveElseRule();
         if (var3 || !this.b.contains(var2)) {
            Rule var4 = var1.convertToElseRule();
            if (!var3 && (var2.getLoop() == null || !var2.getLoop())) {
               if (!this.b.contains(var4)) {
                  this.b.add(var4);
                  this.a.getLogger().logAddRuleToExecuteQueue(var4, true);
                  this.d.add(var1);
               }
            } else {
               this.a.getLogger().logAddRuleToExecuteQueue(var4, true);
               this.d.add(var1);
            }
         }
      }
   }

   public void clean() {
      this.d.clear();
      this.b.clear();
   }
}
