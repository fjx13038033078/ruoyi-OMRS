import request from '@/utils/request'

// 查询衣物列表
// scope: all-全部公开衣物, mine-我的衣物, favorite-我的收藏
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

// 切换发布者自己的收藏标记（只能操作自己的衣物）
export function toggleSelfFavorite(id, isFavorite) {
  return request({
    url: '/outfit/clothes/selfFavorite/' + id + '/' + isFavorite,
    method: 'put'
  })
}

// 切换收藏状态（收藏/取消收藏任何人公开的衣物）
export function toggleFavorite(clothesId) {
  return request({
    url: '/outfit/clothes/favorite/' + clothesId,
    method: 'put'
  })
}

// 添加收藏
export function addFavorite(clothesId) {
  return request({
    url: '/outfit/clothes/favorite/' + clothesId,
    method: 'post'
  })
}

// 取消收藏
export function removeFavorite(clothesId) {
  return request({
    url: '/outfit/clothes/favorite/' + clothesId,
    method: 'delete'
  })
}

// 获取衣柜统计信息
export function getStatistics() {
  return request({
    url: '/outfit/clothes/statistics',
    method: 'get'
  })
}
