package com.bstek.urule.model.rule;

import com.bstek.urule.LocaleHolder;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class SimpleValue extends AbstractValue {
   private String content;
   private ValueType valueType = ValueType.Input;

   @Override
   public ValueType getValueType() {
      return this.valueType;
   }

   public String getContent() {
      return this.content;
   }

   public void setContent(String var1) {
      this.content = var1;
   }

   @JsonIgnore
   @Override
   public String getId() {
      String var1 = LocaleHolder.isEnglish() ? "Text" : "字符";
      String var2 = "[" + var1 + "]" + this.content;
      if (this.arithmetic != null) {
         var2 = var2 + this.arithmetic.getId();
      }

      return var2;
   }

   @JsonIgnore
   @Override
   public String getValueId() {
      String var1 = LocaleHolder.isEnglish() ? "Text" : "字符";
      return "[" + var1 + "]" + this.content;
   }
}
