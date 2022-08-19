package bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author myd
 * @date 2022/8/9  18:45
 */

public class PropertyValue {
    private  String  name;
    private  String value;
    public PropertyValue(){}
    public PropertyValue(String name, String value) {
        this.name = name;
        this.value = value;
    }
    public String getName() {
        return name;
    }
    public String getValue() {
        return value;
    }

//    public PropertyValue add()

}
