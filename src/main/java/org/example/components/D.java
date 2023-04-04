package org.example.components;

import org.example.core.annotation.Component;
import org.example.core.annotation.LogMethod;

@Component
public class D implements DoSomething {

    protected final B b;

    protected final C c;

    public D(B b, C c) {
        this.b = b;
        this.c = c;
    }

    @LogMethod
    public void doSomething() {
        b.doSomething();
        c.doSomething();
        System.out.println("D");
    }

    public void changeNameForB(String name) {
        b.changeName(name);
    }

}
