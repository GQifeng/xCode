import request from '@/utils/request'

// 查询购物车列表
export function listShoppingCart(query) {
  return request({
    url: '/system/shoppingCart/list',
    method: 'get',
    params: query
  })
}

// 查询购物车所有列表
export function listAllShoppingCart(query) {
  return request({
    url: '/system/shoppingCart/listAll',
    method: 'get',
    params: query
  })
}

// 查询购物车详细
export function getShoppingCart(id) {
  return request({
    url: '/system/shoppingCart/getInfo/' + id,
    method: 'get'
  })
}

// 新增购物车
export function addShoppingCart(data) {
  return request({
    url: '/system/shoppingCart/add',
    method: 'post',
    data: data
  })
}

// 修改购物车
export function updateShoppingCart(data) {
  return request({
    url: '/system/shoppingCart/edit',
    method: 'post',
    data: data
  })
}

// 删除购物车
export function delShoppingCart(id) {
  return request({
    url: '/system/shoppingCart/remove/' + id,
    method: 'get'
  })
}

// 结算
export function shoppingCartSettlement(data) {
  return request({
    url: '/system/shoppingCart/settlement',
    method: 'post',
    data: data
  })
}
