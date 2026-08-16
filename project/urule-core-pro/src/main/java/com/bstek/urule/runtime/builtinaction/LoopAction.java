package com.bstek.urule.runtime.builtinaction;

import com.bstek.urule.action.ActionId;
import com.bstek.urule.model.library.action.annotation.ActionBean;
import com.bstek.urule.model.library.action.annotation.ActionMethod;
import com.bstek.urule.model.library.action.annotation.ActionMethodParameter;
import com.bstek.urule.model.rule.loop.LoopObjectThreadLocal;

@ActionBean(name = "循环操作", ename = "Loop")
public class LoopAction {
   public static final String BREAK_LOOP_ACTION_ID = "_loop_rule_break_tag__";

   @ActionMethod(name = "中断循环")
   @ActionMethodParameter(names = {})
   @ActionId("_loop_rule_break_tag__")
   public String breakLoop() {
      return "break";
   }

   @ActionMethod(name = "当前循环对象")
   @ActionMethodParameter(names = {})
   public Object currentLoopObject() {
      return LoopObjectThreadLocal.getLoopObject();
   }
}
