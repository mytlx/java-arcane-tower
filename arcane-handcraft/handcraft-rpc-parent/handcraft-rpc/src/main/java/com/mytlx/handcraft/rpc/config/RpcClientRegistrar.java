package com.mytlx.handcraft.rpc.config;

import com.mytlx.handcraft.rpc.client.RpcClient;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-13 23:53:13
 */
public class RpcClientRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata,
                                        BeanDefinitionRegistry registry, BeanNameGenerator generator) {
        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(EnableRpcClient.class.getName());
        if (annotationAttributes != null) {
            String clientId = (String) annotationAttributes.get("clientId");
            String[] packages = (String[]) annotationAttributes.get("packages");
            if (clientId != null && packages != null && packages.length > 0) {
                // 注册 RpcClient 为 BeanDefinition
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RpcClient.class);
                builder.addPropertyValue("clientId", clientId);
                builder.addPropertyValue("scanPackages", packages);
                builder.setScope(GenericBeanDefinition.SCOPE_SINGLETON);
                builder.setLazyInit(false);

                registry.registerBeanDefinition("rpcClient", builder.getBeanDefinition());
            }
        }

    }
}
