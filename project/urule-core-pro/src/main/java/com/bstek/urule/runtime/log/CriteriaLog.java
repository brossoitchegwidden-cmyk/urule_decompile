package com.bstek.urule.runtime.log;

import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.EvaluateResponse;

public class CriteriaLog extends DataLog {
   private static final String b = "^^条件：%s";
   private static final String c = "^^Condition: %s";
   private static final String d = "满足";
   private static final String e = "不满足";
   private static final String f = "match";
   private static final String g = "mismatch";
   private static final String h = "左值： %s";
   private static final String i = "left value： %s";
   private static final String j = "右值： %s";
   private static final String k = "right value: %s";

   public CriteriaLog(Criteria var1, EvaluateResponse var2) {
      String var3 = var1.getId();
      StringBuffer var4 = new StringBuffer();
      var4.append(String.format(this.a() ? "^^Condition: %s" : "^^条件：%s", var3));
      String var5 = var2.getResult() ? (this.a() ? "match" : "满足") : (this.a() ? "mismatch" : "不满足");
      var4.append("➤" + var5);
      String var6 = this.a() ? "left value： %s" : "左值： %s";
      String var7 = var2.getLeftResult() == null ? "null" : var2.getLeftResult().toString();
      var4.append(", " + String.format(var6, var7));
      if (var2.getOp() != null) {
         var4.append("【" + var2.getOp().toString() + "】");
      }

      String var8 = this.a() ? "right value: %s" : "右值： %s";
      String var9 = var2.getRightResult() == null ? "null" : var2.getRightResult().toString();
      var4.append(" " + String.format(var8, var9));
      this.a = var4.toString();
   }
}
