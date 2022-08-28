<template>
  <div>
    <!--  NavBar  -->
    <div class="fixed z-20 w-full bg-white">
      <div class="flex h-14 justify-center items-center place-self-center shadow-xl">
        <div class="grid grid-cols-5 place-items-center text-lg font-bold">
          <a @click="gotoDesign">编辑问卷</a>
          <el-icon>
            <ArrowRight/>
          </el-icon>
          <span class="text-blue-600">发布问卷</span>
          <el-icon>
            <ArrowRight/>
          </el-icon>
          <a @click="gotoData">数据报表</a>
        </div>
      </div>
    </div>
    <div v-if="state.loading" class="display h-full flex-col bg-gray-100 justify-center items-center relative">
      <div class="p-5">
        <el-card class="box-card w-2/3 mt-10 h-48 flex-row" style="border-radius: 1rem">
          <el-row>
            <el-col :span="6">
              <div class="title px-2 md:px-5 py-3">
                <el-icon>
                  <Edit/>
                </el-icon>
                <span class="text-base md:text-lg mx-1 md:mx-5">问卷分享</span>
                <QrCode :url="state.id" class="mt-2 mx-6"/>
              </div>
            </el-col>
            <el-col :span="12" class="py-7">
              <div class="copyurl h-16 w-full">
                <div class="py-3 w-full px-2 md:px-5 text-blue-500 underline text-sm">
                  <a>{{ shareUrl }}</a>
                  <button class="share-link float-right text-base" :data-clipboard-text="shareUrl" @click="copy">
                    <el-icon>
                      <CopyDocument/>
                    </el-icon>
                  </button>
                </div>
              </div>
              <div>
                <el-tooltip effect="light" content="长按/右键左侧二维码以保存" placement="bottom">
                  <button class="download my-6 w-5/12 h-10">下载问卷二维码</button>
                </el-tooltip>
                <button class="release my-6 w-5/12 h-10 float-right" @click="makeTask">发布互助任务</button>
              </div>
            </el-col>
            <el-col :span="6" class="py-8 text-center">
              <!-- <div class="state w-1/3 align-middle text-center ">收集中</div> -->
              <div class="tag" v-if="state.question.questionnaireStatus === `0`">
                <el-tag size="large" type="warning">未发布</el-tag>
              </div>
              <div class="tag" v-if="state.question.questionnaireStatus === `1`">
                <el-tag size="large" type="success">收集中</el-tag>
              </div>
              <div class="tag" v-if="state.question.questionnaireStatus === `2`">
                <el-tag size="large" type="info">已结束</el-tag>
              </div>
              <div class="stop w-1/2 h-10 text-center py-1.5 my-8">
                <button v-if="state.question.questionnaireStatus === `1`" class="w-full h-full" @click="stopCollect">
                  暂停收集
                </button>
                <button v-else class="w-full h-full" @click="startCollect">
                  开始收集
                </button>
              </div>
            </el-col>
          </el-row>
        </el-card>
        <el-card class="box-card w-2/3 mt-5" style="border-radius: 1rem">
          <div class="title px-2 md:px-5 py-3">
            <el-icon>
              <Setting/>
            </el-icon>
            <span class="text-base md:text-lg mx-1 md:mx-5">问卷设置</span>
          </div>
          <div class="w-5/6 md:w-1/2 h-full m-auto text-sm md:text-base">
            <el-form v-model="state.question" label-width="auto" label-position="left">
              <el-form-item label="问卷标题">
                <el-input :placeholder="state.question.title" v-model="state.question.title"/>
              </el-form-item>
              <el-form-item label="问卷简介">
                <el-input :placeholder="state.question.introduction" v-model="state.question.introduction"/>
              </el-form-item>
              <el-form-item label="到期时间">
                <el-date-picker v-model="state.question.dueTime" type="datetime" placeholder="设置到期时间"
                                :disabled-date="disableDate" format="YYYY-MM-DD HH:mm:ss"
                                value-format="YYYY-MM-DD HH:mm:ss"/>
              </el-form-item>
              <el-form-item label="星标问卷">
                <el-switch v-model="state.question.starFlag"
                           style="--el-switch-on-color: #13ce66; --el-switch-off-color: #afafaf" active-value="1"
                           inactive-value="0"/>
              </el-form-item>
              <el-form-item label="允许填写者修改数据">
                <el-switch v-model="state.question.modifyFlag" active-value="1" inactive-value="0"/>
              </el-form-item>
              <el-form-item label="允许匿名填写">
                <el-switch v-model="state.question.anonymousFlag" active-value="1" inactive-value="0"/>
              </el-form-item>
            </el-form>
            <el-button class="relative top-1 right-1" type="primary" @click="handleSettingsDialogConfirmClose">修改问卷信息
            </el-button>
            <el-button class="relative top-1 right-1" type="primary" @click="handleInviteDialogConfirmClose">邀请其他人填写
            </el-button>
          </div>
        </el-card>
      </div>
      <!-- 互助弹窗 -->
      <el-dialog v-model="state.taskVisible" :close-on-click-modal="false" title="发布互助任务"
                 :before-close="helpDialogCancelClose">
        <el-form v-model="state.task">
          <el-form-item label="积分">
            <el-input-number v-model="state.task.price" :min="1" :max="99"/>
          </el-form-item>
          <el-form-item label="预期数量">
            <el-input-number v-model="state.task.amount" :min="1" :max="1000"/>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="helpDialogCancelClose">取消</el-button>
            <el-button type="primary" @click="helpDialogConfirmClose">确认</el-button>
          </span>
        </template>
      </el-dialog>
      <!-- 互助邀请弹窗 -->
      <el-dialog v-model="state.inviteVisible" :close-on-click-modal="false" title="发布互助任务"
                 :before-close="inviteDialogCancelClose">
        <el-form v-model="state.invite">
          <el-form-item label="用户名">
            <el-input v-model="state.invite.username"/>
          </el-form-item>
          <el-form-item label="Email">
            <el-input v-model="state.invite.email"/>
          </el-form-item>
          <el-form-item label="电话号码">
            <el-input v-model="state.invite.phone"/>
          </el-form-item>
          <el-form-item label="学生学号">
            <el-input v-model="state.invite.studentId"/>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="inviteDialogCancelClose">取消</el-button>
            <el-button type="primary" @click="inviteDialogConfirmClose">确认发送</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
    <div class="flex justify-center items-center" v-else>
      <el-empty description="无问卷信息"/>
    </div>
  </div>
