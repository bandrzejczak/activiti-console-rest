package pl.edu.pw.ii.bpmConsole.interfaces.exceptions;

public class DateParsingException extends RuntimeException {
    public DateParsingException(String datePattern, String date) {
        super(String.format("Couldn't parse date %1$s with date pattern %2$s", date, datePattern));
    }
}
