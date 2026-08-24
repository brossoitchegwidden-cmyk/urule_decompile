package com.bstek.urule.runtime.rete;

import com.bstek.urule.exception.RuleAssertException;
import com.bstek.urule.model.GeneralEntity;
import java.util.Collection;

public class ObjectTypeActivity extends AbstractActivity {
   private Class<?> typeClass;
   private String clazz;

   public ObjectTypeActivity(String clazz) {
      this.clazz = clazz;
   }

   public ObjectTypeActivity(Class<?> typeClass) {
      this.typeClass = typeClass;
   }

   @Override
   public Collection<FactTracker> enter(EvaluationContext context, Object obj, FactTracker tracker) {
      try {
         tracker.setToken(context.nextToken());
         return this.visitPahs(context, obj, tracker);
      } catch (Exception exception) {
         String tipMsg = context.getTipMsg();
         throw new RuleAssertException(tipMsg, exception);
      }
   }

   public boolean support(Object object) {
      if (this.clazz != null && this.clazz.equals("__*__") && this.clazz.equals(object)) {
         return true;
      }

      if (this.typeClass == null && this.clazz == null) {
         return true;
      }

      if (object instanceof GeneralEntity) {
         GeneralEntity generalEntity = (GeneralEntity)object;
         String targetClass = generalEntity.getTargetClass();
         if (this.clazz != null) {
            if (targetClass.equals(this.clazz)) {
               return true;
            }
         } else if (targetClass.equals(this.typeClass.getName())) {
            return true;
         }
      } else if (this.typeClass != null) {
         Class actualClass = object.getClass();
         if (this.typeClass.isAssignableFrom(actualClass) || this.typeClass.getName().equals(actualClass.getName())) {
            return true;
         }
      }

      return false;
   }
}
