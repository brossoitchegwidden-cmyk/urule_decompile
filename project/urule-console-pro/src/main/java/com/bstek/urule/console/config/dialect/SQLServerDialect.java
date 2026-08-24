package com.bstek.urule.console.config.dialect;

public class SQLServerDialect extends Dialect {
   public String getLimitString(String query, int offset, int limit) {
      return query + " offset " + offset + " rows fetch next " + limit + " rows only";
   }
}
