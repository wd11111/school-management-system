package pl.schoolmanagementsystem.common.mark.exception;

public class MarkNotInRangeException extends RuntimeException{

    public MarkNotInRangeException() {
        super("Mark not in range!");
    }
}
