/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 12:53:52
 */

package top.vjin.frame.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Delegate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 去重列表参数接收对象,内部使用 LinkedHashSet ,保持接参一致性
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
@Data
@ApiModel(description = "去重列表参数接收对象")
public class SetDto<T> implements Set<T> {

    @Delegate
    @Valid
    @NotNull
    @ApiModelProperty(value = "数据", hidden = true)
    private Set<T> data = new LinkedHashSet<>();
}
