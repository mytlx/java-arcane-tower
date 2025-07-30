package com.mytlx.arcane.snippets.spi;

import java.util.List;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-07-13 15:26:01
 */
public class FileSearch implements Search {
    @Override
    public List<String> searchDoc(String keyword) {
        System.out.println("文件搜索：" + keyword);
        return null;
    }
}
