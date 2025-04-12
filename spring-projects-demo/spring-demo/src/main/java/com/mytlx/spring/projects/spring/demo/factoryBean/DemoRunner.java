// package com.mytlx.spring.projects.spring.demo.factoryBean;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component;
//
// /**
//  * @author TLX
//  * @version 1.0.0
//  * @since 2025-04-08 9:27
//  */
// @Component
// public class DemoRunner implements CommandLineRunner {
//
//     @Autowired
//     private MyService myService;
//
//     @Autowired
//     @Qualifier("&myFactoryBean")
//     private MyFactoryBean myFactoryBean;
//
//     @Override
//     public void run(String... args) throws Exception {
//         System.out.println("myService = " + myService);
//         System.out.println("myFactoryBean = " + myFactoryBean);
//         System.out.println("myFactoryBean.getObject = " + myFactoryBean.getObject());
//     }
// }
