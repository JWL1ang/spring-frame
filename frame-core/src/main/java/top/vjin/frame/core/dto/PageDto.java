/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 12:45:46
 */

package top.vjin.frame.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 用于分页参数接收查询
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
@Data
@ApiModel(description = "分页请求对象")
public class PageDto {

    @Min(1)
    @ApiModelProperty(value = "当前页码，从1开始，默认1", example = "1")
    protected Integer pageNo;

    @Min(1)
    @Max(1000)
    @ApiModelProperty(value = "每页记录数,最大1000条，默认20", example = "20")
    protected Integer pageSize;

    @Contract("->!null")
    public Integer getPageNo() {
        return pageNo == null ? 1 : pageNo;
    }

    @Contract("->!null")
    public Integer getPageSize() {
        return pageSize == null ? 20 : pageSize;
    }

    /**
     * 转换到 Pageable 实例
     *
     * @return 返回jpa dao pageable实例
     */
    public Pageable toPageable() {
        return PageRequest.of(getPageNo() - 1, getPageSize());
    }


    /**
     * 转换到 Pageable 实例
     *
     * @param direction  排序模式[ASC/DESC]
     * @param properties 排序字段
     * @return 返回 spring data pageable实例
     */
    public Pageable toPageable(Sort.Direction direction, String... properties) {
        return PageRequest.of(getPageNo() - 1, getPageSize(), direction, properties);
    }

    /**
     * 转换到 Pageable 实例
     *
     * @param sort 排序模式
     * @return 返回 spring data pageable实例
     */
    public Pageable toPageable(Sort sort) {
        return PageRequest.of(getPageNo() - 1, getPageSize(), sort);
    }
}
