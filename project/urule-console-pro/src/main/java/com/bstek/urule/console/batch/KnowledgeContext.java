package com.bstek.urule.console.batch;

import com.bstek.urule.runtime.KnowledgeSession;
import java.util.Map;

public class KnowledgeContext {
   private Map params;
   private KnowledgeSession session;

   public KnowledgeContext(KnowledgeSession session, Map params) {
      this.session = session;
      this.params = params;
   }

   public Map getParams() {
      return this.params;
   }

   public void setParams(Map params) {
      this.params = params;
   }

   public KnowledgeSession getSession() {
      return this.session;
   }

   public void setSession(KnowledgeSession session) {
      this.session = session;
   }
}
