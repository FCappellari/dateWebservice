package utils;

public class DateConvertJsonException extends RuntimeException{
	  String message = "DateConvertJsonException";
	  public DateConvertJsonException() { super(); }
	  public DateConvertJsonException(String message) { super(message); }
	  public DateConvertJsonException(String message, Throwable cause) { super(message, cause); }
	  public DateConvertJsonException(Throwable cause) { super(cause); }
}
