package com.bstek.urule;

import java.io.File;

public class SystemUtils {
   private static final String OS_NAME_WINDOWS_PREFIX = "Windows";
   private static final String USER_HOME_KEY = "user.home";
   private static final String USER_DIR_KEY = "user.dir";
   private static final String JAVA_IO_TMPDIR_KEY = "java.io.tmpdir";
   private static final String JAVA_HOME_KEY = "java.home";
   public static final String AWT_TOOLKIT = getSystemProperty("awt.toolkit");
   public static final String FILE_ENCODING = getSystemProperty("file.encoding");
   public static final String FILE_SEPARATOR = getSystemProperty("file.separator");
   public static final String JAVA_AWT_FONTS = getSystemProperty("java.awt.fonts");
   public static final String JAVA_AWT_GRAPHICSENV = getSystemProperty("java.awt.graphicsenv");
   public static final String JAVA_AWT_HEADLESS = getSystemProperty("java.awt.headless");
   public static final String JAVA_AWT_PRINTERJOB = getSystemProperty("java.awt.printerjob");
   public static final String JAVA_CLASS_PATH = getSystemProperty("java.class.path");
   public static final String JAVA_CLASS_VERSION = getSystemProperty("java.class.version");
   public static final String JAVA_COMPILER = getSystemProperty("java.compiler");
   public static final String JAVA_ENDORSED_DIRS = getSystemProperty("java.endorsed.dirs");
   public static final String JAVA_EXT_DIRS = getSystemProperty("java.ext.dirs");
   public static final String JAVA_HOME = getSystemProperty(JAVA_HOME_KEY);
   public static final String JAVA_IO_TMPDIR = getSystemProperty(JAVA_IO_TMPDIR_KEY);
   public static final String JAVA_LIBRARY_PATH = getSystemProperty("java.library.path");
   public static final String JAVA_RUNTIME_NAME = getSystemProperty("java.runtime.name");
   public static final String JAVA_RUNTIME_VERSION = getSystemProperty("java.runtime.version");
   public static final String JAVA_SPECIFICATION_NAME = getSystemProperty("java.specification.name");
   public static final String JAVA_SPECIFICATION_VENDOR = getSystemProperty("java.specification.vendor");
   public static final String JAVA_SPECIFICATION_VERSION = getSystemProperty("java.specification.version");
   public static final String JAVA_UTIL_PREFS_PREFERENCES_FACTORY = getSystemProperty("java.util.prefs.PreferencesFactory");
   public static final String JAVA_VENDOR = getSystemProperty("java.vendor");
   public static final String JAVA_VENDOR_URL = getSystemProperty("java.vendor.url");
   public static final String JAVA_VERSION = getSystemProperty("java.version");
   public static final String JAVA_VM_INFO = getSystemProperty("java.vm.info");
   public static final String JAVA_VM_NAME = getSystemProperty("java.vm.name");
   public static final String JAVA_VM_SPECIFICATION_NAME = getSystemProperty("java.vm.specification.name");
   public static final String JAVA_VM_SPECIFICATION_VENDOR = getSystemProperty("java.vm.specification.vendor");
   public static final String JAVA_VM_SPECIFICATION_VERSION = getSystemProperty("java.vm.specification.version");
   public static final String JAVA_VM_VENDOR = getSystemProperty("java.vm.vendor");
   public static final String JAVA_VM_VERSION = getSystemProperty("java.vm.version");
   public static final String LINE_SEPARATOR = getSystemProperty("line.separator");
   public static final String OS_ARCH = getSystemProperty("os.arch");
   public static final String OS_NAME = getSystemProperty("os.name");
   public static final String OS_VERSION = getSystemProperty("os.version");
   public static final String PATH_SEPARATOR = getSystemProperty("path.separator");
   public static final String USER_COUNTRY = getSystemProperty("user.country") == null ? getSystemProperty("user.region") : getSystemProperty("user.country");
   public static final String USER_DIR = getSystemProperty(USER_DIR_KEY);
   public static final String USER_HOME = getSystemProperty(USER_HOME_KEY);
   public static final String USER_LANGUAGE = getSystemProperty("user.language");
   public static final String USER_NAME = getSystemProperty("user.name");
   public static final String USER_TIMEZONE = getSystemProperty("user.timezone");
   public static final String JAVA_VERSION_TRIMMED = getJavaVersionTrimmed();
   public static final boolean IS_JAVA_1_1 = getJavaVersionMatches("1.1");
   public static final boolean IS_JAVA_1_2 = getJavaVersionMatches("1.2");
   public static final boolean IS_JAVA_1_3 = getJavaVersionMatches("1.3");
   public static final boolean IS_JAVA_1_4 = getJavaVersionMatches("1.4");
   public static final boolean IS_JAVA_1_5 = getJavaVersionMatches("1.5");
   public static final boolean IS_JAVA_1_6 = getJavaVersionMatches("1.6");
   public static final boolean IS_JAVA_1_7 = getJavaVersionMatches("1.7");
   public static final boolean IS_OS_AIX = getOSMatchesName("AIX");
   public static final boolean IS_OS_HP_UX = getOSMatchesName("HP-UX");
   public static final boolean IS_OS_IRIX = getOSMatchesName("Irix");
   public static final boolean IS_OS_LINUX = getOSMatchesName("Linux") || getOSMatchesName("LINUX");
   public static final boolean IS_OS_MAC = getOSMatchesName("Mac");
   public static final boolean IS_OS_MAC_OSX = getOSMatchesName("Mac OS X");
   public static final boolean IS_OS_OS2 = getOSMatchesName("OS/2");
   public static final boolean IS_OS_SOLARIS = getOSMatchesName("Solaris");
   public static final boolean IS_OS_SUN_OS = getOSMatchesName("SunOS");
   public static final boolean IS_OS_UNIX = IS_OS_AIX || IS_OS_HP_UX || IS_OS_IRIX || IS_OS_LINUX || IS_OS_MAC_OSX || IS_OS_SOLARIS || IS_OS_SUN_OS;
   public static final boolean IS_OS_WINDOWS = getOSMatchesName(OS_NAME_WINDOWS_PREFIX);
   public static final boolean IS_OS_WINDOWS_2000 = getOSMatches(OS_NAME_WINDOWS_PREFIX, "5.0");
   public static final boolean IS_OS_WINDOWS_95 = getOSMatches("Windows 9", "4.0");
   public static final boolean IS_OS_WINDOWS_98 = getOSMatches("Windows 9", "4.1");
   public static final boolean IS_OS_WINDOWS_ME = getOSMatches(OS_NAME_WINDOWS_PREFIX, "4.9");
   public static final boolean IS_OS_WINDOWS_NT = getOSMatchesName("Windows NT");
   public static final boolean IS_OS_WINDOWS_XP = getOSMatches(OS_NAME_WINDOWS_PREFIX, "5.1");
   public static final boolean IS_OS_WINDOWS_VISTA = getOSMatches(OS_NAME_WINDOWS_PREFIX, "6.0");
   public static final boolean IS_OS_WINDOWS_7 = getOSMatches(OS_NAME_WINDOWS_PREFIX, "6.1");

