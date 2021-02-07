/*
 * Copyright (c) 2021 vjin.top All rights reserved.
 * created by JW_Liang at 2021/2/7 13:20:2
 */

package top.vjin.frame.core.define;

import top.vjin.frame.core.dto.IEnumVo;

/**
 * 枚举类型接口，实现该接口的类必须是枚举类。
 * <p>实现本接口的枚举应额外编写一个转换器(定义一个类，继承 IEnumConverter，然后标注@Converter(autoApply = true))</p>
 *
 * @author JW_Liang
 * @date 2021-02-07
 */
public interface IEnum {

    /**
     * 获取枚举类型实例要显示的文本。
     *
     * @return 返回枚举类型实例的文本。
     */
    String getText();

    /**
     * 获取枚举类型实例的值。
     *
     * @return 返回枚举类型实例的值。
     */
    String getValue();


    /**
     * 转换为值对象
     *
     * @return 枚举值对象
     */
    default IEnumVo toVo() {
        String name = this instanceof Enum ? ((Enum) this).name() : getValue();
        return new IEnumVo(getText(), getValue(), name);
    }
}
