<template>
  <div class="app-container">
    <!-- 查询范围Tab -->
    <el-tabs v-model="queryParams.scope" @tab-click="handleScopeChange" class="scope-tabs">
      <el-tab-pane label="全部衣物" name="all">
        <template slot="label">
          <i class="el-icon-s-home"></i> 全部衣物
        </template>
      </el-tab-pane>
      <el-tab-pane label="我的衣物" name="mine">
        <template slot="label">
          <i class="el-icon-user"></i> 我的衣物
        </template>
      </el-tab-pane>
      <el-tab-pane label="我的收藏" name="favorite">
        <template slot="label">
          <i class="el-icon-star-on"></i> 我的收藏
        </template>
      </el-tab-pane>
    </el-tabs>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="衣物名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入衣物名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="类别" prop="category">
        <el-select v-model="queryParams.category" placeholder="请选择类别" clearable>
          <el-option
            v-for="dict in dict.type.wardrobe_category"
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
      <el-form-item label="颜色" prop="color">
        <el-select v-model="queryParams.color" placeholder="请选择颜色" clearable>
          <el-option
            v-for="dict in dict.type.wardrobe_color"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status" v-if="queryParams.scope === 'mine'">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.wardrobe_clothes_status"
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
          v-hasPermi="['outfit:clothes:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5" v-if="queryParams.scope === 'mine'">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['outfit:clothes:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5" v-if="queryParams.scope === 'mine'">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['outfit:clothes:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5" v-if="queryParams.scope === 'mine'">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['outfit:clothes:export']"
        >导出</el-button>
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
    <div v-if="viewMode === 'card'" class="clothes-card-container">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="4" v-for="item in clothesList" :key="item.id">
          <el-card class="clothes-card" shadow="hover" :body-style="{ padding: '0' }">
            <div class="card-image" @click="handleView(item)">
              <image-preview v-if="item.imageUrl" :src="item.imageUrl" :width="'100%'" :height="'180px'" />
              <div v-else class="no-image">
                <i class="el-icon-picture-outline"></i>
                <span>暂无图片</span>
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
                <dict-tag :options="dict.type.wardrobe_category" :value="item.category"/>
                <dict-tag v-if="item.season" :options="dict.type.wardrobe_season" :value="item.season"/>
              </div>
              <div class="card-meta">
                <span v-if="item.brand">{{ item.brand }}</span>
                <span v-if="item.price">¥{{ item.price }}</span>
              </div>
              <div class="card-actions">
                <el-button type="text" size="mini" icon="el-icon-view" @click="handleView(item)">查看</el-button>
                <el-button type="text" size="mini" icon="el-icon-edit" @click="handleUpdate(item)" 
                  v-if="isOwner(item)" v-hasPermi="['outfit:clothes:edit']">编辑</el-button>
                <el-button type="text" size="mini" icon="el-icon-delete" @click="handleDelete(item)" 
                  v-if="isOwner(item)" v-hasPermi="['outfit:clothes:remove']">删除</el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-empty v-if="clothesList.length === 0" description="暂无衣物数据"></el-empty>
    </div>

    <!-- 表格视图 -->
    <el-table v-if="viewMode === 'table'" v-loading="loading" :data="clothesList" @selection-change="handleSelectionChange" :key="queryParams.scope">
      <el-table-column type="selection" width="55" align="center" v-if="queryParams.scope === 'mine'" />
      <el-table-column label="图片" align="center" width="80">
        <template slot-scope="scope">
          <image-preview v-if="scope.row.imageUrl" :src="scope.row.imageUrl" :width="50" :height="50" />
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
      <el-table-column label="类别" align="center" prop="category">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wardrobe_category" :value="scope.row.category"/>
        </template>
      </el-table-column>
      <el-table-column label="季节" align="center" prop="season">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wardrobe_season" :value="scope.row.season"/>
        </template>
      </el-table-column>
      <el-table-column label="颜色" align="center" prop="color">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wardrobe_color" :value="scope.row.color"/>
        </template>
      </el-table-column>
      <el-table-column label="品牌" align="center" prop="brand" :show-overflow-tooltip="true" />
      <el-table-column label="价格" align="center" prop="price" width="80">
        <template slot-scope="scope">
          <span v-if="scope.row.price">¥{{ scope.row.price }}</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="收藏数" align="center" prop="favoriteCount" width="70" />
      <el-table-column label="收藏" align="center" width="60">
        <template slot-scope="scope">
          <i 
            :class="scope.row.currentUserFavorite ? 'el-icon-star-on' : 'el-icon-star-off'" 
            :style="{ color: scope.row.currentUserFavorite ? '#f7ba2a' : '#c0c4cc', cursor: 'pointer', fontSize: '18px' }"
            @click="handleToggleFavorite(scope.row)"
          ></i>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" v-if="queryParams.scope === 'mine'">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wardrobe_clothes_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" 
            v-if="isOwner(scope.row)" v-hasPermi="['outfit:clothes:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" 
            v-if="isOwner(scope.row)" v-hasPermi="['outfit:clothes:remove']">删除</el-button>
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

    <!-- 添加或修改衣物对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="衣物名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入衣物名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="品牌" prop="brand">
              <el-input v-model="form.brand" placeholder="请输入品牌" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="类别" prop="category">
              <el-select v-model="form.category" placeholder="请选择类别" style="width: 100%">
                <el-option
                  v-for="dict in dict.type.wardrobe_category"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="子类别" prop="subCategory">
              <el-select v-model="form.subCategory" placeholder="请选择子类别" style="width: 100%" clearable>
                <el-option
                  v-for="dict in dict.type.wardrobe_sub_category"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="适用季节" prop="season">
              <el-select v-model="form.season" placeholder="请选择季节" style="width: 100%">
                <el-option
                  v-for="dict in dict.type.wardrobe_season"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="颜色" prop="color">
              <el-select v-model="form.color" placeholder="请选择颜色" style="width: 100%">
                <el-option
                  v-for="dict in dict.type.wardrobe_color"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="材质" prop="material">
              <el-select v-model="form.material" placeholder="请选择材质" style="width: 100%" clearable>
                <el-option
                  v-for="dict in dict.type.wardrobe_material"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
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
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="尺码" prop="size">
              <el-select v-model="form.size" placeholder="请选择尺码" style="width: 100%" clearable>
                <el-option
                  v-for="dict in dict.type.wardrobe_size"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="价格" prop="price">
              <el-input-number v-model="form.price" :min="0" :precision="2" :step="10" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="购买日期" prop="purchaseDate">
              <el-date-picker v-model="form.purchaseDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
                <el-option
                  v-for="dict in dict.type.wardrobe_clothes_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="是否公开" prop="isPublic">
              <el-radio-group v-model="form.isPublic">
                <el-radio label="1">公开（所有人可见）</el-radio>
                <el-radio label="0">私密（仅自己可见）</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="适用场合" prop="occasion">
          <el-input v-model="form.occasion" placeholder="请输入适用场合，如：上班、约会、运动等" />
        </el-form-item>
        <el-form-item label="洗涤方式" prop="washMethod">
          <el-input v-model="form.washMethod" placeholder="请输入洗涤方式" />
        </el-form-item>
        <el-form-item label="衣物图片" prop="imageUrl">
          <image-upload v-model="form.imageUrl" :limit="3" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入描述信息" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 衣物详情对话框 -->
    <el-dialog title="衣物详情" :visible.sync="viewOpen" width="650px" append-to-body>
      <el-descriptions :column="2" border v-if="viewData">
        <el-descriptions-item label="衣物名称">{{ viewData.name }}</el-descriptions-item>
        <el-descriptions-item label="发布者">
          <div class="detail-author">
            <el-avatar :size="24" :src="viewData.avatar || defaultAvatar" />
            <span>{{ viewData.nickName || '匿名用户' }}</span>
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="品牌">{{ viewData.brand || '-' }}</el-descriptions-item>
        <el-descriptions-item label="收藏数">{{ viewData.favoriteCount || 0 }}</el-descriptions-item>
        <el-descriptions-item label="类别">
          <dict-tag :options="dict.type.wardrobe_category" :value="viewData.category"/>
        </el-descriptions-item>
        <el-descriptions-item label="子类别">
          <dict-tag :options="dict.type.wardrobe_sub_category" :value="viewData.subCategory"/>
        </el-descriptions-item>
        <el-descriptions-item label="季节">
          <dict-tag :options="dict.type.wardrobe_season" :value="viewData.season"/>
        </el-descriptions-item>
        <el-descriptions-item label="颜色">
          <dict-tag :options="dict.type.wardrobe_color" :value="viewData.color"/>
        </el-descriptions-item>
        <el-descriptions-item label="材质">
          <dict-tag :options="dict.type.wardrobe_material" :value="viewData.material"/>
        </el-descriptions-item>
        <el-descriptions-item label="风格">
          <dict-tag :options="dict.type.wardrobe_style" :value="viewData.style"/>
        </el-descriptions-item>
        <el-descriptions-item label="尺码">{{ viewData.size || '-' }}</el-descriptions-item>
        <el-descriptions-item label="价格">{{ viewData.price ? '¥' + viewData.price : '-' }}</el-descriptions-item>
        <el-descriptions-item label="购买日期">{{ viewData.purchaseDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <dict-tag :options="dict.type.wardrobe_clothes_status" :value="viewData.status"/>
        </el-descriptions-item>
        <el-descriptions-item label="穿着次数" v-if="isOwner(viewData)">{{ viewData.wearCount || 0 }}次</el-descriptions-item>
        <el-descriptions-item label="最近穿着" v-if="isOwner(viewData)">{{ viewData.lastWearDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="适用场合" :span="2">{{ viewData.occasion || '-' }}</el-descriptions-item>
        <el-descriptions-item label="洗涤方式" :span="2">{{ viewData.washMethod || '-' }}</el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ viewData.description || '-' }}</el-descriptions-item>
        <el-descriptions-item label="图片" :span="2">
          <image-preview v-if="viewData.imageUrl" :src="viewData.imageUrl" :width="200" :height="200" />
          <span v-else>暂无图片</span>
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
        <el-button type="primary" @click="handleWear(viewData)" v-if="isOwner(viewData)" v-hasPermi="['outfit:clothes:edit']">记录穿着</el-button>
        <el-button @click="viewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listClothes, getClothes, addClothes, updateClothes, delClothes, wearClothes, toggleFavorite } from "@/api/outfit/clothes";

export default {
  name: "Clothes",
  dicts: ['wardrobe_category', 'wardrobe_sub_category', 'wardrobe_season', 'wardrobe_color', 'wardrobe_material', 'wardrobe_clothes_status', 'wardrobe_style', 'wardrobe_size'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 衣物列表
      clothesList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        category: null,
        season: null,
        color: null,
        status: null,
        scope: 'all'  // 查询范围：all-全部, mine-我的, favorite-我的收藏
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "衣物名称不能为空", trigger: "blur" }
        ],
        category: [
          { required: true, message: "请选择类别", trigger: "change" }
        ]
      },
      // 视图模式：card/table
      viewMode: 'card',
      // 详情弹窗
      viewOpen: false,
      viewData: null,
      // 默认头像
      defaultAvatar: require('@/assets/images/profile.jpg'),
      // 当前用户ID
      currentUserId: null
    };
  },
  created() {
    this.currentUserId = this.$store.getters.userId;
    this.getList();
  },
  methods: {
    /** 判断是否为衣物发布者 */
    isOwner(item) {
      return item && item.userId === this.currentUserId;
    },
    /** 查询衣物列表 */
    getList() {
      this.loading = true;
      listClothes(this.queryParams).then(response => {
        this.clothesList = response.rows;
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
        category: null,
        subCategory: null,
        season: null,
        color: null,
        colorHex: null,
        material: null,
        brand: null,
        price: null,
        purchaseDate: null,
        size: null,
        style: null,
        occasion: null,
        washMethod: null,
        imageUrl: null,
        description: null,
        status: '0',
        isPublic: '1'  // 默认公开
      };
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
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加衣物";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids[0];
      getClothes(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改衣物";
      });
    },
    /** 查看详情 */
    handleView(row) {
      getClothes(row.id).then(response => {
        this.viewData = response.data;
        this.viewOpen = true;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateClothes(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addClothes(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除该衣物？').then(() => {
        return delClothes(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('outfit/clothes/export', {
        ...this.queryParams
      }, `clothes_${new Date().getTime()}.xlsx`);
    },
    /** 切换收藏状态 */
    handleToggleFavorite(row) {
      toggleFavorite(row.id).then(response => {
        // 更新当前行的收藏状态
        row.currentUserFavorite = !row.currentUserFavorite;
        // 更新收藏数量
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
      wearClothes(row.id).then(() => {
        this.$modal.msgSuccess("已记录穿着");
        row.wearCount = (row.wearCount || 0) + 1;
        row.lastWearDate = new Date().toISOString().split('T')[0];
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.scope-tabs {
  margin-bottom: 15px;
}

.clothes-card-container {
  margin-top: 15px;
}

.clothes-card {
  margin-bottom: 20px;
  border-radius: 8px;
  overflow: hidden;
  
  .card-image {
    position: relative;
    height: 180px;
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
      
      span {
        margin-right: 10px;
      }
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
</style>
