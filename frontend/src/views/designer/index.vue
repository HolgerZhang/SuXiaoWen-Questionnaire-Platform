<template>
  <div>
    <div class="common-layout" v-if="!state.loading">
      <!--  NavBar  -->
      <div class="fixed z-20 w-full bg-white">
        <div class="flex h-14 justify-center items-center place-self-center shadow-xl">
          <div class="grid grid-cols-5 place-items-center text-lg font-bold">
            <span class="text-blue-600">编辑问卷</span>
            <el-icon>
              <ArrowRight/>
            </el-icon>
            <a @click="gotoShare">发布问卷</a>
            <el-icon>
              <ArrowRight/>
            </el-icon>
            <a @click="gotoData">数据报表</a>
          </div>
        </div>
      </div>
      <!--  Aside  -->
      <div class="fixed z-10 bg-white pt-14 p-5" style="width: 240px">
        <div class="grid grid-cols-1 gap-5 place-content-start pt-5 bg-white">
          <div class="mt-2 text-sm text-center border-2 rounded-xl p-1 bg-gray-50 font-bold">添加题目</div>
          <el-button style="margin-left: 0" @click="addQuestionInit('radio')">单项选择</el-button>
          <el-button style="margin-left: 0" @click="addQuestionInit('checkbox')">多项选择</el-button>
          <el-button style="margin-left: 0" @click="addQuestionInit('text')">结果填空</el-button>
          <div class="mt-2 text-sm text-center border-2 rounded-xl p-1 bg-gray-50 font-bold">问卷设置</div>
          <el-button style="margin-left: 0" @click="questionSettings">问卷属性</el-button>
          <el-tooltip effect="light" content="您需要主动保存问卷数据！" placement="bottom">
            <el-button type="primary" style="margin-left: 0" @click="saveQuestion">保存问卷</el-button>
          </el-tooltip>
          <div class="mt-2 text-sm text-center border-2 rounded-xl p-1 bg-gray-50 font-bold">问卷大纲</div>
          <div class="border-2 rounded-md text-sm leading-6 truncate p-2 text-gray-400">
            <el-scrollbar height="180px">
              <div v-for="(item, index) in state.structure.questions">{{
                  (index + 1) + '.【' + (item.type === 'radio' ? '单项选择' : item.type === 'text' ? '结果填空' : '多项选择') + '】' +
                  item.title
                }}
              </div>
            </el-scrollbar>
          </div>
        </div>
      </div>
      <!--  Main  -->
      <div class="pt-14" style="margin-left: 240px">
        <div class="bg-gray-100 p-5">
          <h1 class="flex justify-center items-center mb-5 mt-5 text-2xl">{{ state.question.title }}</h1>
          <div class="top flex justify-center items-center mb-5" v-if="state.question.introduction !== ''">
            {{ state.question.introduction }}
          </div>
          <el-card v-for="(item, index) in state.structure.questions" style="margin: 10px;border-radius: 1rem">
            <div slot="header" class="clearfix">
              <div style="float: right;">
                <el-tooltip effect="dark" content="修改顺序" placement="top-start">
                  <el-button style="color: #3F3F3F" type="text" @click="editIndex(index)">
                    <el-icon>
                      <Aim/>
                    </el-icon>
                  </el-button>
                </el-tooltip>
                <el-tooltip effect="dark" content="题目设置" placement="top">
                  <el-button style="color: #3F3F3F" type="text" @click="editQuestion(index)">
                    <el-icon>
                      <Setting/>
                    </el-icon>
                  </el-button>
                </el-tooltip>
                <el-tooltip effect="dark" content="删除题目" placement="top-end">
                  <el-button style="color: #F56C6C" type="text" @click="deleteQuestion(index)">
                    <el-icon>
                      <DeleteFilled/>
                    </el-icon>
                  </el-button>
                </el-tooltip>
              </div>
              <div class="text-xl">
                <!--显示必填标识-->
                <span style="color: #F56C6C;">
                  <span v-if="item.required">*&nbsp;</span>
                  <span v-else>&nbsp;&nbsp;</span>
                </span>
                <span style="color: black;margin-right: 3px;">{{ (index + 1) + '.' }}</span>
                {{ item.title }}
                <div class="ml-10 text-gray-600 text-sm mb-1">{{ item.description }}</div>
              </div>
            </div>

            <!--单选题展示-->
            <div class="" v-if="item.type === 'radio'" v-for="choice in item.information">
              <el-radio disabled :label="choice" style="margin: 5px;">{{ choice }}</el-radio>
            </div>

            <!--多选题展示-->
            <el-checkbox-group v-if="item.type === 'checkbox'">
              <div class="text item" v-for="choice in item.information">
                <el-checkbox disabled :label="choice" style="margin: 5px;">{{ choice }}</el-checkbox>
              </div>
            </el-checkbox-group>

            <!--填空题展示-->
            <div class="mt-2 mb-2">
              <el-input disabled v-if="item.type === 'text'" clearable resize="none"></el-input>
            </div>
          </el-card>
        </div>
      </div>
      <!-- 添加题目弹窗 -->
      <el-dialog v-model="state.addOrModifyDialogVisible" :close-on-click-modal="false"
                 :title="state.willAddQuestion._exec === 'add' ? '添加题目' : '修改题目'" :before-close="cancelAddQuestion">
        <el-form ref="form" :model="state.willAddQuestion" label-width="80px">
          <el-form-item label="题目标题" style="width: 100%;">
            <el-input v-model="state.willAddQuestion.title" placeholder="请输入标题"></el-input>
          </el-form-item>
          <el-form-item label="题目注释" style="width: 100%;">
            <el-input v-model="state.willAddQuestion.description" placeholder="请输入题目解释说明"></el-input>
          </el-form-item>
          <el-form-item label="是否必填" style="width: 100%;">
            <el-switch v-model="state.willAddQuestion.required"
                       style="--el-switch-on-color: #13ce66; --el-switch-off-color: #afafaf"/>
          </el-form-item>
          <template v-if="state.willAddQuestion.type === 'radio' || state.willAddQuestion.type === 'checkbox'">
            <el-table :data="state.willAddQuestion.information" style="width: 100%">
              <el-table-column prop="option" label="选项文本"/>
              <el-table-column fixed="right" label="删除">
                <template #default="scope">
                  <el-button link type="danger" size="small" @click.prevent="deleteRow(scope.$index)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-button class="mt-4" style="width: 100%" @click="onAddItem">新增选项</el-button>
          </template>
        </el-form>
        <br>
        <div style="width: 100%;text-align: right">
          <el-button style="margin-left: 10px;" @click="cancelAddQuestion">取消</el-button>
          <el-button type="primary" style="margin-left: 10px;" @click="checkAddQuestion">完成</el-button>
        </div>
      </el-dialog>
      <!-- 问卷设置弹窗 -->
      <el-dialog v-model="state.settingsDialogVisible" :close-on-click-modal="false" title="问卷设置"
                 :before-close="handleSettingsDialogCancelClose">
        <el-form v-model="state.questionToBeSetting">
          <el-form-item label="问卷标题">
            <el-input :placeholder="state.questionToBeSetting.title" v-model="state.questionToBeSetting.title"/>
          </el-form-item>
          <el-form-item label="问卷简介">
            <el-input :placeholder="state.questionToBeSetting.introduction"
                      v-model="state.questionToBeSetting.introduction"/>
          </el-form-item>
          <el-form-item label="到期时间">
            <el-date-picker v-model="state.questionToBeSetting.dueTime" type="datetime" placeholder="设置到期时间"
                            :disabled-date="disableDate" format="YYYY-MM-DD HH:mm:ss"
                            value-format="YYYY-MM-DD HH:mm:ss"/>
          </el-form-item>
          <el-form-item label="星标问卷">
            <el-switch v-model="state.questionToBeSetting.starFlag"
                       style="--el-switch-on-color: #13ce66; --el-switch-off-color: #afafaf" active-value="1"
                       inactive-value="0"/>
          </el-form-item>
          <el-form-item label="允许填写者修改数据">
            <el-switch v-model="state.questionToBeSetting.modifyFlag" active-value="1" inactive-value="0"/>
          </el-form-item>
          <el-form-item label="允许匿名用户填写问卷">
            <el-switch v-model="state.questionToBeSetting.anonymousFlag" active-value="1" inactive-value="0"/>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="handleSettingsDialogCancelClose">取消</el-button>
            <el-button type="primary" @click="handleSettingsDialogConfirmClose">确认</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
    <div class="flex justify-center items-center" v-else>
      <el-empty description="无问卷信息"/>
    </div>
  </div>
