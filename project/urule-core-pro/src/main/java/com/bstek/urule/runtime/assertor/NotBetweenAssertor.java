package com.bstek.urule.runtime.assertor;

import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;

public class NotBetweenAssertor implements Assertor {
   @Override
   public boolean eval(Object left, Object right, Datatype datatype) {
      if (left != null && !"".equals(left) && right != null) {
         String trimmedText = right.toString().trim();
         String[] parts = trimmedText.split(",");
         if (parts.length != 2) {
            throw new RuleException("在区间值比较操作符要求区间值要是由“,”号分隔的两个数字，当前值【" + trimmedText + "】无效.");
         }

         String text = parts[0];
         String text2 = parts[1];
         Op op = Op.GreaterThenEquals;
         Object objectValue = null;
         if (text.startsWith("(")) {
            op = Op.GreaterThen;
            objectValue = text.substring(1, text.length());
         } else if (text.startsWith("[")) {
            op = Op.GreaterThenEquals;
            objectValue = text.substring(1, text.length());
         } else {
            objectValue = text;
         }

         AssertorEvaluator assertorEvaluator = (AssertorEvaluator)Utils.getApplicationContext().getBean("urule.assertorEvaluator");
         boolean flag = assertorEvaluator.evaluate(left, objectValue, datatype, op);
         if (!flag) {
            return true;
         }

         Op op2 = Op.LessThenEquals;
         Object substring = null;
         if (text2.endsWith(")")) {
            op2 = Op.LessThen;
            substring = text2.substring(0, text2.length() - 1);
         } else if (text2.endsWith("]")) {
            op2 = Op.LessThenEquals;
            substring = text2.substring(0, text2.length() - 1);
         } else {
            substring = text2;
         }

         flag = assertorEvaluator.evaluate(left, substring, datatype, op2);
         return !flag;
      } else {
         return false;
      }
   }

   @Override
   public Op supportOp() {
      return Op.NotBetween;
   }
}
