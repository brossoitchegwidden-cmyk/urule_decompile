package com.bstek.urule.console.editor.packet.scenario;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;

public class ObjectField {
   private String name;
   private String label;
   private String value;
   private Datatype datatype;
   private Op op;

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

   public String getValue() {
      return this.value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public Datatype getDatatype() {
      return this.datatype;
   }

   public void setDatatype(Datatype datatype) {
      this.datatype = datatype;
   }

   public Op getOp() {
      return this.op;
   }

   public void setOp(Op op) {
      this.op = op;
   }

   public String toString() {
      String toStringResult = "{name=" + this.name + ", label=" + this.label + ", value=" + this.value;
      if (this.op != null) {
         toStringResult = toStringResult + ", op=" + this.op;
      }

      toStringResult = toStringResult + "}";
      return toStringResult;
   }
}
