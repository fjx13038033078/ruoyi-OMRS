<template>
  <el-form ref="form" :model="form" :rules="rules" label-width="100px">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="身高(cm)" prop="height">
          <el-input-number v-model="form.height" :min="100" :max="250" :precision="1" :step="0.5" controls-position="right" style="width: 100%" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="体重(kg)" prop="weight">
          <el-input-number v-model="form.weight" :min="30" :max="200" :precision="1" :step="0.5" controls-position="right" style="width: 100%" />
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-form-item label="胸围(cm)" prop="bust">
          <el-input-number v-model="form.bust" :min="60" :max="150" :precision="1" controls-position="right" style="width: 100%" />
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="腰围(cm)" prop="waist">
          <el-input-number v-model="form.waist" :min="50" :max="150" :precision="1" controls-position="right" style="width: 100%" />
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="臀围(cm)" prop="hip">
          <el-input-number v-model="form.hip" :min="60" :max="150" :precision="1" controls-position="right" style="width: 100%" />
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-form-item label="上衣尺码" prop="topSize">
          <el-select v-model="form.topSize" placeholder="请选择" style="width: 100%">
            <el-option v-for="dict in dict.type.wardrobe_size" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="裤子尺码" prop="bottomSize">
          <el-select v-model="form.bottomSize" placeholder="请选择" style="width: 100%">
            <el-option v-for="dict in dict.type.wardrobe_bottom_size" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="鞋码" prop="shoeSize">
          <el-input-number v-model="form.shoeSize" :min="34" :max="48" :precision="1" :step="0.5" controls-position="right" style="width: 100%" />
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="肤色" prop="skinTone">
          <el-select v-model="form.skinTone" placeholder="请选择" style="width: 100%">
            <el-option v-for="dict in dict.type.wardrobe_skin_tone" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="体型" prop="bodyType">
          <el-select v-model="form.bodyType" placeholder="请选择" style="width: 100%">
            <el-option v-for="dict in dict.type.wardrobe_body_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="24">
        <el-form-item label="所在城市" prop="location">
          <el-input v-model="form.location" placeholder="请输入所在城市" maxlength="50" />
        </el-form-item>
      </el-col>
    </el-row>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">保存</el-button>
      <el-button type="danger" size="mini" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateBodyInfo } from "@/api/outfit/userProfile";

export default {
  name: "BodyInfo",
  dicts: ['wardrobe_size', 'wardrobe_bottom_size', 'wardrobe_skin_tone', 'wardrobe_body_type'],
  props: {
    user: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      form: {},
      rules: {}
    };
  },
  watch: {
    user: {
      handler(val) {
        if (val) {
          this.form = {
            height: val.height,
            weight: val.weight,
            bust: val.bust,
            waist: val.waist,
            hip: val.hip,
            topSize: val.topSize || '',
            bottomSize: val.bottomSize || '',
            shoeSize: val.shoeSize,
            skinTone: val.skinTone || '',
            bodyType: val.bodyType || '',
            location: val.location || ''
          };
        }
      },
      immediate: true,
      deep: true
    }
  },
  methods: {
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateBodyInfo(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.$emit("refresh");
          });
        }
      });
    },
    close() {
      this.$tab.closePage();
    }
  }
};
</script>

