package exception;

/**
 * @author myd
 * @date 2022/8/17  16:49
 */

public class TypeConvertError extends RuntimeException {
    public TypeConvertError(){}
    public TypeConvertError(String message){
        super(message);
    }
    public TypeConvertError(String message, Throwable cause){
        super(message,cause);
    }
    public TypeConvertError(Throwable cause){
        super(cause);
    }

}
