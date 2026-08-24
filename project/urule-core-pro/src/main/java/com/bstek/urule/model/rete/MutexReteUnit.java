package com.bstek.urule.model.rete;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;

@JsonTypeName("mutex")
public class MutexReteUnit extends ReteUnit {
   private String mutexGroupName;
   private List<ReteUnit> reteUnits;

   public MutexReteUnit() {
   }

   public MutexReteUnit(String mutexGroupName, List<ReteUnit> list) {
      this.mutexGroupName = mutexGroupName;
      this.reteUnits = list;
   }

   public List<ReteUnit> getList() {
      return this.reteUnits;
   }

   public void setList(List<ReteUnit> list) {
      this.reteUnits = list;
   }

   public String getMutexGroupName() {
      return this.mutexGroupName;
   }

   public void setMutexGroupName(String mutexGroupName) {
      this.mutexGroupName = mutexGroupName;
   }
}
