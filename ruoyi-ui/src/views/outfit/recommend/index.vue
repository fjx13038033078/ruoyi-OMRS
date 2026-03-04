<template>
  <div class="app-container">
    <!-- 推荐类型选择 -->
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="综合推荐" name="hybrid">
        <span slot="label"><i class="el-icon-magic-stick"></i> 综合推荐</span>
      </el-tab-pane>
      <el-tab-pane label="天气推荐" name="weather">
        <span slot="label"><i class="el-icon-sunny"></i> 天气推荐</span>
      </el-tab-pane>
      <el-tab-pane label="场景推荐" name="scene">
        <span slot="label"><i class="el-icon-place"></i> 场景推荐</span>
      </el-tab-pane>
      <el-tab-pane label="风格推荐" name="style">
        <span slot="label"><i class="el-icon-s-opportunity"></i> 风格推荐</span>
      </el-tab-pane>
      <el-tab-pane label="季节推荐" name="season">
        <span slot="label"><i class="el-icon-date"></i> 季节推荐</span>
      </el-tab-pane>
      <el-tab-pane label="猜你喜欢" name="collaborative">
        <span slot="label"><i class="el-icon-user"></i> 猜你喜欢</span>
      </el-tab-pane>
    </el-tabs>

    <!-- 天气推荐的城市输入 -->
    <el-row v-if="activeTab === 'weather' || activeTab === 'hybrid'" :gutter="10" class="mb8">
      <el-col :span="6">
        <el-input
          v-model="cityName"
          placeholder="输入城市名称"
          clearable
          @keyup.enter.native="loadRecommendData"
        >
          <el-button slot="append" icon="el-icon-search" @click="loadRecommendData"></el-button>
        </el-input>
      </el-col>
    </el-row>

    <!-- 天气信息 -->
    <el-card v-if="(activeTab === 'weather' || activeTab === 'hybrid') && weatherInfo" class="mb15">
      <div slot="header">
        <span><i class="el-icon-cloudy"></i> 天气信息</span>
      </div>
      <el-descriptions :column="4" border size="small">
        <el-descriptions-item label="城市">{{ weatherInfo.city }}</el-descriptions-item>
        <el-descriptions-item label="天气">{{ weatherInfo.weatherCondition }}</el-descriptions-item>
        <el-descriptions-item label="温度">{{ weatherInfo.temperature }}°C</el-descriptions-item>
        <el-descriptions-item label="体感温度">{{ weatherInfo.feelsLike }}°C</el-descriptions-item>
        <el-descriptions-item label="湿度">{{ weatherInfo.humidity }}%</el-descriptions-item>
        <el-descriptions-item label="风力" :span="3">{{ weatherInfo.windScale }}级</el-descriptions-item>
      </el-descriptions>
      <el-alert v-if="weatherAdvice" :title="weatherAdvice" type="info" :closable="false" class="mt10"></el-alert>
    </el-card>

    <!-- 推荐理由 -->
    <el-alert v-if="recommendReason" :title="recommendReason" type="success" :closable="false" class="mb15">
      <template slot="title">
        <i class="el-icon-magic-stick"></i> {{ recommendReason }}
      </template>
    </el-alert>

    <!-- 推荐结果 -->
    <el-card>
      <div slot="header">
        <span>推荐结果（共{{ clothesList.length }}件）</span>
        <el-button style="float: right; padding: 3px 0" type="text" icon="el-icon-refresh" @click="loadRecommendData">刷新</el-button>
      </div>

      <div v-loading="loading">
        <el-empty v-if="clothesList.length === 0 && !loading" description="暂无推荐数据"></el-empty>
        
        <el-row :gutter="15" v-else>
          <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="4" v-for="item in clothesList" :key="item.id">
            <el-card shadow="hover" :body-style="{ padding: '0' }" class="clothes-card mb15">
              <!-- 图片 -->
              <div class="card-image">
                <image-preview v-if="item.imageUrl" :src="item.imageUrl" :width="'100%'" :height="'180px'" />
                <div v-else class="image-slot">
                  <i class="el-icon-picture-outline"></i>
                </div>
              </div>

              <!-- 信息 -->
              <div class="card-body">
                <div class="clothes-name" :title="item.name">{{ item.name }}</div>
                
                <div class="clothes-tags">
                  <el-tag size="mini" v-if="item.category">{{ getCategoryLabel(item.category) }}</el-tag>
                  <el-tag size="mini" type="success" v-if="item.season">{{ getSeasonLabel(item.season) }}</el-tag>
                </div>

                <div class="clothes-info">
                  <span v-if="item.style">{{ getStyleLabel(item.style) }}</span>
                  <span v-if="item.brand">{{ item.brand }}</span>
                </div>

                <!-- 发布者 -->
                <div class="publisher">
                  <el-avatar :size="20" :src="item.avatar"><i class="el-icon-user-solid"></i></el-avatar>
                  <span>{{ item.nickName || '匿名' }}</span>
                </div>

                <!-- 底部 -->
                <div class="card-footer">
                  <span class="stats">
                    <i class="el-icon-star-on"></i> {{ item.favoriteCount || 0 }}
                  </span>
                  <el-button 
                    v-if="!item.currentUserFavorite" 
                    type="text" 
                    size="mini" 
                    @click="handleFavorite(item)"
                  >收藏</el-button>
                  <span v-else class="favorited"><i class="el-icon-star-on"></i> 已收藏</span>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script>