</template>

<script setup name="Designer">
import {
  getQuestionnaire,
  getQuestionnaireDetail,
  modifyQuestionnaire,
  modifyQuestionnaireStructure
} from '@/api/question/question'
import {useRoute, useRouter} from "vue-router";
import {onMounted, reactive} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";

const route = useRoute();
const router = useRouter();
const store = useStore()
const state = reactive({
  loading: false,//页面加载中
  settingsDialogVisible: false,
  addOrModifyDialogVisible: false,
  questionToBeSetting: {
    questionnaireId: "",
    title: '',
    introduction: '',
    dueTime: '',
    starFlag: '0',
    modifyFlag: "0",
    anonymousFlag: "0"
  },
  totalQuestions: 0,
  willAddQuestion: {
    _exec: -1,
    index: 0,
    type: '',
    title: '',
    description: '',
    information: [],
    required: false//是否必填
  },
  id: '',
  question: {
    "questionnaireId": "",
    "title": "",
    "introduction": "T",
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
  structure: {
    "questions": [],
    "logics": []
  }
})

function disableDate(date) {
  return date <= new Date(Date.now())
}

const deleteRow = (index) => {
  state.willAddQuestion.information.splice(index, 1)
}

const onAddItem = () => {
  ElMessageBox.prompt('请输入选项内容', '添加选项', {
    confirmButtonText: '添加',
    cancelButtonText: '取消',
  }).then(({value}) => {
    if (value.includes('|')) {
      ElMessage({
        type: 'error',
        message: '选项中不能包含|字符',
      })
    } else {
      state.willAddQuestion.information.push({
        option: value || '选项'
      })
    }
  }).catch(() => {
    ElMessage({
      type: 'info',
      message: '添加选项操作已经取消',
    })
  })
}

function editIndex(index) {
  ElMessageBox.prompt('请输入相对位置', '修改题目顺序', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /^-?\d+$/,
    inputErrorMessage: '只能输入数字',
  }).then(({value}) => {
    state.structure.questions[index].index = value
    sortQuestions()
  })
}

