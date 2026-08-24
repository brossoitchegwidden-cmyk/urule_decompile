package com.bstek.urule.console.config.dialect;

public class OrderLimitDialect extends Dialect {
   public String getLimitString(String query, int offset, int limit) {
      return "select * from ( select row_.*, row_number() over () as rownum_ from ( " + query + " ) row_ ) pageTable where pageTable.rownum_ between " + (offset + 1) + " and " + (offset + limit);
   }

   public String getLimitString(String query, int offset, int limit, String orderField) {
      return "select * from ( select row_.*, row_number() over (order by " + orderField + ") as rownum_ from ( " + query + " ) row_ ) pageTable where pageTable.rownum_ between " + (offset + 1) + " and " + (offset + limit);
   }

   public String getLimitString(String query, int limit) {
      return query + " limit  " + limit;
   }

   public String getCountSql(String sql) {
      return "select count(*) TOTAL_ROWS_ from (" + sql + ") countTable";
   }

   public boolean supportTransaction() {
      return false;
   }
}
