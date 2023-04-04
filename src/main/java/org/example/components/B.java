package org.example.components;

import org.example.core.annotation.Component;

@Component
public class B implements DoSomething {

    protected final A a;


    private String name = "B Default";

    public B(A a) {
        this.a = a;
    }

    public void doSomething() {
        a.doSomething();
        System.out.println("B");
    }

    public void changeName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
