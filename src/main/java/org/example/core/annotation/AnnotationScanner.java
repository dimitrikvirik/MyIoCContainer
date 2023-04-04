package org.example.core.annotation;

import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationScanner {

    public static Map<String, Class<?>> componentScanner(String packageName) {
        Reflections reflections = new Reflections(packageName);


        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Component.class);

        Map<String, Class<?>> components = new HashMap<>();
        for (Class<?> annotatedClass : annotatedClasses) {
            String name = annotatedClass.getAnnotation(Component.class).name();

            if (name.isBlank()) {
                name = annotatedClass.getSimpleName();
            }

            components.put(name, annotatedClass);
        }
        return components;
    }

    public static Map<String, Class<?>> proxyComponentScanner(String packageName) {
        Reflections reflections = new Reflections(packageName);


        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(ProxyComponent.class);

        Map<String, Class<?>> components = new HashMap<>();
        for (Class<?> annotatedClass : annotatedClasses) {
            String name = annotatedClass.getAnnotation(ProxyComponent.class).target();

            if (name.isBlank()) {
                name = annotatedClass.getSimpleName();
            }

            components.put(name, annotatedClass);
        }
        return components;
    }

}
