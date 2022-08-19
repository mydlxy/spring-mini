package context;

import bean.BeanDefinition;
import bean.BeanReferences;
import bean.PropertyValue;
import bean.PropertyValues;
import exception.BeanConstructorNotFoundException;
import exception.BeanCycleDependError;
import exception.BeanNameRepeatException;
import utils.AnnotationUtils;
import utils.BeanUtils;
import utils.TypeConvert;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author myd
 * @date 2022/8/4  9:39
 */

public class BeanFactory extends BeanPostProcessor{


    public static final String SINGLE = "single";

     ConfigInfo configInfo = ConfigInfo.getInstance();

    /*从xml，注解 获取到注入到容器的类的信息*/
     Map<String,BeanDefinition> registerBean = new HashMap<>();
    /* 通过beanDefinition创建的单例对象集合，没有给对象的属性赋值:一级缓存*/
     Map<String,Object> initSingleBean = new HashMap<>();
    /*二级缓存：存储被aop，以及其他处理之后的bean*/
     Map<String,Object>  postSingleBean = new HashMap<>();

    /*三级缓存：在postBean之后，装配bean属性；这是一个完整的，可以被使用的bean*/

     IocContainer container  = IocContainer.getInstance();

    /*检测 循环依赖*/
    private List<String> creatingBeanName = new ArrayList<>();
    /*单例BeanFactory*/
    private static BeanFactory beanFactory = new BeanFactory();

    public static BeanFactory getInstance(){
        return beanFactory;
    }

    public  void registerBean(String name,BeanDefinition beanDefinition){
        if(registerBean.containsKey(name))
            throw  new BeanNameRepeatException("register bean name repeat :" +name);
        registerBean.put(name,beanDefinition);
    }
    public BeanDefinition getRegisterBeanByName(String name){
        return registerBean.get(name);
    }
    public  Map<String,BeanDefinition> getRegisterBean(){
        return registerBean;
    }
    /*xml中bean标签配置的id */
    public <T>T  getInitSingleBeanByName(String name){
        Object bean = initSingleBean.get(name);
        if(bean == null)return null;
        return (T)bean;
    }

    public void addInitSingleBean(String name,Object initBean){
        if(initSingleBean.containsKey(name))
            throw  new BeanNameRepeatException("register bean name repeat :" +name);

        initSingleBean.put(name,initBean);
    }
    public void addInitSingleBeans(Map<String,Object> beans){
        beans.forEach((name,bean)->addInitSingleBean(name,bean));
    }
    public Object createBeanByBeanDefinition(BeanDefinition beanDefinition,boolean createSingleBean) throws Exception {

        String beanName = beanDefinition.getBeanName();
        if(createSingleBean && !beanDefinition.getScope().equals(SINGLE))
            throw new RuntimeException(" during single create bean  ["+creatingBeanName.get(creatingBeanName.size()-1)+" have prototype bean ["+beanName+"]");
        if(creatingBeanName.contains(beanName)){
            StringBuilder sb = new StringBuilder("");
            creatingBeanName.forEach(name->sb.append(name+"->"));
            throw new BeanCycleDependError("during creat bean ,cycle dependency error :"+sb.toString()+beanName);
        }
        if(initSingleBean.containsKey(beanName))
            return container.getBean(beanName);
        /*将beanName添加到正在创建bean的名单*/
        creatingBeanName.add(beanName);

        Object bean = null;
       if(beanDefinition.getConstructorValues() == null && beanDefinition.getConstructorReference() == null) {//无参构造
           bean = beanDefinition.getBeanClass().newInstance();
       }else{//有参构造
           //匹配构造参数
           Constructor constructor = BeanUtils.getConstructor(beanDefinition.getBeanClass(),beanDefinition);
           Parameter[] parameters = constructor.getParameters();
           Object[] constructorParamValues = new Object[parameters.length];
           PropertyValues constructorValues = beanDefinition.getConstructorValues();
           BeanReferences constructorReference = beanDefinition.getConstructorReference();
           for (int i = 0; i < parameters.length; i++) {
               String paramName = parameters[i].getName();
               String value = null;
               if(constructorValues != null){
                   PropertyValue propertyByName = constructorValues.getPropertyByName(paramName);
                   if(propertyByName.getValue().matches("\\$\\{.+\\}")){
                       value = TypeConvert.getPropertiesValue(propertyByName.getValue(),configInfo);
                   }
                   constructorParamValues[i] = TypeConvert.simpleTypeConvert(value,parameters[i].getType().getName());
               }else{
                   value = constructorReference.getBeanReferenceByName(paramName).getRef();//可能是 @Autowired赋值：全限定类名
                   BeanDefinition paramBeanDefinition = getRegisterBeanByName(value);
                   if(paramBeanDefinition == null){
                       paramBeanDefinition = getBeanDefinitionByClassName(value);
                   }
                   if(paramBeanDefinition == null)
                       throw new NullPointerException("not found ref:"+value);
                   Object paramBean = createBeanByBeanDefinition(paramBeanDefinition,createSingleBean);
                   constructorParamValues[i] = paramBean;
               }
           }
           bean = constructor.newInstance(constructorParamValues);
        }
       creatingBeanName.remove(beanName);
       if(createSingleBean)initSingleBean.put(beanName,bean);
       return bean;
    }
    public  Object beanFillPropertiesValue(BeanDefinition beanDefinition,Object bean,boolean single) throws Exception {
        BeanUtils.beanFillPropertiesValue(beanDefinition,bean,this);
        if(single)container.addBeanToContainer(beanDefinition.getBeanName(),bean);
        return bean;
    }

