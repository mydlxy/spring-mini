package parse.xml.node;

import bean.BeanDefinition;
import context.ConfigInfo;
import org.dom4j.Element;

import java.util.List;

/**
 * @author myd
 * @date 2022/8/9  18:47
 */

public class Bean implements XmlNode {

    private static final String LABEL_NAME ="bean";


    //bean标签属性
    private static final String ID = "id";
    private static final String CLASS ="class";
    private static final String SCOPE = "scope";

    private static final String DEFAULT_SCOPE = "single";

    //property，constructor 标签
    private static final String NAME = "name";
    private static final String TYPE = "type";

    private static final String VALUE = "value";
    private static final String REF = "ref";

    private static Bean bean = new Bean();

    public static String getLabelName() {
        return LABEL_NAME;
    }

    public static XmlNode getInstance() {
        return bean;
    }

    @Override
    public void parseLabel(Element node, ConfigInfo configInfo) {

        BeanDefinition beanDefinition = new BeanDefinition();
        String className = node.attributeValue(CLASS);
        String id = node.attributeValue(ID);
        String scope = node.attributeValue(SCOPE);
        if(id == null || id.trim().length() == 0)id = getSimpleName(className);
        if(scope == null || scope.trim().length() == 0)scope = DEFAULT_SCOPE;
        try {
            beanDefinition.setBeanClass(Class.forName(className)).setBeanName(id).setScope(scope);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //解析bean子标签
        parseChildrenLabel(node.elements(),beanDefinition);
        //注册beanDefinition
        configInfo.registerBean(id,beanDefinition);

    }

     String getSimpleName(String className){
        int index = className.lastIndexOf(".");
        return className.substring(index+1);
     }


     void parseChildrenLabel(List<Element> children,BeanDefinition beanDefinition){
        if(children == null || children.isEmpty())return ;
        children.forEach((child)->{
            String type = child.getName();
            if(type.equals("property"))
                parseProperty(child,beanDefinition);
            else
                parseConstructor(child,beanDefinition);
        });

     }


      void parseProperty(Element property,BeanDefinition beanDefinition){
        String name = property.attributeValue(NAME);
        String ref  = property.attributeValue(REF);
        String value = property.attributeValue(VALUE);
        if(ref == null){
            beanDefinition.setProperty(name,value);
        }else{
            beanDefinition.setPropertyReference(name,ref);
        }
     }

     void parseConstructor(Element constructor,BeanDefinition beanDefinition){
        //type:class类型
         String name = constructor.attributeValue(NAME);
//         String type = constructor.attributeValue(TYPE);
         String ref  = constructor.attributeValue(REF);
         String value = constructor.attributeValue(VALUE);
         if(ref == null){
             beanDefinition.setConstructor(name,value);
         }else{
             beanDefinition.setConstructorReference(name,ref);
         }

     }








}
