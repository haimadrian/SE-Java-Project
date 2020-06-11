package org.spa.controller.util.log.factory;

import org.spa.controller.util.log.ComponentLogHandler;

import java.util.function.Consumer;

/**
 * Used to propagate log messages to any other component. e.g. a UI component.<br/>
 * This is done using the Observer pattern. We accept a {@link ComponentLogHandler} which listens to log messages through this adapter.
 *
 * @author Haim Adrian
 * @since 10-May-20
 */
class ComponentAdapter extends ConsoleAdapter {
   private final Appendable out;
   private final Appendable err;

   /**
    * Constructs a new {@link ComponentAdapter}
    *
    * @param handler A {@link ComponentLogHandler} that will be notified upon log messages are received
    */
   public ComponentAdapter(ComponentLogHandler handler) {
      out = new ComponentPrintStream(handler::handleOut);
      err = new ComponentPrintStream(handler::handleErr);
   }

   @Override
   protected Appendable getOutStream() {
      return out;
   }

   @Override
   protected Appendable getErrStream() {
      return err;
   }

   private static class ComponentPrintStream implements Appendable {
      final Consumer<CharSequence> handler;

      public ComponentPrintStream(Consumer<CharSequence> handler) {
         this.handler = handler;
      }

      @Override
      public Appendable append(CharSequence csq) {
         handler.accept(csq);
         return this;
      }

      @Override
      public Appendable append(CharSequence csq, int start, int end) {
         append(csq);
         return this;
      }

      @Override
      public Appendable append(char c) {
         append("" + c);
         return this;
      }
   }
}
