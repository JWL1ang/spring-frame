/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 12:52:1
 */

package top.vjin.frame.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Delegate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

/**
 * 列表参数接收对象,内部使用 LinkedList 最大化插入性能与迭代性能
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
@Data
@ApiModel(description = "列表参数接收对象")
public class ListDto<T> implements List<T> {

    @Delegate
    @Valid
    @NotNull
    @ApiModelProperty(value = "数据", hidden = true)
    private List<T> data = new LinkedList<>();
}
