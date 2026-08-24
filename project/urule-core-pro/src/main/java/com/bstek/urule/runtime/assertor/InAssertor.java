package com.bstek.urule.runtime.assertor;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;
import java.util.Collection;

public class InAssertor implements Assertor {
   @Override
   public boolean eval(Object left, Object right, Datatype datatype) {
      if (left != null && right != null) {
         if (right instanceof Collection) {
            Collection right2 = (Collection)right;
            if (left instanceof Collection) {
               Collection left2 = (Collection)left;
               boolean evalResult = false;

               for (Object objectValue : left2) {
                  for (Object objectValue2 : right2) {
                     if (objectValue2.toString().equals(objectValue.toString())) {
                        evalResult = true;
                        break;
                     }

                     evalResult = false;
                  }
               }

               return evalResult;
            } else {
               String[] parts = left.toString().split(",");
               boolean evalResult2 = false;

               for (String text : parts) {
                  for (Object objectValue3 : right2) {
                     if (objectValue3.toString().equals(text)) {
                        evalResult2 = true;
                        break;
                     }

                     evalResult2 = false;
                  }
               }

               return evalResult2;
            }
         } else {
            if (!(right instanceof String)) {
               return false;
            }

            String right3 = (String)right;
            String[] parts2 = right3.split(",");
            if (left instanceof Collection) {
               Collection left3 = (Collection)left;
               boolean evalResult3 = false;

               for (Object objectValue4 : left3) {
                  for (String text2 : parts2) {
                     if (objectValue4.toString().equals(text2)) {
                        evalResult3 = true;
                        break;
                     }

                     evalResult3 = false;
                  }
               }

               return evalResult3;
            } else {
               String[] parts3 = left.toString().split(",");
               boolean evalResult4 = false;

               for (String text3 : parts3) {
                  for (String text4 : parts2) {
                     if (text4.equals(text3)) {
                        evalResult4 = true;
                        break;
                     }

                     evalResult4 = false;
                  }
               }

               return evalResult4;
            }
         }
      } else {
         return false;
      }
   }

   @Override
   public Op supportOp() {
      return Op.In;
   }
}
