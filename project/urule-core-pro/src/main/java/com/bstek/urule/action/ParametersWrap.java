package com.bstek.urule.action;

import com.bstek.urule.model.library.Datatype;

class ParametersWrap {
   private Datatype[] a;
   private Object[] b;

   public Datatype[] getDatatypes() {
      return this.a;
   }

   public void setDatatypes(Datatype[] var1) {
      this.a = var1;
   }

   public Object[] getValues() {
      return this.b;
   }

   public void setValues(Object[] var1) {
      this.b = var1;
   }

   public String valuesToString() {
      if (this.b == null) {
         return "";
      }

      StringBuffer var1 = new StringBuffer();

      for (Object var5 : this.b) {
         if (var1.length() > 0) {
            var1.append(",");
         }

         if (var5 == null) {
            var1.append("null");
         } else {
            var1.append(var5);
         }
      }

      return var1.toString();
   }
}
