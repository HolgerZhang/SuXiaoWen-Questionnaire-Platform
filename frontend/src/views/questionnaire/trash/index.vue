<template>
  <div>
    <div class="title">问卷列表</div>
    <div class="target">
      <el-icon style="top: 3px;left: -10px;">
        <Folder/>
      </el-icon>
      问卷设计者
    </div>
    <div class="nairelist">
      <el-table :data="deleted" border stripe style="width: 100%">
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
        <el-table-column align="center" prop="starFlag" label="星标" width="80">
          <template #default="scope">
            <el-icon v-if="scope.row.starFlag === '1'" color="#E6A23C" :size="20">
              <StarFilled/>
            </el-icon>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作">
          <template #default="scope">
            <el-button type="text" size="small" @click="clear(scope.row.questionnaireId)">清空数据</el-button>
            <el-button type="text" size="small" @click="recover(scope.row.questionnaireId)">恢复</el-button>
            <el-button type="text" size="small" @click="del(scope.row.questionnaireId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup name="Trash">
import {ElMessage, ElMessageBox} from 'element-plus';
import {
  listUserQuestionnaire,
  recoverQuestionnaire,
  clearQuestionnaireData,
  deleteQuestionnaireCompletely
} from "@/api/question/question"
import {onActivated, onMounted} from 'vue';
import {useStore} from "vuex";


const store = useStore()
const deleted = ref()
const getDeleteList = () => {
  listUserQuestionnaire(store.getters.userId, "delete").then(res => {
    deleted.value = res.data
  })
}

onActivated(() => {
  getDeleteList()
})

const recover = (id) => {
  ElMessageBox.alert('确定要恢复问卷吗？', '恢复提示', {
    confirmButtonText: '确定',
    callback: (action) => {
      recoverQuestionnaire(id).then(res => {
        if (res.code === 200) {
          getDeleteList()
          ElMessage.success(res.msg)
        } else {
          ElMessage.error(res.msg)
        }
      })
    },
  })
}
const clear = (id) => {
  ElMessageBox.alert('确定要清空问卷吗？', '清空提示', {
    confirmButtonText: '确定',
    callback: (action) => {
      clearQuestionnaireData(id).then(res => {
        if (res.code === 200) {
          getDeleteList()
          ElMessage.success(res.msg)
        } else {
          ElMessage.error(res.msg)
        }
      })
    },
  })
}

const del = (id) => {
  ElMessageBox.alert('确定要删除问卷吗？', '删除提示', {
    confirmButtonText: '确定',
    callback: (action) => {
      deleteQuestionnaireCompletely(id).then(res => {
        if (res.code === 200) {
          getDeleteList()
          ElMessage.success(res.msg)
        } else {
          ElMessage.error(res.msg)
        }
      })
    },
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

.nairelist {
  position: absolute;
  top: 75px;
  left: 5%;
  right: 5%;
  border-radius: 5px;
}

.trash {
  position: relative;
  left: 20%;
  width: 60%;
  color: #169E4C;
  border-radius: 5px;
  background: #DCF9C6;
  border: 2px solid #0F7026;
  text-align: center;
}

.target {
  position: absolute;
  top: 30px;
  right: 5%;
  border: 1px solid #dbdcdb;
  border-radius: 3px;
  width: 218px;
  height: 32px;
  text-align: center;
  line-height: 30px;
  color: #b6b3b3;
}
</style>
