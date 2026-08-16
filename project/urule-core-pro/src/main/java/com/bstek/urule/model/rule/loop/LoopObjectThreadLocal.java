package com.bstek.urule.model.rule.loop;

public class LoopObjectThreadLocal {
   private static final ThreadLocal<Object> loopThreadLocal = new ThreadLocal<>();

   public static final void setLoopObject(Object var0) {
      loopThreadLocal.set(var0);
   }

   public static final Object getLoopObject() {
      return loopThreadLocal.get();
   }

   public static final void clean() {
      loopThreadLocal.remove();
   }
}
