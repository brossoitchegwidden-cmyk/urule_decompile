package com.bstek.urule.runtime.log;

public class MetLog extends DataLog {
   private int b;
   private boolean c;
   private int d;

   public MetLog(int var1, int var2, boolean var3) {
      this.b = var1;
      this.c = var3;
      this.d = var2;
      StringBuffer var4 = new StringBuffer("--");
      if (var3) {
         var4.append("只有");
      } else {
         var4.append("至少");
      }

      var4.append(var1 + "个条件成立，");
      var4.append("实际成立条件" + var2 + "个，");
      if (var3) {
         if (var1 == var2) {
            var4.append("满足");
         } else {
            var4.append("不满足");
         }
      } else if (var2 >= var1) {
         var4.append("满足");
      } else {
         var4.append("不满足");
      }

      this.a = var4.toString();
   }

   public int getMet() {
      return this.b;
   }

   public boolean isOnly() {
      return this.c;
   }

   public int getMatchedCount() {
      return this.d;
   }
}
