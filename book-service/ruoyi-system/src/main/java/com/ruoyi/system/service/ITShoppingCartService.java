package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.TShoppingCart;
import com.ruoyi.system.vo.TShoppingCartVo;

import java.util.List;

/**
 * 购物车Service接口
 *
 * @author F
 * @date 2023-02-03
 */
public interface ITShoppingCartService extends IService<TShoppingCart> {

    /**
     * 分页查询
     *
     * @param entity
     * @return
     */
    TableDataInfo<TShoppingCartVo> queryList(TShoppingCartVo entity);

    /**
     * 查询全部
     *
     * @param entity
     * @return
     */
    List<TShoppingCartVo> queryAll(TShoppingCartVo entity);

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    TShoppingCartVo queryById(Long id);
}
