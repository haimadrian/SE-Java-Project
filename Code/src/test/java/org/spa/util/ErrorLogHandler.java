package org.spa.util;

import org.spa.common.util.log.ComponentLogHandler;
import org.spa.common.util.log.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * A component log handler that sets a mark when there is an error, so we can detect if there was
 * an error in the business logic part from a unit test class.
 *
 * @author Haim Adrian
 * @since 05-Jun-20
 */
public class ErrorLogHandler implements ComponentLogHandler {
   private final Logger logger;
   private final Set<CharSequence> errors = new HashSet<>();

   public ErrorLogHandler(Logger logger) {
      this.logger = logger;
   }

   @Override
   public void handleOut(CharSequence line) {
      logger.info(line);
   }

   @Override
   public void handleErr(CharSequence line) {
      errors.add(line);
   }

   public boolean hasError() {
      return !errors.isEmpty();
   }

   public Set<CharSequence> getErrors() {
      return errors;
   }
}
