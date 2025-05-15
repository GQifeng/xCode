package com.ruoyi.web.controller.system;

import cn.hutool.core.util.ObjectUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.TProduct;
import com.ruoyi.system.domain.TStock;
import com.ruoyi.system.service.ITProductService;
import com.ruoyi.system.vo.TStockVo;
import com.ruoyi.system.service.ITStockService;
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
 * 出入库记录Controller
 *
 * @author F
 * @date 2023-02-03
 */
@Api(tags = "出入库记录Controller")
@RestController
@RequestMapping("/system/stock")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TStockController extends BaseController {

    private final ITStockService tStockService;

    private final ITProductService productService;

    @ApiOperation("查询出入库记录列表")
    @PreAuthorize("@ss.hasPermi('system:stock:list')")
    @GetMapping("/list")
    public TableDataInfo<TStockVo> list(TStockVo entity) {
        return tStockService.queryList(entity);
    }

    @ApiOperation("查询出入库记录所有列表")
    @GetMapping("/listAll")
    public AjaxResult listAll(TStockVo entity) {
        return AjaxResult.success("查询成功", tStockService.queryAll(entity));
    }

    @ApiOperation("导出出入库记录列表")
    @PreAuthorize("@ss.hasPermi('system:stock:export')")
    @Log(title = "出入库记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TStockVo entity) {
        List<TStockVo> list = tStockService.queryAll(entity);
        ExcelUtil<TStockVo> util = new ExcelUtil<>(TStockVo.class);
        util.exportExcel(response, list, "出入库记录数据");
    }

    @ApiOperation("获取出入库记录详细信息")
    @PreAuthorize("@ss.hasPermi('system:stock:query')")
    @GetMapping(value = "/getInfo/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success("查询成功", tStockService.queryById(id));
    }

    @ApiOperation("新增出入库记录")
    @PreAuthorize("@ss.hasPermi('system:stock:add')")
    @Log(title = "出入库记录", businessType = BusinessType.INSERT)
    @PostMapping("add")
    public AjaxResult add(@RequestBody TStockVo entity) {
        TProduct product = productService.getById(entity.getTableId());
        if (ObjectUtil.isEmpty(product)){
            throw new ServiceException("操作失败");
        }
        if (entity.getType().equals("1")){
            if (entity.getNum()>product.getProductNum()){
                throw new ServiceException("出库数量大于现有库存数量");
            }
            product.setProductNum(product.getProductNum()-entity.getNum());
        }else {
            product.setProductNum(product.getProductNum()+entity.getNum());
        }
        productService.updateById(product);
        return toAjax(tStockService.save(entity));
    }

    @ApiOperation("修改出入库记录")
    @PreAuthorize("@ss.hasPermi('system:stock:edit')")
    @Log(title = "出入库记录", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    public AjaxResult edit(@RequestBody TStockVo entity) {
        TProduct product = productService.getById(entity.getTableId());
        if (ObjectUtil.isEmpty(product)){
            throw new ServiceException("操作失败");
        }
        TStock stock = tStockService.getById(entity.getId());

        if (entity.getType().equals("1")){
            if (entity.getNum()>(product.getProductNum()+stock.getNum())){
                throw new ServiceException("出库数量大于现有库存数量");
            }
            product.setProductNum((product.getProductNum()+stock.getNum())-entity.getNum());
        }else {
            product.setProductNum((product.getProductNum()-stock.getNum())+entity.getNum());
        }
        productService.updateById(product);
        return toAjax(tStockService.updateById(entity));
    }

    @ApiOperation("删除出入库记录")
    @PreAuthorize("@ss.hasPermi('system:stock:remove')")
    @Log(title = "出入库记录", businessType = BusinessType.DELETE)
	@GetMapping("/remove/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tStockService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }
}
