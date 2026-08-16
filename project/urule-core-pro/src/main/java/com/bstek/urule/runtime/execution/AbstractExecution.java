package com.bstek.urule.runtime.execution;

import com.bstek.urule.model.rule.PredefineExecutionUnit;
import com.bstek.urule.model.rule.PredefineGroup;
import com.bstek.urule.runtime.FactManager;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.KnowledgeSessionImpl;
import com.bstek.urule.runtime.agenda.Agenda;
import com.bstek.urule.runtime.log.LogManager;
import com.bstek.urule.runtime.monitor.MonitorManager;
import com.bstek.urule.runtime.rete.ReteInstance;
import com.bstek.urule.runtime.rete.ReteInstanceUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractExecution {
   protected Agenda a;
   protected FactManager b;
   protected MonitorManager c;
   protected KnowledgeSession d;
   protected List<ReteInstance> e;
   protected List<PredefineExecutionUnit> f;
   private Map<String, List<ReteInstanceUnit>> g = new HashMap<>();
   private Map<String, List<ReteInstanceUnit>> h = new HashMap<>();

   public AbstractExecution(KnowledgeSession var1, Map<String, String> var2) {
      this.d = var1;
      this.b = var1.getFactManager();
      this.a = new Agenda(var1, var2, this.h, this.g);
      this.c = new MonitorManager(var1);
      this.a(var1);
   }

   private void a(KnowledgeSession var1) {
      KnowledgeSession var2 = var1.getParentSession();
      if (var2 != null) {
         this.b(var2);
      }

      this.e = var1.getReteInstanceList();
      this.f = ((KnowledgeSessionImpl)var1).getPredefineExecutionUnits();

      for (PredefineExecutionUnit var4 : this.f) {
         PredefineGroup var5 = var4.getGroup();
         if (var5 != null) {
            this.a(var5);
         }
      }

      this.a(this.e);
   }

   private void a(PredefineGroup var1) {
      KnowledgePackageWrapper var2 = var1.getKnowledgePackageWrapper();
      if (var2 != null) {
         ArrayList var3 = new ArrayList();
         List var4 = var2.getKnowledgePackage().getAloneReteInstances();
         var3.addAll(var4);
         ReteInstance var5 = var2.getKnowledgePackage().loadReteInstance();
         if (var5 != null) {
            var3.add(var5);
         }

         this.a(var3);
      }

      if (var1.getNextGroup() != null) {
         this.a(var1.getNextGroup());
      }
   }

   private void b(KnowledgeSession var1) {
      KnowledgeSession var2 = var1.getParentSession();
      if (var2 != null) {
         this.b(var2);
      }

      LogManager var3 = this.d.getLogManager();

      for (ReteInstance var6 : var1.getReteInstanceList()) {
         var3.addRuleData(var6.getAllRuleData());
         Map var7 = var6.getPendedGroupReteInstancesMap();
         if (var7 != null) {
            this.h.putAll(var7);
         }
      }
   }

   private void a(List<ReteInstance> var1) {
      LogManager var2 = this.d.getLogManager();

      for (ReteInstance var4 : var1) {
         var2.addRuleData(var4.getAllRuleData());
         Map var5 = var4.getPendedGroupReteInstancesMap();
         if (var5 != null) {
            this.h.putAll(var5);
         }

         Map var6 = var4.getMutexGroupReteInstancesMap();
         if (var6 != null) {
            this.g.putAll(var6);
         }
      }
   }

   public Agenda getAgenda() {
      return this.a;
   }

   protected void a() {
      this.a.clean();
      this.b.clean();
   }
}
