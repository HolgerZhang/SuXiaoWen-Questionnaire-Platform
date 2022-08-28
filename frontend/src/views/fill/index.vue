<template>
  <div>
    <div class="display flex-col justify-center items-center" v-if="state.available">
      <div style="position: absolute; top: 20px; right: 20px">
        <el-popover placement="left" trigger="click">
          <template #reference>
            <el-button>分享</el-button>
          </template>
          <qr-code :url="route.params.id"></qr-code>
        </el-popover>
      </div>
      <div class="content">
        <h1 class="flex justify-center items-center mb-5 mt-5 text-2xl">{{ state.question.title }}</h1>
        <div class="top flex justify-center items-center" v-if="state.question.introduction !== ''">
          {{ state.question.introduction }}
        </div>
        <section>
          <el-card class="box-card" style="border-radius: 1rem" v-for="(item, index) in state.structure.questions">
            <div slot="header" class="clearfix">
              <div class="text-xl">
                <!--显示必填标识-->
                <span style="color: #F56C6C;">
                  <span v-if="item.required">*</span>
                  <span v-else>&nbsp;</span>
                </span>
                {{ (index + 1) + '. ' + item.title }}
              </div>
              <div class="ml-8 text-gray-600 text-sm mb-1">{{ item.description }}</div>
            </div>

            <!--单选题展示-->
            <div class="" v-if="item.type === 'radio'" v-for="choice in item.information">
              <el-radio v-model="state.answer[item.id]" :label="choice" style="margin: 5px;">{{
                  choice
                }}
              </el-radio>
            </div>

            <!--多选题展示-->
            <el-checkbox-group v-if="item.type === 'checkbox'" v-model="state.answer[item.id]">
              <div class="text item" v-for="choice in item.information">
                <el-checkbox :label="choice" style="margin: 5px;">{{ choice }}</el-checkbox>
              </div>
            </el-checkbox-group>

            <!--填空题展示-->
            <div class="mt-2 mb-2">
              <el-input v-if="item.type === 'text'" clearable v-model="state.answer[item.id]" resize="none"></el-input>
            </div>
          </el-card>
          <el-button type="primary" style="margin: 5px;" @click="submit" :loading="state.submitLoading">提交</el-button>

        </section>
      </div>
    </div>
    <div class="flex justify-center items-center" v-else>
      <el-empty description="无问卷信息"/>
    </div>
    <div class="flex justify-center items-center m-3 p-2">
      <el-link class="flex justify-center items-center" type="info" href="/index">苏小问&nbsp;提供技术支持</el-link>
    </div>
  </div>
</template>

<script setup name="Fill">
import {reactive} from "vue";
import {useRoute, useRouter} from "vue-router";
import {onMounted} from "vue";
import {ElMessageBox, ElNotification} from "element-plus";
import {getQuestionnaireAnonymous, getQuestionnaire, getQuestionnaireDetail} from "@/api/question/question";
import QrCode from "@/components/QrCode";
import {submitAnswerAnonymous, submitAnswer} from "@/api/question/answer";
import {getToken} from "@/utils/auth";
import {finishByQuestionId} from "@/api/question/help-center";

const route = useRoute();
const router = useRouter();
const state = reactive({
  submitLoading: false,
  available: false,
  id: '',
  answer: {},
  question: {},
  structure: {
    "id": "",
    "questions": [],
    "logics": []
  }
})

function handelRequestError(rsp) {
  if (rsp.err === 401) {
    ElMessageBox.alert('问卷需要您登录后进一步操作，即将转向登录页面', '需要认证', {
      confirmButtonText: '好',
      callback: (action) => {
        router.replace({
          path: '/account',
          query: {
            action: "login",
            redirect: '/fill/' + state.id
          }
        })
      }
    })
  } else if (rsp.err === 404) (
      ElMessageBox.alert('问卷不可用，请检查URL！', '问卷不可用', {
        confirmButtonText: '返回首页',
        callback: (action) => {
          router.replace({
            path: '/welcome',
          })
        }
      })
  )
  else if (rsp.err === 403) {
    ElMessageBox.alert(rsp.msg, '遇到错误', {
      confirmButtonText: '返回首页',
      callback: (action) => {
        router.replace({
          path: '/welcome'
        })
      }
    })
  }
}

