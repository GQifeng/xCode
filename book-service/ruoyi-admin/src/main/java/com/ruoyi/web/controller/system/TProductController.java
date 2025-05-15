package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.vo.TProductVo;
import com.ruoyi.system.service.ITProductService;
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
 * 商品信息Controller
 *
 * @author F
 * @date 2023-02-03
 */
@Api(tags = "商品信息Controller")
@RestController
@RequestMapping("/system/product")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TProductController extends BaseController {

    private final ITProductService tProductService;

    @ApiOperation("查询商品信息列表")
//    @PreAuthorize("@ss.hasPermi('system:product:list')")
    @GetMapping("/list")
    public TableDataInfo<TProductVo> list(TProductVo entity) {
        return tProductService.queryList(entity);
    }

    @ApiOperation("查询商品信息所有列表")
    @GetMapping("/listAll")
    public AjaxResult listAll(TProductVo entity) {
        return AjaxResult.success("查询成功", tProductService.queryAll(entity));
    }

    @ApiOperation("导出商品信息列表")
    @PreAuthorize("@ss.hasPermi('system:product:export')")
    @Log(title = "商品信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TProductVo entity) {
        List<TProductVo> list = tProductService.queryAll(entity);
        ExcelUtil<TProductVo> util = new ExcelUtil<>(TProductVo.class);
        util.exportExcel(response, list, "商品信息数据");
    }

    @ApiOperation("获取商品信息详细信息")
    @PreAuthorize("@ss.hasPermi('system:product:query')")
    @GetMapping(value = "/getInfo/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success("查询成功", tProductService.queryById(id));
    }

    @ApiOperation("新增商品信息")
    @PreAuthorize("@ss.hasPermi('system:product:add')")
    @Log(title = "商品信息", businessType = BusinessType.INSERT)
    @PostMapping("add")
    public AjaxResult add(@RequestBody TProductVo entity) {
        return toAjax(tProductService.save(entity));
    }

    @ApiOperation("修改商品信息")
    @PreAuthorize("@ss.hasPermi('system:product:edit')")
    @Log(title = "商品信息", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    public AjaxResult edit(@RequestBody TProductVo entity) {
        return toAjax(tProductService.updateById(entity));
    }

    @ApiOperation("删除商品信息")
    @PreAuthorize("@ss.hasPermi('system:product:remove')")
    @Log(title = "商品信息", businessType = BusinessType.DELETE)
	@GetMapping("/remove/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tProductService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
