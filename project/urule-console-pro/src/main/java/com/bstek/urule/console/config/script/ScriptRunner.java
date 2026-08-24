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

/**
 * Executes SQL scripts line by line or as a whole, with configurable delimiter
 * and transaction handling. Based on the MyBatis ScriptRunner contract.
 */
public class ScriptRunner {
   private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
   private static final String DEFAULT_DELIMITER = ";";
   private static final Pattern DELIMITER_PATTERN = Pattern.compile("^\\s*((--)|(//))?\\s*(//)?\\s*@DELIMITER\\s+([^\\s]+)", 2);
   private final Connection connection;
   private boolean stopOnError;
   private boolean throwWarning;
   private boolean autoCommit;
   private boolean sendFullScript;
   private boolean removeCRs;
   private boolean escapeProcessing = true;
   private PrintWriter logWriter;
   private PrintWriter errorLogWriter;
   private String delimiter;
   private boolean fullLineDelimiter;

   public ScriptRunner(Connection connection) {
      this.logWriter = new PrintWriter(System.out);
      this.errorLogWriter = new PrintWriter(System.err);
      this.delimiter = DEFAULT_DELIMITER;
      this.connection = connection;
   }

   public void setStopOnError(boolean stopOnError) {
      this.stopOnError = stopOnError;
   }

   public void setThrowWarning(boolean throwWarning) {
      this.throwWarning = throwWarning;
   }

   public void setAutoCommit(boolean autoCommit) {
      this.autoCommit = autoCommit;
   }

   public void setSendFullScript(boolean sendFullScript) {
      this.sendFullScript = sendFullScript;
   }

   public void setRemoveCRs(boolean removeCRs) {
      this.removeCRs = removeCRs;
   }

   public void setEscapeProcessing(boolean escapeProcessing) {
      this.escapeProcessing = escapeProcessing;
   }

   public void setLogWriter(PrintWriter logWriter) {
      this.logWriter = logWriter;
   }

   public void setErrorLogWriter(PrintWriter errorLogWriter) {
      this.errorLogWriter = errorLogWriter;
   }

   public void setDelimiter(String delimiter) {
      this.delimiter = delimiter;
   }

   public void setFullLineDelimiter(boolean fullLineDelimiter) {
      this.fullLineDelimiter = fullLineDelimiter;
   }

   public void runScript(Reader reader) {
      this.setAutoCommit();

      try {
         if (this.sendFullScript) {
            this.executeFullScript(reader);
         } else {
            this.executeLineByLine(reader);
         }
      } catch (Exception exception) {
         throw exception;
      } finally {
         this.rollbackConnection();
      }

   }

   private void executeFullScript(Reader reader) {
      StringBuilder script = new StringBuilder();

      try {
         BufferedReader bufferedReader = new BufferedReader(reader);

         String line;
         while((line = bufferedReader.readLine()) != null) {
            script.append(line);
            script.append(LINE_SEPARATOR);
         }

         String sql = script.toString();
         this.println(sql);
         this.executeStatement(sql);
         this.commitConnection();
      } catch (Exception exception) {
         String message = "Error executing: " + script + ".  Cause: " + exception;
         this.error(message);
         throw new RuntimeException(message, exception);
      }
   }

   private void executeLineByLine(Reader reader) {
      StringBuilder command = new StringBuilder();

      try {
         BufferedReader bufferedReader = new BufferedReader(reader);

         String line;
         while((line = bufferedReader.readLine()) != null) {
            this.handleLine(command, line);
         }

         this.commitConnection();
         this.checkForMissingLineTerminator(command);
      } catch (Exception exception) {
         String message = "Error executing: " + command + ".  Cause: " + exception;
         this.error(message);
         throw new RuntimeException(message, exception);
      }
   }

   public void closeConnection() {
      try {
         this.connection.close();
      } catch (Exception exception) {
      }

   }

   private void setAutoCommit() {
      try {
         if (this.autoCommit != this.connection.getAutoCommit()) {
            this.connection.setAutoCommit(this.autoCommit);
         }

      } catch (Throwable throwable) {
         throw new RuntimeException("Could not set AutoCommit to " + this.autoCommit + ". Cause: " + throwable, throwable);
      }
   }

