package com.bstek.urule;

import java.io.File;

public class SystemUtils {
   private static final String a = "Windows";
   private static final String b = "user.home";
   private static final String c = "user.dir";
   private static final String d = "java.io.tmpdir";
   private static final String e = "java.home";
   public static final String AWT_TOOLKIT = c("awt.toolkit");
   public static final String FILE_ENCODING = c("file.encoding");
   public static final String FILE_SEPARATOR = c("file.separator");
   public static final String JAVA_AWT_FONTS = c("java.awt.fonts");
   public static final String JAVA_AWT_GRAPHICSENV = c("java.awt.graphicsenv");
   public static final String JAVA_AWT_HEADLESS = c("java.awt.headless");
   public static final String JAVA_AWT_PRINTERJOB = c("java.awt.printerjob");
   public static final String JAVA_CLASS_PATH = c("java.class.path");
   public static final String JAVA_CLASS_VERSION = c("java.class.version");
   public static final String JAVA_COMPILER = c("java.compiler");
   public static final String JAVA_ENDORSED_DIRS = c("java.endorsed.dirs");
   public static final String JAVA_EXT_DIRS = c("java.ext.dirs");
   public static final String JAVA_HOME = c("java.home");
   public static final String JAVA_IO_TMPDIR = c("java.io.tmpdir");
   public static final String JAVA_LIBRARY_PATH = c("java.library.path");
   public static final String JAVA_RUNTIME_NAME = c("java.runtime.name");
   public static final String JAVA_RUNTIME_VERSION = c("java.runtime.version");
   public static final String JAVA_SPECIFICATION_NAME = c("java.specification.name");
   public static final String JAVA_SPECIFICATION_VENDOR = c("java.specification.vendor");
   public static final String JAVA_SPECIFICATION_VERSION = c("java.specification.version");
   public static final String JAVA_UTIL_PREFS_PREFERENCES_FACTORY = c("java.util.prefs.PreferencesFactory");
   public static final String JAVA_VENDOR = c("java.vendor");
   public static final String JAVA_VENDOR_URL = c("java.vendor.url");
   public static final String JAVA_VERSION = c("java.version");
   public static final String JAVA_VM_INFO = c("java.vm.info");
   public static final String JAVA_VM_NAME = c("java.vm.name");
   public static final String JAVA_VM_SPECIFICATION_NAME = c("java.vm.specification.name");
   public static final String JAVA_VM_SPECIFICATION_VENDOR = c("java.vm.specification.vendor");
   public static final String JAVA_VM_SPECIFICATION_VERSION = c("java.vm.specification.version");
   public static final String JAVA_VM_VENDOR = c("java.vm.vendor");
   public static final String JAVA_VM_VERSION = c("java.vm.version");
   public static final String LINE_SEPARATOR = c("line.separator");
   public static final String OS_ARCH = c("os.arch");
   public static final String OS_NAME = c("os.name");
   public static final String OS_VERSION = c("os.version");
   public static final String PATH_SEPARATOR = c("path.separator");
   public static final String USER_COUNTRY = c("user.country") == null ? c("user.region") : c("user.country");
   public static final String USER_DIR = c("user.dir");
   public static final String USER_HOME = c("user.home");
   public static final String USER_LANGUAGE = c("user.language");
   public static final String USER_NAME = c("user.name");
   public static final String USER_TIMEZONE = c("user.timezone");
   public static final String JAVA_VERSION_TRIMMED = a();
   public static final boolean IS_JAVA_1_1 = a("1.1");
   public static final boolean IS_JAVA_1_2 = a("1.2");
   public static final boolean IS_JAVA_1_3 = a("1.3");
   public static final boolean IS_JAVA_1_4 = a("1.4");
   public static final boolean IS_JAVA_1_5 = a("1.5");
   public static final boolean IS_JAVA_1_6 = a("1.6");
   public static final boolean IS_JAVA_1_7 = a("1.7");
   public static final boolean IS_OS_AIX = b("AIX");
   public static final boolean IS_OS_HP_UX = b("HP-UX");
   public static final boolean IS_OS_IRIX = b("Irix");
   public static final boolean IS_OS_LINUX = b("Linux") || b("LINUX");
   public static final boolean IS_OS_MAC = b("Mac");
   public static final boolean IS_OS_MAC_OSX = b("Mac OS X");
   public static final boolean IS_OS_OS2 = b("OS/2");
   public static final boolean IS_OS_SOLARIS = b("Solaris");
   public static final boolean IS_OS_SUN_OS = b("SunOS");
   public static final boolean IS_OS_UNIX = IS_OS_AIX || IS_OS_HP_UX || IS_OS_IRIX || IS_OS_LINUX || IS_OS_MAC_OSX || IS_OS_SOLARIS || IS_OS_SUN_OS;
   public static final boolean IS_OS_WINDOWS = b("Windows");
   public static final boolean IS_OS_WINDOWS_2000 = c("Windows", "5.0");
   public static final boolean IS_OS_WINDOWS_95 = c("Windows 9", "4.0");
   public static final boolean IS_OS_WINDOWS_98 = c("Windows 9", "4.1");
   public static final boolean IS_OS_WINDOWS_ME = c("Windows", "4.9");
   public static final boolean IS_OS_WINDOWS_NT = b("Windows NT");
   public static final boolean IS_OS_WINDOWS_XP = c("Windows", "5.1");
   public static final boolean IS_OS_WINDOWS_VISTA = c("Windows", "6.0");
   public static final boolean IS_OS_WINDOWS_7 = c("Windows", "6.1");

   public static File getJavaHome() {
      return new File(System.getProperty("java.home"));
   }

   public static File getJavaIoTmpDir() {
      return new File(System.getProperty("java.io.tmpdir"));
   }

   private static boolean a(String var0) {
      return a(JAVA_VERSION_TRIMMED, var0);
   }

   private static String a() {
      if (JAVA_VERSION != null) {
         for (int var0 = 0; var0 < JAVA_VERSION.length(); var0++) {
            char var1 = JAVA_VERSION.charAt(var0);
            if (var1 >= '0' && var1 <= '9') {
               return JAVA_VERSION.substring(var0);
            }
         }
      }

      return null;
   }

   private static boolean c(String var0, String var1) {
      return a(OS_NAME, OS_VERSION, var0, var1);
   }

   private static boolean b(String var0) {
      return b(OS_NAME, var0);
   }

   private static String c(String var0) {
      try {
         return System.getProperty(var0);
      } catch (SecurityException var2) {
         System.err.println("Caught a SecurityException reading the system property '" + var0 + "'; the SystemUtils property value will default to null.");
         return null;
      }
   }

   public static File getUserDir() {
      return new File(System.getProperty("user.dir"));
   }

   public static File getUserHome() {
      return new File(System.getProperty("user.home"));
   }

   public static boolean isJavaAwtHeadless() {
      return JAVA_AWT_HEADLESS != null ? JAVA_AWT_HEADLESS.equals(Boolean.TRUE.toString()) : false;
   }

   static boolean a(String var0, String var1) {
      return var0 == null ? false : var0.startsWith(var1);
   }

   static boolean a(String var0, String var1, String var2, String var3) {
      return var0 != null && var1 != null ? var0.startsWith(var2) && var1.startsWith(var3) : false;
   }

   static boolean b(String var0, String var1) {
      return var0 == null ? false : var0.startsWith(var1);
   }
}
