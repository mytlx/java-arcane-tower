package com.mytlx.handcraft.rpc.config;

import com.mytlx.handcraft.rpc.client.RpcClient;
import com.mytlx.handcraft.rpc.model.RemoteService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.lang.NonNull;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-16 11:46:49
 */
public class RpcServiceScanPostProcessor implements BeanDefinitionRegistryPostProcessor {

    Set<RemoteServiceFieldHolder> fieldHolderSet = new HashSet<>();

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        for (String beanDefinitionName : registry.getBeanDefinitionNames()) {
            String beanClassName = registry.getBeanDefinition(beanDefinitionName).getBeanClassName();
            if (beanClassName != null) {
                try {
                    Class<?> beanClass = Class.forName(beanClassName);
                    for (Field field : beanClass.getDeclaredFields()) {
                        if (field.isAnnotationPresent(AutoRemoteInjection.class)) {
                            field.setAccessible(true);
                            AutoRemoteInjection annotation = field.getAnnotation(AutoRemoteInjection.class);
                            // 需要保存下来该 field，同一个类可能会注入多次，需要去重
                            RemoteServiceFieldHolder fieldHolder = new RemoteServiceFieldHolder(field, annotation);
                            fieldHolderSet.add(fieldHolder);
                        }

                    }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        for (RemoteServiceFieldHolder fieldHolder : fieldHolderSet) {
            Class<?> fieldClass = fieldHolder.getRemoteServiceField().getType();
            if (RemoteService.class.isAssignableFrom(fieldClass)) {
                // 生成代理对象
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RemoteServiceFactoryBean.class);
                // 传入构造方法参数种类的名字
                // builder.addConstructorArgValue(fieldClass);

                builder.addPropertyValue("rpcInterfaceClass", fieldClass);
                builder.addPropertyValue("targetClientId", fieldHolder.getTargetClientId());
                builder.addPropertyValue("remoteClient", new RuntimeBeanReference(RpcClient.class));
                builder.addPropertyValue("fallbackClass", fieldHolder.getFallbackClass());

                registry.registerBeanDefinition(fieldHolder.getAlias(), builder.getBeanDefinition());
            }
        }
    }
}
