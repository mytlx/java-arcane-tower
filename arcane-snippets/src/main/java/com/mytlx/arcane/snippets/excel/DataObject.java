package com.mytlx.arcane.snippets.excel;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-10-28 18:00:37
 */
@Data
@Accessors(chain = true)
public class DataObject {

    /**
     * code_id: id
     * INTEGER(8) NOT NULL
     */
    private Long codeId;

    /**
     * detailed_accounts: 明细科目维度类型
     * VARCHAR(64) NOT NULL
     */
    private String detailedAccounts;

    /**
     * code_code: 枚举值编码
     * VARCHAR(128) NOT NULL
     */
    private String codeCode;

    /**
     * level1: 一级枚举值
     * VARCHAR(128)
     */
    private String level1;

    /**
     * level2: 二级枚举值
     * VARCHAR(128)
     */
    private String level2;

    /**
     * level3: 三级枚举值
     * VARCHAR(128)
     */
    private String level3;

    /**
     * level4: 四级枚举值
     * VARCHAR(128)
     */
    private String level4;

    /**
     * level5: 五级枚举值
     * VARCHAR(128)
     */
    private String level5;

    /**
     * code_c_name: 中文名称（各级编码用-拼接）
     * VARCHAR(512) NOT NULL
     */
    private String codeCName;

    /**
     * code_l_name: 繁体中文名称
     * VARCHAR(512) NOT NULL
     */
    private String codeLName;

    /**
     * code_e_name: 英文名称
     * VARCHAR(512) NOT NULL
     */
    private String codeEName;

    /**
     * is_last: 是否末级枚举值（1是0否）
     * TINYINT NOT NULL
     */
    private Boolean isLast;


}
