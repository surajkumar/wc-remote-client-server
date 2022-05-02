package com.wannacall.utils.logging;

import java.io.*;

public class PrintInterceptor extends PrintStream {
    private final PrintWriter writer;

    public PrintInterceptor(String logFile, OutputStream out) throws IOException {
        super(out);
        writer = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));
    }

    public void print(String s) {
        super.print(s);
        writer.print(s);
    }

    @Override
    public void println(String s) {
        super.println(s);
        writer.println(s);
        writer.flush();
    }

    public void close() {
        writer.close();
    }
}
