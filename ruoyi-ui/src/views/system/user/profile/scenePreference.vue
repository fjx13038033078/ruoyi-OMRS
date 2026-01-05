<template>
  <div class="scene-preference">
    <div class="tip-text">选择您常用的穿搭场景（可多选）</div>
    <el-checkbox-group v-model="selectedScenes" @change="handleChange">
      <el-checkbox
        v-for="dict in dict.type.wardrobe_scene"
        :key="dict.value"
        :label="dict.value"
        border
        class="scene-checkbox"
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
import { getUserScenes, saveUserScenes } from "@/api/outfit/userProfile";

export default {
  name: "ScenePreference",
  dicts: ['wardrobe_scene'],
  data() {
    return {
      loading: false,
      selectedScenes: [],
      sceneList: []
    };
  },
  created() {
    this.getScenes();
  },
  methods: {
    getScenes() {
      getUserScenes().then(response => {
        this.sceneList = response.data || [];
        this.selectedScenes = this.sceneList.map(item => item.sceneCode);
      });
    },
    handleChange(val) {
      this.selectedScenes = val;
    },
    submit() {
      if (this.selectedScenes.length === 0) {
        this.$modal.msgWarning("请至少选择一个场景偏好");
        return;
      }
      this.loading = true;
      const scenes = this.selectedScenes.map(code => {
        const dictItem = this.dict.type.wardrobe_scene.find(d => d.value === code);
        return {
          sceneCode: code,
          sceneName: dictItem ? dictItem.label : code,
          frequency: 1
        };
      });
      saveUserScenes(scenes).then(response => {
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
.scene-preference {
  padding: 10px 0;
}
.tip-text {
  margin-bottom: 15px;
  color: #606266;
  font-size: 14px;
}
.scene-checkbox {
  margin: 5px 10px 5px 0;
}
.btn-group {
  margin-top: 20px;
}
</style>

