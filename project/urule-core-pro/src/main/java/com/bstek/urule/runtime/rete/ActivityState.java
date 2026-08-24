package com.bstek.urule.runtime.rete;

import java.util.HashSet;
import java.util.Set;

public abstract class ActivityState {
   private String id;
   private boolean passed;
   private Set<Integer> tokensSet = new HashSet<>();

   public ActivityState(String id) {
      this.id = id;
   }

   public String getId() {
      return this.id;
   }

   public void setPassed(boolean passed) {
      this.passed = passed;
   }

   public boolean isPassed() {
      return this.passed;
   }

   public Set<Integer> getTokensSet() {
      return this.tokensSet;
   }
}
