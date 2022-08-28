<template>
  <el-alert title="每当您重新设置邮箱后，我们都需要验证您邮箱的可用性！请检查您的收件箱中发件人为 sxw-suda@outlook.com 的验证邮件。"
            description="请于收到邮件的一天内在下方填写并提交。如有多封邮件，请填写最新的；若未找到，请检查垃圾邮件箱或重新发送。"
            style="margin-bottom: 18px;"
            type="success" :closable="false" effect="dark"/>
  <el-form v-if="user.email" ref="userRef" :model="user" label-width="80px">
    <el-form-item label="邮箱" prop="email">
      <el-input :placeholder="user.email" disabled/>
    </el-form-item>
    <el-form-item label="验证信息" prop="code">
      <el-input v-model="state.code" maxlength="64"/>
    </el-form-item>
    <el-form-item>
      <el-button @click="send">发送验证邮件</el-button>
      <el-button type="primary" @click="submit">验证邮箱</el-button>
      <el-button type="danger" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
  <el-alert v-else title="尚未填写邮箱信息，请完善个人资料！"
            style="margin-bottom: 18px;"
            type="error" :closable="false" effect="dark"/>
</template>

<script setup>
import {requestVerifyMail, fillVerifyInfo} from "@/api/system/user";
import {getCurrentInstance, reactive} from "vue";
import {ElMessageBox} from "element-plus";
import {useStore} from "vuex";

const props = defineProps({
  user: {
    type: Object
  }
});

const state = reactive({
  code: ''
})

const {proxy} = getCurrentInstance();
const store = useStore();

/** 提交按钮 */
function submit() {
  proxy.$refs.userRef.validate(valid => {
    if (valid) {
      fillVerifyInfo(state.code.trim()).then(response => {
        if (response.code === 200) {
          ElMessageBox.alert('已获得权限：普通用户—问卷设计者/问卷填写者。', '账户验证成功', {
            confirmButtonText: '好',
            callback: action => {
              location.reload();
            }
          })
        } else {
          proxy.$modal.msgError("账户验证不成功")
        }
      });
    }
  });
}

/** 关闭按钮 */
function close() {
  proxy.$tab.closePage();
}

/** 发送验证邮件 */
function send() {
  requestVerifyMail().then(response => {
    if (response.code === 200) {
      proxy.$modal.msgSuccess("邮件发送成功，请于24h内填写。若找不到，可以检查垃圾邮件箱哦～");
    } else {
      proxy.$modal.msgError("邮件发送失败，请重试")
    }
  })
}

</script>

<style scoped>

</style>
