package exception;

/**
 * @author myd
 * @date 2022/8/17  15:26
 */

public class BeanNameRepeatException extends  RuntimeException{

    public BeanNameRepeatException(){}
    public BeanNameRepeatException(String message){
        super(message);
    }
    public BeanNameRepeatException(String message, Throwable cause){
        super(message,cause);
    }
    public BeanNameRepeatException(Throwable cause){
        super(cause);
    }

}
