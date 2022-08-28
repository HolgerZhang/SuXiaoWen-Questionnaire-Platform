<template>
  <el-form ref="userRef" :model="state.uniAccountInfo" :rules="rules" label-width="80px">
    <el-form-item label="学号" prop="studentId">
      <el-input v-model="state.uniAccountInfo.studentId" maxlength="15"/>
    </el-form-item>
    <el-form-item label="真实姓名" prop="realName">
      <el-input v-model="state.uniAccountInfo.name" maxlength="10"/>
    </el-form-item>
    <el-form-item label="专业" prop="major">
      <el-input v-model="state.uniAccountInfo.major" maxlength="32"/>
    </el-form-item>
    <el-form-item label="年级" prop="grade">
      <el-input v-model="state.uniAccountInfo.grade" maxlength="10"/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit">保存</el-button>
      <el-button type="danger" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
import {reactive} from "vue";
import {getCurrentInstance, ref} from "vue";
import {ElNotification} from "element-plus";
import {updateUniAccountInfo, getUniAccountInfo} from "@/api/system/uniAccount";

const props = defineProps({
  user: {
    type: Object
  }
});
const state = reactive({
  uniAccountInfo: {
    userId: props.user.userId,
    studentId: props.user.studentId,
    name: '',
    major: '',
    grade: ''
  }
})
const rules = ref({
  studentId: [{required: true, message: "学号不能为空", trigger: "blur"}]
});
const {proxy} = getCurrentInstance();

/** 提交按钮 */
function submit() {
  state.uniAccountInfo.userId = props.user.userId
  proxy.$refs.userRef.validate(valid => {
    if (valid) {
      updateUniAccountInfo(state.uniAccountInfo).then(response => {
        proxy.$modal.msgSuccess("修改成功");
        location.reload();
      });
    }
  });
}

/** 关闭按钮 */
function close() {
  proxy.$tab.closePage();
}

function loadUniAccountInfo() {
  getUniAccountInfo().then(response => {
    console.log(response)
    state.uniAccountInfo.studentId = response.studentId;
    state.uniAccountInfo.name = response.name
    state.uniAccountInfo.major = response.major
    state.uniAccountInfo.grade = response.grade
  })
}

loadUniAccountInfo();

</script>

<style scoped>

</style>
