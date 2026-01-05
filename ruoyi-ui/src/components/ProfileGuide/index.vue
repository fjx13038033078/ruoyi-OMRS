<template>
  <el-dialog
    title="完善个人画像"
    :visible.sync="dialogVisible"
    width="650px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :show-close="false"
    center
    class="profile-guide-dialog"
  >
    <div class="guide-header">
      <i class="el-icon-user-solid guide-icon"></i>
      <p class="guide-tip">为了给您提供更精准的穿搭推荐，请先完善以下基本信息</p>
    </div>
    
    <el-steps :active="currentStep" finish-status="success" simple class="guide-steps">
      <el-step title="基本信息"></el-step>
      <el-step title="风格偏好"></el-step>
      <el-step title="常用场景"></el-step>
    </el-steps>

    <!-- 步骤1：基本信息 -->
    <div v-show="currentStep === 0" class="step-content">
      <el-form ref="basicForm" :model="basicForm" :rules="basicRules" label-width="90px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="身高(cm)" prop="height">
              <el-input-number v-model="basicForm.height" :min="100" :max="250" :precision="1" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="体重(kg)" prop="weight">
              <el-input-number v-model="basicForm.weight" :min="30" :max="200" :precision="1" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="体型" prop="bodyType">
              <el-select v-model="basicForm.bodyType" placeholder="请选择体型" style="width: 100%">
                <el-option v-for="dict in dict.type.wardrobe_body_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="肤色" prop="skinTone">
              <el-select v-model="basicForm.skinTone" placeholder="请选择肤色" style="width: 100%">
                <el-option v-for="dict in dict.type.wardrobe_skin_tone" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="所在城市" prop="location">
              <el-input v-model="basicForm.location" placeholder="请输入所在城市，用于获取天气信息" maxlength="50" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>

    <!-- 步骤2：风格偏好 -->
    <div v-show="currentStep === 1" class="step-content">
      <div class="select-tip">请选择您喜欢的穿搭风格（可多选）</div>
      <el-checkbox-group v-model="selectedStyles" class="style-group">
        <el-checkbox
          v-for="dict in dict.type.wardrobe_style"
          :key="dict.value"
          :label="dict.value"
          border
          class="style-checkbox"
        >
          {{ dict.label }}
        </el-checkbox>
      </el-checkbox-group>
    </div>

    <!-- 步骤3：常用场景 -->
    <div v-show="currentStep === 2" class="step-content">
      <div class="select-tip">请选择您的常用穿搭场景（可多选）</div>
      <el-checkbox-group v-model="selectedScenes" class="style-group">
        <el-checkbox
          v-for="dict in dict.type.wardrobe_scene"
          :key="dict.value"
          :label="dict.value"
          border
          class="style-checkbox"
        >
          {{ dict.label }}
        </el-checkbox>
      </el-checkbox-group>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button v-if="currentStep > 0" @click="prevStep">上一步</el-button>
      <el-button v-if="currentStep < 2" type="primary" @click="nextStep">下一步</el-button>
      <el-button v-if="currentStep === 2" type="primary" :loading="submitLoading" @click="submitProfile">完成</el-button>
      <el-button type="text" @click="skipGuide">稍后填写</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { submitUserQuestionnaire } from "@/api/outfit/userProfile";

export default {
  name: "ProfileGuide",
  dicts: ['wardrobe_body_type', 'wardrobe_skin_tone', 'wardrobe_style', 'wardrobe_scene'],
  data() {
    return {
      dialogVisible: false,
      currentStep: 0,
      submitLoading: false,
      basicForm: {
        height: null,
        weight: null,
        bodyType: '',
        skinTone: '',
        location: ''
      },
      basicRules: {
        height: [{ required: true, message: '请输入身高', trigger: 'blur' }],
        location: [{ required: true, message: '请输入所在城市', trigger: 'blur' }]
      },
      selectedStyles: [],
      selectedScenes: []
    };
  },
  methods: {
    show() {
      // 只有在弹窗未显示时才重置，避免重复触发导致跳回第一步
      if (!this.dialogVisible) {
        this.currentStep = 0;
        this.resetForm();
        this.dialogVisible = true;
      }
    },
    hide() {
      this.dialogVisible = false;
    },
    resetForm() {
      this.basicForm = {
        height: null,
        weight: null,
        bodyType: '',
        skinTone: '',
        location: ''
      };
      this.selectedStyles = [];
      this.selectedScenes = [];
    },
    prevStep() {
      if (this.currentStep > 0) {
        this.currentStep--;
      }
    },
    nextStep() {
      if (this.currentStep === 0) {
        this.$refs.basicForm.validate(valid => {
          if (valid) {
            this.currentStep++;
          }
        });
      } else if (this.currentStep === 1) {
        if (this.selectedStyles.length === 0) {
          this.$modal.msgWarning("请至少选择一个风格偏好");
          return;
        }
        this.currentStep++;
      }
    },
    submitProfile() {
      if (this.selectedScenes.length === 0) {
        this.$modal.msgWarning("请至少选择一个常用场景");
        return;
      }
      
      this.submitLoading = true;
      
      // 构建风格偏好数据
      const styles = this.selectedStyles.map(code => {
        const dictItem = this.dict.type.wardrobe_style.find(d => d.value === code);
        return {
          styleCode: code,
          styleName: dictItem ? dictItem.label : code,
          preferenceLevel: 1
        };
      });
      
      // 构建场景偏好数据
      const scenes = this.selectedScenes.map(code => {
        const dictItem = this.dict.type.wardrobe_scene.find(d => d.value === code);
        return {
          sceneCode: code,
          sceneName: dictItem ? dictItem.label : code,
          frequencyLevel: 1
        };
      });
      
      const data = {
        bodyInfo: this.basicForm,
        styles: styles,
        scenes: scenes
      };
      
      submitUserQuestionnaire(data).then(response => {
        this.$modal.msgSuccess("个人画像保存成功！");
        this.$store.commit('SET_QUESTIONNAIRE_COMPLETED', '1');
        this.dialogVisible = false;
        this.$emit('completed');
      }).finally(() => {
        this.submitLoading = false;
      });
    },
    skipGuide() {
      this.$confirm('稍后可在"个人中心"完善个人画像，未完善画像可能影响推荐效果', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.dialogVisible = false;
      }).catch(() => {});
    }
  }
};
</script>

<style lang="scss" scoped>
.profile-guide-dialog {
  ::v-deep .el-dialog__header {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    padding: 15px 20px;
    .el-dialog__title {
      color: #fff;
      font-size: 18px;
    }
  }
  ::v-deep .el-dialog__body {
    padding: 20px 30px;
  }
}

.guide-header {
  text-align: center;
  margin-bottom: 20px;
  .guide-icon {
    font-size: 48px;
    color: #667eea;
  }
  .guide-tip {
    color: #606266;
    font-size: 14px;
    margin-top: 10px;
  }
}

.guide-steps {
  margin-bottom: 25px;
}

.step-content {
  min-height: 200px;
  padding: 10px 0;
}

.select-tip {
  color: #606266;
  font-size: 14px;
  margin-bottom: 15px;
}

.style-group {
  display: flex;
  flex-wrap: wrap;
}

.style-checkbox {
  margin: 5px 10px 5px 0 !important;
  width: calc(33% - 10px);
  text-align: center;
}

.dialog-footer {
  text-align: center;
}
</style>

