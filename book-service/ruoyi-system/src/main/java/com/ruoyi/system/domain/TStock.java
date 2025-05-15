package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 出入库记录对象 t_stock
 *
 * @author F
 * @date 2023-02-03
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TStock extends BaseEntity {

    /**
     * 主键Id
     */
    private Long id;

    /**
     * 关联Id
     */
    @Excel(name = "关联Id")
    private Long tableId;

    /**
     * 类型（0：入库，1：出库）
     */
    @Excel(name = "类型", readConverterExp = "0=：入库，1：出库")
    private String type;

    /**
     * 数量
     */
    @Excel(name = "数量")
    private Long num;

}
