<template>
  <div>
    <div class="title">问卷列表</div>
    <div class="example-block">
      <el-select v-model="value" size="large">
        <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"/>
      </el-select>
    </div>
    <div class="nairelist">
      <div v-if="value === 'designer'">
        <el-table :data="designer" border stripe style="width: 100%">
          <el-table-column header-align="center" align="left" prop="title" label="问卷题目"/>
          <el-table-column header-align="center" align="left" prop="introduction" label="问卷简介"/>
          <el-table-column align="center" label="问卷状态" width="100">
            <template #default="scope">
              <el-tag v-if="scope.row.questionnaireStatus === '1'" type="success">收集中</el-tag>
              <el-tag v-if="scope.row.questionnaireStatus === '0'" type="warning">未发布</el-tag>
              <el-tag v-if="scope.row.questionnaireStatus === '2'" type="info">已结束</el-tag>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="createTime" label="创建时间" width="100"/>
          <el-table-column align="center" prop="anonymousFlag" label="匿名答题" width="100">
            <template #default="scope">
              <el-tag v-if="scope.row.anonymousFlag === '1'" type="success">可匿名</el-tag>
              <el-tag v-else type="danger">不可匿名</el-tag>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作">
            <template #default="scope">
              <el-button type="text" size="small" @click="goEdit(scope.row.questionnaireId)">编辑</el-button>
              <el-button type="text" size="small" @click="goShare(scope.row.questionnaireId)">发布</el-button>
              <el-button type="text" size="small" @click="goCollections(scope.row.questionnaireId)">数据</el-button>
              <el-button type="text" size="small" @click="del(scope.row.questionnaireId)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div v-else>
        <el-table :data="user" border stripe style="width: 100%">
          <el-table-column header-align="center" align="left" prop="detail" label="问卷题目"/>
          <el-table-column align="center" label="问卷状态" width="100">
            <template #default="scope">
              <el-tag v-if="scope.row.answerStatus === '0'" type="success">有效</el-tag>
              <el-tag v-if="scope.row.answerStatus === '1'" type="warning">无效</el-tag>
              <el-tag v-if="scope.row.answerStatus === '2'" type="info">邀请</el-tag>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="fillIp" label="填写IP" width="100"/>
          <el-table-column align="center" prop="createTime" label="完成时间" width="100"/>
          <el-table-column align="center" label="操作">
            <template #default="scope">
              <el-button type="text" size="small">编辑</el-button>
              <el-button class="share-link" type="text" size="small"
                         :data-clipboard-text="shareUrl + scope.row.questionnaireId" @click="copy">分享
              </el-button>
              <el-button type="text" size="small"><a :href="shareUrl + scope.row.questionnaireId"
                                                     target="_black">再填一份</a></el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script setup name="Star">
import {ref} from 'vue'
import Clipboard from 'clipboard'
import {listUserQuestionnaire} from '@/api/question/question';
import {listUserAnswers} from '@/api/question/answer';

const router = useRouter()
const store = useStore()
let value = ref('designer')
let designer = ref()
let user = ref()
const shareUrl = import.meta.env.VITE_APP_SHARE_URL
const options = [
  {
    value: 'designer',
    label: '问卷设计者',
  },
  {
    value: 'user',
    label: '问卷填写者',
  },
]

onActivated(() => {
  getDesigner()
  getUser()
})


const getDesigner = () => {
  listUserQuestionnaire(store.getters.userId, "star").then(res => {
    designer.value = res.data
  })
}

const getUser = () => {
  listUserAnswers(store.getters.userId, "star").then(res => {
    user.value = res.data
  })
}


const del = (id) => {
  ElMessageBox.alert('确定要删除问卷吗？', '删除提示', {
    confirmButtonText: '确定',
    callback: (action) => {
      deleteQuestionnaire(id).then(res => {
        if (res.code === 200) {
          getDesigner()
          ElMessage.success(res.msg)
        } else {
          ElMessage.error(res.msg)
        }
      })
    },
  })
}

const goEdit = (id) => {
  router.push("/designer/index?id=" + id)
}
const goShare = (id) => {
  router.push("/designer/share?id=" + id)
}
const goCollections = (id) => {
  router.push("/designer/collections?id=" + id)
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
</script>

<style scoped>
.title {
  position: absolute;
  top: 30px;
  left: 5%;
  font-family: SourceHanSansCN-Bold;
  font-size: 20px;
  font-weight: bold;
  text-align: center;
  letter-spacing: 0em;
  color: #3D3D3D;
}

.example-block {
  position: absolute;
  top: 30px;
  right: 5%;
}

.nairelist {
  position: absolute;
  top: 75px;
  left: 5%;
  right: 5%;
  border-radius: 5px;
  background: #EEEEEE;
}

.unpublished {
  position: relative;
  left: 20%;
  width: 60%;
  color: #F7A500;
  border-radius: 5px;
  background: #F9EDC6;
  border: 2px solid #A17704;
  text-align: center;
}

.ongoing {
  position: relative;
  left: 20%;
  width: 60%;
  color: #169E4C;
  border-radius: 5px;
  background: #DCF9C6;
  border: 2px solid #0F7026;
  text-align: center;
}

.done {
  position: relative;
  left: 20%;
  width: 60%;
  color: #5E5E5E;
  border-radius: 5px;
  background: #D0D0D0;
  border: 2px solid #313131;
  text-align: center;
}
</style>
