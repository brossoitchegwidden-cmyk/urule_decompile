package com.bstek.urule.console.config.dialect;

public class DB2Dialect extends Dialect {
   public String getLimitString(String query, int offset, int limit) {
      return offset == 0 ? query + " fetch first " + limit + " rows only" : "select * from ( select inner2_.*, rownumber() over(order by order of inner2_) as rownum_ from ( " + query + " fetch first " + limit + " rows only ) as inner2_ ) as inner1_ where rownum_ > " + offset + " order by rownum_";
   }
}
