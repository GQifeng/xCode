package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.system.domain.TStock;
import com.ruoyi.system.vo.TStockVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 出入库记录Mapper接口
 *
 *
 */
public interface TStockMapper extends BaseMapper<TStock> {

    Page<TStockVo> queryList(Page<?> page, @Param("entity") TStockVo entity);

    List<TStockVo> queryList(@Param("entity") TStockVo entity);

    TStockVo queryById(@Param("id") Long id);

}
