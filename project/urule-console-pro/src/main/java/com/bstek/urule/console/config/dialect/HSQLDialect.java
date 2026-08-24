package com.bstek.urule.console.config.dialect;

public class HSQLDialect extends Dialect {
   public String getLimitString(String query, int offset, int limit) {
      return offset == 0 ? query + " limit " + limit : query + " offset " + offset + " limit " + limit;
   }
}
