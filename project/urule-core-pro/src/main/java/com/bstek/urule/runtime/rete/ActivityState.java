package com.bstek.urule.runtime.rete;

import java.util.HashSet;
import java.util.Set;

public abstract class ActivityState {
   private String a;
   private boolean b;
   private Set<Integer> c = new HashSet<>();

   public ActivityState(String var1) {
      this.a = var1;
   }

   public String getId() {
      return this.a;
   }

   public void setPassed(boolean var1) {
      this.b = var1;
   }

   public boolean isPassed() {
      return this.b;
   }

   public Set<Integer> getTokensSet() {
      return this.c;
   }
}
