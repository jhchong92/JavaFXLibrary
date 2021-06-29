package javafxlibrary.utils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class RobotLog {

    private static boolean ignoreDuplicates = false;
    private static LinkedList<String> loggedMessages = new LinkedList<>();
    private static Logger instance;

    public static Logger getInstance() {
        if (instance == null) {
            instance = Logger.getLogger("MyLog");
            FileHandler fh;

            try {

                // This block configure the logger with handler and formatter
                fh = new FileHandler("JavaFxLogFile.log");
                instance.addHandler(fh);
                SimpleFormatter formatter = new SimpleFormatter();
                fh.setFormatter(formatter);

                // the following statement is used to log any messages
                instance.info("My first log");
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return instance;
    }

    public static void ignoreDuplicates() {
        ignoreDuplicates = true;
    }

    public static void reset() {
        ignoreDuplicates = false;
        loggedMessages.clear();
    }

    public static void info(String message) {
        if (shouldLogMessage(message)) {
            System.out.println("*INFO* " + message);
        }
        getInstance().info(message);
    }

    public static void html(String message) {
        if (shouldLogMessage(message)) {
            System.out.println("*HTML* " + message);
        }
        getInstance().info("HTML");
        getInstance().info(message);
    }

    public static void debug(String message) {
        if (shouldLogMessage(message)) {
            System.out.println("*DEBUG* " + message);
        }
        getInstance().info("DEBUG");
        getInstance().info(message);
    }

    public static void trace(String message) {
        if (shouldLogMessage(message)) {
            System.out.println("*TRACE* " + message);
        }
        getInstance().info("TRACE");
        getInstance().info(message);
    }

    public static void warn(String message) {
        if (shouldLogMessage(message)) {
            System.out.println("*WARN* " + message);
        }
        getInstance().info("WARN");
        getInstance().info(message);
    }

    public static void error(String message) {
        if (shouldLogMessage(message)) {
            System.out.println("*ERROR* " + message);
        }
        getInstance().info("ERROR");
        getInstance().info(message);
    }

    private static boolean shouldLogMessage(String message) {
        if (ignoreDuplicates) {
            if (loggedMessages.contains(message)) {
                return false;
            } else {
                loggedMessages.add(message);
                return true;
            }
        } else {
            return true;
        }
    }
}
