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
   private Context context;
   private Set<Rule> executedRules = new HashSet<>();
   private Set<Rule> currentCycleRules = new HashSet<>();
   private List<Activation> activations = new ArrayList<>();

   public ActivationRuleBox(Context context) {
      this.context = context;
   }

   public void execute(AgendaFilter filter, int max) {
      this.currentCycleRules.clear();
      List items = this.drainActivations();
      int number = 0;

      while (items.size() > 0) {
         for (Activation activation : (Iterable<Activation>)(Iterable<?>)(items)) {
            if (filter == null || filter.accept(activation)) {
               if (number >= max) {
                  break;
               }

               activation.execute(this.context);
               number++;
            }
         }

         if (number >= max) {
            break;
         }

         items = this.drainActivations();
      }
   }

   private List<Activation> drainActivations() {
      ArrayList items = new ArrayList(this.activations.size());
      items.addAll(this.activations);
      this.activations.clear();
      if (items.size() > 1) {
         Collections.sort(items);
      }

      return items;
   }

   private boolean canActivate(Rule rule, boolean flag) {
      boolean flag2 = true;
      if (!flag && this.executedRules.contains(rule)) {
         if (rule.getLoop() != null && rule.getLoop()) {
            flag2 = true;
         } else {
            flag2 = false;
         }
      }

      this.context.getLogger().logAddRuleToExecuteQueue(rule, flag2);
      return flag2;
   }

   public boolean add(Activation activation, boolean prime) {
      Rule rule = activation.getRule();
      this.currentCycleRules.add(rule);
      if (this.canActivate(rule, prime)) {
         this.executedRules.add(rule);
         return this.activations.add(activation);
      } else {
         return false;
      }
   }

   public void addElseRule(Activation activation) {
      Rule rule = activation.getRule();
      if (!this.currentCycleRules.contains(rule)) {
         boolean flag = PropertyConfigurer.isEnabledActiveElseRule();
         if (flag || !this.executedRules.contains(rule)) {
            Rule rule2 = activation.convertToElseRule();
            if (!flag && (rule.getLoop() == null || !rule.getLoop())) {
               if (!this.executedRules.contains(rule2)) {
                  this.executedRules.add(rule2);
                  this.context.getLogger().logAddRuleToExecuteQueue(rule2, true);
                  this.activations.add(activation);
               }
            } else {
               this.context.getLogger().logAddRuleToExecuteQueue(rule2, true);
               this.activations.add(activation);
            }
         }
      }
   }

   public void clean() {
      this.activations.clear();
      this.executedRules.clear();
   }
}
