package com.bstek.urule.runtime;

import com.bstek.urule.model.library.action.Method;
import java.util.Comparator;

class BuiltInActionLibraryBuilder$2 implements Comparator<Method> {
   final BuiltInActionLibraryBuilder a;

   BuiltInActionLibraryBuilder$2(BuiltInActionLibraryBuilder var1) {
      this.a = var1;
   }

   public int compare(Method var1, Method var2) {
      return var1.getName().compareTo(var2.getName());
   }
}
