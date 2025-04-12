package com.mytlx.spring.projects.spring.demo.factoryBean;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-08 9:11
 */
public class MyFactoryBean implements FactoryBean<MyService> {

    @Override
    public MyService getObject() throws Exception {
        return new MyService("从 FactoryBean 创建");
    }

    @Override
    public Class<?> getObjectType() {
        return MyService.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
