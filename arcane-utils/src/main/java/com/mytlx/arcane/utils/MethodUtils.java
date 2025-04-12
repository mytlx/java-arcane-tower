package com.mytlx.arcane.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

public class MethodUtils {
    public MethodUtils() {
    }

    public static <T> Constructor<T> getConstructor(Class<T> target, Class<?>... args) throws NoSuchMethodException {
        Class<?>[] tmp = (Class[]) Arrays.copyOf(args, args.length);
        int i = 0;

        while(i < args.length) {
            while(true) {
                try {
                    return target.getConstructor(tmp);
                } catch (NoSuchMethodException var8) {
                    if (tmp[i].equals(Object.class)) {
                        Class<?>[] interfaces = args[i].getInterfaces();
                        int j = 0;

                        while(j < interfaces.length) {
                            tmp[i] = interfaces[j];

                            try {
                                return target.getConstructor(tmp);
                            } catch (NoSuchMethodException var7) {
                                ++j;
                            }
                        }

                        tmp[i] = args[i];
                        ++i;
                        break;
                    }

                    tmp[i] = tmp[i].getSuperclass();
                }
            }
        }

        return target.getConstructor();
    }

    public static Method getMethod(Class<?> clazz, String name) {
        List<Method> list = getMethods(clazz, name);
        return list != null && list.size() > 0 ? (Method)list.get(0) : null;
    }

    public static Method getMethod(Class<?> clazz, String name, Class<?>... params) {
        List<Method> list = getMethods(clazz, name);
        if (list != null) {
            Iterator var4 = list.iterator();

            while(var4.hasNext()) {
                Method method = (Method)var4.next();
                if (Arrays.equals(method.getParameterTypes(), params)) {
                    return method;
                }
            }
        }

        return null;
    }

    public static List<Method> getMethods(Class<?> clazz, String name) {
        return (List)getMethodMap(clazz).get(name);
    }

    public static List<Method> getMethodRegex(Class<?> clazz, String regex) {
        Map<String, List<Method>> map = getMethodMap(clazz);
        List<Method> result = new ArrayList();
        Iterator it = map.entrySet().iterator();

        while(true) {
            Entry entry;
            do {
                if (!it.hasNext()) {
                    return result;
                }

                entry = (Entry)it.next();
            } while(regex != null && !((String)entry.getKey()).matches(regex));

            result.addAll((Collection)entry.getValue());
        }
    }

    private static Map<String, List<Method>> getMethodMap(final Class<?> clazz) {
        return (Map) CacheUtils.getValue(MethodUtils.class, clazz.toString(), new CacheUtils.Provider<String, Map<String, List<Method>>>() {
            public Map<String, List<Method>> get() {
                Map<String, List<Method>> map = new HashMap();
                Method[] methods = clazz.getMethods();
                Method[] var4 = methods;
                int var5 = methods.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    Method method = var4[var6];
                    method.setAccessible(true);
                    if (!map.containsKey(method.getName())) {
                        map.put(method.getName(), new ArrayList());
                    }

                    ((List)map.get(method.getName())).add(method);
                }

                return map;
            }

            public int maxSize() {
                return 100;
            }
        });
    }

    public static Method getSetter(Class<?> clazz, String name) {
        if (clazz != null && name != null) {
            String setName = "set" + name.substring(0, 1).toUpperCase() + (name.length() > 1 ? name.substring(1, name.length()) : "");
            Method result = getMethod(clazz, setName);
            return result == null ? getIs(clazz, name) : result;
        } else {
            throw new IllegalArgumentException("参数不能为空。");
        }
    }

    public static Method getGetter(Class<?> clazz, String name) {
        if (clazz != null && name != null) {
            String getName = "get" + name.substring(0, 1).toUpperCase() + (name.length() > 1 ? name.substring(1, name.length()) : "");
            Method result = getMethod(clazz, getName);
            return result == null ? getIs(clazz, name) : result;
        } else {
            throw new IllegalArgumentException("参数不能为空。");
        }
    }

    public static Method getIs(Class<?> clazz, String name) {
        if (clazz != null && name != null) {
            String isName = "is" + name.substring(0, 1).toUpperCase() + (name.length() > 1 ? name.substring(1, name.length()) : "");
            return getMethod(clazz, isName);
        } else {
            throw new IllegalArgumentException("参数不能为空。");
        }
    }
}
