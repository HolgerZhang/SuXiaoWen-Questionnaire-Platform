<template>
  <div>
    <div class="display flex-col justify-center items-center"
         v-if="state.available">
      <div class="content">
        <h1 class="flex justify-center items-center mb-5 mt-5 text-2xl">{{ state.question.title }}&nbsp;答卷数据</h1>
        <div class="top flex justify-center items-center" v-if="state.question.introduction !== ''">
          {{ state.question.introduction }} | 答题时间：{{ state.answerInfo.createTime }}
        </div>
        <section class="p-10">
          <el-card class="box-card m-5" style="border-radius: 1rem" v-for="(item, index) in state.structure.questions">
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

            <div v-if="state.question.modifyFlag === '1'">
              <!--单选题展示-->
              <div class="" v-if="item.type==='radio'" v-for="choice in item.information">
                <el-radio v-model="state.answer[item.id]" :label="choice" style="margin: 5px;">{{
                    choice
                  }}
                </el-radio>
              </div>
              <!--多选题展示-->
              <el-checkbox-group v-if="item.type==='checkbox'" v-model="state.answer[item.id]">
                <div class="text item" v-for="choice in item.information">
                  <el-checkbox :label="choice" style="margin: 5px;">{{ choice }}</el-checkbox>
                </div>
              </el-checkbox-group>
              <!--填空题展示-->
              <div class="mt-2 mb-2">
                <el-input v-if="item.type==='text'" clearable v-model="state.answer[item.id]" resize="none"/>
              </div>
            </div>

            <div v-else>
              <!--单选题展示-->
              <div class="" v-if="item.type==='radio'" v-for="choice in item.information">
                <el-radio disabled v-model="state.answer[item.id]" :label="choice" style="margin: 5px;">{{
                    choice
                  }}
                </el-radio>
              </div>
              <!--多选题展示-->
              <el-checkbox-group disabled v-if="item.type==='checkbox'" v-model="state.answer[item.id]">
                <div class="text item" v-for="choice in item.information">
                  <el-checkbox disabled :label="choice" style="margin: 5px;">{{ choice }}</el-checkbox>
                </div>
              </el-checkbox-group>
              <!--填空题展示-->
              <div class="mt-2 mb-2">
                <el-input disabled v-if="item.type==='text'" clearable v-model="state.answer[item.id]" resize="none"/>
              </div>
            </div>
          </el-card>
          <div class="flex justify-items-center items-center m-5">
            <el-button class="flex justify-self-center items-center"
                       v-if="state.question.modifyFlag === '1'" type="primary" style="margin: 5px;" @click="submit"
                       :loading="state.submitLoading">
              修改答卷
            </el-button>
          </div>

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

<script setup name="Detail">
import {reactive} from "vue";
import {useRoute, useRouter} from "vue-router";
import {onMounted} from "vue";
import {ElMessageBox, ElNotification} from "element-plus";
import {getQuestionnaire, getQuestionnaireDetail} from "@/api/question/question";
import {modifyAnswer, answerDetail, answerDetailDoc, submitAnswerAnonymous, submitAnswer} from "@/api/question/answer";

