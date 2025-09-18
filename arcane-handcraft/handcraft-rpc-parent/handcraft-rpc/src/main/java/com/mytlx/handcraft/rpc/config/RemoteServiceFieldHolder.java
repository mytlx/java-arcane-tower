package com.mytlx.handcraft.rpc.config;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-09-16 12:05:38
 */
@Data
public class RemoteServiceFieldHolder {
    private Field remoteServiceField;
    private String targetClientId;
    private String fieldClassName;
    private String alias;
    private Class<?> fallbackClass;

    public RemoteServiceFieldHolder(Field remoteServiceField, AutoRemoteInjection annotation) {
        this.remoteServiceField = remoteServiceField;
        this.targetClientId = annotation.targetClientId();
        this.fieldClassName = remoteServiceField.getType().getName();
        this.alias = remoteServiceField.getName();
        if (annotation.fallbackClass() != void.class) {
            this.fallbackClass = annotation.fallbackClass();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RemoteServiceFieldHolder that)) return false;
        return Objects.equals(fieldClassName, that.fieldClassName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fieldClassName);
    }
}
