package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.TStock;
import com.ruoyi.system.vo.TStockVo;

import java.util.List;

/**
 * 出入库记录Service接口
 *
 * @author F
 * @date 2023-02-03
 */
public interface ITStockService extends IService<TStock> {

    /**
     * 分页查询
     *
     * @param entity
     * @return
     */
    TableDataInfo<TStockVo> queryList(TStockVo entity);

    /**
     * 查询全部
     *
     * @param entity
     * @return
     */
    List<TStockVo> queryAll(TStockVo entity);

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    TStockVo queryById(Long id);
}
