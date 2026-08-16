package com.bstek.urule.console.config.dialect;

public class SQLServerDialect extends Dialect {
   public String getLimitString(String var1, int var2, int var3) {
      return var1 + " offset " + var2 + " rows fetch next " + var3 + " rows only";
   }
}
