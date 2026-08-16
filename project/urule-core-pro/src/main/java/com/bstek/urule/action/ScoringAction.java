package com.bstek.urule.action;

import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.scorecard.runtime.ScoreRuntimeValue;
import com.bstek.urule.runtime.rete.Context;
import com.bstek.urule.runtime.rete.ValueCompute;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScoringAction extends AbstractAction {
   private Value b;
   private int c;
   private String d;
   private String e;
   private ActionType f = ActionType.Scoring;
   public static final String SCORE_CARD_RUNTIME_VALUE = "_score_card_runtime_value_";

   public ScoringAction(int var1, String var2, String var3) {
      this.c = var1;
      this.d = var2;
      this.e = var3;
   }

   @Override
   public ActionValue execute(Context var1, Map<String, Object> var2) {
      ValueCompute var3 = (ValueCompute)var1.getApplicationContext().getBean("urule.valueCompute");
      Object var4 = var3.complexValueCompute(this.b, var1, var2);
      ScoreRuntimeValue var5 = new ScoreRuntimeValue(this.c, this.d, this.e, var4);
      Map var6 = var1.getWorkingMemory().getParameters();
      List var7 = null;
      if (var6.containsKey("_score_card_runtime_value_")) {
         var7 = (List)var6.get("_score_card_runtime_value_");
      } else {
         var7 = new ArrayList();
         var6.put("_score_card_runtime_value_", var7);
      }

      var7.add(var5);
      return null;
   }

   public Value getValue() {
      return this.b;
   }

   public void setValue(Value var1) {
      this.b = var1;
   }

   public String getName() {
      return this.d;
   }

   public String getWeight() {
      return this.e;
   }

   @Override
   public ActionType getActionType() {
      return this.f;
   }

   public int getRowNumber() {
      return this.c;
   }
}
