package exception;

/**
 * @author myd
 * @date 2022/8/9  22:14
 */

public class XmlNotFoundClassException   extends  ClassNotFoundException{

    public XmlNotFoundClassException(){super();}
    public XmlNotFoundClassException(String message){
        super(message);
    }

    public XmlNotFoundClassException(String message,Throwable throwable){
        super(message,throwable);
    }


}
