package com.bstek.urule.action;

import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.scorecard.runtime.ScoreRuntimeValue;
import com.bstek.urule.runtime.rete.Context;
import com.bstek.urule.runtime.rete.ValueCompute;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScoringAction extends AbstractAction {
   private Value value;
   private int rowNumber;
   private String name;
   private String weight;
   private ActionType actionType = ActionType.Scoring;
   public static final String SCORE_CARD_RUNTIME_VALUE = "_score_card_runtime_value_";

   public ScoringAction(int rowNumber, String name, String weight) {
      this.rowNumber = rowNumber;
      this.name = name;
      this.weight = weight;
   }

   @Override
   public ActionValue execute(Context context, Map<String, Object> factMap) {
      ValueCompute valueCompute = (ValueCompute)context.getApplicationContext().getBean("urule.valueCompute");
      Object objectValue = valueCompute.complexValueCompute(this.value, context, factMap);
      ScoreRuntimeValue scoreRuntimeValue = new ScoreRuntimeValue(this.rowNumber, this.name, this.weight, objectValue);
      Map parameters = context.getWorkingMemory().getParameters();
      List _score_card_runtime_value_ = null;
      if (parameters.containsKey("_score_card_runtime_value_")) {
         _score_card_runtime_value_ = (List)parameters.get("_score_card_runtime_value_");
      } else {
         _score_card_runtime_value_ = new ArrayList();
         parameters.put("_score_card_runtime_value_", _score_card_runtime_value_);
      }

      _score_card_runtime_value_.add(scoreRuntimeValue);
      return null;
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value value) {
      this.value = value;
   }

   public String getName() {
      return this.name;
   }

   public String getWeight() {
      return this.weight;
   }

   @Override
   public ActionType getActionType() {
      return this.actionType;
   }

   public int getRowNumber() {
      return this.rowNumber;
   }
}
