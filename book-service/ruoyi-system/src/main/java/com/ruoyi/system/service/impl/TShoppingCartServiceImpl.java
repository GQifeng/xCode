package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.system.domain.TShoppingCart;
import com.ruoyi.system.vo.TShoppingCartVo;
import com.ruoyi.system.mapper.TShoppingCartMapper;
import com.ruoyi.system.service.ITShoppingCartService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 购物车Service业务层处理
 *
 * @author F
 * @date 2023-02-03
 */
@Service
public class TShoppingCartServiceImpl extends ServiceImpl<TShoppingCartMapper, TShoppingCart> implements ITShoppingCartService {

    @Override
    public TableDataInfo<TShoppingCartVo> queryList(TShoppingCartVo entity) {
        return PageUtils.buildDataInfo(this.baseMapper.queryList(PageUtils.buildPage(), entity));
    }

    @Override
    public List<TShoppingCartVo> queryAll(TShoppingCartVo entity) {
        return this.baseMapper.queryList(entity);
    }

    @Override
    public TShoppingCartVo queryById(Long id) {
        return this.baseMapper.queryById(id);
    }
}
