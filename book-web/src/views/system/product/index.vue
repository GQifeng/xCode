<!-- 商品信息 -->
<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="商品类型" prop="typeId">
        <el-select v-model="queryParams.typeId" placeholder="请选择商品类型" filterable clearable>
          <el-option v-for="(item,index) in typeList" :label="item.typeName" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="商品名称" prop="productName">
        <el-input v-model="queryParams.productName" placeholder="请输入商品名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="库存数量" prop="productNum">
        <el-input v-model="queryParams.productNum" placeholder="请输入库存数量" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
          v-hasPermi="['system:product:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['system:product:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['system:product:remove']">删除</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['system:product:export']">导出</el-button>
      </el-col> -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="productList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="商品ID" align="center" prop="id" />
      <el-table-column label="商品类型" align="center" prop="typeName" />
      <el-table-column label="商品名称" align="center" prop="productName" />
      <el-table-column label="价格" align="center" prop="price" />
      <el-table-column label="商品图片" align="center" prop="productImg" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.productImg" :width="50" :height="50" />
        </template>
      </el-table-column>
      <el-table-column label="商品详情" align="center" prop="productDetails" />
      <el-table-column label="库存数量" align="center" prop="productNum" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['system:product:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['system:product:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改商品信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="650px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" :inline="true" label-width="80px">
        <el-form-item label="商品类型" prop="typeId">
          <el-select v-model="form.typeId" placeholder="请选择商品类型" filterable >
            <el-option v-for="(item,index) in typeList" :label="item.typeName" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商品名称" prop="productName">
          <el-input v-model="form.productName" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input v-model="form.price" placeholder="请输入价格" />
        </el-form-item>
        <el-form-item label="库存数量" prop="productNum">
          <el-input v-model="form.productNum" disabled placeholder="请输入库存数量" />
        </el-form-item>
        <el-form-item label="商品图片" prop="productImg">
          <image-upload v-model="form.productImg" :limit="1" />
        </el-form-item>
        <el-form-item label="商品详情" prop="productDetails">
          <editor v-model="form.productDetails" :min-height="192" />
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
    listProduct,
    getProduct,
    delProduct,
    addProduct,
    updateProduct
  } from "@/api/system/product";

  import {
    listType,
  } from "@/api/system/type";

  export default {
    name: "Product",
    data() {
      return {
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
        // 商品信息表格数据
        productList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          typeId: null,
          productName: null,
          productImg: null,
          productDetails: null,
          productNum: null,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          id: [{
            required: true,
            message: "$comment不能为空",
            trigger: "blur"
          }],
          typeId: [{
            required: true,
            message: "商品类型不能为空",
            trigger: "change"
          }],
          productName: [{
            required: true,
            message: "商品名称不能为空",
            trigger: "blur"
          }],
          productImg: [{
            required: true,
            message: "商品图片不能为空",
            trigger: "blur"
          }],
          productDetails: [{
            required: true,
            message: "商品详情不能为空",
            trigger: "blur"
          }],
          productNum: [{
            required: true,
            message: "库存数量不能为空",
            trigger: "blur"
          }],
        },
        typeList:[]
      };
    },
    created() {
      this.getList();
      this.getTypeList();
    },
    methods: {
      getTypeList(){
        listType().then(res=>{
          this.typeList = res.rows;
        })
      },
      /** 查询商品信息列表 */
      getList() {
        this.loading = true;
        listProduct(this.queryParams).then(response => {
          this.productList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          id: null,
          typeId: null,
          productName: null,
          productImg: null,
          productDetails: null,
          productNum: 0,
          price:0
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加商品信息";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getProduct(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改商品信息";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != null) {
              updateProduct(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addProduct(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const ids = row.id || this.ids;
        this.$modal.confirm('是否确认删除商品信息编号为"' + ids + '"的数据项？').then(function() {
          return delProduct(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('system/product/export', {
          ...this.queryParams
        }, `product_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>
