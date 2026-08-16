package com.bstek.urule.console.editor;

import com.bstek.urule.console.database.model.datasource.FieldType;
import com.bstek.urule.model.library.variable.Act;

public class PropertyItem {
   private String a;
   private String b;
   private FieldType c;
   private Act d;

   public PropertyItem() {
      this.c = FieldType.String;
      this.d = Act.InOut;
   }

   public String getName() {
      return this.a;
   }

   public void setName(String var1) {
      this.a = var1;
   }

   public String getLabel() {
      return this.b;
   }

   public void setLabel(String var1) {
      this.b = var1;
   }

   public FieldType getDataType() {
      return this.c;
   }

   public void setDataType(FieldType var1) {
      this.c = var1;
   }

   public Act getAct() {
      return this.d;
   }

   public void setAct(Act var1) {
      this.d = var1;
   }
}