</template>

<script setup name="Share">
import QrCode from '@/components/QrCode/index.vue';
import Clipboard from 'clipboard'
import {useRoute, useRouter} from "vue-router";
import {computed, onMounted, reactive, ref} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import {getQuestionnaire, modifyQuestionnaire} from "@/api/question/question";
import {create, deleteTask, existTask, invite} from "@/api/question/help-center";

const route = useRoute();
const router = useRouter();

function disableDate(date) {
  return date <= new Date(Date.now())
}

const state = reactive({
  id: '',
  loading: false,
  question: {
    "questionnaireId": "",
    "title": "",
    "introduction": "",
    "structure": null,
    "userId": 0,
    "createTime": null,
    "dueTime": '',
    "questionnaireStatus": "0",
    "delFlag": "0",
    "starFlag": "0",
    "modifyFlag": "0",
    "anonymousFlag": "0"
  },
  taskVisible: false,
  inviteVisible: false,
  invite: {
    username: '',
    email: '',
    phone: '',
    studentId: '',
  },
  task: {
    questionnaireId: '',
    price: 0,
    amount: 0,
  }
})

const shareUrl = computed(() => {
  return import.meta.env.VITE_APP_SHARE_URL + state.id
})

function backToNew(msg) {
  ElMessageBox.alert(msg || '未知的问卷信息', '404', {
    confirmButtonText: '去创建问卷',
    callback: (action) => {
      router.replace({
        path: '/questionnaire/new'
      })
    },
  })
}

function helpDialogCancelClose() {
  state.taskVisible = false
}

function inviteDialogCancelClose() {
  state.inviteVisible = false
}

function helpDialogConfirmClose() {
  create(state.task).then(response => {
    if (response.err === undefined) {
      ElMessage({
        message: '发布成功',
        type: 'success'
      })
    } else {
      ElMessage({
        message: response.msg,
        type: 'error'
      })
    }
    state.taskVisible = false
  })
}

onMounted(() => {
  console.log("获取到的参数: ", route.query.id);
  if (!route.query.id || route.query.id === '') {
    backToNew()
  }
  state.id = route.query.id;
  getQuestionnaireData()
})

const getQuestionnaireData = () => {
  getQuestionnaire(state.id).then(response => {
    if (response.err !== undefined) {
      backToNew(response.msg)
      return
    }
    state.question = response.question
    if (state.question.delFlag && state.question.delFlag !== '0') {
      backToNew('问卷已被删除')
    }
    state.loading = true;
  }).catch(err => {
    backToNew()
  })
}

function gotoData() {
  router.push({
    path: '/designer/collections',
    query: {
      'id': state.id
    }
  })
}

function gotoDesign() {
  router.push({
    path: '/designer/index',
    query: {
      'id': state.id
    }
  })
}

