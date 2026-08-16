package com.bstek.urule.console.config.dialect;

public class PostgreSQLDialect extends Dialect {
   public String getLimitString(String var1, int var2, int var3) {
      return var2 == 0 ? var1 + " limit " + var3 : var1 + " limit " + var3 + " offset " + var2;
   }
}
