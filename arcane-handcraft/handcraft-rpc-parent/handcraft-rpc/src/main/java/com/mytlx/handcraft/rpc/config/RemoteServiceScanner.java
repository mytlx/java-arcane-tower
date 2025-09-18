package com.mytlx.handcraft.rpc.config;

import com.mytlx.handcraft.rpc.client.RpcClient;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.lang.NonNull;

import java.util.Map;
import java.util.Set;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-18 16:17:39
 */
public class RemoteServiceScanner extends ClassPathBeanDefinitionScanner {

    public RemoteServiceScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
        addIncludeFilter((metadataReader, metadataReaderFactory) -> {
            ClassMetadata classMetadata = metadataReader.getClassMetadata();
            AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
            return classMetadata.isInterface() && annotationMetadata.hasAnnotation(AutoRemoteInjection.class.getName());
        });
    }

    @NonNull
    @Override
    protected Set<BeanDefinitionHolder> doScan(@NonNull String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);

        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
            BeanDefinition beanDefinition = beanDefinitionHolder.getBeanDefinition();
            if (beanDefinition instanceof ScannedGenericBeanDefinition sbd) {
                AnnotationMetadata metadata = sbd.getMetadata();
                Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(AutoRemoteInjection.class.getName());
                if (annotationAttributes == null) {
                    continue;
                }
                Object targetClientIdObj = annotationAttributes.get("targetClientId");
                if (targetClientIdObj == null) {
                    continue;
                }
                String targetClientId = targetClientIdObj.toString();
                Class<?> fallbackClass = (Class<?>) annotationAttributes.get("fallbackClass");
                String beanClassName = sbd.getBeanClassName();

                sbd.setBeanClass(RemoteServiceFactoryBean.class);
                sbd.getPropertyValues().addPropertyValue("rpcInterfaceClass", beanClassName);
                sbd.getPropertyValues().addPropertyValue("targetClientId", targetClientId);
                sbd.getPropertyValues().addPropertyValue("remoteClient", new RuntimeBeanReference(RpcClient.class));
                sbd.getPropertyValues().addPropertyValue("fallbackClass", fallbackClass);
            }
        }

        return beanDefinitionHolders;
    }

    @Override
    protected boolean isCandidateComponent(@NonNull AnnotatedBeanDefinition beanDefinition) {
        return true;
    }
}
