package com.bstek.urule.runtime.assertor;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;
import java.util.Collection;

public class InAssertor implements Assertor {
   @Override
   public boolean eval(Object var1, Object var2, Datatype var3) {
      if (var1 != null && var2 != null) {
         if (var2 instanceof Collection) {
            Collection var16 = (Collection)var2;
            if (var1 instanceof Collection) {
               Collection var18 = (Collection)var1;
               boolean var21 = false;

               for (Object var27 : var18) {
                  for (Object var33 : var16) {
                     if (var33.toString().equals(var27.toString())) {
                        var21 = true;
                        break;
                     }

                     var21 = false;
                  }
               }

               return var21;
            } else {
               String[] var17 = var1.toString().split(",");
               boolean var20 = false;

               for (String var32 : var17) {
                  for (Object var37 : var16) {
                     if (var37.toString().equals(var32)) {
                        var20 = true;
                        break;
                     }

                     var20 = false;
                  }
               }

               return var20;
            }
         } else {
            if (!(var2 instanceof String)) {
               return false;
            }

            String var4 = (String)var2;
            String[] var5 = var4.split(",");
            if (var1 instanceof Collection) {
               Collection var19 = (Collection)var1;
               boolean var22 = false;

               for (Object var28 : var19) {
                  for (String var38 : var5) {
                     if (var28.toString().equals(var38)) {
                        var22 = true;
                        break;
                     }

                     var22 = false;
                  }
               }

               return var22;
            } else {
               String[] var6 = var1.toString().split(",");
               boolean var7 = false;

               for (String var11 : var6) {
                  for (String var15 : var5) {
                     if (var15.equals(var11)) {
                        var7 = true;
                        break;
                     }

                     var7 = false;
                  }
               }

               return var7;
            }
         }
      } else {
         return false;
      }
   }

   @Override
   public Op supportOp() {
      return Op.In;
   }
}
