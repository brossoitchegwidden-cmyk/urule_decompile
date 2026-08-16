package com.bstek.urule.console.config.script;

import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.sql.Connection;

public class DataSourceInitializer {
   private static String a = "com/bstek/urule/console/config/script/urule-";
   private static final String b = "sqlserver";
   private static final String c = "sybase";
   private static final String d = ";";

   private static void a(Connection var0, String var1, String var2) throws Exception {
      ScriptRunner var3 = new ScriptRunner(var0);
      var3.setAutoCommit(false);
      Resources.setCharset(Charset.forName("UTF-8"));
      var3.setLogWriter((PrintWriter)null);
      var3.setSendFullScript(false);
      var3.setEscapeProcessing(false);
      String var4 = ";";
      if ("sqlserver".equals(var1) || "sybase".equals(var1)) {
         var3.setSendFullScript(true);
         var4 = "GO";
      }

      var3.setDelimiter(var4);
      var3.runScript(Resources.getResourceAsReader(var2 + ".sql"));
   }

   public static void executeSchema(Connection var0, String var1) throws Exception {
      a(var0, var1, a + "schema-" + var1);
   }

   public static void executeInitData(Connection var0, String var1) throws Exception {
      if (!"sqlserver".equalsIgnoreCase(var1) && !"sybase".equalsIgnoreCase(var1)) {
         a(var0, var1, a + "data");
      } else {
         a(var0, var1, a + "data-sqlserver");
      }

   }
}
