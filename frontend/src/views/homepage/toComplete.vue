<template>
  <div class="absolute w-2/5 right-14 toComplete">
    <span style="float:right">待填写的</span><br>
    <ul>
      <item2 v-for="questionNaire in myNaires" :key="questionNaire.questionnaireId"
             :theList="questionNaire"></item2>
    </ul>
  </div>
</template>

<script setup name="toComplete">
import item2 from './item2.vue'
import {todo} from "@/api/question/answer";
import {onActivated} from "vue";

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

let myNaires = ref();

const getTodo = () => {
  todo().then(res => {
    console.log(1111, res)
    for (let index in res.data) {
      switch (res.data[index].questionnaire.questionnaireStatus) {
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
    myNaires.value = res.data.slice(0, 4)
    console.log(11111, myNaires)
  })
}

onActivated(() => {
  getTodo()
})

</script>

<style lang="scss" scoped>
.toComplete {
  height: 508px;
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
