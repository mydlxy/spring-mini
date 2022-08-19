package context;

import exception.BeanNameRepeatException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author myd
 * @date 2022/8/9  23:42
 */

public class IocContainer {



    private Map<String,Object> container = new HashMap<>();
    private IocContainer(){}
    private static IocContainer iocContainer = new IocContainer();
    public static IocContainer getInstance(){
        return iocContainer;
    }

    public void addBeanToContainer(String name,Object bean){
        if(container.containsKey(name))throw new BeanNameRepeatException("bean name repeat"+name);
        container.put(name,bean);
    }

    public <T>T getBean(String name){
        Object bean = container.get(name);
        if(bean == null)
            return null;
        else
            return (T)bean;
    }

    public  Map<String,Object> getContainer(){
        return container;
    }





}