   private void commitConnection() {
      try {
         if (!this.connection.getAutoCommit()) {
            this.connection.commit();
         }

      } catch (Throwable throwable) {
         throw new RuntimeException("Could not commit transaction. Cause: " + throwable, throwable);
      }
   }

   private void rollbackConnection() {
      try {
         if (!this.connection.getAutoCommit()) {
            this.connection.rollback();
         }
      } catch (Throwable throwable) {
      }

   }

   private void checkForMissingLineTerminator(StringBuilder command) {
      if (command != null && command.toString().trim().length() > 0) {
         throw new RuntimeException("Line missing end-of-line terminator (" + this.delimiter + ") => " + command);
      }
   }

   private void handleLine(StringBuilder command, String line) throws SQLException {
      String trimmedLine = line.trim();
      if (this.lineIsComment(trimmedLine)) {
         Matcher matcher = DELIMITER_PATTERN.matcher(trimmedLine);
         if (matcher.find()) {
            this.delimiter = matcher.group(5);
         }

         this.println(trimmedLine);
      } else if (this.commandReadyToExecute(trimmedLine)) {
         command.append(line.substring(0, line.lastIndexOf(this.delimiter)));
         command.append(LINE_SEPARATOR);
         this.println(command);
         this.executeStatement(command.toString());
         command.setLength(0);
      } else if (trimmedLine.length() > 0) {
         command.append(line);
         command.append(LINE_SEPARATOR);
      }

   }

   private boolean lineIsComment(String line) {
      return line.startsWith("//") || line.startsWith("--");
   }

   private boolean commandReadyToExecute(String trimmedLine) {
      return !this.fullLineDelimiter && trimmedLine.contains(this.delimiter) || this.fullLineDelimiter && trimmedLine.equals(this.delimiter);
   }

   private void executeStatement(String sql) throws SQLException {
      SQLException executionException = null;
      boolean hasResults = false;
      Statement statement = this.connection.createStatement();
      statement.setEscapeProcessing(this.escapeProcessing);
      String sqlToExecute = sql;
      if (this.removeCRs) {
         sqlToExecute = sql.replaceAll("\r\n", "\n");
      }

      if (this.stopOnError) {
         hasResults = statement.execute(sqlToExecute);
         if (this.throwWarning) {
            SQLWarning warnings = statement.getWarnings();
            if (warnings != null) {
               throw warnings;
            }
         }
      } else {
         try {
            hasResults = statement.execute(sqlToExecute);
         } catch (SQLException sQLException) {
            String message = "Error executing: " + sql + ".  Cause: " + sQLException;
            this.error(message);
            executionException = sQLException;
         }
      }

      this.printResults(statement, hasResults);

      try {
         statement.close();
      } catch (Exception exception) {
         throw exception;
      }

      if (executionException != null) {
         throw executionException;
      }
   }

   private void printResults(Statement statement, boolean hasResults) {
      try {
         if (hasResults) {
            ResultSet resultSet = statement.getResultSet();
            if (resultSet != null) {
               ResultSetMetaData metaData = resultSet.getMetaData();
               int columnCount = metaData.getColumnCount();

               for(int index = 0; index < columnCount; ++index) {
                  String columnLabel = metaData.getColumnLabel(index + 1);
                  this.print(columnLabel + "\t");
               }

               this.println("");

               while(resultSet.next()) {
                  for(int columnIndex = 0; columnIndex < columnCount; ++columnIndex) {
                     String value = resultSet.getString(columnIndex + 1);
                     this.print(value + "\t");
                  }

                  this.println("");
               }
            }
         }
      } catch (SQLException sQLException) {
         this.error("Error printing results: " + sQLException.getMessage());
      }

   }

   private void print(Object value) {
      if (this.logWriter != null) {
         this.logWriter.print(value);
         this.logWriter.flush();
      }

   }

   private void println(Object value) {
      if (this.logWriter != null) {
         this.logWriter.println(value);
         this.logWriter.flush();
      }

   }

   private void error(Object value) {
      if (this.errorLogWriter != null) {
         this.errorLogWriter.println(value);
         this.errorLogWriter.flush();
      }

   }
}
