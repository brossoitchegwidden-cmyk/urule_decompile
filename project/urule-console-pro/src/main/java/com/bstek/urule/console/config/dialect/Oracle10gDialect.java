package com.bstek.urule.console.config.dialect;

public class Oracle10gDialect extends Dialect {
   public String getLimitString(String query, int offset, int limit) {
      return offset == 0 ? "select * from ( " + query + ") where rownum <= " + limit : "select * from ( select row_.*, rownum rownum_ from ( " + query + " ) row_ ) where rownum_ <= " + (offset + limit) + " and rownum_ > " + offset;
   }

   public String getCountSql(String sql) {
      return "select count(*) TOTAL_ROWS_ from (" + sql + ") countTable";
   }
}
