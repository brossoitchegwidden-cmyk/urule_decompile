package com.bstek.urule.runtime;

import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.exception.RuleExecutionException;
import com.bstek.urule.model.GeneralEntity;
import com.bstek.urule.model.rule.PredefineExecutionUnit;
import com.bstek.urule.runtime.agenda.AgendaFilter;
import com.bstek.urule.runtime.execution.RuleExecution;
import com.bstek.urule.runtime.log.LogManager;
import com.bstek.urule.runtime.response.FlowExecutionResponse;
import com.bstek.urule.runtime.response.RuleExecutionResponse;
import com.bstek.urule.runtime.rete.ReteInstance;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KnowledgeSessionImpl extends AbstractWorkingMemory implements KnowledgeSession {
   private List<ReteInstance> h = new ArrayList<>();
   private List<PredefineExecutionUnit> i = new ArrayList<>();
   private List<KnowledgePackage> j = new ArrayList<>();

   protected KnowledgeSessionImpl(KnowledgePackage var1, boolean var2, long var3) {
      this(new KnowledgePackage[]{var1}, null, var2, var3);
   }

   protected KnowledgeSessionImpl(KnowledgePackage var1, KnowledgeSession var2, boolean var3, long var4) {
      this(new KnowledgePackage[]{var1}, var2, var3, var4);
   }

   protected KnowledgeSessionImpl(KnowledgePackage[] var1, KnowledgeSession var2, boolean var3, long var4) {
      this.a = new LogManager(var2);
      this.b = new FactManager(var2);
      HashMap var6 = new HashMap();

      for (KnowledgePackage var10 : var1) {
         this.a(var10);
         var6.putAll(var10.getVariableCateogoryMap());
      }

      this.initFromParentSession(var2);
      this.c = new RuleExecution(this, var6, var3, var4);
   }

   private void a(KnowledgePackage var1) {
      this.j.add(var1);
      if (var1.getPredefineExecutionUnits() != null) {
         this.i.addAll(var1.getPredefineExecutionUnits());
      }

      this.h.addAll(var1.getAloneReteInstances());
      this.h.add(var1.loadReteInstance());
      this.b.initKnowledgePackageParameters(var1);
   }

   @Override
   public void initFromParentSession(KnowledgeSession var1) {
      if (var1 != null) {
         this.d = var1;
         this.g = var1.getKnowledgeSessionMap();
         this.f.putAll(var1.getSessionValueMap());
         AbstractWorkingMemory var2 = (AbstractWorkingMemory)var1;
         this.e = var2.getPredefineValueMap();
      }
   }

   @Override
   public boolean insert(Object var1) {
      boolean var2 = super.insert(var1);
      if (var1 instanceof GeneralEntity) {
         GeneralEntity var3 = (GeneralEntity)var1;
         Utils.assignVariableObjectDefaultValue(var3, this);
      }

      return var2;
   }

   @Override
   public RuleExecutionResponse fireRules() {
      return this.a(null, null, Integer.MAX_VALUE);
   }

   @Override
   public RuleExecutionResponse fireRules(int var1) {
      return this.a(null, null, var1);
   }

   @Override
   public RuleExecutionResponse fireRules(AgendaFilter var1) {
      return this.a(var1, null, Integer.MAX_VALUE);
   }

   @Override
   public RuleExecutionResponse fireRules(AgendaFilter var1, int var2) {
      return this.a(var1, null, var2);
   }

   @Override
   public RuleExecutionResponse fireRules(Map<String, Object> var1) {
      return this.a(null, var1, Integer.MAX_VALUE);
   }

   @Override
   public RuleExecutionResponse fireRules(Map<String, Object> var1, AgendaFilter var2) {
      return this.a(var2, var1, Integer.MAX_VALUE);
   }

   @Override
   public RuleExecutionResponse fireRules(Map<String, Object> var1, AgendaFilter var2, int var3) {
      return this.a(var2, var1, var3);
   }

   @Override
   public RuleExecutionResponse fireRules(Map<String, Object> var1, int var2) {
      return this.a(null, var1, var2);
   }

   @Override
   public FlowExecutionResponse startProcess(String var1) {
      return this.startProcess(var1, null);
   }

   @Override
   public FlowExecutionResponse startProcess(String var1, Map<String, Object> var2) {
      return this.c.startProcess(var1, var2);
   }

   private RuleExecutionResponse a(AgendaFilter var1, Map<String, Object> var2, int var3) {
      try {
         return this.c.fireRules(var1, var2, var3);
      } catch (RuleExecutionException var5) {
         throw new RuleException(var5.getMessage());
      }
   }

   @Override
   public List<KnowledgePackage> getKnowledgePackageList() {
      return this.j;
   }

   @Override
   public void writeLogFile() throws IOException {
      this.a.writeLog();
   }

   @Override
   public Map<String, KnowledgeSession> getKnowledgeSessionMap() {
      return this.g;
   }

   @Override
   public KnowledgeSession getParentSession() {
      return this.d;
   }

   @Override
   public List<ReteInstance> getReteInstanceList() {
      return this.h;
   }

   public List<PredefineExecutionUnit> getPredefineExecutionUnits() {
      return this.i;
   }
}
