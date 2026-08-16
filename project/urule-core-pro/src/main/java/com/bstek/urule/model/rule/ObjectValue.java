package com.bstek.urule.model.rule;

import com.bstek.urule.LocaleHolder;

public class ObjectValue extends AbstractValue {
   private Object object;

   public ObjectValue(Object var1) {
      this.object = var1;
   }

   @Override
   public ValueType getValueType() {
      return ValueType.Object;
   }

   public Object getObject() {
      return this.object;
   }

   @Override
   public String getId() {
      String var1 = LocaleHolder.isEnglish() ? "Object" : "对象";
      return "[" + var1 + "]" + this.object.toString();
   }

   @Override
   public String getValueId() {
      String var1 = LocaleHolder.isEnglish() ? "Object" : "对象";
      return "[" + var1 + "]" + this.object.toString();
   }
}
