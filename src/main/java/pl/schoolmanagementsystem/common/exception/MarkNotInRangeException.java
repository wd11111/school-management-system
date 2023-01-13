package pl.schoolmanagementsystem.common.exception;

public class MarkNotInRangeException extends RuntimeException{

    public MarkNotInRangeException() {
        super("Mark not in range!");
    }
}
