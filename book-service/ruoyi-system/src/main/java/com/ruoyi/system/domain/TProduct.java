package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 商品信息对象 t_product
 *
 * @author F
 * @date 2023-02-03
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TProduct extends BaseEntity {

    /**
     * $column.columnComment
     */
    private Long id;

    /**
     * 商品类型
     */
    @Excel(name = "商品类型")
    private Long typeId;

    /**
     * 商品名称
     */
    @Excel(name = "商品名称")
    private String productName;

    /**
     * 商品图片
     */
    @Excel(name = "商品图片")
    private String productImg;

    /**
     * 商品详情
     */
    @Excel(name = "商品详情")
    private String productDetails;

    /**
     * 库存数量
     */
    @Excel(name = "库存数量")
    private Long productNum;

    private BigDecimal price;

}