function editQuestion(index) {
  state.willAddQuestion.title = state.structure.questions[index].title
  state.willAddQuestion.description = state.structure.questions[index].description
  state.willAddQuestion.information = []
  for (let i = 0; i < state.structure.questions[index].information.length; i++) {
    state.willAddQuestion.information.push({option: state.structure.questions[index].information[i]})
  }
  state.willAddQuestion.type = state.structure.questions[index].type
  state.willAddQuestion._exec = index
  state.addOrModifyDialogVisible = true
}

function addQuestionInit(type) {
  state.willAddQuestion.title = '题目'
  state.willAddQuestion.description = '本题说明'
  state.willAddQuestion.information = []
  state.willAddQuestion.index = state.totalQuestions
  state.willAddQuestion.type = type
  state.willAddQuestion._exec = 'add'
  state.addOrModifyDialogVisible = true
}

function gotoShare() {
  router.push({
    path: '/designer/share',
    query: {
      'id': state.id
    }
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

function questionSettings() {
  console.log(state.question)
  state.questionToBeSetting.questionnaireId = state.question.questionnaireId
  state.questionToBeSetting.title = state.question.title
  state.questionToBeSetting.introduction = state.question.introduction
  state.questionToBeSetting.dueTime = state.question.dueTime
  state.questionToBeSetting.starFlag = state.question.starFlag
  state.questionToBeSetting.anonymousFlag = state.question.anonymousFlag
  state.questionToBeSetting.modifyFlag = state.question.modifyFlag
  state.settingsDialogVisible = true;
}

function handleSettingsDialogConfirmClose() {
  state.settingsDialogVisible = false;
  if (state.questionToBeSetting.dueTime !== '') {
    state.questionToBeSetting.dueTime = state.questionToBeSetting.dueTime.toString()
  }
  state.questionToBeSetting.starFlag = state.questionToBeSetting.starFlag ? '1' : '0'
  state.questionToBeSetting.anonymousFlag = state.questionToBeSetting.anonymousFlag ? '1' : '0'
  state.questionToBeSetting.modifyFlag = state.questionToBeSetting.modifyFlag ? '1' : '0'
  console.log("Confirm", state.questionToBeSetting)
  const setQuestion = (_) => {
    state.questionToBeSetting.questionnaireId = state.question.questionnaireId
    state.question.title = state.questionToBeSetting.title
    state.question.introduction = state.questionToBeSetting.introduction
    state.question.dueTime = state.questionToBeSetting.dueTime
    state.question.starFlag = state.questionToBeSetting.starFlag
    state.question.anonymousFlag = state.questionToBeSetting.anonymousFlag
    state.question.modifyFlag = state.questionToBeSetting.modifyFlag
  }
  modifyQuestionnaire(state.questionToBeSetting).then(setQuestion)
}

function handleSettingsDialogCancelClose() {
  state.settingsDialogVisible = false;
  console.log("Cancel", state.questionToBeSetting)
}

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

function sortQuestions() {
  state.structure.questions.sort((a, b) => {
    return a.index - b.index
  })
  for (let i = 0; i < state.totalQuestions; i++) {
    state.structure.questions[i].index = i
  }
}

onMounted(() => {
  console.log("获取到的参数: ", route.query.id);
  if (!route.query.id || route.query.id === '') {
    backToNew()
  }
  state.id = route.query.id;
  getQuestionnaire(state.id).then(response => {
    if (response.err !== undefined) {
      backToNew(response.msg)
      return
    }
    state.question = response.question
    if (state.question.delFlag && state.question.delFlag !== '0') {
      backToNew('问卷已被删除')
      return
    }
    getQuestionnaireDetail(state.id).then(response => {
      if (response.err !== undefined) {
        backToNew(response.msg)
        return
      }
      state.structure = response.structure
      state.totalQuestions = state.structure.questions.length
      sortQuestions()
      console.log(state.loading);
      state.loading = false;
    })
  }).catch(err => {
    backToNew()
  })
});

//删除问题
function deleteQuestion(index) {
  ElMessageBox.alert('确定删除此题目?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    state.structure.questions.splice(index, 1);
    state.totalQuestions -= 1
    sortQuestions()
    console.log(state.structure.questions)
  });
}

function cancelAddQuestion() {
  state.addOrModifyDialogVisible = false
}

//确认添加/保存题目
function checkAddQuestion() {
  console.log(state.structure.questions)
  let options = state.willAddQuestion.information
  state.willAddQuestion.information = []
  for (let i = 0; i < options.length; i++) {
    state.willAddQuestion.information.push(options[i].option)
  }
  if (state.willAddQuestion._exec === 'add') {
    state.structure.questions.push({
      "type": state.willAddQuestion.type,
      "title": state.willAddQuestion.title,
      "description": state.willAddQuestion.description,
      "index": state.willAddQuestion.index,
      "required": state.willAddQuestion.required,
      "information": state.willAddQuestion.information
    })
    state.totalQuestions += 1
  } else {
    let index = state.willAddQuestion._exec
    state.structure.questions[index].type = state.willAddQuestion.type
    state.structure.questions[index].title = state.willAddQuestion.title
    state.structure.questions[index].description = state.willAddQuestion.description
    state.structure.questions[index].index = state.willAddQuestion.index
    state.structure.questions[index].required = state.willAddQuestion.required
    state.structure.questions[index].information = state.willAddQuestion.information
  }
  sortQuestions()
  state.addOrModifyDialogVisible = false
  console.log(state.structure.questions)
}

function saveQuestion() {
  modifyQuestionnaireStructure(state.structure).then(response => {
    gotoShare()
  }).catch(err => {
    ElMessageBox.alert('问卷保存失败，请重试', '问卷保存失败', {
      confirmButtonText: '好',
      callback: (action) => {
      }
    })
  })
}

</script>

