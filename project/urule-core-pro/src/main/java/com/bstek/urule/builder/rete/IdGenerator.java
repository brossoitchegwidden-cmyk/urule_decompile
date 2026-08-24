package com.bstek.urule.builder.rete;

public class IdGenerator {
   private int currentId = 1;
   private static final ThreadLocal<Integer> LAST_GENERATED_ID = new ThreadLocal<>();

   public IdGenerator() {
      Integer lastGeneratedId = LAST_GENERATED_ID.get();
      if (lastGeneratedId != null) {
         this.currentId = lastGeneratedId;
      }
   }

   public static final void clean() {
      LAST_GENERATED_ID.remove();
   }

   public int nextId() {
      LAST_GENERATED_ID.set(++this.currentId);
      return this.currentId;
   }
}
