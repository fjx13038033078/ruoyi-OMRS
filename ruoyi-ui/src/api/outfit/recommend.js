import request from '@/utils/request'

/**
 * 基于天气的推荐
 * @param {string} cityName - 城市名称（可选）
 */
export function getWeatherRecommend(cityName) {
  return request({
    url: '/outfit/recommend/weather',
    method: 'get',
    params: { cityName }
  })
}

/**
 * 基于场景的推荐
 * @param {string} sceneCode - 场景编码（可选）
 */
export function getSceneRecommend(sceneCode) {
  return request({
    url: '/outfit/recommend/scene',
    method: 'get',
    params: { sceneCode }
  })
}

/**
 * 基于风格偏好的推荐
 * @param {string} styleCode - 风格编码（可选）
 */
export function getStyleRecommend(styleCode) {
  return request({
    url: '/outfit/recommend/style',
    method: 'get',
    params: { styleCode }
  })
}

/**
 * 基于季节规律的推荐
 * @param {string} season - 季节（可选）
 */
export function getSeasonRecommend(season) {
  return request({
    url: '/outfit/recommend/season',
    method: 'get',
    params: { season }
  })
}

/**
 * 协同过滤推荐
 */
export function getCollaborativeRecommend() {
  return request({
    url: '/outfit/recommend/collaborative',
    method: 'get'
  })
}

/**
 * 混合推荐
 * @param {string} cityName - 城市名称（可选）
 */
export function getHybridRecommend(cityName) {
  return request({
    url: '/outfit/recommend/hybrid',
    method: 'get',
    params: { cityName }
  })
}
