package com.bstek.urule.model.rule;

import com.bstek.urule.LocaleHolder;

public class ObjectValue extends AbstractValue {
   private Object object;

   public ObjectValue(Object object) {
      this.object = object;
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
      String text = LocaleHolder.isEnglish() ? "Object" : "对象";
      return "[" + text + "]" + this.object.toString();
   }

   @Override
   public String getValueId() {
      String text = LocaleHolder.isEnglish() ? "Object" : "对象";
      return "[" + text + "]" + this.object.toString();
   }
}
