package com.mytlx.handcraft.rpc.config;

import com.mytlx.handcraft.rpc.client.RpcClient;
import com.mytlx.handcraft.rpc.model.RemoteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;

import java.lang.reflect.Field;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-15 14:59:28
 */
public class PostStartupProcessor implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        // 获取所有的 beanDefinition
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();

        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanDefinitionName);
            Field[] fields = bean.getClass().getDeclaredFields();

            // 遍历类中的所有字段，找到标注了 @AutoRemoteInjection 的
            for (Field field : fields) {
                if (field.isAnnotationPresent(AutoRemoteInjection.class)) {
                    AutoRemoteInjection annotation = field.getAnnotation(AutoRemoteInjection.class);
                    String targetClientId = annotation.targetClientId();
                    Class<?> fieldTypeClass = field.getType();

                    // 如果 字段类型 是 RemoteService 子类
                    if (RemoteService.class.isAssignableFrom(fieldTypeClass) && StringUtils.isNotBlank(targetClientId)) {
                        // 获取 rpcClient，并生成 proxy
                        RpcClient rpcClient = applicationContext.getBean(RpcClient.class);
                        Object proxy = rpcClient.getProxy(fieldTypeClass, targetClientId);
                        field.setAccessible(true);

                        try {
                            // 将 proxy 设置到字段上
                            field.set(bean, proxy);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            }
        }
    }

}
