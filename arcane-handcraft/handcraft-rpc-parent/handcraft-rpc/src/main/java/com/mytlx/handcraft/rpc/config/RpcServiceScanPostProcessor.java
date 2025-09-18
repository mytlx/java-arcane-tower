package com.mytlx.handcraft.rpc.config;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-16 11:46:49
 */
public class RpcServiceScanPostProcessor implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata metadata,
                                        @NonNull BeanDefinitionRegistry registry, @NonNull BeanNameGenerator generator) {
        // 启动类类名
        String applicationClassName = metadata.getClassName();
        String basePackage = null;
        try {
            Class<?> applicationClass = Class.forName(applicationClassName);
            basePackage = applicationClass.getPackageName();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        RemoteServiceScanner scanner = new RemoteServiceScanner(registry, false);
        scanner.doScan(basePackage);
    }
}
