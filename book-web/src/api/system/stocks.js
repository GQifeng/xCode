import request from '@/utils/request'

// 查询出入库记录列表
export function listStock(query) {
  return request({
    url: '/system/stock/list',
    method: 'get',
    params: query
  })
}

// 查询出入库记录所有列表
export function listAllStock(query) {
  return request({
    url: '/system/stock/listAll',
    method: 'get',
    params: query
  })
}

// 查询出入库记录详细
export function getStock(id) {
  return request({
    url: '/system/stock/getInfo/' + id,
    method: 'get'
  })
}

// 新增出入库记录
export function addStock(data) {
  return request({
    url: '/system/stock/add',
    method: 'post',
    data: data
  })
}

// 修改出入库记录
export function updateStock(data) {
  return request({
    url: '/system/stock/edit',
    method: 'post',
    data: data
  })
}

// 删除出入库记录
export function delStock(id) {
  return request({
    url: '/system/stock/remove/' + id,
    method: 'get'
  })
}
