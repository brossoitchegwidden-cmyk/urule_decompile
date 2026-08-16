package com.bstek.urule.runtime.execution;

import com.bstek.urule.Utils;
import com.bstek.urule.action.WorkingMemoryHolderAdapter;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.exception.RuleExecutionException;
import com.bstek.urule.model.rule.Predefine;
import com.bstek.urule.model.rule.PredefineExecutionUnit;
import com.bstek.urule.model.rule.PredefineGroup;
import com.bstek.urule.model.rule.PredefineValueType;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.VariableCategoryValue;
import com.bstek.urule.model.rule.loop.LoopObjectThreadLocal;
import com.bstek.urule.runtime.AbstractWorkingMemory;
import com.bstek.urule.runtime.DynamicSpringConfigLoaderImpl;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.agenda.AgendaFilter;
import com.bstek.urule.runtime.response.ExecutionResponseImpl;
import com.bstek.urule.runtime.response.RuleExecutionResponse;
import com.bstek.urule.runtime.rete.EvaluationContext;
import com.bstek.urule.runtime.rete.ReteInstance;
import com.bstek.urule.runtime.rete.ReteInstanceUnit;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleExecution extends FlowExecution {
   private boolean g;
   private long h;
   private List<String> i = new ArrayList<>();
   private static String j = "";

   public RuleExecution(KnowledgeSession var1, Map<String, String> var2, boolean var3, long var4) {
      super(var1, var2);
      this.h = var4;
      this.g = var3;
   }

   public RuleExecutionResponse fireRules(AgendaFilter var1, Map<String, Object> var2, int var3) {
      this.a.getEvaluationContext().reset();
      this.d.getLogManager().clean();
      Map var4 = this.b.buildRuntimeParameters(var2);
      this.c.doMonitorInputData(var4);

      try {
         WorkingMemoryHolderAdapter.set(this.d);
         RuleExecutionResponse var5 = this.a(var1, var3);
         this.c.setTotalDuration(var5.getDuration());
         this.c.doMonitor(var4);
         this.a();
         return var5;
      } finally {
         WorkingMemoryHolderAdapter.clean();
      }
   }

   public RuleExecutionResponse reevaluationRete(Object var1) {
      this.b.addToFactsMap(var1);
      ArrayList var2 = new ArrayList();
      var2.add(var1);
      return this.a(var2);
   }

   private RuleExecutionResponse a(AgendaFilter var1, int var2) {
      if (DynamicSpringConfigLoaderImpl.getLimit() != this.h) {
         throw new RuntimeException(new String(Base64.getDecoder().decode("RW52aXJvbm1lbnQgaXMgYnJva2Vu")));
      }

      if (this.g && DynamicSpringConfigLoaderImpl.getAuthInfo() != null) {
         if (DynamicSpringConfigLoaderImpl.getLimit() >= 0L) {
            long var8 = new Date().getTime();
            if (var8 > DynamicSpringConfigLoaderImpl.getLimit()) {
               throw new RuleExecutionException(j);
            }
         }
      } else {
         long var3 = new Date().getTime();
         if (var3 > DynamicSpringConfigLoaderImpl.getTrialExpired()) {
            throw new RuleExecutionException(j);
         }
      }

      long var9 = System.currentTimeMillis();
      ExecutionResponseImpl var5 = new ExecutionResponseImpl();

      for (PredefineExecutionUnit var7 : this.f) {
         if (var7.getGroup() != null) {
            this.a(var7.getGroup(), var1, var2);
         }
      }

      for (ReteInstance var11 : this.e) {
         this.a(var1, var2, var11);
      }

      var5.setDuration(System.currentTimeMillis() - var9);
      return var5;
   }

   private String a(PredefineGroup var1, Predefine var2) {
      return "规则文件【" + var1.getFilePath() + "】中的预定义对象【" + var2.getName() + "】";
   }

   private void a(PredefineGroup var1, AgendaFilter var2, int var3) {
      if (var1 != null) {
         AbstractWorkingMemory var4 = (AbstractWorkingMemory)this.d;
         HashMap var5 = new HashMap<>(this.b.getFactMap());
         Predefine var6 = var1.getPredefine();
         PredefineValueType var7 = var6.getValueType();
         EvaluationContext var8 = this.a.getEvaluationContext();
         Value var9 = var6.getValue();
         if (var7.equals(PredefineValueType.from)) {
            if (var9 == null) {
               throw new RuleException(this.a(var1, var6) + "值未定义");
            }

            try {
               Object var10 = var8.getValueCompute().complexValueCompute(var9, var8, this.b.getFactMap());
               var4.setPredefineValue(var6.getUuid(), var10);
               this.a(var2, var3, var1);
            } catch (Exception var14) {
               if (var14.getCause() instanceof InvocationTargetException) {
                  InvocationTargetException var11 = (InvocationTargetException)var14.getCause();
                  throw new RuleException(this.a(var1, var6) + "赋值失败:" + var11.getTargetException().getMessage());
               }

               throw new RuleException(this.a(var1, var6) + "赋值失败:" + var14.getMessage());
            }
         } else if (var7.equals(PredefineValueType.in)) {
            if (var9 == null) {
               throw new RuleException(this.a(var1, var6) + "值未定义");
            }

            for (Object var12 : this.a(var1, var6, var9, var8)) {
               var4.setPredefineValue(var6.getUuid(), var12);
               LoopObjectThreadLocal.setLoopObject(var12);
               this.b.insertLoopFact(var12);
               if (var6.withConditions()) {
                  String var13 = Utils.getClassName(var12);
                  var5.put(var13, var12);
                  if (!var6.evalCriterias(var8, var5)) {
                     continue;
                  }
               }

               this.a(var2, var3, var1);
               LoopObjectThreadLocal.clean();
            }
         } else {
            this.a(var2, var3, var1);
         }
      }
   }

   private void a(AgendaFilter var1, int var2, PredefineGroup var3) {
      KnowledgePackageWrapper var4 = var3.getKnowledgePackageWrapper();
      if (var4 != null) {
         KnowledgePackage var5 = var4.getKnowledgePackage();
         if (var5.getAloneReteInstances().size() == 0) {
            ReteInstance var6 = var5.loadReteInstance();
            this.a(var1, var2, var6);
         } else {
            for (ReteInstance var7 : var5.getAloneReteInstances()) {
               this.a(var1, var2, var7);
            }
         }
      }

      this.a(var3.getNextGroup(), var1, var2);
   }

   private void a(AgendaFilter var1, int var2, ReteInstance var3) {
      List var4 = this.b.getFactList();

      for (Object var6 : var4) {
         this.a.doRete(var3, var6, true);
      }

      this.a.doRete(var3, "__*__", true);
      this.a((Collection<Object>)var4, var3);
      this.a.execute(var3, var1, var2);
      this.i.clear();
   }

   private RuleExecutionResponse a(List<Object> var1) {
      long var2 = System.currentTimeMillis();
      ExecutionResponseImpl var4 = new ExecutionResponseImpl();

      for (PredefineExecutionUnit var6 : this.f) {
         if (var6.getGroup() != null) {
            this.a(var6.getGroup(), var1);
         }
      }

      for (ReteInstance var8 : this.e) {
         this.a(var1, var8);
      }

      var4.setDuration(System.currentTimeMillis() - var2);
      return var4;
   }

   private void a(PredefineGroup var1, List<Object> var2) {
      if (var1 != null) {
         AbstractWorkingMemory var3 = (AbstractWorkingMemory)this.d;
         HashMap var4 = new HashMap<>(this.b.getFactMap());
         Predefine var5 = var1.getPredefine();
         PredefineValueType var6 = var5.getValueType();
         EvaluationContext var7 = this.a.getEvaluationContext();
         Value var8 = var5.getValue();
         if (var6.equals(PredefineValueType.from)) {
            if (var8 == null) {
               throw new RuleException(this.a(var1, var5) + "值未定义");
            }

            Object var9 = var7.getValueCompute().complexValueCompute(var8, var7, this.b.getFactMap());
            var3.setPredefineValue(var5.getUuid(), var9);
            this.a(var2, var1);
         } else if (var6.equals(PredefineValueType.in)) {
            if (var8 == null) {
               throw new RuleException(this.a(var1, var5) + "值未定义");
            }

            for (Object var11 : this.a(var1, var5, var8, var7)) {
               var3.setPredefineValue(var5.getUuid(), var11);
               LoopObjectThreadLocal.setLoopObject(var11);
               this.b.insertLoopFact(var11);
               if (var5.withConditions()) {
                  String var12 = Utils.getClassName(var11);
                  var4.put(var12, var11);
                  if (!var5.evalCriterias(var7, var4)) {
                     continue;
                  }
               }

               this.a(var2, var1);
               LoopObjectThreadLocal.clean();
            }
         } else {
            this.a(var2, var1);
         }
      }
   }

   private void a(List<Object> var1, PredefineGroup var2) {
      KnowledgePackageWrapper var3 = var2.getKnowledgePackageWrapper();
      if (var3 != null) {
         KnowledgePackage var4 = var3.getKnowledgePackage();
         if (var4.getAloneReteInstances().size() == 0) {
            ReteInstance var5 = var4.loadReteInstance();
            this.a(var1, var5);
         } else {
            for (ReteInstance var6 : var4.getAloneReteInstances()) {
               this.a(var1, var6);
            }
         }
      }

      this.a(var2.getNextGroup(), var1);
   }

   private Collection<?> a(PredefineGroup var1, Predefine var2, Value var3, EvaluationContext var4) {
      if (var3.getArithmetic() == null && var3 instanceof VariableCategoryValue) {
         VariableCategoryValue var9 = (VariableCategoryValue)var3;
         String var10 = var9.getVariableCategory();
         String var7 = var4.getVariableCategoryClass(var10);
         List var8 = this.b.getFacts(var7);
         if (var8 == null) {
            throw new RuleException("当前工作区中不存在【" + var7 + "】类型的对象");
         } else {
            return var8;
         }
      } else {
         Object var5 = var4.getValueCompute().complexValueCompute(var3, var4, this.b.getFactMap());
         if (var5 instanceof Collection) {
            return (Collection<?>)var5;
         }

         if (null == var5) {
            throw new RuleException(this.a(var1, var2) + "值未定义");
         }

         ArrayList var6 = new ArrayList();
         var6.add(var5);
         return var6;
      }
   }

   private void a(List<Object> var1, ReteInstance var2) {
      for (Object var4 : var1) {
         this.a.doRete(var2, var4, false);
      }

      this.a.doRete(var2, "__*__", false);
      this.a((Collection<Object>)var1, var2);
      this.a.reEvaluationExecute(var2);
   }

   private void a(Collection<Object> var1, ReteInstance var2) {
      Map var3 = var2.getMutexGroupReteInstancesMap();
      if (var3 != null) {
         Collection var4 = null;

         for (String var6 : (Iterable<String>)(Iterable<?>)(var3.keySet())) {
            String var7 = var2.getId() + var6;
            if (!this.i.contains(var7)) {
               this.a.getContext().cleanTipMsg();
               this.a.getContext().addTipMsg("执行互斥组:" + var6 + "");
               List var8 = (List)var3.get(var6);
               Date var9 = new Date();

               for (ReteInstanceUnit var11 : (Iterable<ReteInstanceUnit>)(Iterable<?>)(var8)) {
                  Date var12 = var11.getEffectiveDate();
                  if (var12 == null || var12.compareTo(var9) <= 0) {
                     Date var13 = var11.getExpiresDate();
                     if (var13 == null || var13.compareTo(var9) >= 0) {
                        ReteInstance var14 = var11.getReteInstance();

                        for (Object var16 : var1) {
                           var4 = this.a.doRete(var14, var16, true);
                           if (var4 != null && var4.size() != 0) {
                              this.i.add(var7);
                              break;
                           }
                        }

                        if (var4 != null && var4.size() != 0) {
                           break;
                        }

                        var4 = this.a.doRete(var14, "__*__", true);
                        if (var4 != null) {
                           this.i.add(var7);
                           break;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @Override
   protected void a() {
      super.a();
      this.i.clear();
   }

   static {
      try {
         j = URLDecoder.decode(
            "%E5%BD%93%E5%89%8D%E4%BD%BF%E7%94%A8%E7%9A%84%E6%98%AFURule%20Pro%E8%AF%95%E7%94%A8%E7%89%88%E5%B7%B2%E8%BF%87%E6%9C%9F%EF%BC%8C%E8%AF%B7%E9%87%87%E8%B4%AD%E6%AD%A3%E5%BC%8F%E7%89%88%E6%9C%AC%EF%BC%81(The%20trial%20period%20has%20expired%2Cplease%20purchase%20the%20official%20version!)",
            "UTF-8"
         );
      } catch (UnsupportedEncodingException var1) {
         var1.printStackTrace();
      }
   }
}