    public ConfigInfo getConfigInfo(){
        return configInfo;
    }
    public IocContainer getContainer(){
        return container;
    }
    public Object createPrototype(BeanDefinition beanDefinition ) throws Exception {
        Object protoBean = createBeanByBeanDefinition(beanDefinition, false);
        beanPostProcessor();
        Object bean = beanFillPropertiesValue(beanDefinition, protoBean,false);
        return bean;
    }


    public boolean matchClass(Class beanClass,String targetClassName){

        return beanClass.getName().equals(targetClassName) ||
               AnnotationUtils.matchSuperOrInterface(beanClass,targetClassName);
    }


    public BeanDefinition getBeanDefinitionByClassName(String className){
        int classCount = 0;
        BeanDefinition beanDef = null;
        boolean same = false;
        for (BeanDefinition beanDefinition : beanFactory.getRegisterBean().values()) {
            if(beanDefinition.getBeanClass().getName().equals(className)){
                beanDef = beanDefinition;
                if(!same){
                    classCount=0;
                    same=  true;
                }
                classCount+=1;
            }else if(!same){
                boolean match = AnnotationUtils.matchSuperOrInterface(beanDefinition.getBeanClass(),className);
                if(match)classCount+=1;
            }
        }
        if(classCount ==0 )
            throw new RuntimeException(" container not found class:"+className);
        if(classCount >1)
            throw new RuntimeException("too many class:"+className);

        return beanDef;

    }


    public Object getBean(Class beanClass) {
        BeanDefinition beanDef = getBeanDefinitionByClassName(beanClass.getName());
        String scope = beanDef.getScope();
        if(scope.equals(BeanFactory.SINGLE)){
            for (Object bean : container.getContainer().values()) {
                if(bean.getClass().equals(beanClass))return bean;
            }
        }else{
            try {
                return  createPrototype(beanDef);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException(" container not found class:"+beanClass.toGenericString());
    }


    public Object getBean(String name) {
        BeanDefinition beanDefinition = getRegisterBeanByName(name);
        String scope = beanDefinition.getScope();
        Object bean =null;
        //单例bean的获取
        if(scope.equals(BeanFactory.SINGLE))
            bean =  container.getBean(name);
        else {
            try {
                bean = createPrototype(beanDefinition);
            } catch (Throwable e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        if(bean == null)throw new RuntimeException("not found bean id:"+name);
        return bean;
    }

    public  Map<String,Object> getPostSingleBean(){

        return postSingleBean;
    }

    public Object getBeanFromPostSingleBeanByName(String name){
        return postSingleBean.get(name);
    }

    public Object getBeanFromPostSingleBeanByClassName(String className){

        for (Object bean : postSingleBean.values()) {
            if(matchClass(bean.getClass(),className))return bean;
        }
        return null;
    }

}
