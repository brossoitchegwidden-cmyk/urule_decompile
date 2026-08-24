package com.bstek.urule.runtime;

import com.bstek.urule.Utils;
import com.bstek.urule.model.library.action.SpringBean;
import java.util.List;
import org.springframework.context.ApplicationContext;

public class ActionUtils {
   public static SpringBean getBuiltinAction(String beanId) {
      ApplicationContext applicationContext = Utils.getApplicationContext();
      BuiltInActionLibraryBuilder builtInActionLibraryBuilder = (BuiltInActionLibraryBuilder)applicationContext.getBean("urule.builtInActionLibraryBuilder");
      List builtInActions = builtInActionLibraryBuilder.getBuiltInActions();
      SpringBean springBean = null;

      for (SpringBean springBean2 : (Iterable<SpringBean>)(Iterable<?>)(builtInActions)) {
         if (springBean2.getId().equals(beanId)) {
            springBean = springBean2;
            break;
         }
      }

      return springBean;
   }
}
