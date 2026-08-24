package com.bstek.urule.runtime.rete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public abstract class AbstractActivity implements Activity {
   private List<Path> paths;
   protected String activityId = UUID.randomUUID().toString();

   @Override
   public List<Path> getPaths() {
      return this.paths;
   }

   public void addPath(Path path) {
      if (this.paths == null) {
         this.paths = new ArrayList<>();
      }

      this.paths.add(path);
   }

   protected List<FactTracker> visitPahs(EvaluationContext context, Object obj, FactTracker tracker) {
      ArrayList visitPahsResult = new ArrayList();
      if (this.paths != null && this.paths.size() != 0) {
         int number = this.paths.size();

         for (Path path : this.paths) {
            Collection items = null;
            AbstractActivity to = (AbstractActivity)path.getTo();
            Set pathPassedSet = context.getPathPassedSet();
            pathPassedSet.add(path.getId());
            boolean flag = to.orNodeTokensExist(context, tracker.getTokens());
            if (!flag) {
               if (number > 1) {
                  FactTracker factTracker = tracker.newSubFactTracker();
                  factTracker.setCurrentPath(path);
                  items = to.enter(context, obj, factTracker);
               } else {
                  tracker.setCurrentPath(path);
                  items = to.enter(context, obj, tracker);
               }

               if (items != null) {
                  visitPahsResult.addAll(items);
               }
            }
         }

         return visitPahsResult;
      } else {
         return visitPahsResult;
      }
   }

   @Override
   public boolean orNodeTokensExist(EvaluationContext context, Set<Integer> tokens) {
      List paths = this.getPaths();
      if (paths != null && paths.size() == 1) {
         Path path = (Path)paths.get(0);
         AbstractActivity to = (AbstractActivity)path.getTo();
         return to.orNodeTokensExist(context, tokens);
      } else {
         return false;
      }
   }
}
