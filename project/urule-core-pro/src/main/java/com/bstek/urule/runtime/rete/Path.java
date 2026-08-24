package com.bstek.urule.runtime.rete;

import java.util.UUID;

public class Path {
   private Activity to;
   private String id = UUID.randomUUID().toString();

   public Path(Activity to) {
      this.to = to;
      if (to instanceof JoinActivity) {
         JoinActivity joinActivity = (JoinActivity)to;
         joinActivity.addFromPath(this);
      }
   }

   public Activity getTo() {
      return this.to;
   }

   public String getId() {
      return this.id;
   }
}
