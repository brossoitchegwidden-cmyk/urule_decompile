package com.bstek.urule.console.config.dialect;

public class DB2Dialect extends Dialect {
   public String getLimitString(String var1, int var2, int var3) {
      return var2 == 0 ? var1 + " fetch first " + var3 + " rows only" : "select * from ( select inner2_.*, rownumber() over(order by order of inner2_) as rownum_ from ( " + var1 + " fetch first " + var3 + " rows only ) as inner2_ ) as inner1_ where rownum_ > " + var2 + " order by rownum_";
   }
}
