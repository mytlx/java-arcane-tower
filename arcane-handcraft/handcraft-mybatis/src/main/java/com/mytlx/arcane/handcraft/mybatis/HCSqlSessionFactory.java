package com.mytlx.arcane.handcraft.mybatis;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-08-20 13:49:58
 */
@Slf4j
public class HCSqlSessionFactory {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/java-arcane-tower?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    @SuppressWarnings("unchecked")
    public <T> T getMapper(Class<T> mapperClass) {
        return (T) Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class[]{mapperClass},
                new SelectInvocationHandler()
        );
    }

    static class SelectInvocationHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().startsWith("select")) {
                return invokeSelect(proxy, method, args);
            }
            return null;
        }

        private Object invokeSelect(Object proxy, Method method, Object[] args) {
            String sql = createSql(method);
            log.info("sql = {}", sql);

            try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    if (arg instanceof String) {
                        ps.setString(i + 1, (String) arg);
                    } else if (arg instanceof Integer) {
                        ps.setInt(i + 1, (Integer) arg);
                    } else if (arg instanceof Long) {
                        ps.setLong(i + 1, (Long) arg);
                    }
                }

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return parseResult(rs, method.getReturnType());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        private Object parseResult(ResultSet rs, Class<?> returnType) throws Exception {
            Constructor<?> constructor = returnType.getConstructor();
            Object result = constructor.newInstance();
            Field[] fields = returnType.getDeclaredFields();
            for (Field field : fields) {
                String name = field.getName();
                Object column = null;
                if (field.getType() == String.class) {
                    column = rs.getString(name);
                } else if (field.getType() == Long.class) {
                    column = rs.getLong(name);
                } else if (field.getType() == Integer.class) {
                    column = rs.getInt(name);
                }
                field.setAccessible(true);
                field.set(result, column);
            }
            return result;
        }

        private String createSql(Method method) {
            List<String> selectColumns = getSelectColumns(method.getReturnType());
            String tableName = getSelectTableName(method.getReturnType());
            String where = getSelectWhere(method);

            StringBuilder sb = new StringBuilder();
            sb.append("SELECT ");
            sb.append(String.join(",", selectColumns));
            sb.append(" FROM ");
            sb.append(tableName);
            sb.append(" WHERE ");
            sb.append(where);

            return sb.toString();
        }

        private List<String> getSelectColumns(Class<?> returnType) {
            return Arrays.stream(returnType.getDeclaredFields())
                    .map(Field::getName)
                    .collect(Collectors.toList());
        }

        private String getSelectTableName(Class<?> returnType) {
            HCTableName annotation = returnType.getAnnotation(HCTableName.class);
            if (annotation == null) {
                throw new RuntimeException("请使用HCTableName注解标注表名");
            }
            return annotation.value();
        }

        private String getSelectWhere(Method method) {
            return Arrays.stream(method.getParameters())
                    .map(parameter -> {
                        HCParam paramAnno = parameter.getAnnotation(HCParam.class);
                        String condition = paramAnno.value();
                        return condition + " = ?";
                    }).collect(Collectors.joining(" and "));
        }
    }


}
