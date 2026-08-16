package com.bstek.urule.runtime.expr;

import com.bstek.urule.Utils;
import java.math.BigDecimal;

public class ValueWrapper {
   private BigDecimal a;
   private Object b;
   private String c;

   public ValueWrapper(Object var1, String var2) {
      this.b = var1;
      this.c = var2;
   }

   public ValueWrapper(Object var1, BigDecimal var2, String var3) {
      this.b = var1;
      this.a = var2;
      this.c = var3;
   }

   public BigDecimal getBigDecimalValue() {
      if (this.a != null) {
         return this.a;
      }

      if (this.b != null && !this.b.equals("")) {
         try {
            this.a = Utils.toBigDecimal(this.b);
         } catch (Exception var2) {
         }
      }

      if (Utils.isSpaceToZero() && this.b != null && this.b.equals("")) {
         this.a = BigDecimal.valueOf(0L);
      }

      return this.a;
   }

   public Object getData() {
      return this.a != null ? this.a : this.b;
   }

   public Object getOriginalValue() {
      return this.b;
   }

   public String originalValueToString() {
      if (this.b == null) {
         return "null";
      } else if (this.b instanceof Number) {
         BigDecimal var1 = Utils.toBigDecimal(this.b);
         return var1.toPlainString();
      } else {
         return this.b.toString();
      }
   }

   public String getValueId() {
      return this.c;
   }
}
