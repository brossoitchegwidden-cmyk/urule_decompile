package com.bstek.urule.console.database.service.file;

import com.bstek.urule.console.database.model.RuleFile;
import java.util.HashMap;
import java.util.Map;

public class CopyContextHolder {
   private static final ThreadLocal idReplacementContext = new ThreadLocal();
   private static final ThreadLocal copiedFileContext = new ThreadLocal();

   public static Map getIdMap() {
      Object objectValue = (Map)CopyContextHolder.idReplacementContext.get();
      if (objectValue == null) {
         objectValue = new HashMap();
         CopyContextHolder.idReplacementContext.set(objectValue);
      }

      return (Map)objectValue;
   }

   public static Map getFileMap() {
      Object objectValue = (Map)CopyContextHolder.copiedFileContext.get();
      if (objectValue == null) {
         objectValue = new HashMap();
         CopyContextHolder.copiedFileContext.set(objectValue);
      }

      return (Map)objectValue;
   }

   public static void addId(long id, long newId) {
      Map idMap = getIdMap();
      String text = "id=\"" + id + "\"";
      String text2 = "id=\"" + newId + "\"";
      idMap.put(text, text2);
      text = "file=\"" + id + "\"";
      text2 = "file=\"" + newId + "\"";
      idMap.put(text, text2);
   }

   public static void addFile(long id, RuleFile ruleFile) {
      Map fileMap = getFileMap();
      fileMap.put(id, ruleFile);
   }

   public static void clear() {
      CopyContextHolder.idReplacementContext.remove();
      CopyContextHolder.copiedFileContext.remove();
   }
}
