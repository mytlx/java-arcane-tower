package com.mytlx.spring.demo.factoryBean;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-08 9:11
 */
public class MyService {

    private String name;

    public MyService(String name) {
        this.name = name;
    }

    public void printName() {
        System.out.println("MyService.name = " + name);
    }
}
