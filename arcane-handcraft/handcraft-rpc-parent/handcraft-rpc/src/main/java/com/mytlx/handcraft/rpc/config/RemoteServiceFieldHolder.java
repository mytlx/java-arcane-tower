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

    public RemoteServiceFieldHolder(Field remoteServiceField, String targetClientId) {
        this.remoteServiceField = remoteServiceField;
        this.targetClientId = targetClientId;
        this.fieldClassName = remoteServiceField.getType().getName();
        this.alias = remoteServiceField.getName();
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
