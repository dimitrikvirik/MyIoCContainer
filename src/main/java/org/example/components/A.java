package org.example.components;

import org.example.core.annotation.Component;

@Component
public class A implements DoSomething {

    public void doSomething() {
        System.out.println("A");
    }

}
