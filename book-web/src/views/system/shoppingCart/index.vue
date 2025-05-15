<!-- 购物车 -->
<template>
  <div class="app-container" >
    <div class="cart-web">
      <div class="search">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="商品名称" prop="productName">
        <el-input v-model="queryParams.productName" placeholder="请输入商品名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    </div>
<div  class="cart-table-container">
    <el-table v-loading="loading" :data="shoppingCartList" ref="shoppingCartList"
      @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="商品图片" align="center" prop="productImg" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.productImg" :width="100" :height="100" />

        </template>
      </el-table-column>
      <el-table-column label="商品名称" sortable align="center" prop="productName"></el-table-column>
      <el-table-column label="用户" align="center" prop="createBy" />

      <el-table-column label="数量" align="center" sortable prop="num" />
      <el-table-column label="价格" align="center" sortable prop="price" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width"
        v-if="activeName=='shoppingCart'">
        <template slot-scope="scope">
          <el-button type="danger" round @click="handleDelete(scope.row)" v-hasPermi="['system:shoppingCart:remove']">删除
          </el-button>
        </template>
      </el-table-column>
      <!-- <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="商品名称" align="center" sortable prop="productName"/>
      <el-table-column label="商品图片" align="center" prop="productImg" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.productImg" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="用户" align="center" prop="createBy"/>
      <el-table-column label="数量" align="center" sortable prop="num"/>
      <el-table-column label="价格" align="center" sortable prop="price"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width"
                       v-if="activeName=='shoppingCart'"
      >
        <template slot-scope="scope">
          <el-button type="danger" round @click="handleDelete(scope.row)"
                     v-hasPermi="['system:shoppingCart:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column> -->
    </el-table>



    <div style="padding-left: 95%;padding-top: 1%">
      <div style="position: absolute;right: 7%;margin-top: 8px;">合计：{{ totalPrice }}元
      </div>
      <el-button type="success"  plain :disabled="single" @click="handleSettlement"
        v-hasPermi="['system:shoppingCart:edit']">结算
      </el-button>
    </div>
</div>
</div>
  </div>

</template>

<script>
  import {
    listShoppingCart,
    getShoppingCart,
    delShoppingCart,
    addShoppingCart,
    updateShoppingCart,
    shoppingCartSettlement
  } from '@/api/system/shoppingCart'

  export default {
    name: 'ShoppingCart',
    data() {
      return {
        //选中数据
                selectionData: [],
        totalPrice: 0,
        // isIndeterminate: false,
        // checkAll: false,
        // 根路径
        baseURL: process.env.VUE_APP_BASE_API,
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 购物车表格数据
        shoppingCartList: [],
        // 弹出层标题
        title: '',
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          userId: null,
          productName: null,
          num: null,
          price: null,
          status: '1'
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          id: [{
            required: true,
            message: '主键Id不能为空',
            trigger: 'blur'
          }],
          userId: [{
            required: true,
            message: '用户id不能为空',
            trigger: 'blur'
          }],
          tableId: [{
            required: true,
            message: '关联id不能为空',
            trigger: 'blur'
          }],
          num: [{
            required: true,
            message: '数量不能为空',
            trigger: 'blur'
          }],
          price: [{
            required: true,
            message: '价格不能为空',
            trigger: 'blur'
          }]
        },
        activeName: 'shoppingCart',
        id: null
      }
    },
    created() {
      this.getList()

    },
    mounted() {},
    methods: {
        // 计算总价和总数量
            calTotalPrice() {
              let _this = this;
              _this.totalPrice = 0;
              _this.cartTotalCount = 0;
              // 如果有选中项
              if (_this.selectionData.length > 0) {
                _this.selectionData.forEach((item) => {
                  _this.totalPrice += item.num * item.price;
                  _this.cartTotalCount += item.num;
                });
              }
            },
      tabsHandleClick(tab, event) {
        this.getList()
      },
      handleSettlement() {
        let data = {
          ids: this.ids.toString()
        }
        shoppingCartSettlement(data).then(res => {
          this.$modal.msgSuccess('结算成功')
          this.getList()
        })
      },
      /** 查询购物车列表 */
      getList() {
        if (this.$store.state.user.roles != null && this.$store.state.user.roles[0] == 'admin') {
          this.queryParams.status = 0
        }
        this.loading = true
        listShoppingCart(this.queryParams).then(response => {
          this.shoppingCartList = response.rows
          this.total = response.total
          this.loading = false
        })
      },
      // 取消按钮
      cancel() {
        this.open = false
        this.reset()
      },
      // 表单重置
      reset() {
        this.form = {
          id: null,
          userId: null,
          tableId: null,
          num: null,
          price: null
        }
        this.resetForm('form')
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1
        this.getList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      },
      handleCheckAllChange(val) {
        console.log(val)
        this.$refs.shoppingCartList.clearSelection();
        if (val) {
          this.handleSelectionChange(this.shoppingCartList)
        } else {
          this.handleSelectionChange()
        }
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.totalPrice = 0
        for (let i = 0; i < selection.length; i++) {
          this.totalPrice += selection[i].price
        }
        this.ids = selection.map(item => item.id)
        // this.checkAll = selection.length === this.shoppingCartList.length
        // this.isIndeterminate = selection.length > 0 && selection.length < this.shoppingCartList.length
        this.single = selection.length < 1
        this.multiple = !selection.length
        // console.log(ids)
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset()
        this.open = true
        this.title = '添加购物车'
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset()
        const id = row.id || this.ids
        getShoppingCart(id).then(response => {
          this.form = response.data
          this.open = true
          this.title = '修改购物车'
        })
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs['form'].validate(valid => {
          if (valid) {
            if (this.form.id != null) {
              updateShoppingCart(this.form).then(response => {
                this.$modal.msgSuccess('修改成功')
                this.open = false
                this.getList()
              })
            } else {
              addShoppingCart(this.form).then(response => {
                this.$modal.msgSuccess('新增成功')
                this.open = false
                this.getList()
              })
            }
          }
        })
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const ids = row.id || this.ids
        this.$modal.confirm('是否确认删除购物车编号为"' + ids + '"的数据项？').then(function() {
          return delShoppingCart(ids)
        }).then(() => {
          this.getList()
          this.$modal.msgSuccess('删除成功')
        }).catch(() => {})
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('system/shoppingCart/export', {
          ...this.queryParams
        }, `shoppingCart_${new Date().getTime()}.xlsx`)
      }
    }
  }
