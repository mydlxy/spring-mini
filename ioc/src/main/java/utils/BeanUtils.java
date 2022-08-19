package utils;

import bean.BeanDefinition;
import bean.BeanReferences;
import bean.PropertyValue;
import bean.PropertyValues;
import context.BeanFactory;
import exception.BeanConstructorNotFoundException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author myd
 * @date 2022/8/17  16:41
 */

public class BeanUtils {


    /*匹配 构造方法的参数类型，来确定使用哪个构造方法*/
    public static Constructor getConstructor(Class beanClass, BeanDefinition beanDefinition){
        Constructor[] constructors = beanClass.getConstructors();
        List<String> paramNames = getConstructorParamName(beanDefinition);
        for (Constructor constructor : constructors) {
            Parameter[] parameters = constructor.getParameters();
            if(parameters.length == paramNames.size()){
                boolean match = matchParams(parameters, paramNames);
                if(match)return constructor;
            }
        }
        StringBuilder names = new StringBuilder("");
        paramNames.forEach(name -> names.append(" "+ name));
        throw new BeanConstructorNotFoundException("beanName ["+beanDefinition.getBeanName()+"] not found constructor parameters is:"+names.toString());
    }
    /*使用构造参数的字段名称*/
    public static List<String> getConstructorParamName(BeanDefinition beanDefinition){
        PropertyValues constructorValues = beanDefinition.getConstructorValues();
        BeanReferences constructorReference = beanDefinition.getConstructorReference();
        List<String> paramNames = new ArrayList<>();
        if(constructorValues != null)constructorValues.getPropertyValues().keySet().forEach(key->paramNames.add(key));
        if(constructorReference != null)constructorReference.getMap().keySet().forEach(key->paramNames.add(key));
        return paramNames;
    }
    /*匹配参数列表*/
    public  static boolean matchParams(Parameter[] parameters,List<String> paramNames){
        for (Parameter parameter : parameters) {
            String name = parameter.getName();
            if(!paramNames.contains(name))return false;
        }
        return true;
    }


    /*填充bean属性值*/
    public static void beanFillPropertiesValue(BeanDefinition beanDefinition, Object bean, BeanFactory beanFactory) throws Exception {
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        BeanReferences propertyReferences = beanDefinition.getPropertyReferences();
        Class beanClass = bean.getClass();
        Field[] fields = beanClass.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if(propertyValues != null){//value
                    if(propertyValues.getPropertyValues().containsKey(fieldName)){
                        String value = propertyValues.getPropertyValues().get(fieldName).getValue();
                        field.set(bean,convertValue(value,field.getType().getName(),beanFactory));
                        continue;
                    }
                }
                if(propertyReferences != null){//ref

                    if(propertyReferences.getMap().containsKey(fieldName)){

                        String ref = propertyReferences.getBeanReferenceByName(fieldName).getRef();

//                        Object beanDef = beanFactory.getRegisterBeanByName(ref);
//                        if(beanDef == null)
//                            throw new ClassNotFoundException(" bean property ref not found "+ ref);
                        Object beanRef = beanFactory.getBeanFromPostSingleBeanByName(ref);
                        //
                        if(beanRef == null)
                            beanRef = beanFactory.getBeanFromPostSingleBeanByClassName(ref);

                        if(beanRef != null)
                            field.set(bean,beanRef);
                        else{
                            BeanDefinition beanDef = beanFactory.getRegisterBeanByName(ref);
                            if(beanDef == null)
                                throw new ClassNotFoundException(" bean property ref not found "+ ref);
                            Object prototypeBean = beanFactory.createPrototype(beanDef);
                            field.set(bean,prototypeBean);
                        }
                    }
                }
            }

    }

    /*将string转换成正确的类型*/
    public static Object convertValue(String value ,String type,BeanFactory beanFactory){
        if(value.matches("\\$\\{.+\\}")){
            value = TypeConvert.getPropertiesValue(value,beanFactory.getConfigInfo());
        }
        return TypeConvert.simpleTypeConvert(value,type);
    }



}