const route = useRoute();
const router = useRouter();
const state = reactive({
  submitLoading: false,
  available: false,
  id: '',
  answer: {},
  answerInfo: {},
  question: {
    "questionnaireId": "DA3A764D-D890-795B-012F-DD6855DC317C",
    "title": "Test",
    "introduction": "Test for DA3A764D-D890-795B-012F-DD6855DC317C",
    "structure": null,
    "userId": 110,
    "createTime": null,
    "dueTime": null,
    "questionnaireStatus": "0",
    "delFlag": "0",
    "starFlag": "0",
    "modifyFlag": "1",
    "anonymousFlag": "1"
  },
  structure: {
    "id": "12143y2784623895734807580347",
    "questions": [
      {
        "id": "23473927483",
        "type": "radio",
        "title": "题目1",
        "description": "题目注释",
        "index": 1,
        "required": true,
        "information": ['AAAAA', 'BBBBB', 'CCCCC']
      },
      {
        "id": "23473wewer483",
        "type": "text",
        "title": "题目1",
        "description": "题目注释",
        "index": 2,
        "required": true,
        "information": []
      },
      {
        "id": "23444927484",
        "type": "checkbox",
        "title": "题目2dsfsdfsdfsdsdf",
        "description": "题目注sdfsdfsdfdsfs释",
        "index": 0,
        "required": true,
        "information": ['AAAAA', 'BBBBB', 'CCCCC', 'DDDD']
      }
    ],
    "logics": [
      {
        "id": "237498274",
        "belongs": null,
        "type": null,
        "detail": null,
        "targets": [],
        "negative": false
      }
    ]
  }
})

function handelRequestError(rsp) {
  if (rsp.err === 404) (
      ElMessageBox.alert('问卷/答卷不可用，请检查URL！', '404', {
        confirmButtonText: '返回首页',
        callback: (action) => {
          router.replace({
            path: '/welcome',
          })
        }
      })
  )
  else if (rsp.err === 403) {
    ElMessageBox.alert(rsp.msg || '未知错误，请重试', '遇到错误', {
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
  console.log("获取到的参数: ", route.params.aid);
  if (!route.params.aid || route.params.aid.trim() === '') {
    ElMessageBox.alert('未知的答卷信息', '404', {
      confirmButtonText: '返回首页',
      callback: (action) => {
        router.replace({
          path: '/welcome'
        })
      },
    })
  } else {
    state.id = route.params.aid;
    answerDetail(state.id).then(response => {
      if (response.err !== undefined) {
        handelRequestError(response)
        return
      }
      state.answerInfo = response.data
      if (!answerAvailable(state.answerInfo)) {
        handelRequestError({err: 403, msg: '答卷已被标记为无效，无法访问'})
        return
      } else if (state.answerInfo.answerStatus === '2') {
        ElMessageBox.alert('您还没有填写该问卷，即将前往填写！', '问卷待填写', {
          confirmButtonText: '好',
          callback: (action) => {
            router.replace({
              path: '/fill/' + state.answerInfo.questionnaireId
            })
          }
        })
      }
      getQuestionnaire(state.answerInfo.questionnaireId).then(response => {
        console.log(response)
        if (response.err !== undefined) {
          handelRequestError(response)
          return
        }
        state.question = response.question
        if (!questionAvailable(state.question)) {
          handelRequestError({err: 403, msg: '问卷已被锁定，不可访问！'})
          return
        }
        getQuestionnaireDetail(state.answerInfo.questionnaireId).then(response => {
          if (response.err !== undefined) {
            handelRequestError(response)
            return
          }
          state.structure = response.structure
          state.structure.questions.sort((a, b) => {
            return a.index - b.index
          })
          answerDetailDoc(state.answerInfo.detail).then(response => {
            if (response.err !== undefined) {
              // handelRequestError(response)
              return
            }
            state.answer = {}

            console.log(response.data.answers)
            for (let key in response.data.answers) {
              let ans = response.data.answers[key]
              if (ans.indexOf('|') !== -1) {
                ans = ans.split('|')
              }
              state.answer[key] = ans
            }
            state.available = true
            console.log(state.answer, state.answerInfo)
          })
        })
      })
    }).catch(err => {
      handelRequestError({err: 403})
    })
  }
})

function questionAvailable(question) {
  return question.delFlag && question.delFlag === '0';
}

function answerAvailable(answerInfo) {
  return answerInfo.answerStatus && answerInfo.answerStatus !== '1';
}

function answerModifiable(question, answerInfo) {
  return answerInfo.answerStatus !== '1' && question.modifyFlag === '1'
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
  modifyAnswer(state.answerInfo.answerId, state.answer).then(response => {
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

</script>

<style scoped>

</style>
