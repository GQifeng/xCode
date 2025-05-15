package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.system.domain.TStock;
import com.ruoyi.system.vo.TStockVo;
import com.ruoyi.system.mapper.TStockMapper;
import com.ruoyi.system.service.ITStockService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 出入库记录Service业务层处理
 *
 * @author F
 * @date 2023-02-03
 */
@Service
public class TStockServiceImpl extends ServiceImpl<TStockMapper, TStock> implements ITStockService {

    @Override
    public TableDataInfo<TStockVo> queryList(TStockVo entity) {
        return PageUtils.buildDataInfo(this.baseMapper.queryList(PageUtils.buildPage(), entity));
    }

    @Override
    public List<TStockVo> queryAll(TStockVo entity) {
        return this.baseMapper.queryList(entity);
    }

    @Override
    public TStockVo queryById(Long id) {
        return this.baseMapper.queryById(id);
    }
}
