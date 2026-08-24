package com.bstek.urule.runtime.assertor;

import com.bstek.urule.Utils;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class EqualsIgnoreCaseAssertor implements Assertor {
   @Override
   public boolean eval(Object left, Object right, Datatype datatype) {
      if (left == null && right == null) {
         return true;
      }

      if (left != null && right != null) {
         BigDecimal decimalValue = null;
         BigDecimal decimalValue2 = null;
         switch (datatype) {
            case String:
               return left.toString().equalsIgnoreCase(right.toString());
            case Boolean:
               return left.toString().equals(right.toString());
            case Date:
               Date dateValue = (Date)datatype.convert(left);
               Date dateValue2 = (Date)datatype.convert(right);
               Calendar calendar = Calendar.getInstance();
               calendar.setTime(dateValue);
               Calendar calendar2 = Calendar.getInstance();
               calendar2.setTime(dateValue2);
               return calendar.compareTo(calendar2) == 0;
            case Double:
               decimalValue = Utils.toBigDecimal(left);
               decimalValue2 = Utils.toBigDecimal(right);
               return decimalValue.compareTo(decimalValue2) == 0;
            case Float:
               decimalValue = Utils.toBigDecimal(left);
               decimalValue2 = Utils.toBigDecimal(right);
               return decimalValue.compareTo(decimalValue2) == 0;
            case Integer:
               decimalValue = Utils.toBigDecimal(left);
               decimalValue2 = Utils.toBigDecimal(right);
               return decimalValue.compareTo(decimalValue2) == 0;
            case Long:
               decimalValue = Utils.toBigDecimal(left);
               decimalValue2 = Utils.toBigDecimal(right);
               return decimalValue.compareTo(decimalValue2) == 0;
            case BigDecimal:
               decimalValue = Utils.toBigDecimal(left);
               decimalValue2 = Utils.toBigDecimal(right);
               return decimalValue.compareTo(decimalValue2) == 0;
            case Enum:
               Enum localValue = (Enum)left;
               if (right instanceof Enum) {
                  Enum localValue2 = (Enum)right;
                  return localValue.equals(localValue2);
               }

               Enum enumValue = Enum.valueOf((Class<Enum>)localValue.getClass(), right.toString());
               return localValue.equals(enumValue);
            default:
               return right.toString().equalsIgnoreCase(left.toString());
         }
      } else {
         return false;
      }
   }

   @Override
   public Op supportOp() {
      return Op.EqualsIgnoreCase;
   }
}
