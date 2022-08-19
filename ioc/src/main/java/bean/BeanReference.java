package bean;

/**
 * @author myd
 * @date 2022/8/9  18:45
 */

public class BeanReference {

    private String name;

    private String ref ;

//    private Class beanClass;

    public BeanReference(String name, String ref) {
        this.name = name;
        this.ref = ref;
    }

    public String getName() {
        return name;
    }

    public String getRef() {
        return ref;
    }
}
