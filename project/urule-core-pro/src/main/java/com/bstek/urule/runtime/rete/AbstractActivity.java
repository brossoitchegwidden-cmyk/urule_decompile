package com.bstek.urule.runtime.rete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public abstract class AbstractActivity implements Activity {
   private List<Path> b;
   protected String a = UUID.randomUUID().toString();

   @Override
   public List<Path> getPaths() {
      return this.b;
   }

   public void addPath(Path var1) {
      if (this.b == null) {
         this.b = new ArrayList<>();
      }

      this.b.add(var1);
   }

   protected List<FactTracker> a(EvaluationContext var1, Object var2, FactTracker var3) {
      ArrayList var4 = new ArrayList();
      if (this.b != null && this.b.size() != 0) {
         int var5 = this.b.size();

         for (Path var7 : this.b) {
            Collection var8 = null;
            AbstractActivity var9 = (AbstractActivity)var7.getTo();
            Set var10 = var1.getPathPassedSet();
            var10.add(var7.getId());
            boolean var11 = var9.orNodeTokensExist(var1, var3.getTokens());
            if (!var11) {
               if (var5 > 1) {
                  FactTracker var12 = var3.newSubFactTracker();
                  var12.setCurrentPath(var7);
                  var8 = var9.enter(var1, var2, var12);
               } else {
                  var3.setCurrentPath(var7);
                  var8 = var9.enter(var1, var2, var3);
               }

               if (var8 != null) {
                  var4.addAll(var8);
               }
            }
         }

         return var4;
      } else {
         return var4;
      }
   }

   @Override
   public boolean orNodeTokensExist(EvaluationContext var1, Set<Integer> var2) {
      List var3 = this.getPaths();
      if (var3 != null && var3.size() == 1) {
         Path var4 = (Path)var3.get(0);
         AbstractActivity var5 = (AbstractActivity)var4.getTo();
         return var5.orNodeTokensExist(var1, var2);
      } else {
         return false;
      }
   }
}
