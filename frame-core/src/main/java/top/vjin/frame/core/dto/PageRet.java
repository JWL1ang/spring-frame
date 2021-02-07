/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 12:42:33
 */

package top.vjin.frame.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.function.Function;

/**
 * 分页响应对象
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "分页响应对象")
public class PageRet<T> extends ListRet<T> {

    private static final long serialVersionUID = 2613422180883101933L;

    @ApiModelProperty(value = "当前页码，从1开始", example = "1")
    protected Integer pageNo;

    @ApiModelProperty(value = "每页记录数", example = "20")
    protected Integer pageSize;

    @ApiModelProperty(value = "总页数", example = "20")
    protected Integer totalPages;

    @ApiModelProperty(value = "总数据行数", example = "20")
    protected Integer totalElements;

    /**
     * 构建分页响应对象
     *
     * @param page jpa 分页
     * @param <T>  类型
     * @return 分页响应对象
     */
    public static <T> PageRet<T> of(Page<T> page) {
        PageRet<T> ret = new PageRet<>(page.getNumber() + 1, page.getSize(), page.getTotalPages(), (int) page.getTotalElements());
        ret.asOk(page.getContent());
        return ret;
    }

    /**
     * 转换内容并构建分页响应对象
     *
     * @param page      jpa 分页
     * @param converter 类型转换器
     * @param <T>       类型
     * @return 分页响应对象
     */
    public static <T, R> PageRet<R> map(Page<T> page, Function<T, R> converter) {
        return of(page.map(converter));
    }
}
