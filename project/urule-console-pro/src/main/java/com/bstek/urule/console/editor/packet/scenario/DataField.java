package com.bstek.urule.console.editor.packet.scenario;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;

public class DataField {
   private String a;
   private String b;
   private String c;
   private Datatype d;
   private Op e;

   public String getUuid() {
      return this.a;
   }

   public void setUuid(String var1) {
      this.a = var1;
   }

   public String getName() {
      return this.b;
   }

   public void setName(String var1) {
      this.b = var1;
   }

   public String getLabel() {
      return this.c;
   }

   public void setLabel(String var1) {
      this.c = var1;
   }

   public Op getOp() {
      return this.e;
   }

   public void setOp(Op var1) {
      this.e = var1;
   }

   public Datatype getDatatype() {
      return this.d;
   }

   public void setDatatype(Datatype var1) {
      this.d = var1;
   }
}
