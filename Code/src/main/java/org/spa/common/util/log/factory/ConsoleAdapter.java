package org.spa.common.util.log.factory;

import org.spa.common.util.log.Level;
import org.spa.common.util.log.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

/**
 * Used for logging messages to console. (out and err)
 * @author Haim Adrian
 * @since 10-May-20
 */
class ConsoleAdapter implements Logger {

   @Override
   public void log(Level level, Supplier<CharSequence> messageSupplier) {
      LogRecord logRecord = new LogRecord(LocalDateTime.now(), level, messageSupplier.get());
      doLog(logRecord);
   }

   @Override
   public void log(Level level, Supplier<CharSequence> messageSupplier, Throwable thrown) {
      LogRecord logRecord = new LogRecord(LocalDateTime.now(), level, messageSupplier.get(), thrown);
      doLog(logRecord);
   }

   @Override
   public void log(Level level, CharSequence message) {
      LogRecord logRecord = new LogRecord(LocalDateTime.now(), level, message);
      doLog(logRecord);
   }

   @Override
   public void log(Level level, CharSequence message, Throwable thrown) {
      LogRecord logRecord = new LogRecord(LocalDateTime.now(), level, message, thrown);
      doLog(logRecord);
   }

   protected Appendable getOutStream() {
      return System.out;
   }

   protected Appendable getErrStream() {
      return System.err;
   }

   private void doLog(LogRecord logRecord) {
      try {
         CharSequence line = formatMessage(logRecord) + System.lineSeparator();
         if (logRecord.getLevel().ordinal() < Level.WARN.ordinal()) {
            getOutStream().append(line);
         } else {
            getErrStream().append(line);
         }
      } catch (Exception e) {
         String message = "Error has occurred at doLog. Failed record: " + logRecord;
         LogRecord newRecord = new LogRecord(LocalDateTime.now(), Level.ERROR, message, e);
         System.err.println(formatMessage(newRecord));
      }
   }

   private static String formatMessage(LogRecord logRecord) {
      StringBuilder msg = new StringBuilder();

      //@formatter:off
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
      msg.append(logRecord.getTime().format(formatter)).append(' ')
            .append(logRecord.getLevel().name())
            .append('[').append(Thread.currentThread().getName()).append("] - ")
            .append(logRecord.getMessage());
      //@formatter:on

      if (logRecord.getThrown() != null) {
         StringWriter sw = new StringWriter();
         try (PrintWriter pw = new PrintWriter(sw)) {
            pw.println();
            logRecord.getThrown().printStackTrace(pw);
         }

         msg.append(System.lineSeparator()).append(sw.toString());
      }

      return msg.toString();
   }

   private static class LogRecord {
      private final LocalDateTime time;
      private final Level level;
      private final CharSequence message;
      private final Throwable thrown;

      public LogRecord(LocalDateTime time, Level level, CharSequence message, Throwable thrown) {
         this.time = time;
         this.level = level;
         this.message = message;
         this.thrown = thrown;
      }

      public LogRecord(LocalDateTime time, Level level, CharSequence message) {
         this(time, level, message, null);
      }

      public LocalDateTime getTime() {
         return time;
      }

      public Level getLevel() {
         return level;
      }

      public CharSequence getMessage() {
         return message;
      }

      public Throwable getThrown() {
         return thrown;
      }

      @Override
      public String toString() {
         return "LogRecord{" +
               "time=" + time +
               ", level=" + level +
               ", message=" + message +
               '}';
      }
   }
}
