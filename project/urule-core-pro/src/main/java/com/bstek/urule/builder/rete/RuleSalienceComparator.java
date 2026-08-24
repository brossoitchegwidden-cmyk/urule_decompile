package com.bstek.urule.builder.rete;

import com.bstek.urule.model.rule.Rule;
import java.util.Comparator;

/** Orders rules by descending salience while preserving null salience last. */
final class RuleSalienceComparator implements Comparator<Rule> {
   @Override
   public int compare(Rule left, Rule right) {
      Integer leftSalience = left.getSalience();
      Integer rightSalience = right.getSalience();
      if (leftSalience != null && rightSalience != null) {
         return rightSalience - leftSalience;
      }
      if (rightSalience != null) {
         return -1;
      }
      return leftSalience != null ? 1 : 0;
   }
}
