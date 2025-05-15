import request from '@/utils/request'

// 查询商品信息列表
export function listProduct(query) {
  return request({
    url: '/system/product/list',
    method: 'get',
    params: query
  })
}

// 查询商品信息所有列表
export function listAllProduct(query) {
  return request({
    url: '/system/product/listAll',
    method: 'get',
    params: query
  })
}

// 查询商品信息详细
export function getProduct(id) {
  return request({
    url: '/system/product/getInfo/' + id,
    method: 'get'
  })
}

// 新增商品信息
export function addProduct(data) {
  return request({
    url: '/system/product/add',
    method: 'post',
    data: data
  })
}

// 修改商品信息
export function updateProduct(data) {
  return request({
    url: '/system/product/edit',
    method: 'post',
    data: data
  })
}

// 删除商品信息
export function delProduct(id) {
  return request({
    url: '/system/product/remove/' + id,
    method: 'get'
  })
}
