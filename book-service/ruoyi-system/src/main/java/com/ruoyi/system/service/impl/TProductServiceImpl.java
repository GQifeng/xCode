package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.system.domain.TProduct;
import com.ruoyi.system.vo.TProductVo;
import com.ruoyi.system.mapper.TProductMapper;
import com.ruoyi.system.service.ITProductService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品信息Service业务层处理
 *
 * @author F
 * @date 2023-02-03
 */
@Service
public class TProductServiceImpl extends ServiceImpl<TProductMapper, TProduct> implements ITProductService {

    @Override
    public TableDataInfo<TProductVo> queryList(TProductVo entity) {
        return PageUtils.buildDataInfo(this.baseMapper.queryList(PageUtils.buildPage(), entity));
    }

    @Override
    public List<TProductVo> queryAll(TProductVo entity) {
        return this.baseMapper.queryList(entity);
    }

    @Override
    public TProductVo queryById(Long id) {
        return this.baseMapper.queryById(id);
    }
}
