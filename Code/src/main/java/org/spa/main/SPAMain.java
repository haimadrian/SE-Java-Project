package org.spa.main;

import org.apache.logging.log4j.LogManager;
import org.spa.common.SPAApplication;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.ui.HomePage;
import org.spa.ui.util.Controls;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @author Haim Adrian
 * @since 04-May-20
 */
public class SPAMain {
    private static final Logger logger = LoggerFactory.getLogger(SPAMain.class);

    private static final String STDOUT_LOGGER_NAME = "stdout";
    private static final String STDERR_LOGGER_NAME = "stderr";
    private static final String IGNORED_NEWLINE = System.lineSeparator();

    static {
        redirectStreams();
        Controls.tweakPLAF();
    }

    public static void main(String[] args) throws IOException {
        logger.info("Starting application");
        JFrame mainForm = new JFrame("SPA Store");
        mainForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainForm.setPreferredSize(new Dimension(screenSize.width - 200, screenSize.height - 200));
        SwingUtilities.invokeLater(() -> mainForm.setPreferredSize(new Dimension(screenSize.width - 200, screenSize.height - 250)));

        SPAApplication.getInstance().start();

        HomePage homePage = new HomePage(mainForm);

        mainForm.setContentPane(homePage);
        mainForm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                logger.info("Exiting application");
                SPAApplication.getInstance().stop();
                LogManager.shutdown();
            }
        });
        mainForm.pack();
        Controls.centerDialog(mainForm);
        mainForm.setVisible(true);
    }

    /**
     * Redirect System.out and System.err java process streams into logging files configured according to
     * log4j's log4j2.xml file.
     */
    private static void redirectStreams() {
        // redirect the streams
        System.setOut(new PrintStream(new LoggingStream(STDOUT_LOGGER_NAME), true));
        System.setErr(new PrintStream(new LoggingStream(STDERR_LOGGER_NAME), true));

        // test this stuff
        System.out.println(getJavaVersionString());
    }

    private static String getJavaVersionString() {
        return "java version \"" + System.getProperty("java.version") + "\"" + System.lineSeparator() + System.getProperty("java.runtime.name") +
              " (build " + System.getProperty("java.runtime.version") + ")" + System.lineSeparator() + System.getProperty("java.vm.name") +
              " (build " + System.getProperty("java.vm.version") + ", " + System.getProperty("java.vm.info") + ")";
    }

    /**
     * This output stream holds all streamed data in the internal buffer. On flush() it will send buffer data
     * to the relevant logger.
     */
    private static class LoggingStream extends OutputStream {
        /**
         * Internal stream buffer
         */
        private final StringBuilder sb;

        /**
         * The logger where we flush internal buffer to
         */
        private final Logger logger;

        /**
         * Constructs a new {@link LoggingStream}
         * @param loggerName Name of the logger to log messages to
         */
        public LoggingStream(String loggerName) {
            sb = new StringBuilder(128);
            logger = LoggerFactory.getLogger(loggerName);
        }

        /**
         * Writes the specified byte to this output stream. The general contract for <code>write</code> is
         * that one byte is written to the output stream. The byte to be written is the eight low-order bits of
         * the argument <code>b</code>. The 24 high-order bits of <code>b</code> are ignored.
         * <p>
         * Subclasses of <code>OutputStream</code> must provide an implementation for this method.
         *
         * @param b the <code>byte</code>.
         */
        @Override
        public void write(int b) {
            sb.append((char) b);
        }

        /**
         * Flushes this output stream and forces any buffered output bytes to be written out. The general
         * contract of <code>flush</code> is that calling it is an indication that, if any bytes previously
         * written have been buffered by the implementation of the output stream, such bytes should immediately
         * be written to their intended destination.
         * <p>
         * The <code>flush</code> method of <code>OutputStream</code> does nothing.
         */
        @Override
        public void flush() {
            if (sb.length() > 0) {
                String message = sb.toString();

                // When calling System.out.println, there is the print of the message which is flushed
                // and we print it with a new line as part of the logger implementation, and then there is additional
                // newLine() call of the println, which we would like to ignore cause the logger already prints a new line.
                if (!IGNORED_NEWLINE.equals(message)) {
                    logger.info(message);
                }

                sb.setLength(0);
            }
        }

        /**
         * Closes this output stream and releases any system resources associated with this stream. The general
         * contract of <code>close</code> is that it closes the output stream. A closed stream cannot perform
         * output operations and cannot be reopened.
         * <p>
         * The <code>close</code> method of <code>OutputStream</code> does nothing.
         */
        @Override
        public void close() {
            flush();
        }
    }
}

