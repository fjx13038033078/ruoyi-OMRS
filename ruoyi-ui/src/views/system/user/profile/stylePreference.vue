<template>
  <div class="style-preference">
    <div class="tip-text">选择您喜欢的穿搭风格（可多选）</div>
    <el-checkbox-group v-model="selectedStyles" @change="handleChange">
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
    <div class="btn-group">
      <el-button type="primary" size="mini" @click="submit" :loading="loading">保存</el-button>
    </div>
  </div>
</template>

<script>
import { getUserStyles, saveUserStyles } from "@/api/outfit/userProfile";

export default {
  name: "StylePreference",
  dicts: ['wardrobe_style'],
  data() {
    return {
      loading: false,
      selectedStyles: [],
      styleList: []
    };
  },
  created() {
    this.getStyles();
  },
  methods: {
    getStyles() {
      getUserStyles().then(response => {
        this.styleList = response.data || [];
        this.selectedStyles = this.styleList.map(item => item.styleCode);
      });
    },
    handleChange(val) {
      this.selectedStyles = val;
    },
    submit() {
      if (this.selectedStyles.length === 0) {
        this.$modal.msgWarning("请至少选择一个风格偏好");
        return;
      }
      this.loading = true;
      const styles = this.selectedStyles.map(code => {
        const dictItem = this.dict.type.wardrobe_style.find(d => d.value === code);
        return {
          styleCode: code,
          styleName: dictItem ? dictItem.label : code,
          preferenceLevel: 1
        };
      });
      saveUserStyles(styles).then(response => {
        this.$modal.msgSuccess("保存成功");
        this.$emit("refresh");
      }).finally(() => {
        this.loading = false;
      });
    }
  }
};
</script>

<style scoped>
.style-preference {
  padding: 10px 0;
}
.tip-text {
  margin-bottom: 15px;
  color: #606266;
  font-size: 14px;
}
.style-checkbox {
  margin: 5px 10px 5px 0;
}
.btn-group {
  margin-top: 20px;
}
</style>

