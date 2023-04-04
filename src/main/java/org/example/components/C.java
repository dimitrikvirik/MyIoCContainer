package org.example.components;

import org.example.core.annotation.Component;
import org.example.core.annotation.LogMethod;

@Component
public class C implements DoSomething {
    public C() {
    }

    @LogMethod
    public void doSomething() {
        System.out.println("C");
    }
}
