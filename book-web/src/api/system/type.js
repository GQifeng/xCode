import request from '@/utils/request'

// 查询类型管理列表
export function listType(query) {
  return request({
    url: '/system/type/list',
    method: 'get',
    params: query
  })
}

export function tjList(query) {
  return request({
    url: '/system/type/tjList',
    method: 'get',
    params: query
  })
}

// 查询类型管理所有列表
export function listAllType(query) {
  return request({
    url: '/system/type/listAll',
    method: 'get',
    params: query
  })
}

// 查询类型管理详细
export function getType(id) {
  return request({
    url: '/system/type/getInfo/' + id,
    method: 'get'
  })
}

// 新增类型管理
export function addType(data) {
  return request({
    url: '/system/type/add',
    method: 'post',
    data: data
  })
}

// 修改类型管理
export function updateType(data) {
  return request({
    url: '/system/type/edit',
    method: 'post',
    data: data
  })
}

// 删除类型管理
export function delType(id) {
  return request({
    url: '/system/type/remove/' + id,
    method: 'get'
  })
}
