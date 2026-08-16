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
         String var1 = LocaleHolder.isEnglish() ? "Function" : "函数";
         this.id = "[" + var1 + "]" + this.label + "(" + this.parameter.getId() + ")";
         if (this.arithmetic != null) {
            this.id = this.id + this.arithmetic.getId();
         }
      }

      return this.id;
   }

   @Override
   public String getValueId() {
      String var1 = LocaleHolder.isEnglish() ? "Function" : "函数";
      String var2 = "";
      if (this.parameter != null) {
         var2 = this.parameter.getId();
      }

      return "[" + var1 + "]" + this.label + "(" + var2 + ")";
   }

   @Override
   public ValueType getValueType() {
      return this.valueType;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getLabel() {
      return this.label;
   }

   public void setLabel(String var1) {
      this.label = var1;
   }

   public void setValueType(ValueType var1) {
      this.valueType = var1;
   }

   public CommonFunctionParameter getParameter() {
      return this.parameter;
   }

   public void setParameter(CommonFunctionParameter var1) {
      this.parameter = var1;
   }
}
