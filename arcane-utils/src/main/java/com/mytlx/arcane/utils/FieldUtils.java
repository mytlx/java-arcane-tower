package com.mytlx.arcane.utils;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;


public class FieldUtils {

    /**
     * @param clazz 类名
     * @param field 字段名
     * @return 返回类的指定 Field 对象，已经设置 Accessible
     */
    public static Field traverseField(Class<?> clazz, String field) throws NoSuchFieldException {

        Field result = ensureFieldsStored(clazz).get(field);
        if (result != null) return result;
        if (clazz == Object.class) {
            throw new NoSuchFieldException("不存在目标Field");
        }
        return traverseField(clazz.getSuperclass(), field);
    }

    /**
     * @param clazz
     * @return 目标类全部字段组成的信息
     */
    public static List<Field> getFields(Class<?> clazz) {

        return new ArrayList<Field>(ensureFieldsStored(clazz).values());
    }

    /**
     * 确认缓存中已存在目标类字段信息并返回该字段信息
     *
     * @param clazz
     * @return 目标类字段信息
     */
    private static Map<String, Field> ensureFieldsStored(final Class<?> clazz) {

        return CacheUtils.getValue(clazz, new CacheUtils.Provider<Class<?>, Map<String, Field>>() {

            @Override
            public Map<String, Field> get() {

                Field[] fields = clazz.getDeclaredFields();
                Arrays.sort(fields, new Comparator<Field>() {
                    @Override
                    public int compare(Field o1, Field o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                }); //按字段名自然顺序排序，确保每次返回顺序相同
                Map<String, Field> result = new LinkedHashMap<String, Field>();
                for (Field field : fields) {
                    field.setAccessible(true);
                    result.put(field.getName(), field);
                }
                return result;
            }

            @Override
            public int maxSize() {
                return 100;
            }

        });
    }

    /**
     * 返回目标类存在 type 注解声明的字段
     *
     * @param clazz
     * @param type
     * @return
     */
    public static List<Field> getFields(Class<?> clazz, final Class<? extends Annotation> type) {

        return getFields(clazz, new FieldFilter() {
            @Override
            public boolean accept(Field field) {
                return field.isAnnotationPresent(type);
            }
        });
    }

    /**
     * 返回目标类及目标父类中存在 type 注解声明的字段
     *
     * @param clazz
     * @param type
     * @return
     */
    public static List<Field> traverseGetFields(Class<?> clazz, final Class<? extends Annotation> type) {


        return traverseGetFields(clazz, new FieldFilter() {
            @Override
            public boolean accept(Field field) {
                return field.isAnnotationPresent(type);
            }
        });
    }

    /**
     * 根据过滤策略过滤目标类中全部字段，并将符合条件的字段组成数组返回
     *
     * @param clazz
     * @param filter
     * @return 目标类中符合条件的字段数组
     */
    public static List<Field> getFields(Class<?> clazz, FieldFilter filter) {

        Map<String, Field> tmp = ensureFieldsStored(clazz);
        List<Field> result = new ArrayList<Field>(tmp.size());

        for (Field field : tmp.values()) {
            if (filter.accept(field)) {
                result.add(field);
            }
        }
        return result;
    }

    /**
     * 根据过滤策略过滤目标类及目标父类中全部字段，并将符合条件的字段组成数组返回
     *
     * @param clazz
     * @param filter
     * @return
     */
    public static List<Field> traverseGetFields(Class<?> clazz, FieldFilter filter) {
        List<Field> result = new ArrayList<Field>();
        do {
            result.addAll(getFields(clazz, filter));
        } while (clazz.getSuperclass() != null && !(clazz = clazz.getSuperclass()).equals(Object.class));
        return result;
    }

    public static interface FieldFilter {

        /**
         * @param field
         * @return true - 有效<br>false - 无效
         */
        public boolean accept(Field field);

        public static FieldFilter ALL = new FieldFilter() {
            @Override
            public boolean accept(Field field) {
                return true;
            }
        };
    }

}
