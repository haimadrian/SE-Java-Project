package org.spa.common.util.log;

public interface ComponentLogHandler {
   void handleOut(CharSequence line);

   default void handleErr(CharSequence line) {
      handleOut(line);
   }
}
