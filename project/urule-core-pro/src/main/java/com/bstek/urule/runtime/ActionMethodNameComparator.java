package com.bstek.urule.runtime;

import com.bstek.urule.model.library.action.Method;
import java.util.Comparator;

/** Orders exposed action methods by their display name. */
final class ActionMethodNameComparator implements Comparator<Method> {
   @Override
   public int compare(Method left, Method right) {
      return left.getName().compareTo(right.getName());
   }
}
