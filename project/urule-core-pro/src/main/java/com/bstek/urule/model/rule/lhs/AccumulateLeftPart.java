package com.bstek.urule.model.rule.lhs;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.Utils;
import com.bstek.urule.model.rule.loop.LoopTarget;
import com.bstek.urule.model.rule.loop.LoopTargetType;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.rete.Context;
import com.bstek.urule.runtime.rete.EvaluationContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class AccumulateLeftPart implements LeftPart {
   private LoopTargetType loopTargetType;
   private LoopTarget loopTarget;
   private List<ConditionItem> conditionItems;
   private List<CalculateItem> calculateItems;
   private Junction junction;
   private String id;
   private static final Logger log = Logger.getLogger(AccumulateLeftPart.class.getName());

   public EvaluateResponse evaluate(EvaluationContext var1, Map<String, Object> var2) {
      List var3 = this.buildLoopTarget(var1, var2);
      if (var3 == null) {
         log.warning("AccumulateLeftPart collection value is null,return false.");
         EvaluateResponse var13 = new EvaluateResponse();
         var13.setResult(false);
         return var13;
      }

      HashMap var4 = new HashMap();
      var4.putAll(var2);
      HashMap var5 = new HashMap();
      boolean var6 = false;

      for (Object var8 : var3) {
         String var9 = Utils.getClassName(var8);
         var4.put(var9, var8);
         boolean var10 = this.evalCondition(var1, var4);
         if (var10) {
            var6 = true;

            for (CalculateItem var12 : this.calculateItems) {
               var12.calculate(var1, var4, var8, var5);
            }
         }
      }

      if (!var6) {
         for (CalculateItem var17 : this.calculateItems) {
            var17.init(var5);
         }
      }

      for (CalculateData var18 : (Iterable<CalculateData>)(Iterable<?>)(var5.values())) {
         var18.buildValue(var1, var2);
      }

      EvaluateResponse var16 = null;

      for (ConditionItem var20 : this.conditionItems) {
         EvaluateResponse var21 = var20.eval(var5, var1, var2);
         if (!var21.getResult()) {
            var16 = var21;
            break;
         }
      }

      if (var16 == null) {
         var16 = new EvaluateResponse();
         var16.setResult(true);
      }

      return var16;
   }

   private boolean evalCondition(EvaluationContext var1, Map<String, Object> var2) {
      if (this.junction == null) {
         return true;
      }

      List var3 = this.junction.getCriterions();
      if (var3 != null && var3.size() != 0) {
         boolean var4 = true;
         if (this.junction instanceof Or) {
            var4 = false;
         }

         boolean var5 = false;

         for (Criterion var7 : (Iterable<Criterion>)(Iterable<?>)(var3)) {
            Criteria var8 = (Criteria)var7;
            EvaluateResponse var9 = var8.evaluate(var1, var2);
            if (var4) {
               if (!var9.getResult()) {
                  var5 = false;
                  break;
               }

               var5 = true;
            } else {
               if (var9.getResult()) {
                  var5 = true;
                  break;
               }

               var5 = false;
            }
         }

         return var5;
      } else {
         return true;
      }
   }

   private List<Object> buildLoopTarget(Context var1, Map<String, Object> var2) {
      Object var3 = var1.getValueCompute().complexValueCompute(this.loopTarget.getValue(), var1, var2);
      if (this.loopTargetType.equals(LoopTargetType.list)) {
         if (var3 instanceof Collection) {
            ArrayList var11 = new ArrayList();
            Collection var12 = (Collection)var3;
            var11.addAll(var12);
            return var11;
         } else {
            return null;
         }
      } else {
         KnowledgeSession var4 = (KnowledgeSession)var1.getWorkingMemory();
         List var5 = var4.getFactList();
         String var6 = Utils.getClassName(var3);
         ArrayList var7 = new ArrayList();

         for (Object var9 : var5) {
            String var10 = Utils.getClassName(var9);
            if (var10.equals(var6)) {
               var7.add(var9);
            }
         }

         return var7;
      }
   }

   public LoopTargetType getLoopTargetType() {
      return this.loopTargetType;
   }

   public void setLoopTargetType(LoopTargetType var1) {
      this.loopTargetType = var1;
   }

   public LoopTarget getLoopTarget() {
      return this.loopTarget;
   }

   public void setLoopTarget(LoopTarget var1) {
      this.loopTarget = var1;
   }

   public List<ConditionItem> getConditionItems() {
      return this.conditionItems;
   }

   public void setConditionItems(List<ConditionItem> var1) {
      this.conditionItems = var1;
   }

   public List<CalculateItem> getCalculateItems() {
      return this.calculateItems;
   }

   public void setCalculateItems(List<CalculateItem> var1) {
      this.calculateItems = var1;
   }

   public Junction getJunction() {
      return this.junction;
   }

   public void setJunction(Junction var1) {
      this.junction = var1;
   }

   @Override
   public String getId() {
      if (this.id != null) {
         return this.id;
      }

      String var1 = LocaleHolder.isEnglish() ? "Accumulate" : "聚合";
      this.id = var1 + "," + this.loopTargetType.toString() + ":" + this.loopTarget.getValue().getId();
      int var2 = 0;

      for (ConditionItem var4 : this.conditionItems) {
         if (var2 == 0) {
            String var5 = LocaleHolder.isEnglish() ? "condition" : "条件";
            this.id = this.id + "；" + var5 + ":";
         } else {
            this.id = this.id + ",";
         }

         this.id = this.id + var4.getLeft() + var4.getOp() + var4.getValue().getId();
         var2++;
      }

      boolean var7 = false;

      for (CalculateItem var10 : this.calculateItems) {
         if (!var7) {
            String var12 = LocaleHolder.isEnglish() ? "compute" : "计算";
            this.id = this.id + ";" + var12 + ":";
         } else {
            this.id = this.id + ",";
         }

         this.id = this.id + var10.getType();
         if (var10.getValue() != null) {
            this.id = this.id + "(" + var10.getValue().getId() + ")";
         }

         if (var10.isEnableAssignment()) {
            String var13 = LocaleHolder.isEnglish() ? "set" : "赋给";
            this.id = this.id + var13 + var10.getAssignTargetType() + ":" + var10.getAssignVariableCategory() + "." + var10.getAssignVariableLabel();
            if (var10.getKeyLabel() != null) {
               this.id = this.id + "." + var10.getKeyLabel();
            }
         }
      }

      if (this.junction != null && this.junction.getCriterions() != null) {
         for (Criterion var14 : this.junction.getCriterions()) {
            if (var14 instanceof Criteria) {
               Criteria var6 = (Criteria)var14;
               this.id = this.id + var6.getId();
            }
         }
      }

      return this.id;
   }
}
