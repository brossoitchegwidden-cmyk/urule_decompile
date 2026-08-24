package com.bstek.urule.console.editor.store;

public interface ClipboardStore {
   void set(String key, String value);

   String get(String key);

   void remove(String key);
}
