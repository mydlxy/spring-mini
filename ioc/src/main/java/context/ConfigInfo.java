package context;

import bean.BeanDefinition;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author myd
 * @date 2022/8/9  22:50
 */

public class ConfigInfo{

    private  Map<String , Properties> propertiesMap = new HashMap<>();

//    private  BeanFactory beanFactory = BeanFactory.getInstance();

    private static ConfigInfo configInfo= new ConfigInfo();
    private ConfigInfo(){}
    public static ConfigInfo getInstance(){
        return configInfo;
    }

    public String getPropertiesValueByName(String name){
        for (Properties properties : propertiesMap.values()) {
            String value =(String) properties.get(name);
            if(value != null)return value;
        }
        return null;
    }


    public void addProperties(String propertiesName,Properties properties){
        if(properties.containsKey(propertiesName))
            throw new RuntimeException("properties file name repeat");
        else
            propertiesMap.put(propertiesName,properties);
    }

    public void registerBean(String name,BeanDefinition beanDefinition){
        if(BeanFactory.getInstance().getRegisterBean().containsKey(name))
            throw  new RuntimeException("bean id repeat  [ "+name+" ]");
        else
            BeanFactory.getInstance().registerBean(name,beanDefinition);
    }


}
