package com.ruoyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.system.domain.TType;
import com.ruoyi.system.vo.TTypeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类型管理Mapper接口
 *
 *
 */
public interface TTypeMapper extends BaseMapper<TType> {

    Page<TTypeVo> queryList(Page<?> page, @Param("entity") TTypeVo entity);

    List<TTypeVo> queryList(@Param("entity") TTypeVo entity);

    TTypeVo queryById(@Param("id") Long id);

}
