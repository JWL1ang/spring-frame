/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 13:21:29
 */

package top.vjin.frame.core.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 枚举值对象
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IEnumVo {

    /** 枚举文本 */
    @ApiModelProperty(value = "枚举显示文本", example = "申请中")
    private String text;

    /** 枚举值 */
    @ApiModelProperty(value = "枚举数据库值", example = "1")
    private String value;

    /** 枚举名称 */
    @ApiModelProperty(value = "枚举英文名称", example = "APPLYING")
    private String name;
}
