package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.vo.TProductVo;
import com.ruoyi.system.vo.TTypeVo;
import com.ruoyi.system.service.ITTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.page.TableDataInfo;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 类型管理Controller
 *
 * @author F
 * @date 2023-02-03
 */
@Api(tags = "类型管理Controller")
@RestController
@RequestMapping("/system/type")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TTypeController extends BaseController {

    private final ITTypeService tTypeService;

    @ApiOperation("查询类型管理列表")
//    @PreAuthorize("@ss.hasPermi('system:type:list')")
    @GetMapping("/list")
    public TableDataInfo<TTypeVo> list(TTypeVo entity) {
        return tTypeService.queryList(entity);
    }

    @ApiOperation("推荐商品")
//    @PreAuthorize("@ss.hasPermi('system:type:list')")
    @GetMapping("/tjList")
    public Object tjList(TProductVo entity) throws Exception {
        return tTypeService.getTjList(entity);
    }

    @ApiOperation("查询类型管理所有列表")
    @GetMapping("/listAll")
    public AjaxResult listAll(TTypeVo entity) {
        return AjaxResult.success("查询成功", tTypeService.queryAll(entity));
    }

    @ApiOperation("导出类型管理列表")
    @PreAuthorize("@ss.hasPermi('system:type:export')")
    @Log(title = "类型管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TTypeVo entity) {
        List<TTypeVo> list = tTypeService.queryAll(entity);
        ExcelUtil<TTypeVo> util = new ExcelUtil<>(TTypeVo.class);
        util.exportExcel(response, list, "类型管理数据");
    }

    @ApiOperation("获取类型管理详细信息")
    @PreAuthorize("@ss.hasPermi('system:type:query')")
    @GetMapping(value = "/getInfo/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success("查询成功", tTypeService.queryById(id));
    }

    @ApiOperation("新增类型管理")
    @PreAuthorize("@ss.hasPermi('system:type:add')")
    @Log(title = "类型管理", businessType = BusinessType.INSERT)
    @PostMapping("add")
    public AjaxResult add(@RequestBody TTypeVo entity) {
        return toAjax(tTypeService.save(entity));
    }

    @ApiOperation("修改类型管理")
    @PreAuthorize("@ss.hasPermi('system:type:edit')")
    @Log(title = "类型管理", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    public AjaxResult edit(@RequestBody TTypeVo entity) {
        return toAjax(tTypeService.updateById(entity));
    }

    @ApiOperation("删除类型管理")
    @PreAuthorize("@ss.hasPermi('system:type:remove')")
    @Log(title = "类型管理", businessType = BusinessType.DELETE)
	@GetMapping("/remove/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tTypeService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
