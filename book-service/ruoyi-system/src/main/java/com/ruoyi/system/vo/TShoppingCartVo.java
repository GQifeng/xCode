package com.ruoyi.system.vo;

import java.math.BigDecimal;
import com.ruoyi.system.domain.TShoppingCart;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 购物车Vo对象 t_shopping_cart
 *
 * @author F
 * @date 2023-02-03
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TShoppingCartVo extends TShoppingCart {

    private String productName;

    private String productImg;

    private String ids;

}
