<template>
  <div class="dashboard-container">
    <!-- 顶部标题区域 -->
    <div class="header-section">
      <div class="title-wrapper">
        <div class="title-decoration">
          <span class="deco-line left"></span>
          <span class="deco-diamond"></span>
          <span class="deco-line right"></span>
        </div>
        <div class="main-title">
          <span class="title-text">智能衣柜推荐管理系统</span>
        </div>
        <div class="sub-title">SMART WARDROBE RECOMMENDATION SYSTEM</div>
        <div class="title-decoration bottom">
          <span class="deco-dot"></span>
          <span class="deco-dot"></span>
          <span class="deco-dot"></span>
        </div>
      </div>
      <!-- 统计卡片 -->
      <div class="stats-cards">
        <div class="stat-card" v-for="(item, index) in statsCards" :key="index">
          <div class="stat-icon">
            <i :class="item.icon"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ item.value }}</div>
            <div class="stat-label">{{ item.label }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="charts-section">
      <el-row :gutter="20">
        <!-- 衣物分类占比 - 玫瑰图 -->
        <el-col :span="12">
          <el-card class="chart-card" shadow="hover">
            <div slot="header" class="chart-header">
              <span class="chart-title">
                <i class="el-icon-pie-chart"></i> 衣物分类占比
              </span>
            </div>
            <div ref="categoryChart" class="chart-container"></div>
          </el-card>
        </el-col>

        <!-- 材质分布 - 横向柱状图 -->
        <el-col :span="12">
          <el-card class="chart-card" shadow="hover">
            <div slot="header" class="chart-header">
              <span class="chart-title">
                <i class="el-icon-s-data"></i> 材质分布统计
              </span>
            </div>
            <div ref="materialChart" class="chart-container"></div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <!-- 热门穿搭排行 - 柱状图 -->
        <el-col :span="12">
          <el-card class="chart-card" shadow="hover">
            <div slot="header" class="chart-header">
              <span class="chart-title">
                <i class="el-icon-star-on"></i> 热门穿搭排行
              </span>
            </div>
            <div ref="popularChart" class="chart-container"></div>
          </el-card>
        </el-col>

        <!-- 季节与颜色分布 - 雷达图 -->
        <el-col :span="12">
          <el-card class="chart-card" shadow="hover">
            <div slot="header" class="chart-header">
              <span class="chart-title">
                <i class="el-icon-sunny"></i> 季节分布分析
              </span>
            </div>
            <div ref="seasonChart" class="chart-container"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getHomeStatistics, getClothesCategoryStats, getClothesMaterialStats, getPopularOutfits, getClothesSeasonStats } from '@/api/outfit/statistics'

