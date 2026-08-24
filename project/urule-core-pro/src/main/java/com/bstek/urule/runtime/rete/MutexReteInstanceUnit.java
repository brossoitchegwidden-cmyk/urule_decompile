package com.bstek.urule.runtime.rete;

import java.util.List;

public class MutexReteInstanceUnit extends ReteInstanceUnit {
   private String mutexGroupName;
   private List<ReteInstance> reteInstances;

   public MutexReteInstanceUnit(String mutexGroupName, List<ReteInstance> reteInstances) {
      this.mutexGroupName = mutexGroupName;
      this.reteInstances = reteInstances;
   }

   public String getMutexGroupName() {
      return this.mutexGroupName;
   }

   public List<ReteInstance> getReteInstances() {
      return this.reteInstances;
   }
}
