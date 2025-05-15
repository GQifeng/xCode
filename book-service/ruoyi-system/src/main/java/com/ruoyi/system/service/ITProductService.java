package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.TProduct;
import com.ruoyi.system.vo.TProductVo;

import java.util.List;

/**
 * 商品信息Service接口
 *
 * @author F
 * @date 2023-02-03
 */
public interface ITProductService extends IService<TProduct> {

    /**
     * 分页查询
     *
     * @param entity
     * @return
     */
    TableDataInfo<TProductVo> queryList(TProductVo entity);

    /**
     * 查询全部
     *
     * @param entity
     * @return
     */
    List<TProductVo> queryAll(TProductVo entity);

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    TProductVo queryById(Long id);
}
