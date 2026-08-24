package com.bstek.urule.model.scorecard.runtime;

public class ScoreRuntimeValue {
   public static final String SCORE_VALUE = "scoring_value";
   private int rowNumber;
   private String name;
   private String weight;
   private Object value;

   public ScoreRuntimeValue(int rowNumber, String name, String weight, Object value) {
      this.rowNumber = rowNumber;
      this.name = name;
      this.weight = weight;
      this.value = value;
   }

   public int getRowNumber() {
      return this.rowNumber;
   }

   public String getName() {
      return this.name;
   }

   public String getWeight() {
      return this.weight;
   }

   public Object getValue() {
      return this.value;
   }
}
