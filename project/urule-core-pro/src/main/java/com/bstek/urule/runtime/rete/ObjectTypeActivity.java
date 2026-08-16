package com.bstek.urule.runtime.rete;

import com.bstek.urule.exception.RuleAssertException;
import com.bstek.urule.model.GeneralEntity;
import java.util.Collection;

public class ObjectTypeActivity extends AbstractActivity {
   private Class<?> b;
   private String c;

   public ObjectTypeActivity(String var1) {
      this.c = var1;
   }

   public ObjectTypeActivity(Class<?> var1) {
      this.b = var1;
   }

   @Override
   public Collection<FactTracker> enter(EvaluationContext var1, Object var2, FactTracker var3) {
      try {
         var3.setToken(var1.nextToken());
         return this.a(var1, var2, var3);
      } catch (Exception var6) {
         String var5 = var1.getTipMsg();
         throw new RuleAssertException(var5, var6);
      }
   }

   public boolean support(Object var1) {
      if (this.c != null && this.c.equals("__*__") && this.c.equals(var1)) {
         return true;
      }

      if (this.b == null && this.c == null) {
         return true;
      }

      if (var1 instanceof GeneralEntity) {
         GeneralEntity var2 = (GeneralEntity)var1;
         String var3 = var2.getTargetClass();
         if (this.c != null) {
            if (var3.equals(this.c)) {
               return true;
            }
         } else if (var3.equals(this.b.getName())) {
            return true;
         }
      } else if (this.b != null) {
         Class var4 = var1.getClass();
         if (this.b.isAssignableFrom(var4) || this.b.getName().equals(var4.getName())) {
            return true;
         }
      }

      return false;
   }
}
