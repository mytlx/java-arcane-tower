package com.mytlx.spring.projects.spring.demo.lifeCycle;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-05 16:18
 */
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof LifeCycleBean) {
            System.out.println("(1) BeanPostProcessor#BeforeInitialization: " + beanName);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof LifeCycleBean) {
            System.out.println("(2) BeanPostProcessor#AfterInitialization: " + beanName);
        }
        return bean;
    }

}