import { 
  getWeatherRecommend, 
  getSceneRecommend, 
  getStyleRecommend, 
  getSeasonRecommend, 
  getCollaborativeRecommend, 
  getHybridRecommend 
} from '@/api/outfit/recommend'
import { addFavorite } from '@/api/outfit/clothes'

export default {
  name: 'Recommend',
  dicts: ['wardrobe_category', 'wardrobe_style', 'wardrobe_season', 'wardrobe_material'],
  data() {
    return {
      activeTab: 'hybrid',
      cityName: '',
      loading: false,
      clothesList: [],
      weatherInfo: null,
      weatherAdvice: '',
      recommendReason: ''
    }
  },
  mounted() {
    this.loadRecommendData()
  },
  methods: {
    handleTabClick() {
      this.weatherInfo = null
      this.weatherAdvice = ''
      this.loadRecommendData()
    },
    async loadRecommendData() {
      this.loading = true
      this.clothesList = []
      this.recommendReason = ''

      try {
        let res
        switch (this.activeTab) {
          case 'weather':
            res = await getWeatherRecommend(this.cityName)
            break
          case 'scene':
            res = await getSceneRecommend()
            break
          case 'style':
            res = await getStyleRecommend()
            break
          case 'season':
            res = await getSeasonRecommend()
            break
          case 'collaborative':
            res = await getCollaborativeRecommend()
            break
          case 'hybrid':
          default:
            res = await getHybridRecommend(this.cityName)
            break
        }

        if (res.data) {
          const data = res.data
          this.clothesList = data.clothes || []
          this.recommendReason = data.reason || ''
          this.weatherInfo = data.weather || data.weatherInfo || null
          this.weatherAdvice = data.advice || data.weatherAdvice || ''
        }
      } catch (error) {
        console.error('获取推荐数据失败', error)
        this.$message.error('获取推荐数据失败')
      } finally {
        this.loading = false
      }
    },
    getCategoryLabel(value) {
      const dict = this.dict.type.wardrobe_category
      const item = dict?.find(d => d.value === value)
      return item ? item.label : value
    },
    getStyleLabel(value) {
      if (!value) return ''
      const styles = value.split(',')
      const dict = this.dict.type.wardrobe_style
      const labels = styles.map(s => {
        const item = dict?.find(d => d.value === s.trim())
        return item ? item.label : s
      })
      return labels[0] || value
    },
    getSeasonLabel(value) {
      const dict = this.dict.type.wardrobe_season
      const item = dict?.find(d => d.value === value)
      return item ? item.label : value
    },
    async handleFavorite(item) {
      try {
        await addFavorite(item.id)
        item.currentUserFavorite = true
        item.favoriteCount = (item.favoriteCount || 0) + 1
        this.$message.success('收藏成功')
      } catch (error) {
        console.error('收藏失败', error)
      }
    }
  }
}
</script>

<style scoped>
.mb8 {
  margin-bottom: 8px;
}
.mb15 {
  margin-bottom: 15px;
}
.mt10 {
  margin-top: 10px;
}

.clothes-card .card-image {
  height: 180px;
  overflow: hidden;
}

.clothes-card .card-image .image-slot {
  width: 100%;
  height: 180px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  color: #909399;
  font-size: 30px;
}

.clothes-card .card-body {
  padding: 12px;
}

.clothes-card .clothes-name {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.clothes-card .clothes-tags {
  margin-bottom: 8px;
}

.clothes-card .clothes-tags .el-tag {
  margin-right: 5px;
}

.clothes-card .clothes-info {
  font-size: 12px;
  color: #909399;
  margin-bottom: 8px;
}

.clothes-card .clothes-info span {
  margin-right: 10px;
}

.clothes-card .publisher {
  display: flex;
  align-items: center;
  font-size: 12px;
  color: #606266;
  padding: 8px 0;
  border-top: 1px solid #EBEEF5;
}

.clothes-card .publisher .el-avatar {
  margin-right: 6px;
}

.clothes-card .card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 8px;
  border-top: 1px solid #EBEEF5;
}

.clothes-card .card-footer .stats {
  font-size: 12px;
  color: #909399;
}

.clothes-card .card-footer .favorited {
  font-size: 12px;
  color: #E6A23C;
}
</style>
