package com.bstek.urule.runtime.rete;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class JoinActivity extends AbstractActivity {
   private List<Path> paths = new ArrayList<>();

   protected boolean allPassed(EvaluationContext context) {
      Set pathPassedSet = context.getPathPassedSet();

      for (Path path : this.paths) {
         if (!pathPassedSet.contains(path.getId())) {
            return false;
         }
      }

      return true;
   }

   public void addFromPath(Path fromPath) {
      this.paths.add(fromPath);
   }
}
