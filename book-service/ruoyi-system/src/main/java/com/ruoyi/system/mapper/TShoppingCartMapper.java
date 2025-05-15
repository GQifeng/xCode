package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.system.domain.TShoppingCart;
import com.ruoyi.system.vo.TShoppingCartVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 购物车Mapper接口
 *
 *
 */
public interface TShoppingCartMapper extends BaseMapper<TShoppingCart> {

    Page<TShoppingCartVo> queryList(Page<?> page, @Param("entity") TShoppingCartVo entity);

    List<TShoppingCartVo> queryList(@Param("entity") TShoppingCartVo entity);

    TShoppingCartVo queryById(@Param("id") Long id);

}
