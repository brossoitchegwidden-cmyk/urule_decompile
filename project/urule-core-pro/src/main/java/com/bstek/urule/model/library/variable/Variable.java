package com.bstek.urule.model.library.variable;

import com.bstek.urule.model.library.Datatype;

public class Variable {
   private String uuid;
   private String name;
   private String label;
   private Datatype type;
   private String dataType;
   private String defaultValue;
   private String childType;
   private String childTypeUuid;
   private Act act;

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
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

   public Datatype getType() {
      return this.type;
   }

   public void setType(Datatype type) {
      this.type = type;
   }

   public String getDataType() {
      return this.dataType;
   }

   public void setDataType(String dataType) {
      this.dataType = dataType;
   }

   public String getDefaultValue() {
      return this.defaultValue;
   }

   public void setDefaultValue(String defaultValue) {
      this.defaultValue = defaultValue;
   }

   public Act getAct() {
      return this.act;
   }

   public void setAct(Act act) {
      this.act = act;
   }

   public String getChildType() {
      return this.childType;
   }

   public void setChildType(String childType) {
      this.childType = childType;
   }

   public String getChildTypeUuid() {
      return this.childTypeUuid;
   }

   public void setChildTypeUuid(String childTypeUuid) {
      this.childTypeUuid = childTypeUuid;
   }
}
