<template>
  <div class="app-container">
    <!-- 查询范围Tab -->
    <el-tabs v-model="queryParams.scope" @tab-click="handleScopeChange" class="scope-tabs">
      <el-tab-pane label="全部穿搭" name="all">
        <template slot="label">
          <i class="el-icon-s-home"></i> 全部穿搭
        </template>
      </el-tab-pane>
      <el-tab-pane label="我的穿搭" name="mine">
        <template slot="label">
          <i class="el-icon-user"></i> 我的穿搭
        </template>
      </el-tab-pane>
      <el-tab-pane label="我的收藏" name="favorite">
        <template slot="label">
          <i class="el-icon-star-on"></i> 我的收藏
        </template>
      </el-tab-pane>
    </el-tabs>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="穿搭名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入穿搭名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="风格" prop="style">
        <el-select v-model="queryParams.style" placeholder="请选择风格" clearable>
          <el-option
            v-for="dict in dict.type.wardrobe_style"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="场景" prop="scene">
        <el-select v-model="queryParams.scene" placeholder="请选择场景" clearable>
          <el-option
            v-for="dict in dict.type.wardrobe_scene"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="季节" prop="season">
        <el-select v-model="queryParams.season" placeholder="请选择季节" clearable>
          <el-option
            v-for="dict in dict.type.wardrobe_season"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['outfit:outfit:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5" v-if="queryParams.scope === 'mine'">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['outfit:outfit:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          :type="viewMode === 'card' ? 'primary' : 'info'"
          plain
          icon="el-icon-menu"
          size="mini"
          @click="viewMode = 'card'"
        >卡片</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          :type="viewMode === 'table' ? 'primary' : 'info'"
          plain
          icon="el-icon-s-grid"
          size="mini"
          @click="viewMode = 'table'"
        >列表</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 卡片视图 -->
    <div v-if="viewMode === 'card'" class="outfit-card-container">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="4" v-for="item in outfitList" :key="item.id">
          <el-card class="outfit-card" shadow="hover" :body-style="{ padding: '0' }">
            <div class="card-image" @click="handleView(item)">
              <image-preview v-if="item.coverImage" :src="item.coverImage" :width="'100%'" :height="'200px'" />
              <div v-else class="no-image">
                <i class="el-icon-s-goods"></i>
                <span>暂无封面</span>
              </div>
              <!-- 收藏按钮 -->
              <div class="favorite-icon" @click.stop="handleToggleFavorite(item)">
                <i :class="item.currentUserFavorite ? 'el-icon-star-on' : 'el-icon-star-off'"
                   :style="{ color: item.currentUserFavorite ? '#f7ba2a' : '#c0c4cc' }"></i>
              </div>
              <!-- 收藏数量 -->
              <div class="favorite-count" v-if="item.favoriteCount > 0">
                <i class="el-icon-star-on"></i> {{ item.favoriteCount }}
              </div>
            </div>
            <div class="card-content">
              <div class="card-title">{{ item.name }}</div>
              <!-- 发布者信息 -->
              <div class="card-author" v-if="queryParams.scope !== 'mine'">
                <el-avatar :size="20" :src="item.avatar || defaultAvatar" />
                <span>{{ item.nickName || '匿名用户' }}</span>
              </div>
              <div class="card-info">
                <dict-tag v-if="item.style" :options="dict.type.wardrobe_style" :value="item.style"/>
                <dict-tag v-if="item.scene" :options="dict.type.wardrobe_scene" :value="item.scene"/>
                <dict-tag v-if="item.season" :options="dict.type.wardrobe_season" :value="item.season"/>
              </div>
              <div class="card-meta" v-if="item.description">
                <span>{{ item.description.length > 30 ? item.description.substring(0, 30) + '...' : item.description }}</span>
              </div>
              <div class="card-actions">
                <el-button type="text" size="mini" icon="el-icon-view" @click="handleView(item)">查看</el-button>
                <el-button type="text" size="mini" icon="el-icon-edit" @click="handleUpdate(item)" 
                  v-if="isOwner(item)" v-hasPermi="['outfit:outfit:edit']">编辑</el-button>
                <el-button type="text" size="mini" icon="el-icon-delete" @click="handleDelete(item)" 
                  v-if="isOwner(item)" v-hasPermi="['outfit:outfit:remove']">删除</el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-empty v-if="outfitList.length === 0" description="暂无穿搭数据"></el-empty>
    </div>

    <!-- 表格视图 -->
    <el-table v-if="viewMode === 'table'" v-loading="loading" :data="outfitList" @selection-change="handleSelectionChange" :key="queryParams.scope">
      <el-table-column type="selection" width="55" align="center" v-if="queryParams.scope === 'mine'" />
      <el-table-column label="封面" align="center" width="100">
        <template slot-scope="scope">
          <image-preview v-if="scope.row.coverImage" :src="scope.row.coverImage" :width="60" :height="60" />
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="名称" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="发布者" align="center" width="120" v-if="queryParams.scope !== 'mine'">
        <template slot-scope="scope">
          <div class="table-author">
            <el-avatar :size="24" :src="scope.row.avatar || defaultAvatar" />
            <span>{{ scope.row.nickName || '匿名' }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="风格" align="center" prop="style" width="90">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wardrobe_style" :value="scope.row.style"/>
        </template>
      </el-table-column>
      <el-table-column label="场景" align="center" prop="scene" width="90">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wardrobe_scene" :value="scope.row.scene"/>
        </template>
      </el-table-column>
      <el-table-column label="季节" align="center" prop="season" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wardrobe_season" :value="scope.row.season"/>
        </template>
      </el-table-column>
      <el-table-column label="收藏数" align="center" prop="favoriteCount" width="70" />
      <el-table-column label="穿着次数" align="center" prop="wearCount" width="80" v-if="queryParams.scope === 'mine'" />
      <el-table-column label="收藏" align="center" width="60">
        <template slot-scope="scope">
          <i 
            :class="scope.row.currentUserFavorite ? 'el-icon-star-on' : 'el-icon-star-off'" 
            :style="{ color: scope.row.currentUserFavorite ? '#f7ba2a' : '#c0c4cc', cursor: 'pointer', fontSize: '18px' }"
            @click="handleToggleFavorite(scope.row)"
          ></i>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" 
            v-if="isOwner(scope.row)" v-hasPermi="['outfit:outfit:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" 
            v-if="isOwner(scope.row)" v-hasPermi="['outfit:outfit:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改穿搭对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="穿搭名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入穿搭名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否公开" prop="isPublic">
              <el-radio-group v-model="form.isPublic">
                <el-radio label="1">公开</el-radio>
                <el-radio label="0">私密</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="风格" prop="style">
              <el-select v-model="form.style" placeholder="请选择风格" style="width: 100%" clearable>
                <el-option
                  v-for="dict in dict.type.wardrobe_style"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="场景" prop="scene">
              <el-select v-model="form.scene" placeholder="请选择场景" style="width: 100%" clearable>
                <el-option
                  v-for="dict in dict.type.wardrobe_scene"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="季节" prop="season">
              <el-select v-model="form.season" placeholder="请选择季节" style="width: 100%" clearable>
                <el-option
                  v-for="dict in dict.type.wardrobe_season"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="封面图片" prop="coverImage">
          <image-upload v-model="form.coverImage" :limit="1" />
        </el-form-item>
        <el-form-item label="选择衣物" prop="clothesIds">
          <div class="clothes-select-area">
            <el-button type="primary" size="small" @click="openClothesSelect">选择衣物</el-button>
            <span class="clothes-count" v-if="form.clothesIds && form.clothesIds.length > 0">
              已选择 {{ form.clothesIds.length }} 件衣物
            </span>
          </div>
          <!-- 已选衣物展示 -->
          <div class="selected-clothes" v-if="selectedClothes.length > 0">
            <div class="clothes-item" v-for="(item, index) in selectedClothes" :key="item.id">
              <image-preview v-if="item.imageUrl" :src="item.imageUrl" :width="60" :height="60" />
              <div v-else class="no-img">
                <i class="el-icon-picture-outline"></i>
              </div>
              <div class="clothes-info">
                <div class="name">{{ item.name }}</div>
                <div class="category">
                  <dict-tag :options="dict.type.wardrobe_category" :value="item.category"/>
                </div>
              </div>
              <i class="el-icon-close remove-btn" @click="removeSelectedClothes(index)"></i>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入穿搭描述/备注" />
        </el-form-item>
        <el-form-item label="标签" prop="tags">
          <el-input v-model="form.tags" placeholder="请输入标签，多个用逗号分隔" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 衣物选择对话框 -->
    <el-dialog title="选择衣物" :visible.sync="clothesSelectOpen" width="900px" append-to-body>
      <el-form :model="clothesQueryParams" ref="clothesQueryForm" size="small" :inline="true">
        <el-form-item label="名称" prop="name">
          <el-input v-model="clothesQueryParams.name" placeholder="请输入名称" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="类别" prop="category">
          <el-select v-model="clothesQueryParams.category" placeholder="请选择" clearable style="width: 120px">
            <el-option
              v-for="dict in dict.type.wardrobe_category"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="searchClothes">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetClothesQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table 
        :data="clothesOptions" 
        @selection-change="handleClothesSelectChange"
        ref="clothesTable"
        height="400"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="图片" align="center" width="80">
          <template slot-scope="scope">
            <image-preview v-if="scope.row.imageUrl" :src="scope.row.imageUrl" :width="50" :height="50" />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="名称" align="center" prop="name" :show-overflow-tooltip="true" />
        <el-table-column label="发布者" align="center" width="100">
          <template slot-scope="scope">
            <span>{{ scope.row.nickName || '匿名' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="类别" align="center" prop="category" width="90">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.wardrobe_category" :value="scope.row.category"/>
          </template>
        </el-table-column>
        <el-table-column label="季节" align="center" prop="season" width="80">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.wardrobe_season" :value="scope.row.season"/>
          </template>
        </el-table-column>
        <el-table-column label="颜色" align="center" prop="color" width="80">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.wardrobe_color" :value="scope.row.color"/>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="clothesTotal > 0"
        :total="clothesTotal"
        :page.sync="clothesQueryParams.pageNum"
        :limit.sync="clothesQueryParams.pageSize"
        @pagination="getClothesOptions"
      />
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="confirmClothesSelect">确 定</el-button>
        <el-button @click="clothesSelectOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 穿搭详情对话框 -->
    <el-dialog title="穿搭详情" :visible.sync="viewOpen" width="700px" append-to-body>
      <el-descriptions :column="2" border v-if="viewData">
        <el-descriptions-item label="穿搭名称">{{ viewData.name }}</el-descriptions-item>
        <el-descriptions-item label="发布者">
          <div class="detail-author">
            <el-avatar :size="24" :src="viewData.avatar || defaultAvatar" />
            <span>{{ viewData.nickName || '匿名用户' }}</span>
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="风格">
          <dict-tag :options="dict.type.wardrobe_style" :value="viewData.style"/>
        </el-descriptions-item>
        <el-descriptions-item label="场景">
          <dict-tag :options="dict.type.wardrobe_scene" :value="viewData.scene"/>
        </el-descriptions-item>
        <el-descriptions-item label="季节">
          <dict-tag :options="dict.type.wardrobe_season" :value="viewData.season"/>
        </el-descriptions-item>
        <el-descriptions-item label="收藏数">{{ viewData.favoriteCount || 0 }}</el-descriptions-item>
        <el-descriptions-item label="穿着次数" v-if="isOwner(viewData)">{{ viewData.wearCount || 0 }}次</el-descriptions-item>
        <el-descriptions-item label="最近穿着" v-if="isOwner(viewData)">{{ viewData.lastWearDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ viewData.description || '-' }}</el-descriptions-item>
        <el-descriptions-item label="标签" :span="2">{{ viewData.tags || '-' }}</el-descriptions-item>
        <el-descriptions-item label="封面图片" :span="2">
          <image-preview v-if="viewData.coverImage" :src="viewData.coverImage" :width="200" :height="200" />
          <span v-else>暂无封面</span>
        </el-descriptions-item>
        <el-descriptions-item label="包含衣物" :span="2">
          <div class="outfit-clothes-list" v-if="viewData.clothesList && viewData.clothesList.length > 0">
            <div class="outfit-clothes-item" v-for="item in viewData.clothesList" :key="item.id">
              <image-preview v-if="item.clothes && item.clothes.imageUrl" :src="item.clothes.imageUrl" :width="60" :height="60" />
              <div v-else class="no-img"><i class="el-icon-picture-outline"></i></div>
              <div class="clothes-name">{{ item.clothes ? item.clothes.name : '衣物已删除' }}</div>
            </div>
          </div>
          <span v-else>暂无关联衣物</span>
        </el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button 
          :type="viewData && viewData.currentUserFavorite ? 'warning' : 'primary'" 
          @click="handleToggleFavorite(viewData)"
        >
          <i :class="viewData && viewData.currentUserFavorite ? 'el-icon-star-on' : 'el-icon-star-off'"></i>
          {{ viewData && viewData.currentUserFavorite ? '取消收藏' : '收藏' }}
        </el-button>
        <el-button type="primary" @click="handleWear(viewData)" v-if="isOwner(viewData)" v-hasPermi="['outfit:outfit:edit']">记录穿着</el-button>
        <el-button @click="viewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOutfit, getOutfit, addOutfit, updateOutfit, delOutfit, wearOutfit, toggleFavorite } from "@/api/outfit/outfit";
import { listClothes } from "@/api/outfit/clothes";

export default {
  name: "Outfit",
  dicts: ['wardrobe_style', 'wardrobe_scene', 'wardrobe_season', 'wardrobe_category', 'wardrobe_color'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 穿搭列表
      outfitList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        style: null,
        scene: null,
        season: null,
        scope: 'all'
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "穿搭名称不能为空", trigger: "blur" }
        ]
      },
      // 视图模式
      viewMode: 'card',
      // 详情弹窗
      viewOpen: false,
      viewData: null,
      // 默认头像
      defaultAvatar: require('@/assets/images/profile.jpg'),
      // 当前用户ID
      currentUserId: null,
      // 衣物选择相关
      clothesSelectOpen: false,
      clothesOptions: [],
      clothesTotal: 0,
      clothesQueryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        category: null,
        scope: 'all'  // 查询所有公开的衣物
      },
      tempSelectedClothes: [],  // 临时选中的衣物
      selectedClothes: []  // 已确认选中的衣物
    };
  },
  created() {
    this.currentUserId = this.$store.getters.userId;
    this.getList();
  },
  methods: {
    /** 判断是否为穿搭发布者 */
    isOwner(item) {
      return item && item.userId === this.currentUserId;
    },
    /** 查询穿搭列表 */
    getList() {
      this.loading = true;
      listOutfit(this.queryParams).then(response => {
        this.outfitList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 切换查询范围 */
    handleScopeChange() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        coverImage: null,
        style: null,
        scene: null,
        season: null,
        description: null,
        tags: null,
        isPublic: '1',
        clothesIds: []
      };
      this.selectedClothes = [];
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams.scope = 'all';
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加穿搭";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids[0];
      getOutfit(id).then(response => {
        this.form = response.data;
        // 处理已选衣物
        if (this.form.clothesList && this.form.clothesList.length > 0) {
          this.form.clothesIds = this.form.clothesList.map(item => item.clothesId);
          this.selectedClothes = this.form.clothesList.map(item => item.clothes).filter(c => c);
        }
        this.open = true;
        this.title = "修改穿搭";
      });
    },
    /** 查看详情 */
    handleView(row) {
      getOutfit(row.id).then(response => {
        this.viewData = response.data;
        this.viewOpen = true;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateOutfit(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addOutfit(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除该穿搭？').then(() => {
        return delOutfit(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 切换收藏状态 */
    handleToggleFavorite(row) {
      toggleFavorite(row.id).then(response => {
        row.currentUserFavorite = !row.currentUserFavorite;
        if (row.currentUserFavorite) {
          row.favoriteCount = (row.favoriteCount || 0) + 1;
        } else {
          row.favoriteCount = Math.max(0, (row.favoriteCount || 1) - 1);
        }
        this.$modal.msgSuccess(response.msg);
      });
    },
    /** 记录穿着 */
    handleWear(row) {
      wearOutfit(row.id).then(() => {
        this.$modal.msgSuccess("已记录穿着");
        row.wearCount = (row.wearCount || 0) + 1;
        row.lastWearDate = new Date().toISOString().split('T')[0];
      });
    },
    /** 打开衣物选择对话框 */
    openClothesSelect() {
      this.clothesSelectOpen = true;
      this.getClothesOptions();
      // 回显已选中的衣物
      this.$nextTick(() => {
        if (this.$refs.clothesTable && this.form.clothesIds) {
          this.clothesOptions.forEach(row => {
            if (this.form.clothesIds.includes(row.id)) {
              this.$refs.clothesTable.toggleRowSelection(row, true);
            }
          });
        }
      });
    },
    /** 获取衣物选项 */
    getClothesOptions() {
      listClothes(this.clothesQueryParams).then(response => {
        this.clothesOptions = response.rows;
        this.clothesTotal = response.total;
        // 回显已选中的衣物
        this.$nextTick(() => {
          if (this.$refs.clothesTable && this.form.clothesIds) {
            this.clothesOptions.forEach(row => {
              if (this.form.clothesIds.includes(row.id)) {
                this.$refs.clothesTable.toggleRowSelection(row, true);
              }
            });
          }
        });
      });
    },
    /** 搜索衣物 */
    searchClothes() {
      this.clothesQueryParams.pageNum = 1;
      this.getClothesOptions();
    },
    /** 重置衣物搜索 */
    resetClothesQuery() {
      this.resetForm("clothesQueryForm");
      this.searchClothes();
    },
    /** 衣物选择变化 */
    handleClothesSelectChange(selection) {
      this.tempSelectedClothes = selection;
    },
    /** 确认衣物选择 */
    confirmClothesSelect() {
      this.selectedClothes = [...this.tempSelectedClothes];
      this.form.clothesIds = this.selectedClothes.map(item => item.id);
      this.clothesSelectOpen = false;
    },
    /** 移除已选衣物 */
    removeSelectedClothes(index) {
      this.selectedClothes.splice(index, 1);
      this.form.clothesIds = this.selectedClothes.map(item => item.id);
    }
  }
};
</script>

<style lang="scss" scoped>
.scope-tabs {
  margin-bottom: 15px;
}

.outfit-card-container {
  margin-top: 15px;
}

.outfit-card {
  margin-bottom: 20px;
  border-radius: 8px;
  overflow: hidden;
  
  .card-image {
    position: relative;
    height: 200px;
    background: #f5f7fa;
    cursor: pointer;
    overflow: hidden;
    
    .no-image {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      height: 100%;
      color: #909399;
      
      i {
        font-size: 48px;
        margin-bottom: 10px;
      }
    }
    
    .favorite-icon {
      position: absolute;
      top: 10px;
      right: 10px;
      width: 30px;
      height: 30px;
      background: rgba(255, 255, 255, 0.9);
      border-radius: 50%;
      display: flex;
      justify-content: center;
      align-items: center;
      cursor: pointer;
      transition: all 0.3s;
      
      &:hover {
        transform: scale(1.1);
      }
      
      i {
        font-size: 18px;
      }
    }

    .favorite-count {
      position: absolute;
      bottom: 10px;
      right: 10px;
      background: rgba(0, 0, 0, 0.5);
      color: #fff;
      padding: 2px 8px;
      border-radius: 10px;
      font-size: 12px;
      
      i {
        color: #f7ba2a;
      }
    }
  }
  
  .card-content {
    padding: 12px;
    
    .card-title {
      font-size: 15px;
      font-weight: 500;
      color: #303133;
      margin-bottom: 8px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .card-author {
      display: flex;
      align-items: center;
      gap: 6px;
      margin-bottom: 8px;
      font-size: 12px;
      color: #909399;
    }
    
    .card-info {
      margin-bottom: 8px;
      display: flex;
      flex-wrap: wrap;
      gap: 5px;
      
      ::v-deep .el-tag {
        margin: 0;
      }
    }
    
    .card-meta {
      font-size: 13px;
      color: #909399;
      margin-bottom: 10px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    
    .card-actions {
      border-top: 1px solid #ebeef5;
      padding-top: 10px;
      display: flex;
      justify-content: space-around;
    }
  }
}

.table-author {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

.detail-author {
  display: flex;
  align-items: center;
  gap: 6px;
}

.clothes-select-area {
  display: flex;
  align-items: center;
  gap: 10px;
  
  .clothes-count {
    color: #67c23a;
    font-size: 14px;
  }
}

.selected-clothes {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 10px;
  padding: 10px;
  background: #f5f7fa;
  border-radius: 4px;
  
  .clothes-item {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 5px 10px;
    background: #fff;
    border-radius: 4px;
    border: 1px solid #dcdfe6;
    position: relative;
    
    .no-img {
      width: 60px;
      height: 60px;
      background: #f5f7fa;
      display: flex;
      justify-content: center;
      align-items: center;
      color: #909399;
    }
    
    .clothes-info {
      .name {
        font-size: 14px;
        color: #303133;
      }
      .category {
        font-size: 12px;
        margin-top: 4px;
      }
    }
    
    .remove-btn {
      position: absolute;
      top: -8px;
      right: -8px;
      width: 18px;
      height: 18px;
      background: #f56c6c;
      color: #fff;
      border-radius: 50%;
      display: flex;
      justify-content: center;
      align-items: center;
      cursor: pointer;
      font-size: 12px;
      
      &:hover {
        background: #f78989;
      }
    }
  }
}

.outfit-clothes-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  
  .outfit-clothes-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 8px;
    background: #f5f7fa;
    border-radius: 4px;
    
    .no-img {
      width: 60px;
      height: 60px;
      background: #e4e7ed;
      display: flex;
      justify-content: center;
      align-items: center;
      color: #909399;
      border-radius: 4px;
    }
    
    .clothes-name {
      margin-top: 5px;
      font-size: 12px;
      color: #606266;
      max-width: 80px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
}
</style>
