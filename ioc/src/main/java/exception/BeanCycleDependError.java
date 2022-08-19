package exception;

/**
 * @author myd
 * @date 2022/8/17  17:13
 */

public class BeanCycleDependError  extends RuntimeException {
    public BeanCycleDependError(){}
    public BeanCycleDependError(String message){
        super(message);
    }
    public BeanCycleDependError(String message, Throwable cause){
        super(message,cause);
    }
    public BeanCycleDependError(Throwable cause){
        super(cause);
    }

}
