package com.bstek.urule.runtime;

import com.bstek.urule.exception.RuleException;
import java.lang.reflect.Field;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

public class ProxyUtils {
   public static Object getTargetObject(Object obj) {
      if (AopUtils.isAopProxy(obj)) {
         return AopUtils.isJdkDynamicProxy(obj) ? resolveJdkProxyTarget(obj) : resolveCglibTarget(obj);
      } else {
         return obj;
      }
   }

   private static Object resolveCglibTarget(Object objectValue) {
      try {
         Field declaredField = objectValue.getClass().getDeclaredField("CGLIB$CALLBACK_0");
         declaredField.setAccessible(true);
         Object objectValue2 = declaredField.get(objectValue);
         Field declaredField2 = objectValue2.getClass().getDeclaredField("advised");
         declaredField2.setAccessible(true);
         return ((AdvisedSupport)declaredField2.get(objectValue2)).getTargetSource().getTarget();
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   private static Object resolveJdkProxyTarget(Object objectValue) {
      try {
         Field declaredField = objectValue.getClass().getSuperclass().getDeclaredField("h");
         declaredField.setAccessible(true);
         AopProxy aopProxy = (AopProxy)declaredField.get(objectValue);
         Field declaredField2 = aopProxy.getClass().getDeclaredField("advised");
         declaredField2.setAccessible(true);
         return ((AdvisedSupport)declaredField2.get(aopProxy)).getTargetSource().getTarget();
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }
}