onMounted(() => {
  console.log("获取到的参数: ", route.params.id);
  if (!route.params.id || route.params.id.trim() === '') {
    ElMessageBox.alert('未知的问卷信息', '404', {
      confirmButtonText: '返回首页',
      callback: (action) => {
        router.replace({
          path: '/welcome'
        })
      },
    })
  } else {
    state.id = route.params.id;
    if (getToken()) {
      getQuestionnaire(state.id).then(response => {
        console.log(response)
        if (response.err !== undefined) {
          handelRequestError(response)
          return
        }
        state.question = response.question
        if (questionAvailable(state.question)) {
          handelRequestError({err: 404})
          return
        }
        getQuestionnaireDetail(state.id).then(response => {
          if (response.err !== undefined) {
            handelRequestError(response)
            return
          }
          state.structure = response.structure
          state.structure.questions.sort((a, b) => {
            return a.index - b.index
          })
          state.available = true
        })
      }).catch(err => {
        handelRequestError({err: 403})
      })
    } else {
      getQuestionnaireAnonymous(state.id).then(response => {
        console.log(response)
        if (response.err !== undefined) {
          handelRequestError(response)
          return
        }
        state.available = true
        state.question = response.question
        state.structure = response.structure
        state.structure.questions.sort((a, b) => {
          return a.index - b.index
        })
        console.log(state)
        if (!questionAnonymous(state.question)) {
          handelRequestError({err: 401})
        } else if (questionAvailable(state.question)) {
          handelRequestError({err: 404})
        }
      }).catch(err => {
        handelRequestError({err: 403})
      })
    }
  }
});

function questionAnonymous(question) {
  if (getToken()) {  //已登录，可填写
    return true
  }
  return question.anonymousFlag && question.anonymousFlag === '1'
}

function questionAvailable(question) {
  return question.delFlag && question.delFlag === '1';
}

function submit() {
  // console.log(state.answer)
  for (let i in state.structure.questions) {
    if (state.structure.questions[i].required === true) {
      let check = state.answer[state.structure.questions[i].id]
      // console.log(typeof check)
      if (check === undefined) {
        ElNotification({
          title: '请检查答卷',
          message: '有必答题未作答',
          type: 'error',
        })
        return
      } else {
        if (typeof check === "object") {
          state.answer[state.structure.questions[i].id] = check.join("|")
        }
      }
    }
  }
  console.log(">>>", state.answer)
  if (getToken()) {
    submitAnswer(state.question.questionnaireId, state.answer).then(response => {
      console.log(response)
      if (response.err !== undefined) {
        handelRequestError(response)
      } else {
        finishByQuestionId(state.question.questionnaireId).then(rsp => {
          console.log(3, rsp)
          if (response.err === undefined) {
            ElMessageBox.alert('问卷成功提交！' + rsp.msg || '' + ' 感谢您的参与，即将返回主页！', '问卷成功提交', {
              confirmButtonText: '好',
              callback: (action) => {
                router.replace({
                  path: '/welcome'
                })
              }
            })
          } else {
            ElMessageBox.alert('问卷成功提交，感谢您的参与，即将返回主页！', '问卷成功提交', {
              confirmButtonText: '好',
              callback: (action) => {
                router.replace({
                  path: '/welcome'
                })
              }
            })
          }
        })
      }
    })
  } else {
    submitAnswerAnonymous(state.question.questionnaireId, state.answer).then(response => {
      if (response.err !== undefined) {
        handelRequestError(response)
      } else {
        ElMessageBox.alert('问卷成功提交，感谢您的参与，即将返回主页！', '问卷成功提交', {
          confirmButtonText: '好',
          callback: (action) => {
            router.replace({
              path: '/welcome'
            })
          }
        })
      }
    }).catch(err => {
      ElNotification({
        title: '问卷提交失败',
        message: err,
        type: 'error',
      })
    })
  }
}

</script>

<style lang="scss" scoped>
.display {
  text-align: center;
  padding: 20px;
}

.display .top {
  color: #606266;
  padding: 0 10px 10px 10px;
  border-bottom: 3px solid #409EFF;
  font-size: 15px;
  line-height: 22px;
  text-align: left;
}

.display .content {
  width: 100%;
  max-width: 800px;
  display: inline-block;
  text-align: center;
}

.display .box-card {
  text-align: left;
  width: 100%;
  margin: 20px 0 20px 0;
  padding: 10px;
}

.display .bottom {
  margin: 20px 10px 20px 10px;
  color: #909399;
}

.display a:link,
a:visited,
a:active {
  color: #909399;
  text-decoration: none;
}
</style>
