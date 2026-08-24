package com.bstek.urule.console.editor;

import com.bstek.urule.console.database.model.datasource.FieldType;
import com.bstek.urule.model.library.variable.Act;

public class PropertyItem {
   private String name;
   private String label;
   private FieldType dataType;
   private Act act;

   public PropertyItem() {
      this.dataType = FieldType.String;
      this.act = Act.InOut;
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

   public FieldType getDataType() {
      return this.dataType;
   }

   public void setDataType(FieldType dataType) {
      this.dataType = dataType;
   }

   public Act getAct() {
      return this.act;
   }

   public void setAct(Act act) {
      this.act = act;
   }
}
