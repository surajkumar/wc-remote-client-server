package com.wannacall.utils.logging;

public class LoggerOptions {
    private boolean infoEnabled;
    private boolean debugEnabled;
    private boolean warningEnabled;
    private boolean errorEnabled;

    public LoggerOptions(boolean infoEnabled, boolean debugEnabled, boolean warningEnabled, boolean errorEnabled) {
        this.infoEnabled = infoEnabled;
        this.debugEnabled = debugEnabled;
        this.warningEnabled = warningEnabled;
        this.errorEnabled = errorEnabled;
    }

    public boolean isLevelDisabled(Logger.Level level) {
        return switch (level) {
            case INFO -> !infoEnabled;
            case WARNING -> !warningEnabled;
            case ERROR -> !errorEnabled;
            case DEBUG -> !debugEnabled;
            default ->  false;
        };
    }

    public boolean isInfoEnabled() {
        return infoEnabled;
    }

    public LoggerOptions setInfoEnabled(boolean infoEnabled) {
        this.infoEnabled = infoEnabled;
        return this;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public LoggerOptions setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
        return this;
    }

    public boolean isWarningEnabled() {
        return warningEnabled;
    }

    public LoggerOptions setWarningEnabled(boolean warningEnabled) {
        this.warningEnabled = warningEnabled;
        return this;
    }

    public boolean isErrorEnabled() {
        return errorEnabled;
    }

    public LoggerOptions setErrorEnabled(boolean errorEnabled) {
        this.errorEnabled = errorEnabled;
        return this;
    }

    public static LoggerOptions defaultOptions() {
        return new LoggerOptions(true, true, true, true);
    }
}
