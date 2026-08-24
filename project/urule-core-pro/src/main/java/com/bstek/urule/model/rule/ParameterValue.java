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

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   @Override
   public ValueType getValueType() {
      return this.valueType;
   }

   public String getVariableName() {
      return this.variableName;
   }

   public void setVariableName(String variableName) {
      this.variableName = variableName;
   }

   public String getVariableLabel() {
      return this.variableLabel;
   }

   public void setVariableLabel(String variableLabel) {
      this.variableLabel = variableLabel;
   }

   public String getKeyName() {
      return this.keyName;
   }

   public void setKeyName(String keyName) {
      this.keyName = keyName;
   }

   public String getKeyLabel() {
      return this.keyLabel;
   }

   public void setKeyLabel(String keyLabel) {
      this.keyLabel = keyLabel;
   }

   public String getKeyUuid() {
      return this.keyUuid;
   }

   public void setKeyUuid(String keyUuid) {
      this.keyUuid = keyUuid;
   }

   public String getKeyCategoryUuid() {
      return this.keyCategoryUuid;
   }

   public void setKeyCategoryUuid(String keyCategoryUuid) {
      this.keyCategoryUuid = keyCategoryUuid;
   }

   public Datatype getDatatype() {
      return this.datatype;
   }

   public void setDatatype(Datatype datatype) {
      this.datatype = datatype;
   }

   @JsonIgnore
   @Override
   public String getId() {
      String text = LocaleHolder.isEnglish() ? "Parameter" : "参数";
      String id = "[P]" + text;
      if (this.keyLabel != null) {
         id = id + "." + this.keyLabel;
      }

      id = id + "." + this.variableLabel;
      if (this.arithmetic != null) {
         id = id + this.arithmetic.getId();
      }

      return id;
   }

   @JsonIgnore
   @Override
   public String getValueId() {
      String text = LocaleHolder.isEnglish() ? "Parameter" : "参数";
      String text2 = "[P]" + text;
      if (this.keyLabel != null) {
         text2 = text2 + "." + this.keyLabel;
      }

      return text2 + "." + this.variableLabel;
   }
}
