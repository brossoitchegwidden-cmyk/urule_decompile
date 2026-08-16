package com.bstek.urule.model.rule;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.model.library.Datatype;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ParameterValue extends AbstractValue {
   private String uuid;
   private String keyName;
   private String keyLabel;
   private String keyUuid;
   private String keyCategoryUuid;
   private Datatype datatype;
   private String variableName;
   private String variableLabel;
   private ValueType valueType = ValueType.Parameter;

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String var1) {
      this.uuid = var1;
   }

   @Override
   public ValueType getValueType() {
      return this.valueType;
   }

   public String getVariableName() {
      return this.variableName;
   }

   public void setVariableName(String var1) {
      this.variableName = var1;
   }

   public String getVariableLabel() {
      return this.variableLabel;
   }

   public void setVariableLabel(String var1) {
      this.variableLabel = var1;
   }

   public String getKeyName() {
      return this.keyName;
   }

   public void setKeyName(String var1) {
      this.keyName = var1;
   }

   public String getKeyLabel() {
      return this.keyLabel;
   }

   public void setKeyLabel(String var1) {
      this.keyLabel = var1;
   }

   public String getKeyUuid() {
      return this.keyUuid;
   }

   public void setKeyUuid(String var1) {
      this.keyUuid = var1;
   }

   public String getKeyCategoryUuid() {
      return this.keyCategoryUuid;
   }

   public void setKeyCategoryUuid(String var1) {
      this.keyCategoryUuid = var1;
   }

   public Datatype getDatatype() {
      return this.datatype;
   }

   public void setDatatype(Datatype var1) {
      this.datatype = var1;
   }

   @JsonIgnore
   @Override
   public String getId() {
      String var1 = LocaleHolder.isEnglish() ? "Parameter" : "参数";
      String var2 = "[P]" + var1;
      if (this.keyLabel != null) {
         var2 = var2 + "." + this.keyLabel;
      }

      var2 = var2 + "." + this.variableLabel;
      if (this.arithmetic != null) {
         var2 = var2 + this.arithmetic.getId();
      }

      return var2;
   }

   @JsonIgnore
   @Override
   public String getValueId() {
      String var1 = LocaleHolder.isEnglish() ? "Parameter" : "参数";
      String var2 = "[P]" + var1;
      if (this.keyLabel != null) {
         var2 = var2 + "." + this.keyLabel;
      }

      return var2 + "." + this.variableLabel;
   }
}
