package com.bstek.urule.console.config.dialect;

public class InformixDialect extends Dialect {
   public String getLimitString(String querySelect, int offset, int limit) {
      return (new StringBuffer(querySelect.length() + 8)).append(querySelect).insert(querySelect.toLowerCase().indexOf("select") + 6, " skip " + offset + " first " + limit).toString();
   }
}
