package com.bstek.urule.runtime;

import com.bstek.urule.model.library.action.SpringBean;
import java.util.Comparator;

/** Orders exposed Spring beans by their display name. */
final class SpringBeanNameComparator implements Comparator<SpringBean> {
   @Override
   public int compare(SpringBean left, SpringBean right) {
      return left.getName().compareTo(right.getName());
   }
}
