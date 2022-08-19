package exception;

/**
 * @author myd
 * @date 2022/8/17  16:24
 */

public class BeanConstructorNotFoundException extends RuntimeException
{

    public BeanConstructorNotFoundException(){}
    public BeanConstructorNotFoundException(String message){
        super(message);
    }
    public BeanConstructorNotFoundException(String message, Throwable cause){
        super(message,cause);
    }
    public BeanConstructorNotFoundException(Throwable cause){
        super(cause);
    }

}
