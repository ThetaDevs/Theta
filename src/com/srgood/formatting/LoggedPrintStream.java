package com.srgood.formatting;

import java.io.PrintStream;

/**
 * Created by srgood on 1/1/2017.
 */
public class LoggedPrintStream extends PrintStream {

    private static final String newLine = System.getProperty("line.separator");

    private final StringBuffer sb = new StringBuffer();
    private int printedLines = 0;


    public LoggedPrintStream(PrintStream original) {
        super(original);
    }

    @Override
    public void print(double d) {
        this.printedLines++;
        sb.append(d);
        super.print(d);
    }

    @Override
    public void print(String s) {

        sb.append(s);
        super.print(s);
    }

    public void println(String s) {
        sb.append(s).append(newLine);
        super.println(s);
    }

    public void println() {
        sb.append(newLine);
        super.println();
    }

    public String getAllWrittenText() {
        return sb.toString();
    }

    public int getNumLines() {
        return sb.toString().;
    }
}
