package bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author myd
 * @date 2022/8/9  19:49
 */

public class BeanReferences {

    Map<String,BeanReference> map  = new HashMap<>();

    public void addProperty(String name,BeanReference beanReference){
        map.put(name,beanReference);
    }

    public BeanReference getBeanReferenceByName(String propertyName){
        return map.get(propertyName);
    }

    public  Map<String,BeanReference>  getMap(){
        return map;
    }

}
