package com.bstek.urule.console.config.dialect;

public class OrderLimitDialect extends Dialect {
   public String getLimitString(String var1, int var2, int var3) {
      return "select * from ( select row_.*, row_number() over () as rownum_ from ( " + var1 + " ) row_ ) pageTable where pageTable.rownum_ between " + (var2 + 1) + " and " + (var2 + var3);
   }

   public String getLimitString(String var1, int var2, int var3, String var4) {
      return "select * from ( select row_.*, row_number() over (order by " + var4 + ") as rownum_ from ( " + var1 + " ) row_ ) pageTable where pageTable.rownum_ between " + (var2 + 1) + " and " + (var2 + var3);
   }

   public String getLimitString(String var1, int var2) {
      return var1 + " limit  " + var2;
   }

   public String getCountSql(String var1) {
      return "select count(*) TOTAL_ROWS_ from (" + var1 + ") countTable";
   }

   public boolean supportTransaction() {
      return false;
   }
}
