package com.bstek.urule.console.config.dialect;

public class DmDialect extends Dialect {
   public String getLimitString(String query, int offset, int limit) {
      return query + " limit " + offset + "," + limit;
   }
}
