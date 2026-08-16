package com.bstek.urule.runtime.agenda;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.runtime.FactManager;
import com.bstek.urule.runtime.WorkingMemory;
import com.bstek.urule.runtime.rete.Context;
import com.bstek.urule.runtime.rete.ContextImpl;
import com.bstek.urule.runtime.rete.EvaluationContext;
import com.bstek.urule.runtime.rete.EvaluationContextImpl;
import com.bstek.urule.runtime.rete.FactTracker;
import com.bstek.urule.runtime.rete.MutexReteInstanceUnit;
import com.bstek.urule.runtime.rete.ReteInstance;
import com.bstek.urule.runtime.rete.ReteInstanceUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class Agenda {
   private ActivationRuleBox a;
   private Context b;
   private FactManager c;
   private EvaluationContextImpl d;
   private Map<String, List<ReteInstanceUnit>> e;
   private Map<String, List<ReteInstanceUnit>> f;

   public Agenda(WorkingMemory var1, Map<String, String> var2, Map<String, List<ReteInstanceUnit>> var3, Map<String, List<ReteInstanceUnit>> var4) {
      this.c = var1.getFactManager();
      this.f = var3;
      this.e = var4;
      this.b = new ContextImpl(var1, var2);
      this.a = new ActivationRuleBox(this.b);
      this.d = new EvaluationContextImpl(var1, var2);
   }

   public Collection<FactTracker> doRete(ReteInstance var1, Object var2, boolean var3) {
      Collection var4 = var1.enter(this.d, var2);
      this.a(var4, var2, var3);
      return var4;
   }

   public void execute(ReteInstance var1, AgendaFilter var2, int var3) {
      this.a.execute(var2, var3);
      this.a.clean();
      this.d.resetActivitiesState();
   }

   public void reEvaluationExecute(ReteInstance var1) {
      this.a.execute(null, Integer.MAX_VALUE);
   }

   public void activePendedGroupAndExecute(String var1) {
      this.a(var1, true);
   }

   public void activePendedGroup(String var1) {
      this.a(var1, false);
   }

   private void a(String var1, boolean var2) {
      if (!this.f.containsKey(var1)) {
         throw new RuleException("执行组 [" + var1 + "] 不存在！");
      }

      this.b.cleanTipMsg();
      this.d.resetActivitiesState();
      List var3 = this.c.getFactList();

      for (ReteInstanceUnit var6 : this.f.get(var1)) {
         if (this.a(var6)) {
            if (var6 instanceof MutexReteInstanceUnit) {
               MutexReteInstanceUnit var10 = (MutexReteInstanceUnit)var6;
               this.b.addTipMsg("执行执行组[" + var1 + "]下的互斥组:" + var10.getMutexGroupName() + "");
               this.a(var10, var3);
            } else {
               ReteInstance var7 = var6.getReteInstance();

               for (Object var9 : var3) {
                  this.doRete(var7, var9, true);
               }

               this.doRete(var7, "__*__", true);
            }
         }
      }

      if (var2) {
         this.a.execute(null, Integer.MAX_VALUE);
      }
   }

   private void a(MutexReteInstanceUnit var1, List<Object> var2) {
      this.d.resetActivitiesState();

      for (ReteInstance var5 : var1.getReteInstances()) {
         Collection var6 = null;

         for (Object var8 : var2) {
            var6 = this.doRete(var5, var8, true);
            if (var6 != null && var6.size() > 0) {
               break;
            }
         }

         if (var6 == null || var6.size() == 0) {
            var6 = this.doRete(var5, "__*__", true);
         }

         if (var6 != null && var6.size() != 0) {
            break;
         }
      }
   }

   public void activeMutexGroupRule(String var1, String var2) {
      if (StringUtils.isBlank(var1)) {
         throw new RuleException("互斥组名不能为空，当前操作只能发生在互斥组内部！");
      }

      if (!this.e.containsKey(var1)) {
         throw new RuleException("互斥组 [" + var1 + "] 不存在!");
      }

      this.d.resetActivitiesState();
      List var3 = this.e.get(var1);
      List var4 = this.c.getFactList();

      for (ReteInstanceUnit var6 : (Iterable<ReteInstanceUnit>)(Iterable<?>)(var3)) {
         String var7 = var6.getRuleName();
         if (var7.equals(var2) && this.a(var6)) {
            ReteInstance var8 = var6.getReteInstance();

            for (Object var10 : var4) {
               this.doRete(var8, var10, false);
            }

            this.doRete(var8, "__*__", false);
            break;
         }
      }
   }

   private boolean a(ReteInstanceUnit var1) {
      Date var2 = new Date();
      Date var3 = var1.getEffectiveDate();
      if (var3 != null && var3.compareTo(var2) < 0) {
         return false;
      }

      Date var4 = var1.getExpiresDate();
      return var4 == null || var4.compareTo(var2) <= 0;
   }

   private void a(Collection<FactTracker> var1, Object var2, boolean var3) {
      if (var1 != null) {
         for (FactTracker var5 : var1) {
            Activation var6 = var5.getActivation();
            Rule var7 = var6.getRule();
            if (var2.equals("__*__") && var7.isWithElse()) {
               this.a.addElseRule(var6);
            } else {
               this.a.add(var6, var3);
            }
         }
      }
   }

   public Context getContext() {
      return this.b;
   }

   public EvaluationContext getEvaluationContext() {
      return this.d;
   }

   public void clean() {
      this.a.clean();
   }
}
