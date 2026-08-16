package com.bstek.urule.runtime;

import com.bstek.urule.runtime.execution.RuleExecution;
import com.bstek.urule.runtime.log.LogManager;
import com.bstek.urule.runtime.rete.Context;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractWorkingMemory implements WorkingMemory {
   protected LogManager a;
   protected FactManager b;
   protected RuleExecution c;
   protected KnowledgeSession d;
   protected Map<String, Object> e = new HashMap<>();
   protected Map<String, Object> f = new HashMap<>();
   protected Map<String, KnowledgeSession> g = new HashMap<>();

   public Object getPredefineValue(String var1) {
      return this.e.get(var1);
   }

   public void setPredefineValue(String var1, Object var2) {
      this.e.put(var1, var2);
   }

   public Map<String, Object> getPredefineValueMap() {
      return this.e;
   }

   @Override
   public Map<String, Object> getParameters() {
      return this.b.getParameters();
   }

   @Override
   public boolean insert(Object var1) {
      return this.b.insert(var1);
   }

   @Override
   public boolean update(Object var1) {
      this.c.reevaluationRete(var1);
      return true;
   }

   @Override
   public Object getParameter(String var1) {
      return this.b.getParameters().get(var1);
   }

   @Override
   public Map<String, Object> getAllFactsMap() {
      return this.b.getFactMap();
   }

   @Override
   public List<Object> getFactList() {
      return this.b.getFactList();
   }

   @Override
   public KnowledgeSession getKnowledgeSession(String var1) {
      return this.g.get(var1);
   }

   @Override
   public void putKnowledgeSession(String var1, KnowledgeSession var2) {
      if (this.g.containsKey(var1)) {
         this.g.put(var1, var2);
      }
   }

   @Override
   public Object getSessionValue(String var1) {
      return this.f.get(var1);
   }

   @Override
   public void setSessionValue(String var1, Object var2) {
      this.f.put(var1, var2);
   }

   @Override
   public Map<String, Object> getSessionValueMap() {
      return this.f;
   }

   @Override
   public void activeRule(String var1, String var2) {
      this.c.getAgenda().activeMutexGroupRule(var1, var2);
   }

   @Override
   public void activePendedGroup(String var1) {
      this.c.getAgenda().activePendedGroup(var1);
   }

   @Override
   public void activePendedGroupAndExecute(String var1) {
      this.c.getAgenda().activePendedGroupAndExecute(var1);
   }

   @Override
   public Context getContext() {
      return this.c.getAgenda().getContext();
   }

   @Override
   public FactManager getFactManager() {
      return this.b;
   }

   @Override
   public LogManager getLogManager() {
      return this.a;
   }
}