</script>
<style scoped>
  /* 购物车web端 */
  .cart-web {
    width: 100%;
    height: auto;
    background: #e8e2e0;
  }

.search{
  padding-top: 25px;
}
  /* 购物车头部 */
  .cart-header {
    width: 100%;
    height: 58px;
    padding: 11px 0;
    background-color: #fff;
    display: flex;
    justify-content: center;
  }

  .cart-header .cart-header-contain {
    width: 80%;
    display: flex;
    align-items: center;
  }

  /* 购物车标题 */
  .cart-header .cart-header-contain .cart-title {
    font-size: 18px;
    font-weight: 500;
  }

  .cart-contain {
    width: 100%;
    height: auto;
    display: flex;
    justify-content: center;
    align-items: center;
    padding-top: 24px;
  }

  .cart-table-container {
    width: 95%;
    margin-left: 35px;
    border-radius: 25px;
    padding: 5px 30px;
    background: #FFF;
    min-height: calc(100vh - 130px);
    position: relative;
  }

  .cart-filter-bar {
    width: 100%;
    display: flex;
    justify-content: space-between;
  }

  .cart-filter-bar .switch-cart {
    overflow: hidden;
    color: #000;
    font-size: 18px;
    font-weight: 600;
    height: 72px;
    line-height: 72px;
  }

  .cart-filter-bar .cart-sum {
    font-size: 14px;
    display: flex;
    align-items: center;
  }

  .cart-sum .total-symbol {
    font-weight: 400;
    margin-right: 12px;
    font-size: 24px;
    color: #ff5000;
  }

  /* 激活状态的支付按钮 */
  .cart-sum .pay-btn-active {
    width: 74px;
    height: 42px;
    line-height: 42px;
    color: #fff;
    background: #ff5000;
    border-radius: 21px;
    text-align: center;
    cursor: pointer;
    text-decoration: none;
    font-size: 16px;
  }

  /* 未激活状态的支付按钮 */
  .cart-sum .pay-btn-inactive {
    width: 74px;
    height: 42px;
    line-height: 42px;
    color: #fff;
    background: #a3a3a3;
    border-radius: 21px;
    text-align: center;
    cursor: not-allowed;
    text-decoration: none;
    font-size: 16px;
  }

  .inactive-btn {
    background: #aaa !important;
    color: #fff;
    cursor: not-allowed !important;
  }

  .cart-list-content {
    max-height: 100vh;
    padding-bottom: 72px;
    overflow-y: auto;
  }

  /* 购物车底部栏 */
  .cart-filter-bar-bottom {
    width: 95%;
    display: flex;
    justify-content: space-between;
    position: absolute;
    bottom: 0;
    background: #fff;
    z-index: 999;
  }

  .cart-filter-bar-bottom .bar-bottom-left {
    display: flex;
    justify-content: center;
    align-items: center;
  }

  /* 全选 */
  .cart-filter-bar-bottom .all-check {
    overflow: hidden;
    color: #000;
    height: 72px;
    line-height: 72px;
    font-size: 12px;
  }

  .cart-filter-bar-bottom .all-check .all-check-checkbox {
    margin-right: 10px;
  }

  /* 全部删除 */
  .cart-filter-bar-bottom .all-del {
    margin-left: 20px;
  }

  .cart-filter-bar-bottom .cart-sum {
    font-size: 14px;
    display: flex;
    align-items: center;
  }

  .cart-sum .pay-text {
    margin: 0 12px;
  }

  .cart-sum .total-text {
    margin: 0 0 0 12px;
  }

  .cart-box {
    padding: 10px 0;
  }

  .cart-list-item {
    position: relative;
    border-radius: 8px;
    overflow: hidden;
    border: 0;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin: 20px;
    background-color: #fff;
    padding: 8px 0;
  }

  .cart-list-item .cart-item-info {
    display: flex;
    align-items: center;
    padding: 0 0 0 15px;
  }

  .cart-list-item .cart-item-left {
    width: 100px;
    display: flex;
    flex-direction: row;
    align-items: center;
  }

  .cart-list-item .left-check {
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 8%;
  }

  /* 单个选择按钮 */
  .cart-list-item .left-check .checkbox {
    width: 14px;
    height: 14px;
    border-radius: 50%;
    background: #fff;
    border: 1px solid #b9b9b9;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  /* 单个选择按钮选中 */
  .cart-list-item .left-check .checkbox .selected-check {
    width: 10px;
    height: 10px;
    border-radius: 50%;
    background: red;
  }

  .cart-item-right {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    padding: 10px;
  }

  .cart-item-opera {
    display: flex;
    align-items: center;
  }

  /* 删除按钮 */
  .del-btn {
    position: absolute;
    right: 10px;
    bottom: 10px;
    font-size: 17px;
    color: red;
  }
  .cart-item-right .cart-item-info-content {
    margin-right: 20px;
    width: 70px;
  }

  .cart-item-right .goods-name {
    width: 100%;
    font-size: 15px;
    color: #333333;
    margin-top: 3px;
    text-align: left;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }

  .cart-item-right .numbers {
    width: 100%;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin-top: 10px;
  }

  .cart-item-right .numbers .price {
    font-size: 14px;
    line-height: 25px;
    color: #ff0000;
  }

  .bottom-all {
    height: 50px;
    width: 100%;
    padding: 0 2%;
    align-items: center;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    position: fixed;
    bottom: 0;
    left: 0;
    background-color: #fdfdfd;
    z-index: 11;
    border-top: 1px solid #f8f8f8;
  }

  .bottom-all .left {
    float: left;
    width: 28vw;
    font-size: 14px;
    position: relative;
  }

  .bottom-all .left .text {
    position: absolute;
    left: 35px;
    top: 50%;
    transform: translateY(-52%);
    color: #333333;
  }

  .bottom-all .del-text {
    color: red;
    position: absolute;
    left: 80px;
    top: 47%;
    transform: translateY(-52%);
  }

  .bottom-all .left-check {
    padding-left: 8px;
    flex-shrink: 0;
    height: 50px;
    margin-right: 8px;
    align-items: center;
    position: relative;
    display: flex;
  }

  .bottom-all .rights {
    width: 40vw;
    line-height: 50px;
    font-size: 16px;
    text-align: center;
    display: flex;
    align-items: center;
    justify-content: flex-end;
  }

  .bottom-all .settlement-inactive {
    padding: 0 4px;
    border-radius: 16px;
    width: 18vw;
    height: 35px;
    line-height: 35px;
    color: #ffffff;
    font-size: 17px;
    text-align: center;
    margin-right: 20px;
    cursor: not-allowed;
    background-color: #a3a3a3;
  }

  .bottom-all .settlement-active {
    padding: 0 4px;
    border-radius: 16px;
    width: 18vw;
    height: 35px;
    line-height: 35px;
    color: #ffffff;
    font-size: 17px;
    text-align: center;
    margin-right: 20px;
    background-color: #ff5000;
  }

  /* 购物车为空的样式 */
  .cart-no-data {
    text-align: center;
  }

  .total-pay-count {
    font-size: 24px;
    color: #ff5000;
  }

  @media (max-width: 700px) {
    /* 弹窗 */
    .el-message-box {
      width: 300px !important;
    }

    /* el-input-number样式 */
    .el-input-number {
      width: 100px !important;
      line-height: 28px !important;
    }
    .el-input-number .el-input__inner {
      padding-left: 0 !important;
      padding-right: 0 !important;
    }
    .el-input__inner {
      height: 30px !important;
    }
    .el-input-number__decrease,
    .el-input-number__increase {
      width: 20px !important;
    }
  }

</style>
