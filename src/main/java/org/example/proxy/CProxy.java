package org.example.proxy;

import org.example.core.annotation.Before;
import org.example.core.annotation.LogMethod;
import org.example.core.annotation.ProxyComponent;
import org.example.core.proxy.AbstractProxy;

@ProxyComponent(target = "C")
public class CProxy extends AbstractProxy {


    public CProxy(Object target) {
        super(target);
    }

    @Before(LogMethod.class)
    public void log(MethodProxy proxyMethod) {
        System.out.println("Calling method: " + proxyMethod.method().getName());
    }


}