export default {
  name: "Dashboard",
  data() {
    return {
      statsCards: [
        { label: '衣物总数', value: 0, icon: 'el-icon-s-goods' },
        { label: '穿搭方案', value: 0, icon: 'el-icon-s-cooperation' },
        { label: '活跃用户', value: 0, icon: 'el-icon-user-solid' },
        { label: '收藏总数', value: 0, icon: 'el-icon-star-on' }
      ],
      charts: {
        category: null,
        material: null,
        popular: null,
        season: null
      }
    }
  },
  mounted() {
    this.initData()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    Object.values(this.charts).forEach(chart => {
      if (chart) chart.dispose()
    })
  },
  methods: {
    async initData() {
      await this.loadStatistics()
      this.$nextTick(() => {
        this.initCategoryChart()
        this.initMaterialChart()
        this.initPopularChart()
        this.initSeasonChart()
      })
    },
    async loadStatistics() {
      try {
        const res = await getHomeStatistics()
        if (res.data) {
          this.statsCards[0].value = res.data.totalClothes || 0
          this.statsCards[1].value = res.data.totalOutfits || 0
          this.statsCards[2].value = res.data.totalUsers || 0
          this.statsCards[3].value = res.data.totalFavorites || 0
        }
      } catch (e) {
        console.error('加载统计数据失败', e)
      }
    },
    async initCategoryChart() {
      try {
        const res = await getClothesCategoryStats()
        const data = res.data || []
        
        this.charts.category = echarts.init(this.$refs.categoryChart)
        const option = {
          tooltip: {
            trigger: 'item',
            formatter: '{b}: {c} ({d}%)'
          },
          legend: {
            orient: 'vertical',
            right: '5%',
            top: 'center',
            textStyle: { color: '#666' }
          },
          series: [{
            name: '衣物分类',
            type: 'pie',
            radius: ['30%', '70%'],
            center: ['40%', '50%'],
            roseType: 'area',
            itemStyle: {
              borderRadius: 8,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: true,
              formatter: '{b}\n{d}%'
            },
            data: data.length > 0 ? data : [
              { name: '上衣', value: 30 },
              { name: '裤子', value: 25 },
              { name: '裙子', value: 15 },
              { name: '外套', value: 20 },
              { name: '鞋子', value: 10 }
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }],
          color: ['#3498db', '#e74c3c', '#2ecc71', '#f39c12', '#9b59b6', '#1abc9c', '#34495e', '#e67e22']
        }
        this.charts.category.setOption(option)
      } catch (e) {
        console.error('加载分类统计失败', e)
      }
    },
    async initMaterialChart() {
      try {
        const res = await getClothesMaterialStats()
        const data = res.data || []
        
        this.charts.material = echarts.init(this.$refs.materialChart)
        const names = data.map(item => item.name || '未知')
        const values = data.map(item => item.value || 0)
        
        const option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: { type: 'shadow' }
          },
          grid: {
            left: '3%',
            right: '10%',
            bottom: '3%',
            top: '10%',
            containLabel: true
          },
          xAxis: {
            type: 'value',
            axisLabel: { color: '#666' }
          },
          yAxis: {
            type: 'category',
            data: names.length > 0 ? names : ['棉', '涤纶', '羊毛', '丝绸', '麻', '混纺'],
            axisLabel: { color: '#666' },
            axisTick: { show: false }
          },
          series: [{
            name: '数量',
            type: 'bar',
            data: values.length > 0 ? values : [45, 32, 28, 18, 15, 12],
            barWidth: '60%',
            itemStyle: {
              borderRadius: [0, 4, 4, 0],
              color: '#3498db'
            },
            emphasis: {
              itemStyle: {
                color: '#2980b9'
              }
            },
            label: {
              show: true,
              position: 'right',
              color: '#666'
            }
          }]
        }
        this.charts.material.setOption(option)
      } catch (e) {
        console.error('加载材质统计失败', e)
      }
    },
    async initPopularChart() {
      try {
        const res = await getPopularOutfits()
        const data = res.data || []
        
        this.charts.popular = echarts.init(this.$refs.popularChart)
        const names = data.slice(0, 8).map(item => item.name ? (item.name.length > 6 ? item.name.substring(0, 6) + '...' : item.name) : '未命名')
        const favorites = data.slice(0, 8).map(item => item.favoriteCount || 0)
        const wears = data.slice(0, 8).map(item => item.wearCount || 0)
        
        const option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: { type: 'shadow' }
          },
          legend: {
            data: ['收藏数', '穿着次数'],
            top: '5%'
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            top: '18%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: names.length > 0 ? names : ['休闲通勤', '约会穿搭', '运动风', '商务正装', '度假风'],
            axisLabel: { 
              color: '#666',
              rotate: 30,
              interval: 0
            }
          },
          yAxis: {
            type: 'value',
            axisLabel: { color: '#666' }
          },
          series: [
            {
              name: '收藏数',
              type: 'bar',
              data: favorites.length > 0 ? favorites : [120, 98, 85, 72, 60],
              itemStyle: {
                borderRadius: [4, 4, 0, 0],
                color: '#e74c3c'
              }
            },
            {
              name: '穿着次数',
              type: 'bar',
              data: wears.length > 0 ? wears : [85, 76, 95, 45, 38],
              itemStyle: {
                borderRadius: [4, 4, 0, 0],
                color: '#2ecc71'
              }
            }
          ]
        }
        this.charts.popular.setOption(option)
      } catch (e) {
        console.error('加载热门穿搭失败', e)
      }
    },
    async initSeasonChart() {
      try {
        const res = await getClothesSeasonStats()
        const data = res.data || []
        
        this.charts.season = echarts.init(this.$refs.seasonChart)
        
        // 处理数据
        const seasonMap = { '春季': 0, '夏季': 0, '秋季': 0, '冬季': 0, '四季通用': 0 }
        data.forEach(item => {
          if (seasonMap.hasOwnProperty(item.name)) {
            seasonMap[item.name] = item.value
          }
        })
        
        const option = {
          tooltip: {
            trigger: 'item'
          },
          legend: {
            data: ['季节分布'],
            bottom: '5%'
          },
          radar: {
            indicator: [
              { name: '春季', max: Math.max(...Object.values(seasonMap), 50) },
              { name: '夏季', max: Math.max(...Object.values(seasonMap), 50) },
              { name: '秋季', max: Math.max(...Object.values(seasonMap), 50) },
              { name: '冬季', max: Math.max(...Object.values(seasonMap), 50) },
              { name: '四季通用', max: Math.max(...Object.values(seasonMap), 50) }
            ],
            center: ['50%', '50%'],
            radius: '65%',
            axisName: {
              color: '#666',
              fontSize: 12
            },
            splitArea: {
              areaStyle: {
                color: ['rgba(52, 73, 94, 0.05)', 'rgba(52, 73, 94, 0.1)', 
                        'rgba(52, 73, 94, 0.15)', 'rgba(52, 73, 94, 0.2)', 
                        'rgba(52, 73, 94, 0.25)']
              }
            }
          },
          series: [{
            name: '季节分布',
            type: 'radar',
            data: [{
              value: [seasonMap['春季'] || 20, seasonMap['夏季'] || 35, 
                      seasonMap['秋季'] || 25, seasonMap['冬季'] || 18, 
                      seasonMap['四季通用'] || 15],
              name: '季节分布',
              areaStyle: {
                color: 'rgba(231, 76, 60, 0.3)'
              },
              lineStyle: {
                color: '#e74c3c',
                width: 2
              },
              itemStyle: {
                color: '#e74c3c'
              }
            }]
          }]
        }
        this.charts.season.setOption(option)
      } catch (e) {
        console.error('加载季节统计失败', e)
      }
    },
    handleResize() {
      Object.values(this.charts).forEach(chart => {
        if (chart) chart.resize()
      })
    }
  }
}
</script>

