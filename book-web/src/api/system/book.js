import request from '@/utils/request'

// 查询书籍信息列表
export function listBook(query) {
  return request({
    url: '/system/book/list',
    method: 'get',
    params: query
  })
}

// 查询书籍信息所有列表
export function listAllBook(query) {
  return request({
    url: '/system/book/listAll',
    method: 'get',
    params: query
  })
}

// 查询书籍信息详细
export function getBook(id) {
  return request({
    url: '/system/book/getInfo/' + id,
    method: 'get'
  })
}

// 新增书籍信息
export function addBook(data) {
  return request({
    url: '/system/book/add',
    method: 'post',
    data: data
  })
}

// 修改书籍信息
export function updateBook(data) {
  return request({
    url: '/system/book/edit',
    method: 'post',
    data: data
  })
}

// 删除书籍信息
export function delBook(id) {
  return request({
    url: '/system/book/remove/' + id,
    method: 'get'
  })
}
