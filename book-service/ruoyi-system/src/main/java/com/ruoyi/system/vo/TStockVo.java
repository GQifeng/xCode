package com.ruoyi.system.vo;

import com.ruoyi.system.domain.TStock;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 出入库记录Vo对象 t_stock
 *
 * @author F
 * @date 2023-02-03
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TStockVo extends TStock {

    private String tableName;

}
