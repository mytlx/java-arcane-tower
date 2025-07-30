package com.mytlx.arcane.snippets.spi;

import java.util.List;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-07-13 15:26:33
 */
public class DatabaseSearch implements Search {

    @Override
    public List<String> searchDoc(String keyword) {
        System.out.println("数据库搜索：" + keyword);
        return null;
    }
}
