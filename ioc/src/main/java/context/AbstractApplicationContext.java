package context;

/**
 * @author myd
 * @date 2022/8/10  15:00
 */

public abstract class AbstractApplicationContext {

    public void refresh(){
        // 1. 创建 BeanFactory，并加载 BeanDefinition
//        refreshBeanFactory();
//
//        // 2. 获取 BeanFactory
//        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
//
//        // 3. 添加 ApplicationContextAwareProcessor，让继承自 ApplicationContextAware 的 Bean 对象都能感知所属的 ApplicationContext
//        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
//
//        // 4. 在 Bean 实例化之前，执行 BeanFactoryPostProcessor (Invoke factory processors registered as beans in the context.)
//        invokeBeanFactoryPostProcessors(beanFactory);
//
//        // 5. BeanPostProcessor 需要提前于其他 Bean 对象实例化之前执行注册操作
//        registerBeanPostProcessors(beanFactory);
//
//        // 6. 初始化事件发布者
//        initApplicationEventMulticaster();
//
//        // 7. 注册事件监听器
//        registerListeners();
//
//        // 8. 设置类型转换器、提前实例化单例Bean对象
//        finishBeanFactoryInitialization(beanFactory);
//
//        // 9. 发布容器刷新完成事件
//        finishRefresh();

    }
}
