package org.example.core.factory;


import org.example.core.annotation.AnnotationScanner;

import java.util.Map;


//Decorator for IocContainer
public class ApplicationContext extends IocContainer {

    private static ApplicationContext instance;

    private final IocContainer iocContainer;

    private ApplicationContext(IocContainer iocContainer) {
        super();
        this.iocContainer = iocContainer;
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        StackTraceElement main = stack[stack.length - 1];
        String mainClass = main.getClassName();
        try {
            String packageName = Class.forName(mainClass).getPackageName();

            Map<String, Class<?>> componentScanner = AnnotationScanner.componentScanner(packageName);
            iocContainer.setComponents(componentScanner);

            Map<String, Class<?>> proxyComponentScanner = AnnotationScanner.proxyComponentScanner(packageName);
            iocContainer.setProxyComponents(proxyComponentScanner);

            iocContainer.init();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        }
    }


    public static synchronized ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext(new IocContainer());

        }
        return instance;
    }


    @Override
    public <T> T getComponent(String name) {
        return iocContainer.getComponent(name);
    }

    @Override
    public Map<String, Object> getInstances() {
        return iocContainer.getInstances();
    }


}
