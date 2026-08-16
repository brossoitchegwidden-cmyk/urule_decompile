package com.bstek.urule.console.config.script;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScriptRunner {
   private static final String a = System.getProperty("line.separator", "\n");
   private static final String b = ";";
   private static final Pattern c = Pattern.compile("^\\s*((--)|(//))?\\s*(//)?\\s*@DELIMITER\\s+([^\\s]+)", 2);
   private final Connection d;
   private boolean e;
   private boolean f;
   private boolean g;
   private boolean h;
   private boolean i;
   private boolean j = true;
   private PrintWriter k;
   private PrintWriter l;
   private String m;
   private boolean n;

   public ScriptRunner(Connection var1) {
      this.k = new PrintWriter(System.out);
      this.l = new PrintWriter(System.err);
      this.m = ";";
      this.d = var1;
   }

   public void setStopOnError(boolean var1) {
      this.e = var1;
   }

   public void setThrowWarning(boolean var1) {
      this.f = var1;
   }

   public void setAutoCommit(boolean var1) {
      this.g = var1;
   }

   public void setSendFullScript(boolean var1) {
      this.h = var1;
   }

   public void setRemoveCRs(boolean var1) {
      this.i = var1;
   }

   public void setEscapeProcessing(boolean var1) {
      this.j = var1;
   }

   public void setLogWriter(PrintWriter var1) {
      this.k = var1;
   }

   public void setErrorLogWriter(PrintWriter var1) {
      this.l = var1;
   }

   public void setDelimiter(String var1) {
      this.m = var1;
   }

   public void setFullLineDelimiter(boolean var1) {
      this.n = var1;
   }

   public void runScript(Reader var1) {
      this.a();

      try {
         if (this.h) {
            this.a(var1);
         } else {
            this.b(var1);
         }
      } catch (Exception var6) {
         throw var6;
      } finally {
         this.c();
      }

   }

   private void a(Reader var1) {
      StringBuilder var2 = new StringBuilder();

      try {
         BufferedReader var3 = new BufferedReader(var1);

         String var7;
         while((var7 = var3.readLine()) != null) {
            var2.append(var7);
            var2.append(a);
         }

         String var5 = var2.toString();
         this.b((Object)var5);
         this.c(var5);
         this.b();
      } catch (Exception var6) {
         String var4 = "Error executing: " + var2 + ".  Cause: " + var6;
         this.c((Object)var4);
         throw new RuntimeException(var4, var6);
      }
   }

   private void b(Reader var1) {
      StringBuilder var2 = new StringBuilder();

      try {
         BufferedReader var3 = new BufferedReader(var1);

         String var6;
         while((var6 = var3.readLine()) != null) {
            this.a(var2, var6);
         }

         this.b();
         this.a(var2);
      } catch (Exception var5) {
         String var4 = "Error executing: " + var2 + ".  Cause: " + var5;
         this.c((Object)var4);
         throw new RuntimeException(var4, var5);
      }
   }

   public void closeConnection() {
      try {
         this.d.close();
      } catch (Exception var2) {
      }

   }

   private void a() {
      try {
         if (this.g != this.d.getAutoCommit()) {
            this.d.setAutoCommit(this.g);
         }

      } catch (Throwable var2) {
         throw new RuntimeException("Could not set AutoCommit to " + this.g + ". Cause: " + var2, var2);
      }
   }

   private void b() {
      try {
         if (!this.d.getAutoCommit()) {
         }

      } catch (Throwable var2) {
         throw new RuntimeException("Could not commit transaction. Cause: " + var2, var2);
      }
   }

   private void c() {
      try {
         if (!this.d.getAutoCommit()) {
         }
      } catch (Throwable var2) {
      }

   }

   private void a(StringBuilder var1) {
      if (var1 != null && var1.toString().trim().length() > 0) {
         throw new RuntimeException("Line missing end-of-line terminator (" + this.m + ") => " + var1);
      }
   }

   private void a(StringBuilder var1, String var2) throws SQLException {
      String var3 = var2.trim();
      if (this.a(var3)) {
         Matcher var4 = c.matcher(var3);
         if (var4.find()) {
            this.m = var4.group(5);
         }

         this.b((Object)var3);
      } else if (this.b(var3)) {
         var1.append(var2.substring(0, var2.lastIndexOf(this.m)));
         var1.append(a);
         this.b((Object)var1);
         this.c(var1.toString());
         var1.setLength(0);
      } else if (var3.length() > 0) {
         var1.append(var2);
         var1.append(a);
      }

   }

   private boolean a(String var1) {
      return var1.startsWith("//") || var1.startsWith("--");
   }

   private boolean b(String var1) {
      return !this.n && var1.contains(this.m) || this.n && var1.equals(this.m);
   }

   private void c(String var1) throws SQLException {
      SQLException var2 = null;
      boolean var3 = false;
      Statement var4 = this.d.createStatement();
      var4.setEscapeProcessing(this.j);
      String var5 = var1;
      if (this.i) {
         var5 = var1.replaceAll("\r\n", "\n");
      }

      if (this.e) {
         var3 = var4.execute(var5);
         if (this.f) {
            SQLWarning var6 = var4.getWarnings();
            if (var6 != null) {
               throw var6;
            }
         }
      } else {
         try {
            var3 = var4.execute(var5);
         } catch (SQLException var9) {
            String var7 = "Error executing: " + var1 + ".  Cause: " + var9;
            this.c((Object)var7);
            var2 = var9;
         }
      }

      this.a(var4, var3);

      try {
         var4.close();
      } catch (Exception var8) {
         throw var8;
      }

      if (var2 != null) {
         throw var2;
      }
   }

   private void a(Statement var1, boolean var2) {
      try {
         if (var2) {
            ResultSet var3 = var1.getResultSet();
            if (var3 != null) {
               ResultSetMetaData var4 = var3.getMetaData();
               int var5 = var4.getColumnCount();

               for(int var6 = 0; var6 < var5; ++var6) {
                  String var7 = var4.getColumnLabel(var6 + 1);
                  this.a((Object)(var7 + "\t"));
               }

               this.b((Object)"");

               while(var3.next()) {
                  for(int var9 = 0; var9 < var5; ++var9) {
                     String var10 = var3.getString(var9 + 1);
                     this.a((Object)(var10 + "\t"));
                  }

                  this.b((Object)"");
               }
            }
         }
      } catch (SQLException var8) {
         this.c((Object)("Error printing results: " + var8.getMessage()));
      }

   }

   private void a(Object var1) {
      if (this.k != null) {
         this.k.print(var1);
         this.k.flush();
      }

   }

   private void b(Object var1) {
      if (this.k != null) {
         this.k.println(var1);
         this.k.flush();
      }

   }

   private void c(Object var1) {
      if (this.l != null) {
         this.l.println(var1);
         this.l.flush();
      }

   }
}
