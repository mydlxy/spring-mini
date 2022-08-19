package bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author myd
 * @date 2022/8/9  18:45
 */

public class PropertyValues {

    Map<String,PropertyValue> propertyValues = new HashMap<>();
    public void addProperties(String name ,PropertyValue propertyValue){
        propertyValues.put(name,propertyValue);
    }

    public PropertyValue getPropertyByName(String propertyName){
        return propertyValues.get(propertyName);
    }
    public Map<String,PropertyValue> getPropertyValues(){
        return propertyValues;
    }
}
