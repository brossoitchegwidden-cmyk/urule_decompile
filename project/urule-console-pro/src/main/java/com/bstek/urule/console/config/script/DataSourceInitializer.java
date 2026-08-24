package com.bstek.urule.console.config.script;

import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.sql.Connection;

public class DataSourceInitializer {
   private static final String SCRIPT_RESOURCE_PREFIX = "com/bstek/urule/console/config/script/urule-";
   private static final String SQLSERVER = "sqlserver";
   private static final String SYBASE = "sybase";

   private static void configureConnection(Connection connection, String text, String text2) throws Exception {
      ScriptRunner scriptRunner = new ScriptRunner(connection);
      scriptRunner.setAutoCommit(false);
      Resources.setCharset(Charset.forName("UTF-8"));
      scriptRunner.setLogWriter((PrintWriter)null);
      scriptRunner.setSendFullScript(false);
      scriptRunner.setEscapeProcessing(false);
      String text3 = ";";
      if ("sqlserver".equals(text) || "sybase".equals(text)) {
         scriptRunner.setSendFullScript(true);
         text3 = "GO";
      }

      scriptRunner.setDelimiter(text3);
      scriptRunner.runScript(Resources.getResourceAsReader(text2 + ".sql"));
   }

   public static void executeSchema(Connection conn, String platform) throws Exception {
      configureConnection(conn, platform, SCRIPT_RESOURCE_PREFIX + "schema-" + platform);
   }

   public static void executeInitData(Connection conn, String platform) throws Exception {
      if (!"sqlserver".equalsIgnoreCase(platform) && !"sybase".equalsIgnoreCase(platform)) {
         configureConnection(conn, platform, SCRIPT_RESOURCE_PREFIX + "data");
      } else {
         configureConnection(conn, platform, SCRIPT_RESOURCE_PREFIX + "data-sqlserver");
      }

   }
}
