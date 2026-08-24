package com.bstek.urule.runtime.builtinaction;

import com.bstek.urule.model.library.action.annotation.ActionBean;
import com.bstek.urule.model.library.action.annotation.ActionMethod;
import com.bstek.urule.model.library.action.annotation.ActionMethodParameter;

@ActionBean(name = "通用", ename = "Common")
public class CommonAction {
   public static final String BEAN_ID = "urule.commonAction";
   public static final String IF_ERROR = "iferror";

   @ActionMethod(name = "IF_ERROR")
   @ActionMethodParameter(names = {"值", "出错后值"}, enames = {"value", "returnValue"})
   public Object iferror(Object value1, Object obj) {
      return value1 instanceof Exception ? obj : value1;
   }
}
