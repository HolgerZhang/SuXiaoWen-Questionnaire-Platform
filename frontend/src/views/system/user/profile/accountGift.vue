<template>
  <el-result icon="info" :title="'积分余额: '+state.credit">
    <template #extra>
      <el-button type="primary" @click="recharge">账户充值</el-button>
    </template>
  </el-result>

</template>

<script setup>
import {reactive} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import {getCredit, rechargeCredit} from "@/api/system/credit";

const state = reactive({
  credit: '0'
})

function getUserCredit() {
  getCredit().then(response => {
    console.log(response)
    if (response.code === 200) {
      state.credit = response.credit;
    } else {
      state.credit = '获取失败';
    }
  })
}

function recharge() {
  ElMessageBox.prompt('请输入要充值的积分数量', '账户充值', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    inputPattern: /^\d+$/,
    inputErrorMessage: '请输入整数',
  }).then(({value}) => {
    value = Number(value)
    if (value > 0 && value < 1000) {
      rechargeCredit(value).then(response => {
        if (response.code === 200) {
          ElMessage({
            type: 'success',
            message: `成功充值 ${value} 积分`,
          })
          state.credit += value;
        } else {
          ElMessage({
            type: 'error',
            message: `积分充值失败`,
          })
        }
      })
    } else {
      ElMessage({
        type: 'error',
        message: `请输入大于0小于1000的整数`,
      })
    }
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '充值取消',
    })
  })
}

getUserCredit();
</script>

<style scoped>

</style>
