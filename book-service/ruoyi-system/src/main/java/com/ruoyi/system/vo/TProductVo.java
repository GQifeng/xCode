package com.ruoyi.system.vo;

import com.ruoyi.system.domain.TProduct;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 商品信息Vo对象 t_product
 *
 * @author F
 * @date 2023-02-03
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TProductVo extends TProduct {

    private String typeName;

    private String ids;

    private String salesNum;

}
