package utils;

public class LongConvertJsonException extends RuntimeException{
	  String message = "LongConvertJsonException";
	  public LongConvertJsonException() { super(); }
	  public LongConvertJsonException(String message) { super(message); }
	  public LongConvertJsonException(String message, Throwable cause) { super(message, cause); }
	  public LongConvertJsonException(Throwable cause) { super(cause); }
}
