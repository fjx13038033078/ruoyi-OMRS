import request from '@/utils/request'

// 查询穿搭列表
// scope: all-全部公开穿搭, mine-我的穿搭, favorite-我的收藏
export function listOutfit(query) {
  return request({
    url: '/outfit/outfit/list',
    method: 'get',
    params: query
  })
}

// 查询穿搭详细
export function getOutfit(id) {
  return request({
    url: '/outfit/outfit/' + id,
    method: 'get'
  })
}

// 新增穿搭
export function addOutfit(data) {
  return request({
    url: '/outfit/outfit',
    method: 'post',
    data: data
  })
}

// 修改穿搭
export function updateOutfit(data) {
  return request({
    url: '/outfit/outfit',
    method: 'put',
    data: data
  })
}

// 删除穿搭
export function delOutfit(id) {
  return request({
    url: '/outfit/outfit/' + id,
    method: 'delete'
  })
}

// 更新穿着记录
export function wearOutfit(id) {
  return request({
    url: '/outfit/outfit/wear/' + id,
    method: 'put'
  })
}

// 切换收藏状态
export function toggleFavorite(outfitId) {
  return request({
    url: '/outfit/outfit/favorite/' + outfitId,
    method: 'put'
  })
}
