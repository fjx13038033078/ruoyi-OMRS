import request from '@/utils/request'

// 获取首页统计数据
export function getHomeStatistics() {
  return request({
    url: '/outfit/statistics/home',
    method: 'get'
  })
}

// 获取衣物分类统计
export function getClothesCategoryStats() {
  return request({
    url: '/outfit/statistics/clothes/category',
    method: 'get'
  })
}

// 获取衣物材质统计
export function getClothesMaterialStats() {
  return request({
    url: '/outfit/statistics/clothes/material',
    method: 'get'
  })
}

// 获取热门穿搭排行
export function getPopularOutfits() {
  return request({
    url: '/outfit/statistics/outfit/popular',
    method: 'get'
  })
}

// 获取季节分布统计
export function getClothesSeasonStats() {
  return request({
    url: '/outfit/statistics/clothes/season',
    method: 'get'
  })
}

// 获取颜色分布统计
export function getClothesColorStats() {
  return request({
    url: '/outfit/statistics/clothes/color',
    method: 'get'
  })
}

// 获取最近穿搭趋势
export function getOutfitTrend() {
  return request({
    url: '/outfit/statistics/outfit/trend',
    method: 'get'
  })
}
