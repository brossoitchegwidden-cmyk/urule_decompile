package com.bstek.urule.console.config.dialect;

public class InformixDialect extends Dialect {
   public String getLimitString(String var1, int var2, int var3) {
      return (new StringBuffer(var1.length() + 8)).append(var1).insert(var1.toLowerCase().indexOf("select") + 6, " skip " + var2 + " first " + var3).toString();
   }
}
