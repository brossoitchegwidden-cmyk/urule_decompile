package com.bstek.urule.console.config.dialect;

public class H2Dialect extends Dialect {
   public String getLimitString(String var1, int var2, int var3) {
      return (new StringBuffer(var1.length() + 20)).append(var1).append(var2 > 0 ? " limit " + var3 + " offset " + var2 : " limit " + var3).toString();
   }
}
