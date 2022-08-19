package context;

/**
 * @author myd
 * @date 2022/8/4  9:39
 */

public interface ApplicationContext {


    Object getBean(String name);

    Object getBean(Class beanClass);



}
