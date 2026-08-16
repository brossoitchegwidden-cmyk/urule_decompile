package com.bstek.urule.runtime.assertor;

import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;

public class NotBetweenAssertor implements Assertor {
   @Override
   public boolean eval(Object var1, Object var2, Datatype var3) {
      if (var1 != null && !"".equals(var1) && var2 != null) {
         String var4 = var2.toString().trim();
         String[] var5 = var4.split(",");
         if (var5.length != 2) {
            throw new RuleException("在区间值比较操作符要求区间值要是由“,”号分隔的两个数字，当前值【" + var4 + "】无效.");
         }

         String var6 = var5[0];
         String var7 = var5[1];
         Op var8 = Op.GreaterThenEquals;
         Object var9 = null;
         if (var6.startsWith("(")) {
            var8 = Op.GreaterThen;
            var9 = var6.substring(1, var6.length());
         } else if (var6.startsWith("[")) {
            var8 = Op.GreaterThenEquals;
            var9 = var6.substring(1, var6.length());
         } else {
            var9 = var6;
         }

         AssertorEvaluator var10 = (AssertorEvaluator)Utils.getApplicationContext().getBean("urule.assertorEvaluator");
         boolean var11 = var10.evaluate(var1, var9, var3, var8);
         if (!var11) {
            return true;
         }

         Op var12 = Op.LessThenEquals;
         Object var13 = null;
         if (var7.endsWith(")")) {
            var12 = Op.LessThen;
            var13 = var7.substring(0, var7.length() - 1);
         } else if (var7.endsWith("]")) {
            var12 = Op.LessThenEquals;
            var13 = var7.substring(0, var7.length() - 1);
         } else {
            var13 = var7;
         }

         var11 = var10.evaluate(var1, var13, var3, var12);
         return !var11;
      } else {
         return false;
      }
   }

   @Override
   public Op supportOp() {
      return Op.NotBetween;
   }
}
