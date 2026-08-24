package com.bstek.urule.console;

public class ContextHolder {
   private static final ThreadLocal projectIdContext = new ThreadLocal();
   private static final ThreadLocal groupIdContext = new ThreadLocal();

   public static void setProjectId(Long projectId) {
      ContextHolder.projectIdContext.set(projectId);
   }

   public static Long getProjectId() {
      return (Long)ContextHolder.projectIdContext.get();
   }

   public static void setGroupId(String groupId) {
      ContextHolder.groupIdContext.set(groupId);
   }

   public static String getGroupId() {
      return (String)ContextHolder.groupIdContext.get();
   }

   public static void clear() {
      ContextHolder.groupIdContext.remove();
      ContextHolder.projectIdContext.remove();
   }
}
