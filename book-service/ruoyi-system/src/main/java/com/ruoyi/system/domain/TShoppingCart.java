package com.ruoyi.system.domain;

import java.math.BigDecimal;
import com.ruoyi.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 购物车对象 t_shopping_cart
 *
 * @author F
 * @date 2023-02-03
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TShoppingCart extends BaseEntity {

    /**
     * 主键Id
     */
    private Long id;

    /**
     * 用户id
     */
    @Excel(name = "用户id")
    private Long userId;

    /**
     * 关联id
     */
    @Excel(name = "关联id")
    private Long tableId;

    /**
     * 数量
     */
    @Excel(name = "数量")
    private Long num;

    /**
     * 价格
     */
    @Excel(name = "价格")
    private BigDecimal price;

    private String status;
}
