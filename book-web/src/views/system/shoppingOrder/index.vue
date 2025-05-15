<!-- 订单 -->
<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="商品名称" prop="productName">
        <el-input v-model="queryParams.productName" placeholder="请输入商品名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>


    <el-table v-loading="loading" :data="shoppingCartList" :row-class-name="tableRowClassName" ref="shoppingCartList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
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
          <!--          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"-->
          <!--                     v-hasPermi="['system:shoppingCart:edit']"-->
          <!--          >修改-->
          <!--          </el-button>-->
          <el-button type="danger" round @click="handleDelete(scope.row)"
                     v-hasPermi="['system:shoppingCart:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改购物车对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="商品名称"  prop="productName">
          <el-input v-model="form.productName" disabled placeholder="请输入商品名称"/>
        </el-form-item>
        <el-form-item label="商品图片" prop="productImg">
          <image-preview :src="form.productImg" :width="150" :height="150"/>
          <!-- <el-input v-model="form.tableId" placeholder="请输入关联id" /> -->
        </el-form-item>
        <el-form-item label="数量" prop="num">
          <el-input v-model="form.num"  placeholder="请输入数量"/>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input v-model="form.price"  disabled placeholder="请输入价格"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
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
        status: '0'
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
      id:null
    }
  },
  created() {
    this.getList()

  },
  methods: {
    tableRowClassName({row, rowIndex}) {
           if (rowIndex%2 === 1) {
             return 'warning-row';
           } else if (rowIndex%2 === 0) {
             return 'success-row';
           }
           return '';
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
      if(this.$store.state.user.roles!=null&&this.$store.state.user.roles[0]=='admin'){
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
      this.single = selection.length !== 1
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
      this.$modal.confirm('是否确认删除订单编号为"' + ids + '"的订单？').then(function() {
        return delShoppingCart(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {
      })
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