<style scoped lang="scss">
.dashboard-container {
  padding: 20px;
  background: #ecf0f1;
  min-height: calc(100vh - 84px);
}

.header-section {
  margin-bottom: 20px;
}

.title-wrapper {
  text-align: center;
  padding: 40px 0 35px;
  background: #2c3e50;
  border-radius: 4px;
  margin-bottom: 20px;
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: #e74c3c;
  }
  
  .title-decoration {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 15px;
    
    .deco-line {
      width: 60px;
      height: 1px;
      background: rgba(255, 255, 255, 0.3);
      
      &.left { margin-right: 15px; }
      &.right { margin-left: 15px; }
    }
    
    .deco-diamond {
      width: 8px;
      height: 8px;
      background: #e74c3c;
      transform: rotate(45deg);
    }
    
    &.bottom {
      margin-top: 20px;
      margin-bottom: 0;
      gap: 8px;
      
      .deco-dot {
        width: 6px;
        height: 6px;
        background: rgba(255, 255, 255, 0.4);
        border-radius: 50%;
      }
    }
  }
  
  .main-title {
    .title-text {
      font-size: 32px;
      font-weight: 300;
      color: #fff;
      letter-spacing: 8px;
      font-family: 'Microsoft YaHei', sans-serif;
    }
  }
  
  .sub-title {
    margin-top: 12px;
    font-size: 12px;
    color: rgba(255, 255, 255, 0.5);
    letter-spacing: 4px;
    font-weight: 300;
  }
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  
  .stat-card {
    padding: 25px;
    border-radius: 4px;
    color: #fff;
    display: flex;
    align-items: center;
    gap: 20px;
    transition: all 0.3s ease;
    border-left: 4px solid transparent;
    
    &:nth-child(1) {
      background: #34495e;
      border-left-color: #3498db;
    }
    &:nth-child(2) {
      background: #34495e;
      border-left-color: #e74c3c;
    }
    &:nth-child(3) {
      background: #34495e;
      border-left-color: #2ecc71;
    }
    &:nth-child(4) {
      background: #34495e;
      border-left-color: #f39c12;
    }
    
    &:hover {
      transform: translateX(5px);
    }
    
    .stat-icon {
      width: 50px;
      height: 50px;
      background: rgba(255, 255, 255, 0.1);
      border-radius: 4px;
      display: flex;
      align-items: center;
      justify-content: center;
      
      i {
        font-size: 24px;
        opacity: 0.9;
      }
    }
    
    .stat-info {
      .stat-value {
        font-size: 28px;
        font-weight: 600;
      }
      
      .stat-label {
        font-size: 13px;
        opacity: 0.7;
        margin-top: 4px;
        letter-spacing: 1px;
      }
    }
  }
}

.charts-section {
  .chart-card {
    border-radius: 4px;
    overflow: hidden;
    border: none;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    
    ::v-deep .el-card__header {
      background: #fff;
      border-bottom: 1px solid #eee;
      padding: 15px 20px;
    }
    
    .chart-header {
      .chart-title {
        font-size: 15px;
        font-weight: 500;
        color: #2c3e50;
        
        i {
          margin-right: 8px;
          color: #e74c3c;
        }
      }
    }
    
    .chart-container {
      height: 320px;
      padding: 10px;
    }
  }
}

@media (max-width: 1200px) {
  .stats-cards {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .stats-cards {
    grid-template-columns: 1fr;
  }
  
  .title-wrapper .main-title .title-text {
    font-size: 24px;
  }
}
</style>
