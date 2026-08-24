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

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   public String getCategoryUuid() {
      return this.categoryUuid;
   }

   public void setCategoryUuid(String categoryUuid) {
      this.categoryUuid = categoryUuid;
   }

   public String getConstantName() {
      return this.constantName;
   }

   public void setConstantName(String constantName) {
      this.constantName = constantName;
   }

   public String getConstantLabel() {
      return this.constantLabel;
   }

   public void setConstantLabel(String constantLabel) {
      this.constantLabel = constantLabel;
   }

   public String getConstantCategory() {
      return this.constantCategory;
   }

   public void setConstantCategory(String constantCategory) {
      this.constantCategory = constantCategory;
   }

   public Datatype getDatatype() {
      return this.datatype;
   }

   public void setDatatype(Datatype datatype) {
      this.datatype = datatype;
   }

   @Override
   public ValueType getValueType() {
      return this.valueType;
   }

   @JsonIgnore
   @Override
   public String getId() {
      String text = LocaleHolder.isEnglish() ? "Constant" : "常量";
      String id = "[" + text + "]" + this.constantCategory + "." + this.constantLabel;
      if (this.arithmetic != null) {
         id = id + this.arithmetic.getId();
      }

      return id;
   }

   @JsonIgnore
   @Override
   public String getValueId() {
      String text = LocaleHolder.isEnglish() ? "Constant" : "常量";
      return "[" + text + "]" + this.constantCategory + "." + this.constantLabel;
   }
}
