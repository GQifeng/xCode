package com.ruoyi.web.controller.address;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.SysUserAddress;
import com.ruoyi.system.service.SysUserAddressService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 用户地址表(SysUserAddress)表控制层
 *
 * @author makejava
 * @since 2023-09-11 14:11:39
 */
@RestController
@RequestMapping("sysUserAddress")
public class SysUserAddressController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private SysUserAddressService sysUserAddressService;

    /**
     * 分页查询所有数据
     *
     * @return 所有数据
     */
    @GetMapping
    public TableDataInfo selectAll() {
        startPage();
        List<SysUserAddress> list = sysUserAddressService.list();
        return getDataTable(list);
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public AjaxResult selectOne(@PathVariable Serializable id) {
        SysUserAddress byId = sysUserAddressService.getById(id);
        return success(byId);
    }

    /**
     * 新增数据
     *
     * @param sysUserAddress 实体对象
     * @return 新增结果
     */
    @PostMapping
    public AjaxResult insert(@RequestBody SysUserAddress sysUserAddress) {
        List<SysUserAddress> list = sysUserAddressService.list(new LambdaQueryWrapper<SysUserAddress>()
                .eq(SysUserAddress::getUserId, sysUserAddress.getUserId()));
        if (Objects.isNull(list)) {
            // 设置为默认
            sysUserAddress.setIsDefault("0");
        }
        return toAjax(sysUserAddressService.save(sysUserAddress));
    }

    /**
     * 修改数据
     *
     * @param sysUserAddress 实体对象
     * @return 修改结果
     */
    @PutMapping
    public AjaxResult update(@RequestBody SysUserAddress sysUserAddress) {
        return toAjax(sysUserAddressService.updateById(sysUserAddress));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public AjaxResult delete(@RequestParam("idList") List<Long> idList) {
        return toAjax(this.sysUserAddressService.removeByIds(idList));
    }
}

