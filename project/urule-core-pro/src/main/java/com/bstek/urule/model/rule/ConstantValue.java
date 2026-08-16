package com.bstek.urule.model.rule;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.model.library.Datatype;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ConstantValue extends AbstractValue {
   private String uuid;
   private String categoryUuid;
   private String constantName;
   private String constantLabel;
   private String constantCategory;
   private Datatype datatype = Datatype.String;
   private ValueType valueType = ValueType.Constant;

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String var1) {
      this.uuid = var1;
   }

   public String getCategoryUuid() {
      return this.categoryUuid;
   }

   public void setCategoryUuid(String var1) {
      this.categoryUuid = var1;
   }

   public String getConstantName() {
      return this.constantName;
   }

   public void setConstantName(String var1) {
      this.constantName = var1;
   }

   public String getConstantLabel() {
      return this.constantLabel;
   }

   public void setConstantLabel(String var1) {
      this.constantLabel = var1;
   }

   public String getConstantCategory() {
      return this.constantCategory;
   }

   public void setConstantCategory(String var1) {
      this.constantCategory = var1;
   }

   public Datatype getDatatype() {
      return this.datatype;
   }

   public void setDatatype(Datatype var1) {
      this.datatype = var1;
   }

   @Override
   public ValueType getValueType() {
      return this.valueType;
   }

   @JsonIgnore
   @Override
   public String getId() {
      String var1 = LocaleHolder.isEnglish() ? "Constant" : "常量";
      String var2 = "[" + var1 + "]" + this.constantCategory + "." + this.constantLabel;
      if (this.arithmetic != null) {
         var2 = var2 + this.arithmetic.getId();
      }

      return var2;
   }

   @JsonIgnore
   @Override
   public String getValueId() {
      String var1 = LocaleHolder.isEnglish() ? "Constant" : "常量";
      return "[" + var1 + "]" + this.constantCategory + "." + this.constantLabel;
   }
}