function handleSettingsDialogConfirmClose() {
  state.settingsDialogVisible = false;
  if (state.question.dueTime !== '') {
    state.question.dueTime = state.question.dueTime.toString()
  }
  state.question.starFlag = state.question.starFlag ? '1' : '0'
  state.question.anonymousFlag = state.question.anonymousFlag ? '1' : '0'
  state.question.modifyFlag = state.question.modifyFlag ? '1' : '0'
  console.log("Confirm", state.question)
  modifyQuestionnaire(state.question).then(response => {
    ElMessage({
      type: 'success',
      message: '问卷信息设置成功！',
    })
  })
}

function handleInviteDialogConfirmClose() {
  existTask(state.question.questionnaireId).then(response => {
    console.log(response)
    if (response.exist === false) {
      ElMessage({
        message: '互助任务未创建',
        type: 'error'
      })
    } else {
      state.invite = {
        username: '',
        email: '',
        phone: '',
        studentId: '',
      }
      state.inviteVisible = true;
    }
  })
}

function inviteDialogConfirmClose() {
  state.inviteVisible = false
  let inviteData = {}
  if (state.invite.email !== '') {
    inviteData.email = state.invite.email
  }
  if (state.invite.username !== '') {
    inviteData.username = state.invite.username
  }
  if (state.invite.phone !== '') {
    inviteData.phone = state.invite.phone
  }
  if (state.invite.studentId !== '') {
    inviteData.studentId = state.invite.studentId
  }
  existTask(state.question.questionnaireId).then(response => {
    console.log(response)
    if (response.exist === false) {
      ElMessage({
        message: '互助任务未创建',
        type: 'error'
      })
    } else {
      let taskId = response.taskId
      invite(taskId, inviteData).then(rsp => {
        console.log(rsp)
        if (rsp.err === undefined) {
          ElMessage({
            message: '任务邀请成功',
            type: 'success'
          })
        } else {
          ElMessage({
            message: '任务未成功邀请',
            type: 'warning'
          })
        }
      })
    }
  })
}

function makeTask() {
  existTask(state.question.questionnaireId).then(response => {
    console.log(response)
    if (response.exist === false) {
      state.task.questionnaireId = state.question.questionnaireId
      state.taskVisible = true
    } else {
      ElMessageBox.alert('互助任务已存在，是否删除', '互助任务已存在', {
        confirmButtonText: '好',
        callback: (action) => {
          if ('confirm' === action) {
            deleteTask(response.taskId).then(rsp => {
              if (rsp.err === undefined) {
                ElMessage({
                  message: '删除成功',
                  type: 'success'
                })
              } else {
                ElMessage({
                  message: rsp.msg,
                  type: 'error'
                })
              }
            })
          }
        },
      })
    }
  })
}

const copy = () => {
  var clipboard = new Clipboard('.share-link')
  clipboard.on('success', e => {
    ElMessage.success("复制成功")
    //  释放内存
    clipboard.destroy()
  })
  clipboard.on('error', e => {
    // 不支持复制
    ElMessage.error("该浏览器不支持复制")
    // 释放内存
    clipboard.destroy()
  })
}

const stopCollect = () => {
  state.question.questionnaireStatus = "2"
  modifyQuestionnaire(state.question).then(res => {
    ElMessage({
      type: 'success',
      message: '问卷暂停收集',
    })
    getQuestionnaireData()
  })
}

const startCollect = () => {
  state.question.questionnaireStatus = "1"
  modifyQuestionnaire(state.question).then(res => {
    ElMessage({
      type: 'success',
      message: '问卷开始收集',
    })
    getQuestionnaireData()
  })
}
</script>

<style lang="scss" scoped>
.labelstyle {
  font-family: SourceHanSansCN-Regular;
  color: #1A1A1A;
}

.stop {
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  margin-left: auto;
  margin-right: auto;
  border-radius: 10px;
  background: #FAC9C5;
}

.state {
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  margin: auto;
  border-radius: 5px;
  background: #DCF9C6;
  border: 2px solid #0F7026;
}

.release {
  border-radius: 10px;
  background: #FAE9C5;
}

.download {
  border-radius: 10px;
  background: #E2FFC1;
}

.copyurl {
  border-radius: 10px;
  background: #E0E9FA;
}

.title {
  font-family: SourceHanSansCN-Bold;
  font-weight: bold;
  color: #3D3D3D;
}

.display {
  text-align: center;
  padding: 20px;
}

.display .box-card {
  text-align: left;
  margin-left: 17%;
  margin-right: 17%;
}

.tag span {
  padding: 0 57px;
  height: 40px;
}
</style>
