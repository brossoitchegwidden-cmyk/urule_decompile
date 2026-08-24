package com.bstek.urule.console.editor.store;

import com.bstek.urule.Utils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collection;
import java.util.HashMap;

public class StoreTools {
   private static ClipboardStore clipboardStore;
   private static ObjectMapper objectMapper = new ObjectMapper();

   public static void setAttribute(String key, Object obj) {
      StoreTools.clipboardStore.set(key, serializeClipboardValue(obj));
   }

   public static void removeAttribute(String key) {
      StoreTools.clipboardStore.remove(key);
   }

   public static Object getAttribute(String key) {
      try {
         String text = StoreTools.clipboardStore.get(key);
         return StringUtils.isNotBlank(text) ? StoreTools.objectMapper.readValue(text, HashMap.class) : null;
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   private static String serializeClipboardValue(Object objectValue) {
      try {
         String text = StoreTools.objectMapper.writeValueAsString(objectValue);
         return text;
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   static {
      Collection clipboardStores = Utils.getApplicationContext().getBeansOfType(ClipboardStore.class).values();
      if (clipboardStores.size() > 0) {
         StoreTools.clipboardStore = (ClipboardStore)clipboardStores.iterator().next();
      } else {
         StoreTools.clipboardStore = new SessionClipboardStore();
      }

   }
}
