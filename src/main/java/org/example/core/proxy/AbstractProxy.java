package org.example.core.proxy;

import net.sf.cglib.proxy.InvocationHandler;
import org.example.core.annotation.Before;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractProxy implements InvocationHandler {

    public record MethodProxy(Object proxy, Method method, Object[] args) {
    }


    private final Map<String, Method> methods = new HashMap<>();

    private final Object target;

    protected AbstractProxy(Object target) {
        this.target = target;

        for (Method method : target.getClass().getDeclaredMethods()) {
            this.methods.put(method.getName(), method);
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {

        //Calling Before methods
        Arrays.stream(this.getClass().getMethods()).filter(m -> m.isAnnotationPresent(
                Before.class)).forEach(m -> {
            Before before = m.getAnnotation(Before.class);
            if (Arrays.stream(method.getAnnotations()).anyMatch(a -> a.annotationType().equals(before.value()))) {
                try {
                    m.invoke(this, new MethodProxy(proxy, method, args));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });

        return methods.get(method.getName()).invoke(target, args);
    }
}
