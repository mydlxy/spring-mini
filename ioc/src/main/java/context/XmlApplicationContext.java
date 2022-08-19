package context;

import bean.BeanDefinition;
import org.dom4j.DocumentException;
import parse.xml.RegisterParseLabel;
import parse.xml.XmlParse;
import parse.xml.node.XmlNode;
import utils.AnnotationUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @author myd
 * @date 2022/8/9  21:52
 */

public class XmlApplicationContext  implements ApplicationContext {

    BeanFactory beanFactory = BeanFactory.getInstance();

    public XmlApplicationContext(String path) throws Throwable {
        loadBean(path,null);
    }

    public  XmlApplicationContext(String path,Map<String ,XmlNode>maps) throws Throwable {
        loadBean(path,maps);
    }



    void loadBean(String path,Map<String ,XmlNode>maps) throws Throwable {
        //可以加入要解析的标签
        addParseLabel(maps);
        //解析xml
        XmlParse.parseXml(path,ConfigInfo.getInstance());
        //init bean初始化bean；将beanDefinition ->  bean;将单例全部初始化singleton
        initBean();
        //后处理bean
        invokeBeanPostProcessor();
        // 填充singleBean属性值
        fillBeanPropertiesValue();
    }

    void addParseLabel(Map<String, XmlNode> xmlNodes){
        if(xmlNodes == null)return;
        RegisterParseLabel.getRegisterParseLabel().setRegisterParseLabels(xmlNodes);
    }

    void initBean() throws Exception {

        Map<String, BeanDefinition> registerBean = beanFactory.getRegisterBean();
        for (BeanDefinition beanDefinition : registerBean.values()) {
                beanFactory.createBeanByBeanDefinition(beanDefinition,true);
        }
    }

    void invokeBeanPostProcessor(){
        beanFactory.postSingleBean =  beanFactory.initSingleBean;
    }

    void fillBeanPropertiesValue() {
        Map<String, BeanDefinition> registerBean = beanFactory.getRegisterBean();
        beanFactory.postSingleBean.forEach((name,bean)->{
            try {
                beanFactory.beanFillPropertiesValue(registerBean.get(name),bean,true);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

        });
    }

    @Override
    public Object getBean(String name) {
       return beanFactory.getBean(name);
    }



    @Override
    public Object getBean(Class beanClass) {

      return  beanFactory.getBean(beanClass);

    }






    /*初始化bean，如果没有后处理bean，那么initSingleBean就直接赋值给postSingleBean*/
//    @Override
//    public void beanPostProcessor() {
//        beanFactory.postSingleBean =  beanFactory.initSingleBean;
//    }
}
