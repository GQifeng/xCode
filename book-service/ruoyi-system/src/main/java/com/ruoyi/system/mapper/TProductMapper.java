package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.system.domain.TProduct;
import com.ruoyi.system.vo.TProductVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品信息Mapper接口
 *
 *
 *
 */
public interface TProductMapper extends BaseMapper<TProduct> {

    Page<TProductVo> queryList(Page<?> page, @Param("entity") TProductVo entity);

    List<TProductVo> queryList(@Param("entity") TProductVo entity);

    TProductVo queryById(@Param("id") Long id);

}
