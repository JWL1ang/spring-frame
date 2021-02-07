/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 13:0:15
 */

package top.vjin.frame.core.initializer;

import java.util.Collections;
import java.util.List;

/**
 * 初始化器
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
public interface Initializer {

    /**
     * 得到当前初始化器前面的初始化器的Class列表
     */
    default List<Class<? extends Initializer>> getFronts() {
        return Collections.emptyList();
    }

    /**
     * 得到当前初始化器后面的初始化器的Class列表
     */
    default List<Class<? extends Initializer>> getBehinds() {
        return Collections.emptyList();
    }

    /**
     * 执行初始化
     */
    void init();
}
