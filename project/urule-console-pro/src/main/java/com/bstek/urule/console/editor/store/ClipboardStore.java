package com.bstek.urule.console.editor.store;

public interface ClipboardStore {
   void set(String var1, String var2);

   String get(String var1);

   void remove(String var1);
}
