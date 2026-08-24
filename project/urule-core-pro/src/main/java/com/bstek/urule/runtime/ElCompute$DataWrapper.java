package com.bstek.urule.runtime;

import java.math.BigDecimal;

class ElCompute$DataWrapper {
   private String originalText;
   private BigDecimal decimalValue;
   final ElCompute elCompute;

   public ElCompute$DataWrapper(ElCompute elCompute, String originalText, BigDecimal decimalValue) {
      this.elCompute = elCompute;
      this.originalText = originalText;
      this.decimalValue = decimalValue;
   }

   static BigDecimal getDecimalValue(ElCompute$DataWrapper dataWrapper) {
      return dataWrapper.decimalValue;
   }

   static String getOriginalText(ElCompute$DataWrapper dataWrapper) {
      return dataWrapper.originalText;
   }
}
