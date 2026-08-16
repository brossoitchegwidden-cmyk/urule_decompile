package com.bstek.urule.console.config.dialect;

public class Oracle10gDialect extends Dialect {
   public String getLimitString(String var1, int var2, int var3) {
      return var2 == 0 ? "select * from ( " + var1 + ") where rownum <= " + var3 : "select * from ( select row_.*, rownum rownum_ from ( " + var1 + " ) row_ ) where rownum_ <= " + (var2 + var3) + " and rownum_ > " + var2;
   }

   public String getCountSql(String var1) {
      return "select count(*) TOTAL_ROWS_ from (" + var1 + ") countTable";
   }
}
