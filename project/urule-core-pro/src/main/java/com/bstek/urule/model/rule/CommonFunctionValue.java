package com.bstek.urule.model.rule;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class CommonFunctionValue extends AbstractValue {
   @JsonIgnore
   private String id;
   private String name;
   private String label;
   private CommonFunctionParameter parameter;
   private ValueType valueType = ValueType.CommonFunction;

   @Override
   public String getId() {
      if (this.id == null) {
         String text = LocaleHolder.isEnglish() ? "Function" : "函数";
         this.id = "[" + text + "]" + this.label + "(" + this.parameter.getId() + ")";
         if (this.arithmetic != null) {
            this.id = this.id + this.arithmetic.getId();
         }
      }

      return this.id;
   }

   @Override
   public String getValueId() {
      String text = LocaleHolder.isEnglish() ? "Function" : "函数";
      String id = "";
      if (this.parameter != null) {
         id = this.parameter.getId();
      }

      return "[" + text + "]" + this.label + "(" + id + ")";
   }

   @Override
   public ValueType getValueType() {
      return this.valueType;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getLabel() {
      return this.label;
   }

   public void setLabel(String label) {
      this.label = label;
   }

   public void setValueType(ValueType valueType) {
      this.valueType = valueType;
   }

   public CommonFunctionParameter getParameter() {
      return this.parameter;
   }

   public void setParameter(CommonFunctionParameter parameter) {
      this.parameter = parameter;
   }
}
