package org.example;

import org.example.components.DoSomething;
import org.example.core.factory.ApplicationContext;


public class Main {
    public static void main(String[] args) {

        DoSomething d = ApplicationContext.getInstance().getComponent("D");
        d.doSomething();



    }


}