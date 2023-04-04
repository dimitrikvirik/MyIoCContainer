package org.example.components;

import org.example.core.annotation.Component;

@Component
public class E implements DoSomething {

    protected final D d;


    public E(D d) {
        this.d = d;


    }

    public void doSomething() {
        d.doSomething();
        System.out.println("E");
        d.changeNameForB("This is E");
    }
}
