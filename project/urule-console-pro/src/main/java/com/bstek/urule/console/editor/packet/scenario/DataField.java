package com.bstek.urule.console.editor.packet.scenario;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Op;

public class DataField {
   private String uuid;
   private String name;
   private String label;
   private Datatype datatype;
   private Op op;

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

   public Op getOp() {
      return this.op;
   }

   public void setOp(Op op) {
      this.op = op;
   }

   public Datatype getDatatype() {
      return this.datatype;
   }

   public void setDatatype(Datatype datatype) {
      this.datatype = datatype;
   }
}
