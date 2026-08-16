package com.bstek.urule.runtime;

import com.bstek.urule.Utils;
import com.bstek.urule.model.library.action.SpringBean;
import java.util.List;
import org.springframework.context.ApplicationContext;

public class ActionUtils {
   public static SpringBean getBuiltinAction(String var0) {
      ApplicationContext var1 = Utils.getApplicationContext();
      BuiltInActionLibraryBuilder var2 = (BuiltInActionLibraryBuilder)var1.getBean("urule.builtInActionLibraryBuilder");
      List var3 = var2.getBuiltInActions();
      SpringBean var4 = null;

      for (SpringBean var6 : (Iterable<SpringBean>)(Iterable<?>)(var3)) {
         if (var6.getId().equals(var0)) {
            var4 = var6;
            break;
         }
      }

      return var4;
   }
}
