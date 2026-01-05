import request from '@/utils/request'

// 查询衣物列表
export function listClothes(query) {
  return request({
    url: '/outfit/clothes/list',
    method: 'get',
    params: query
  })
}

// 查询衣物详细
export function getClothes(id) {
  return request({
    url: '/outfit/clothes/' + id,
    method: 'get'
  })
}

// 新增衣物
export function addClothes(data) {
  return request({
    url: '/outfit/clothes',
    method: 'post',
    data: data
  })
}

// 修改衣物
export function updateClothes(data) {
  return request({
    url: '/outfit/clothes',
    method: 'put',
    data: data
  })
}

// 删除衣物
export function delClothes(id) {
  return request({
    url: '/outfit/clothes/' + id,
    method: 'delete'
  })
}

// 更新穿着记录
export function wearClothes(id) {
  return request({
    url: '/outfit/clothes/wear/' + id,
    method: 'put'
  })
}

// 切换收藏状态
export function toggleFavorite(id, isFavorite) {
  return request({
    url: '/outfit/clothes/favorite/' + id + '/' + isFavorite,
    method: 'put'
  })
}

// 获取衣柜统计信息
export function getStatistics() {
  return request({
    url: '/outfit/clothes/statistics',
    method: 'get'
  })
}

