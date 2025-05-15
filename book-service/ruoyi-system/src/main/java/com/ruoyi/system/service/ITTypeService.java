package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.TType;
import com.ruoyi.system.vo.TProductVo;
import com.ruoyi.system.vo.TTypeVo;

import java.util.List;
import java.util.Map;

/**
 * 类型管理Service接口
 *
 * @author F
 * @date 2023-02-03
 */
public interface ITTypeService extends IService<TType> {

    /**
     * 分页查询
     *
     * @param entity
     * @return
     */
    TableDataInfo<TTypeVo> queryList(TTypeVo entity);

    /**
     * 查询全部
     *
     * @param entity
     * @return
     */
    List<TTypeVo> queryAll(TTypeVo entity);

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    TTypeVo queryById(Long id);

    Object getTjList(TProductVo entity) throws Exception;
}
