package com.bstek.urule.console.config.dialect;

public class DmDialect extends Dialect {
   public String getLimitString(String var1, int var2, int var3) {
      return var1 + " limit " + var2 + "," + var3;
   }
}
