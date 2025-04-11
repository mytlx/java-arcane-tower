package com.mytlx.spring.demo.lifeCycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-05 16:17
 */
@Component
public class LifeCycleBean implements InitializingBean, DisposableBean, BeanNameAware, BeanFactoryAware, ApplicationContextAware {

    public LifeCycleBean() {
        System.out.println("1. 构造方法调用");
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("2. BeanNameAware#setBeanName: " + name);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        System.out.println("3. BeanFactoryAware#setBeanFactory");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        System.out.println("4. ApplicationContextAware#setApplicationContext");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("5. @PostConstruct 方法执行");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("6. InitializingBean#afterPropertiesSet");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("7. @PreDestroy 方法执行");
    }

    @Override
    public void destroy() {
        System.out.println("8. DisposableBean#destroy");
    }
}
