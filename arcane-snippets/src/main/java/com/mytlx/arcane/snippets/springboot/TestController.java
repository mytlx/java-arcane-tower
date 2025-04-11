package com.mytlx.arcane.snippets.springboot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-03-09 16:52
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }
}
