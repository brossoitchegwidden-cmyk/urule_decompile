package com.bstek.urule.runtime.assertor;

import com.bstek.urule.Utils;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class GreaterThenAssertor implements Assertor {
   @Override
   public boolean eval(Object left, Object right, Datatype datatype) {
      if (left != null && !"".equals(left) && right != null && !"".equals(right)) {
         if (datatype.equals(Datatype.Date)) {
            Date dateValue = (Date)datatype.convert(left);
            Date dateValue2 = (Date)datatype.convert(right);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateValue);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(dateValue2);
            int number = calendar.compareTo(calendar2);
            if (number == 1) {
               return true;
            }
         } else {
            BigDecimal decimalValue = Utils.toBigDecimal(left);
            BigDecimal decimalValue2 = Utils.toBigDecimal(right);
            int number2 = decimalValue.compareTo(decimalValue2);
            if (number2 == 1) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   @Override
   public Op supportOp() {
      return Op.GreaterThen;
   }
}
