package com.bstek.urule.console.config.dialect;

public abstract class Dialect {
   public abstract String getLimitString(String var1, int var2, int var3);

   public String getCountSql(String var1) {
      return "select count(*) TOTAL_ROWS_ from (" + var1 + ") as countTable";
   }

   public String getOriginSql(String var1) {
      String var2 = var1.replaceAll(":\\w+", "?");
      return var2;
   }

   public boolean supportTransaction() {
      return true;
   }
}
