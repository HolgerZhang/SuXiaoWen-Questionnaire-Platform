<template>
  <div>
    <div
        class="fixed z-10 w-1/5 h-full display bg-gray-50 border-gray-600 display flex-col justify-center items-center">
      <div class="relative mx-1 md:mx-4 bg-gray-200 text-sm md:text-lg h-9 align-middle rounded-md py-2 mt-5 md:py-1">
        数据筛选
      </div>
      <el-select v-model="type" class="m-2" placeholder="筛选类型">
        <el-option v-for="item in types" :key="item.value" :label="item.label" :value="item.value"/>
      </el-select>
      <el-button type="primary" @click="getQueryAnswers">筛选</el-button>
      <div class="relative mx-1 md:mx-4 text-xs md:text-base rounded-md py-2 md:py-1 my-3 px-2">筛选符合以下条件的数据
        <button class="relative top-1 text-blue-700" @click="add">
          <el-icon>
            <CirclePlus/>
          </el-icon>
        </button>
      </div>
      <el-scrollbar height="520px">
        <div v-for="cd in tobind" :key="cd"
             class="relative mx-1 md:mx-4 bg-gray-200 text-xs md:text-base py-2 md:py-1 my-3 px-2 card">
          <el-select v-model="cd.questionId" class="float-left mx-1 mt-3 mb-2" placeholder="选择题目">
            <el-option v-for="(item, index) in questions" :key="item.value" :label="index + 1 + '.' + item.title"
                       :value="item.id"/>
          </el-select>
          <el-select v-model="cd.detail" class="float-left mx-1 my-2" placeholder="选择过滤规则">
            <el-option v-for="item in rules" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
          <el-input v-model="cd.text" placeholder="请输入具体规则" class="mx-1 mt-2 mb-3" clearable/>
          <button class="absolute right-2 top-2 text-red-700" @click="onDelete">
            <el-icon>
              <CircleClose/>
            </el-icon>
          </button>
        </div>
      </el-scrollbar>
    </div>
    <div class="display flex-col bg-gray-100 justify-center items-center relative w-4/5 float-right ">
      <el-card v-for="(qs, index) in queryData" :key="qs.id" class="box-card w-5/6 h-80"
               style="border-radius: 1rem">
        <div class="font-semibold text-base md:text-lg">
          {{ index + 1 }}. {{ qs.title }}
        </div>
        <div v-if="qs.type !== 'text'">
          <div class="relative float-right -top-9 w-1/6">
            <el-select v-model="showway[index]" class="m-2" placeholder="Select" @change="handleChange">
              <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"/>
            </el-select>
          </div>
          <div class="relative -top-4" v-if="showway[index] === '1'">
            <el-table :data="qs.information" stripe style="width: 100%">
              <el-table-column prop="title" label="选项" class="w-1/2"/>
              <el-table-column prop="num" label="选择人数" class="w-1/2"/>
            </el-table>
          </div>
          <div v-else-if="showway[index] === '2'" class="relative w-5/6">
            <myhistogram :index="index" :qs="qs"/>
          </div>
          <div v-else class="relative w-5/6">
            <piechart :index="index" :qs="qs"/>
          </div>
        </div>
        <div v-else>
          <el-scrollbar class="m-5" height="200px">
            <li v-for="(item, i) in qs.information">{{
                item
              }}
            </li>
          </el-scrollbar>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup name="Collections">
import {onActivated, reactive, ref} from 'vue-demi';
import {queryAnswers} from '@/api/question/answer';
import myhistogram from './myhistogram.vue';
import piechart from './piechart.vue';
import {getQuestionnaireDetail} from '@/api/question/question';
import {answerDetailDoc} from '@/api/question/answer';
import {useRoute} from "vue-router";

const route = useRoute()
let id = route.query.id
let type = ref("all")

let tobind = ref([
  {
    questionId: null,
    detail: null,
    text: null
  }
])
const showway = ref(['1', '1', '1'])
let questions = ref()
let datacollected = ref()
let queryData = ref()
const handleChange = (value) => {
  console.log(value);
}
const types = [
  {
    value: "all",
    label: "全部满足"
  },
  {
    value: "any",
    label: "满足任一"
  },
]
const options = [
  {
    value: '1',
    label: '表格',
  },
  {
    value: '2',
    label: '柱状图',
  },
  {
    value: '3',
    label: '饼状图',
  },
]

const rules = [
  {
    value: 'C',
    label: '题目答案中包含指定文本',
  },
  {
    value: 'T',
    label: '题目已填写',
  }
]

const add = () => {
  tobind.value.push(
      {
        questionId: null,
        detail: null,
        text: null
      })
}
const onDelete = () => {
  tobind.value.pop()
};

const format = () => {
  let obj = {}
  for (const v of tobind.value) {
    if (v.questionId != null) {
      obj[v.questionId] = v.detail !== 'T' ? v.detail + " " + v.text : 'T'
    }
  }
  return obj
}


const getQueryAnswers = () => {
  queryAnswers(id, format(), type.value).then(res => {
    console.log(res)
    queryData.value = res.data
  })
}

const getQuestionnaireData = () => {
  getQuestionnaireDetail(id).then(res => {
    questions.value = res.structure.questions
    datacollected.value = res.structure.questions
    console.log(datacollected)
  })
}


onActivated(() => {
  getQuestionnaireData()
  getQueryAnswers()
})
</script>
<style lang="scss" scoped>
.card {
  border-radius: 10px;
  box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
}

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
