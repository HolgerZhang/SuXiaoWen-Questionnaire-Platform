<script setup>
import item from './item.vue';
import {onActivated, ref} from "vue";
import {useRouter} from "vue-router";
import {listUserQuestionnaire} from "@/api/question/question";
import {useStore} from "vuex";

let myNaires = ref([
  // {questionnaireId: '001', title: 'SUMSC参观微软苏州报名表', condition: '收集中', classObj: 'ongoing'},
  // {questionnaireId: '002', title: 'SUMSC纪念品投票', condition: '未发布', classObj: 'unpublished'},
  // {questionnaireId: '003', title: 'SUMSC招新宣传表', condition: '已结束', classObj: 'done'},
  // {questionnaireId: '004', title: '个人自律打卡系统调查问卷', condition: '收集中', classObj: 'ongoing'}
]);

const conditionType = {
  ongoing: {
    text: '收集中',
    classObj: 'ongoing'
  },
  unpublished: {
    text: '未发布',
    classObj: 'unpublished'
  },
  done: {
    text: '已结束',
    classObj: 'done'
  }
}

const router = useRouter();
const store = useStore();

const getDesigner = () => {
  listUserQuestionnaire(store.getters.userId, "all").then(res => {
    for (let index in res.data) {
      switch (res.data[index].questionnaireStatus) {
        case '0':
          res.data[index].condition = conditionType.unpublished
          break
        case '1':
          res.data[index].condition = conditionType.ongoing
          break
        case '2':
          res.data[index].condition = conditionType.done
          break
      }
    }
    myNaires.value = res.data
    console.log(1, myNaires)
  })
}

function gotoAll() {
  router.push({
    path: '/questionnaire/all'
  })
}

onActivated(() => {
  getDesigner()
})

</script>
<template>
  <div class="w-2/5 absolute left-14 h-1/2">
    <span class="myRelease" style="margin:10px">我发布的</span>
    <button @click="gotoAll">
      <el-icon style="position:relative;top:2px" size="18px">
        <MoreFilled/>
      </el-icon>
    </button>
    <br>
    <ul>
      <item v-for="questionNaire in myNaires.slice(0, 4)" :key="questionNaire.questionnaireId"
            :theList="questionNaire"></item>
    </ul>
  </div>
</template>

<style scoped lang="scss">
.myRelease {
  font-family: YouSheBiaoTiHei;
  font-size: 20px;
  font-weight: normal;
  letter-spacing: 0em;
  color: #3D3D3D;
}

ul {
  margin: 10px;
}
</style>
