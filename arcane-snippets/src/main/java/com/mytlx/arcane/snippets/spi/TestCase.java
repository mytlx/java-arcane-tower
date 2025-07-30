package com.mytlx.arcane.snippets.spi;

import java.util.ServiceLoader;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-07-13 15:29:06
 */
public class TestCase {

    public static void main(String[] args) {
        ServiceLoader<Search> load = ServiceLoader.load(Search.class);
        for (Search search : load) {
            search.searchDoc("hello world");
        }
    }

}
