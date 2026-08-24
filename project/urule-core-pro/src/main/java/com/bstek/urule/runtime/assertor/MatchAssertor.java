package com.bstek.urule.runtime.assertor;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchAssertor implements Assertor {
   @Override
   public boolean eval(Object left, Object right, Datatype datatype) {
      if (left != null && right != null) {
         Pattern pattern = Pattern.compile(right.toString());
         Matcher matcher = pattern.matcher(left.toString());
         return matcher.matches();
      } else {
         return false;
      }
   }

   @Override
   public Op supportOp() {
      return Op.Match;
   }
}
