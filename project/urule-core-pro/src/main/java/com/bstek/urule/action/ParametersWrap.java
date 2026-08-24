package com.bstek.urule.action;

import com.bstek.urule.model.library.Datatype;

class ParametersWrap {
   private Datatype[] datatypes;
   private Object[] values;

   public Datatype[] getDatatypes() {
      return this.datatypes;
   }

   public void setDatatypes(Datatype[] datatype) {
      this.datatypes = datatype;
   }

   public Object[] getValues() {
      return this.values;
   }

   public void setValues(Object[] values2) {
      this.values = values2;
   }

   public String valuesToString() {
      if (this.values == null) {
         return "";
      }

      StringBuffer stringBuffer = new StringBuffer();

      for (Object objectValue : this.values) {
         if (stringBuffer.length() > 0) {
            stringBuffer.append(",");
         }

         if (objectValue == null) {
            stringBuffer.append("null");
         } else {
            stringBuffer.append(objectValue);
         }
      }

      return stringBuffer.toString();
   }
}
