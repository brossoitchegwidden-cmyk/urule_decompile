package com.bstek.urule.console.config.dialect;

public class H2Dialect extends Dialect {
   public String getLimitString(String query, int offset, int limit) {
      return (new StringBuffer(query.length() + 20)).append(query).append(offset > 0 ? " limit " + limit + " offset " + offset : " limit " + limit).toString();
   }
}
