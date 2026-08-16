package com.bstek.urule.console.editor.store;

import com.bstek.urule.Utils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collection;
import java.util.HashMap;

public class StoreTools {
   private static ClipboardStore a;
   private static ObjectMapper b = new ObjectMapper();

   public static void setAttribute(String var0, Object var1) {
      a.set(var0, a(var1));
   }

   public static void removeAttribute(String var0) {
      a.remove(var0);
   }

   public static Object getAttribute(String var0) {
      try {
         String var1 = a.get(var0);
         return StringUtils.isNotBlank(var1) ? b.readValue(var1, HashMap.class) : null;
      } catch (Exception var2) {
         throw new RuleException(var2);
      }
   }

   private static String a(Object var0) {
      try {
         String var1 = b.writeValueAsString(var0);
         return var1;
      } catch (Exception var3) {
         throw new RuleException(var3);
      }
   }

   static {
      Collection var0 = Utils.getApplicationContext().getBeansOfType(ClipboardStore.class).values();
      if (var0.size() > 0) {
         a = (ClipboardStore)var0.iterator().next();
      } else {
         a = new SessionClipboardStore();
      }

   }
}
