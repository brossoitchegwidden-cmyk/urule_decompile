package com.bstek.urule.runtime;

import java.math.BigDecimal;

class ElCompute$DataWrapper {
   private String b;
   private BigDecimal c;
   final ElCompute a;

   public ElCompute$DataWrapper(ElCompute var1, String var2, BigDecimal var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
   }

   static BigDecimal a(ElCompute$DataWrapper var1) {
      return var1.c;
   }

   static String b(ElCompute$DataWrapper var1) {
      return var1.b;
   }
}
