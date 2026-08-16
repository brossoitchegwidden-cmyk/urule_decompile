package com.bstek.urule.runtime.builtinaction;

import com.bstek.urule.Utils;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

class ListAction$1 implements Comparator<Object> {
   final String a;
   final boolean b;
   final ListAction c;

   ListAction$1(ListAction var1, String var2, boolean var3) {
      this.c = var1;
      this.a = var2;
      this.b = var3;
   }

   @Override
   public int compare(Object var1, Object var2) {
      int var3 = 0;
      String[] var4 = this.a.split(",");

      for (String var8 : var4) {
         var3 = this.a(var8, this.b, var1, var2);
         if (var3 != 0) {
            break;
         }
      }

      return var3;
   }

   private int a(String var1, boolean var2, Object var3, Object var4) {
      Object var5 = Utils.getObjectProperty(var3, var1);
      Object var6 = Utils.getObjectProperty(var4, var1);
      if (var5 == null) {
         return var2 ? 0 : 1;
      } else if (var6 == null) {
         return var2 ? 1 : 0;
      } else if (var5 instanceof String) {
         return var2 ? ((String)var5).compareTo(var6.toString()) : ((String)var6).compareTo(var5.toString());
      } else if (var5 instanceof Date) {
         return var2 ? ((Date)var5).compareTo((Date)var6) : ((Date)var6).compareTo((Date)var5);
      } else if (var5 instanceof Number) {
         BigDecimal var7 = Utils.toBigDecimal(var5);
         BigDecimal var8 = Utils.toBigDecimal(var6);
         return var2 ? var7.compareTo(var8) : var8.compareTo(var7);
      } else {
         return 0;
      }
   }
}
