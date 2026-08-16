package com.bstek.urule.runtime;

import com.bstek.urule.model.library.action.SpringBean;
import java.util.Comparator;

class BuiltInActionLibraryBuilder$1 implements Comparator<SpringBean> {
   final BuiltInActionLibraryBuilder a;

   BuiltInActionLibraryBuilder$1(BuiltInActionLibraryBuilder var1) {
      this.a = var1;
   }

   public int compare(SpringBean var1, SpringBean var2) {
      return var1.getName().compareTo(var2.getName());
   }
}
