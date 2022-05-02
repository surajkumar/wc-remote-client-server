package com.wannacall.utils.logging;

import com.wannacall.utils.ConsoleColours;

import java.sql.Timestamp;

/**
 * This class is used to manage all types of log messages. Logs are stored within the database in the format:
 * logLevel, class called by the logger, function called by the logger, log message, timestamp, the raw log text (as
 * displayed in the console).
 *
 * The supported log levels are defined in {@link Level}.
 *
 * @author Suraj Kumar
 * @version 1.0
 */
public class Logger {
    private static LoggerOptions options = LoggerOptions
            .defaultOptions();

    private Logger() {
        throw new AssertionError();
    }

    public static void setLoggerOptions(LoggerOptions options) {
        if(options == null) {
            System.err.println("Cannot use a null Logger option! Automatically switching to default options.");
            Logger.options = LoggerOptions.defaultOptions();
        } else {
            Logger.options = options;
        }
    }

    /**
     * This method is used to print log messages to the console and handles inserting logs into the database.
     *
     * @param level The type of log this is
     * @param caller The class class that is logging
     * @param function The function that is logging
     * @param message The message to log
     */
    public static void println(Level level, String caller, String function, String message) {
        var ts = new Timestamp(System.currentTimeMillis()).toString();
        var raw = "[" + ts + "][" + caller + "#" + function + "][" + level.name() + "] " + message;
        String colour = switch (level) {
            case INFO -> ConsoleColours.BLUE_BRIGHT;
            case WARNING -> ConsoleColours.YELLOW;
            case ERROR -> ConsoleColours.RED;
            case DEBUG -> ConsoleColours.WHITE;
            default -> ConsoleColours.RESET;
        };

        if(!options.isLevelDisabled(level)) {
            System.out.println(colour + raw + ConsoleColours.RESET);
        }
    }

    /**
     * A recommended helper for info level messages. Info level messages are for non-sensitive information designed
     * to show application flow.
     *
     * @param message The message to log
     */
    public static void info(String message) {
        println(Level.INFO, StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass().getCanonicalName(), Thread.currentThread().getStackTrace()[2].getMethodName(), message);
    }

    /**
     * A recommended helper for debug level messages. Debug level messages are for debugging purposes e.g. checking
     * the current position within code. Debug messages are not displayed to the console if debugging is disabled.
     *
     * @param message The message to log
     */
    public static void debug(String message) {
        println(Level.DEBUG, StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass().getCanonicalName(), Thread.currentThread().getStackTrace()[2].getMethodName(), message);
    }

    /**
     * A recommended helper for error level messages. Error level messages are used to indicate that something
     * bad has happened and immediate attention is required. An example of when to use error is within exception blocks.
     *
     * TODO: Send a notification e.g. email with error message for immediate attention
     *
     * @param message The message to log
     */
    public static void error(String message) {
        println(Level.ERROR, StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass().getCanonicalName(), Thread.currentThread().getStackTrace()[2].getMethodName(), message);
    }

    /**
     * A recommended helper for warning level messages. Warning level messages are to indicate that an operation being performed
     * may result in problems. Although not severe like errors, they are to warn the user of any issues that may or may not
     * require attention.
     *
     * @param message The message to log
     */
    public static void warning(String message) {
        println(Level.WARNING, StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass().getCanonicalName(), Thread.currentThread().getStackTrace()[2].getMethodName(), message);
    }

    /**
     * The different log levels available to the system.
     */
    enum Level {
        INFO("Info"),
        WARNING("Warning"),
        DEBUG("Debug"),
        ERROR("Error");

        protected final String displayName;

        Level(String displayName) {
            this.displayName = displayName;
        }
    }
}
