package com.bstek.urule.console.editor.store;

import com.bstek.urule.console.RequestHolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SessionClipboardStore implements ClipboardStore {
   private Map clipboardsBySessionId = new HashMap();

   protected SessionClipboardStore() {
   }

   public void set(String key, String value) {
      this.initializeState();
      String id = RequestHolder.getRequest().getSession().getId();
      ObjectData objectData = null;
      if (this.clipboardsBySessionId.containsKey(id)) {
         objectData = (ObjectData)this.clipboardsBySessionId.get(id);
      } else {
         objectData = new ObjectData();
         this.clipboardsBySessionId.put(id, objectData);
      }

      objectData.putObject(key, value);
   }

   public String get(String key) {
      this.initializeState();
      String id = RequestHolder.getRequest().getSession().getId();
      if (this.clipboardsBySessionId.containsKey(id)) {
         ObjectData objectData = (ObjectData)this.clipboardsBySessionId.get(id);
         return objectData.getObject(key);
      } else {
         return null;
      }
   }

   public void remove(String key) {
      this.initializeState();
      String id = RequestHolder.getRequest().getSession().getId();
      if (this.clipboardsBySessionId.containsKey(id)) {
         ObjectData objectData = (ObjectData)this.clipboardsBySessionId.get(id);
         objectData.removeObject(key);
      }

   }

   private void initializeState() {
      ArrayList items = new ArrayList();

      for(String text : (Iterable<String>)(Iterable<?>)(this.clipboardsBySessionId.keySet())) {
         ObjectData objectData = (ObjectData)this.clipboardsBySessionId.get(text);
         if (objectData.overdue()) {
            items.add(text);
         }
      }

      for(String text2 : (Iterable<String>)(Iterable<?>)(items)) {
         this.clipboardsBySessionId.remove(text2);
      }

   }
}
