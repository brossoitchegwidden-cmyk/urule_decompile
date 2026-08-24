package com.bstek.urule.console.config.dialect;

public abstract class Dialect {
   public abstract String getLimitString(String query, int offset, int limit);

   public String getCountSql(String sql) {
      return "select count(*) TOTAL_ROWS_ from (" + sql + ") as countTable";
   }

   public String getOriginSql(String sql) {
      String originSql = sql.replaceAll(":\\w+", "?");
      return originSql;
   }

   public boolean supportTransaction() {
      return true;
   }
}
