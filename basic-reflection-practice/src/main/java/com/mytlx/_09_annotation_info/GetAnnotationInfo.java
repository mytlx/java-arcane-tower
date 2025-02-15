package com.mytlx._09_annotation_info;

import java.lang.annotation.Annotation;

/**
 * 9. 获取注解信息
 * 题目：创建一个类，并给它添加一个注解 @Deprecated，然后使用反射获取并打印该类是否被标注为 @Deprecated。
 * <pre>
 * @Deprecated
 * public class OldClass {
 * }
 * </pre>
 * @author TLX
 * @version 1.0.0
 * @since 2025-02-15 18:47
 */
@SuppressWarnings("deprecation")
public class GetAnnotationInfo {

    public static void main(String[] args) {
        // 第一种方式：通过 getAnnotation 获取指定注解
        Deprecated annotation = OldClass.class.getAnnotation(Deprecated.class);
        if (annotation != null) {
            System.out.println("The class is deprecated.");
        }

        // 第二种方式：通过 getAnnotations 获取所有注解并检查是否包含 Deprecated
        Annotation[] annotations = OldClass.class.getAnnotations();
        for (Annotation anno : annotations) {
            if (anno instanceof Deprecated) {
                System.out.println("The class is deprecated.");
            }
        }
    }

}
