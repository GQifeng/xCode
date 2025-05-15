package com.ruoyi.web.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.TProduct;
import com.ruoyi.system.domain.TShoppingCart;
import com.ruoyi.system.service.ITProductService;
import com.ruoyi.system.vo.TShoppingCartVo;
import com.ruoyi.system.service.ITShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.page.TableDataInfo;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 购物车Controller
 *
 * @author F
 * @date 2023-02-03
 */
@Api(tags = "购物车Controller")
@RestController
@RequestMapping("/system/shoppingCart")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TShoppingCartController extends BaseController {

    private final ITShoppingCartService tShoppingCartService;

    private final ITProductService productService;

    @ApiOperation("查询购物车列表")
    @PreAuthorize("@ss.hasPermi('system:shoppingCart:list')")
    @GetMapping("/list")
    public TableDataInfo<TShoppingCartVo> list(TShoppingCartVo entity) {
        if (!SecurityUtils.isAdmin(SecurityUtils.getUserId())){
            entity.setUserId(SecurityUtils.getUserId());
        }
        return tShoppingCartService.queryList(entity);
    }

    @ApiOperation("查询购物车所有列表")
    @GetMapping("/listAll")
    public AjaxResult listAll(TShoppingCartVo entity) {
        return AjaxResult.success("查询成功", tShoppingCartService.queryAll(entity));
    }

    @ApiOperation("导出购物车列表")
    @PreAuthorize("@ss.hasPermi('system:shoppingCart:export')")
    @Log(title = "购物车", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TShoppingCartVo entity) {
        List<TShoppingCartVo> list = tShoppingCartService.queryAll(entity);
        ExcelUtil<TShoppingCartVo> util = new ExcelUtil<>(TShoppingCartVo.class);
        util.exportExcel(response, list, "购物车数据");
    }

    @ApiOperation("获取购物车详细信息")
    @PreAuthorize("@ss.hasPermi('system:shoppingCart:query')")
    @GetMapping(value = "/getInfo/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success("查询成功", tShoppingCartService.queryById(id));
    }

    @ApiOperation("新增购物车")
    @PreAuthorize("@ss.hasPermi('system:shoppingCart:add')")
    @Log(title = "购物车", businessType = BusinessType.INSERT)
    @PostMapping("add")
    public AjaxResult add(@RequestBody TShoppingCartVo entity) {
        entity.setUserId(SecurityUtils.getUserId());
        TProduct product = productService.getById(entity.getTableId());
        if (product==null){
            throw new ServiceException("添加失败");
        }
        if (entity.getNum()>product.getProductNum()){
            throw new ServiceException("库存数量不足");
        }
        entity.setPrice(new BigDecimal(entity.getNum()).multiply(product.getPrice()));
        return toAjax(tShoppingCartService.save(entity));
    }

    @ApiOperation("修改购物车")
    @PreAuthorize("@ss.hasPermi('system:shoppingCart:edit')")
    @Log(title = "购物车", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    public AjaxResult edit(@RequestBody TShoppingCartVo entity) {
        TProduct product = productService.getById(entity.getTableId());
        if (product==null){
            throw new ServiceException("修改失败");
        }
        if (entity.getNum()>product.getProductNum()){
            throw new ServiceException("库存数量不足");
        }
        entity.setPrice(new BigDecimal(entity.getNum()).multiply(product.getPrice()));
        return toAjax(tShoppingCartService.updateById(entity));
    }

    @ApiOperation("删除购物车")
    @PreAuthorize("@ss.hasPermi('system:shoppingCart:remove')")
    @Log(title = "购物车", businessType = BusinessType.DELETE)
	@GetMapping("/remove/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tShoppingCartService.removeByIds(Arrays.asList(ids)) ? 1 : 0);
    }

    @ApiOperation("结算购物车")
    @PreAuthorize("@ss.hasPermi('system:shoppingCart:settlement')")
    @Log(title = "购物车", businessType = BusinessType.UPDATE)
    @PostMapping("settlement")
    public AjaxResult settlement(@RequestBody TShoppingCartVo entity){
        List<TShoppingCart> cartList = tShoppingCartService.list(new LambdaQueryWrapper<TShoppingCart>().in(TShoppingCart::getId, entity.getIds().split(",")));
        cartList.forEach(item->{
            TProduct product = productService.getById(item.getTableId());
            if (item.getNum()>product.getProductNum()){
                throw new ServiceException(product.getProductName()+"库存数量不足");
            }
            product.setProductNum(product.getProductNum()-item.getNum());
            productService.updateById(product);
        });
        boolean update = tShoppingCartService.update(new LambdaUpdateWrapper<TShoppingCart>().set(TShoppingCart::getStatus, "0").in(TShoppingCart::getId,entity.getIds().split(",")));
        return AjaxResult.success();
    }
}