   /**Gets the Java home directory as a File.*/
   public static File getJavaHome() {
      return new File(System.getProperty("java.home"));
   }

   /**Gets the Java IO temporary directory as a File.*/
   public static File getJavaIoTmpDir() {
      return new File(System.getProperty("java.io.tmpdir"));
   }

   private static boolean getJavaVersionMatches(String versionPrefix) {
      return isJavaVersionMatch(JAVA_VERSION_TRIMMED, versionPrefix);
   }

   private static String getJavaVersionTrimmed() {
      if (JAVA_VERSION != null) {
         for (int index = 0; index < JAVA_VERSION.length(); index++) {
            char character = JAVA_VERSION.charAt(index);
            if (character >= '0' && character <= '9') {
               return JAVA_VERSION.substring(index);
            }
         }
      }

      return null;
   }

   private static boolean getOSMatches(String osNamePrefix, String osVersionPrefix) {
      return isOSMatch(OS_NAME, OS_VERSION, osNamePrefix, osVersionPrefix);
   }

   private static boolean getOSMatchesName(String osNamePrefix) {
      return isOSNameMatch(OS_NAME, osNamePrefix);
   }

   private static String getSystemProperty(String property) {
      try {
         return System.getProperty(property);
      } catch (SecurityException exception) {
         System.err.println("Caught a SecurityException reading the system property '" + property + "'; the SystemUtils property value will default to null.");
         return null;
      }
   }

   /**Gets the user directory as a File.*/
   public static File getUserDir() {
      return new File(System.getProperty("user.dir"));
   }

   /**Gets the user home directory as a File.*/
   public static File getUserHome() {
      return new File(System.getProperty("user.home"));
   }

   /**Returns whether the JAVA_AWT_HEADLESS value is true.*/
   public static boolean isJavaAwtHeadless() {
      return JAVA_AWT_HEADLESS != null ? JAVA_AWT_HEADLESS.equals(Boolean.TRUE.toString()) : false;
   }

   static boolean isJavaVersionMatch(String version, String versionPrefix) {
      return version == null ? false : version.startsWith(versionPrefix);
   }

   static boolean isOSMatch(String osName, String osVersion, String osNamePrefix, String osVersionPrefix) {
      return osName != null && osVersion != null ? osName.startsWith(osNamePrefix) && osVersion.startsWith(osVersionPrefix) : false;
   }

   static boolean isOSNameMatch(String osName, String osNamePrefix) {
      return osName == null ? false : osName.startsWith(osNamePrefix);
   }
}
