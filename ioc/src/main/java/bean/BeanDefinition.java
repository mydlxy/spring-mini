package bean;

import java.util.List;

/**
 * @author myd
 * @date 2022/8/9  18:44
 */

public class BeanDefinition<T> {
    /*bean的id*/
    private String beanName;
    /*bean的class*/
    private Class<T> beanClass;
    //字段信息
    private PropertyValues propertyValues;
    private BeanReferences propertyReferences;

    //构造参数信息:每个元素是类型名称className，按照传入类型存储，搜索也是按照这个顺序
    private List<String> constructorParamListOrder;
    private PropertyValues constructorValues;
    private BeanReferences constructorReferences;
    private String scope = "single";

    public BeanDefinition<T> setScope(String scope) {
        this.scope = scope;
        return this;
    }

    public String getScope() {
        return scope;
    }

    /*无参构造*/
    public boolean constructorNonParamList(){
        return constructorParamListOrder == null;
    }

    public BeanDefinition(String beanName, Class<T> beanClass, PropertyValues propertyValues, BeanReferences beanReferences, PropertyValues constructorValues, BeanReferences constructorReferences) {
        this.beanName = beanName;
        this.beanClass = beanClass;
        this.propertyValues = propertyValues;
        this.propertyReferences = beanReferences;
        this.constructorValues = constructorValues;
        this.constructorReferences = constructorReferences;
    }
    public BeanDefinition() {}

    public String getBeanName() {
        return beanName;
    }

    public Class<T> getBeanClass() {
        return beanClass;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public BeanReferences getPropertyReferences() {
        return propertyReferences;
    }

    public PropertyValues getConstructorValues() {
        return constructorValues;
    }

    public BeanReferences getConstructorReference() {
        return constructorReferences;
    }

    public BeanDefinition<T> setBeanName(String beanName) {
        this.beanName = beanName;
        return this;
    }

    public BeanDefinition<T> setBeanClass(Class<T> beanClass) {
        this.beanClass = beanClass;
        return this;
    }

    public BeanDefinition<T> setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
        return this;
    }

    public BeanDefinition<T> setPropertyReferences(BeanReferences propertyReferences) {
        this.propertyReferences = propertyReferences;
        return this;
    }

    public BeanDefinition<T> setConstructorValues(PropertyValues constructorValues) {
        this.constructorValues = constructorValues;
        return this;
    }

    public BeanDefinition<T> setConstructorReferences(BeanReferences constructorReferences) {
        this.constructorReferences = constructorReferences;
        return this;
    }


    public void setProperty(String name ,String value){
        PropertyValue propertyValue  = new PropertyValue(name,value);
        if(propertyValues == null){
            propertyValues = new PropertyValues();
        }
        propertyValues.addProperties(name,propertyValue);
    }

    public void setPropertyReference(String name ,String ref){
        BeanReference beanReference = new BeanReference(name,ref);
        if(propertyReferences == null){
            propertyReferences = new BeanReferences();
        }
        propertyReferences.addProperty(name,beanReference);
    }

    public void setConstructor(String name ,String value){
        PropertyValue propertyValue  = new PropertyValue(name,value);
        if(constructorValues == null){
            constructorValues = new PropertyValues();
        }
        constructorValues.addProperties(name,propertyValue);
    }

    public void setConstructorReference(String name ,String ref){
        BeanReference beanReference = new BeanReference(name,ref);
        if(constructorReferences == null){
            constructorReferences = new BeanReferences();
        }
        constructorReferences.addProperty(name,beanReference);
    }


}
