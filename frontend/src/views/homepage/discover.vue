<template>
  <div class="w-10/12 absolute discover">
    <span style="margin:10px">问卷发现</span>
    <button @click="gotoHelp">
      <el-icon style="position:relative;top:2px" size="18px">
        <MoreFilled/>
      </el-icon>
    </button>
    <br>
    <div v-if="tasks.length > 0">
      <el-row class="flex mx-10 md:mx-24 justify-center p-5">
        <general-item class="mr-5 ml-5" v-for="(questionNaire, index) in tasks" :key="questionNaire.taskId"
                      :theList="questionNaire"
                      :index="index" :enable="true"/>
      </el-row>
    </div>
    <div v-else>
      <el-empty class="w-full mx-10 md:mx-20" description="系统暂无互助问卷"/>
    </div>
  </div>
</template>

<script setup>
import generalItem from "@/views/help/generalItem";
import {useRouter} from "vue-router";
import {onActivated} from "vue";
import {list} from "@/api/question/help-center";

const router = useRouter();

function gotoHelp() {
  router.push({
    path: '/help'
  })
}

onActivated(() => {
  getHelp()
})

function getHelp() {
  list().then(res => {
    tasks.value = res.data.splice(0, 2)
  })
}

let tasks = ref([])

// let myNaires = [{
//   id: '001',
//   title: '诚信资助教育知识问卷',
//   introduction: '积极宣传推广各项国家资助政策，普及银行征信、金融等相关知识，增强学生的诚信意识，引导学生诚实守信，学会感恩，励志前行',
//   amount: 64
// },
//   {
//     id: '002',
//     title: '诚信资助教育知识问卷',
//     introduction: '积极宣传推广各项国家资助政策，普及银行征信、金融等相关知识，增强学生的诚信意识，引导学生诚实守信，学会感恩，励志前行',
//     amount: 64
//   },
//   {
//     id: '003',
//     title: '诚信资助教育知识问卷',
//     introduction: '积极宣传推广各项国家资助政策，普及银行征信、金融等相关知识，增强学生的诚信意识，引导学生诚实守信，学会感恩，励志前行',
//     amount: 64
//   },
//   {
//     id: '004',
//     title: '诚信资助教育知识问卷',
//     introduction: '积极宣传推广各项国家资助政策，普及银行征信、金融等相关知识，增强学生的诚信意识，引导学生诚实守信，学会感恩，励志前行',
//     amount: 64
//   }
// ];
</script>

<style lang="scss" scoped>
.discover {
  left: 5%;
  top: 280px;
  height: 508px;
  font-family: YouSheBiaoTiHei;
  font-size: 20px;
  font-weight: normal;
  letter-spacing: 0em;
  color: #3D3D3D;
}

</style>
