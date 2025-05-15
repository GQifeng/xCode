<!-- 我的推荐 -->
<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px">
      <el-form-item label="商品类型" prop="typeId">
        <el-select v-model="queryParams.typeId" placeholder="请选择商品类型" filterable clearable>
          <el-option v-for="(item,index) in typeList" :label="item.typeName" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="商品名称" prop="productName">
        <el-input v-model="queryParams.productName" placeholder="请输入商品名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="30">
      <el-col :span="25" v-for="(product,index) in productList">

         <el-tooltip :content="productList[index].productDetails" placement="top" effect="light">
        <el-card class="box-card">
          <img :src="baseURL+productList[index].productImg" class="image">
          <div style="padding-top: 10px;padding-left: 10px">
            <div style="text-align: left">{{ productList[index].productName }}</div>
            <div style="color: red;text-align: left;padding-top: 2%">￥{{ productList[index ].price }}</div>
            <el-divider style="margin-top: 10px"></el-divider>
            <div class="bottom clearfix">
              <time class="time">库存：{{ productList[index ].productNum }} |
                销量：{{ productList[index ].salesNum }}
              </time>
              <div style="padding-top: 2%">
               <el-button icon="el-icon-shopping-cart-2" circle type="danger"  class="button"
                 @click="submit(productList[index])">
               </el-button>
              </div>
            </div>
          </div>
        </el-card>
        </el-tooltip>
      </el-col>
    </el-row>

  </div>
</template>

<script>
  import {
    listProduct,
    getProduct,
    delProduct,
    addProduct,
    updateProduct
  } from '@/api/system/product'

  import {
    listType,
    tjList
  } from '@/api/system/type'

  import {
    addShoppingCart
  } from '@/api/system/shoppingCart'

  export default {
    name: 'Product',
    data() {
      return {
        baseURL: process.env.VUE_APP_BASE_API,
        // 总条数
        total: 0,
        // 商品信息表格数据
        productList: [],
        // 查询参数
        queryParams: {
          typeId: null,
          productName: null,
          productImg: null,
          productDetails: null,
          productNum: null
        },
        typeList: [],
        activeName: 'tj'
      }
    },
    created() {
      this.getList()
      this.getTypeList()
    },
    methods: {
      handleTabClick() {
        this.handleQuery()
      },
      submit(row) {
        this.$prompt('请输入购买数量', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /^\d{1,}$/,
          inputErrorMessage: '请输入数量'
        }).then(({
          value
        }) => {
          let data = {
            tableId: row.id,
            num: value
          }
          addShoppingCart(data).then(res => {
            this.$modal.msgSuccess('添加成功')
          })
        }).catch(() => {

        })
      },
      getTypeList() {
        listType().then(res => {
          this.typeList = res.rows
        })
      },
      /** 查询商品信息列表 */
      getList() {
        this.loading = true
        if (this.activeName == 'index') {
          listProduct(this.queryParams).then(response => {
            this.productList = response.rows
          })
        } else {
          tjList(this.queryParams).then(res => {
            this.productList = res.rows
          })
        }
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.getList()
      }
    }
  }
</script>
<style>
  .el-col-25 {
    width: 20%;
  },
  .el-divider--horizontal {
    display: block;
    height: 1px;
    width: 100%;
    margin: 7px 0;
  }

  .user-avatar {
    cursor: pointer;
    width: 100px;
    height: 100px;
    border-radius: 10px;
  }

  .time {
    font-size: 13px;
    color: #999;
  }

  .bottom {
    margin-top: 13px;
    line-height: 12px;
  }

  .button {
    padding: 0;
    float: right;
  }

  .image {
    width: 100%;
    height: 200px;
    display: block;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }

  .clearfix:after {
    clear: both
  }

  .box-card {
    width: 250px;
     margin-top: 10px;
  }
</style>
