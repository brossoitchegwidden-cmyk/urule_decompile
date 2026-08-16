package com.bstek.urule.console.batch;

import com.bstek.urule.runtime.KnowledgeSession;
import java.util.Map;

public class KnowledgeContext {
   private Map a;
   private KnowledgeSession b;

   public KnowledgeContext(KnowledgeSession var1, Map var2) {
      this.b = var1;
      this.a = var2;
   }

   public Map getParams() {
      return this.a;
   }

   public void setParams(Map var1) {
      this.a = var1;
   }

   public KnowledgeSession getSession() {
      return this.b;
   }

   public void setSession(KnowledgeSession var1) {
      this.b = var1;
   }
}
