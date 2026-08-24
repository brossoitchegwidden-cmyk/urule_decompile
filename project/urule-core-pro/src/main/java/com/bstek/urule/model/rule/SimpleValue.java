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

   public void setContent(String content) {
      this.content = content;
   }

   @JsonIgnore
   @Override
   public String getId() {
      String text = LocaleHolder.isEnglish() ? "Text" : "字符";
      String id = "[" + text + "]" + this.content;
      if (this.arithmetic != null) {
         id = id + this.arithmetic.getId();
      }

      return id;
   }

   @JsonIgnore
   @Override
   public String getValueId() {
      String text = LocaleHolder.isEnglish() ? "Text" : "字符";
      return "[" + text + "]" + this.content;
   }
}
