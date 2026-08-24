package com.bstek.urule.console.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class HttpSessionKnowledgeCache {
   private Map sessionObjectsById = new HashMap();

   public Object get(HttpServletRequest req, String key) {
      SessionObject sessionObject = this.getOrCreateSessionObject(req);
      return sessionObject.get(key);
   }

   public void put(HttpServletRequest req, String key, Object obj) {
      SessionObject sessionObject = this.getOrCreateSessionObject(req);
      sessionObject.put(key, obj);
   }

   public void remove(HttpServletRequest req, String key) {
      SessionObject sessionObject = this.getOrCreateSessionObject(req);
      sessionObject.remove(key);
   }

   private SessionObject getOrCreateSessionObject(HttpServletRequest httpServletRequest) {
      this.removeExpiredSessions();
      String id = httpServletRequest.getSession().getId();
      SessionObject sessionObject = null;
      if (this.sessionObjectsById.containsKey(id)) {
         sessionObject = (SessionObject)this.sessionObjectsById.get(id);
      } else {
         sessionObject = new SessionObject();
         this.sessionObjectsById.put(id, sessionObject);
      }

      return sessionObject;
   }

   private void removeExpiredSessions() {
      ArrayList items = new ArrayList();

      for(String text : (Iterable<String>)(Iterable<?>)(this.sessionObjectsById.keySet())) {
         SessionObject sessionObject = (SessionObject)this.sessionObjectsById.get(text);
         if (sessionObject.isExpired()) {
            items.add(text);
         }
      }

      for(String text2 : (Iterable<String>)(Iterable<?>)(items)) {
         this.sessionObjectsById.remove(text2);
      }

   }
}
