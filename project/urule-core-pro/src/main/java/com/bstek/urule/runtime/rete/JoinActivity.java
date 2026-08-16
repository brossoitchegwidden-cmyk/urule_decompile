package com.bstek.urule.runtime.rete;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class JoinActivity extends AbstractActivity {
   private List<Path> b = new ArrayList<>();

   protected boolean a(EvaluationContext var1) {
      Set var2 = var1.getPathPassedSet();

      for (Path var4 : this.b) {
         if (!var2.contains(var4.getId())) {
            return false;
         }
      }

      return true;
   }

   public void addFromPath(Path var1) {
      this.b.add(var1);
   }
}
