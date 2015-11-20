package utils;

public class StringConvertJsonException extends RuntimeException{ 
	  String message = "StringConvertJsonException";
	  public StringConvertJsonException() { super(); }
	  public StringConvertJsonException(String message) { super(message); }
	  public StringConvertJsonException(String message, Throwable cause) { super(message, cause); }
	  public StringConvertJsonException(Throwable cause) { super(cause); }
}
