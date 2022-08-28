<template>
  <div class="latest w-10/12 left-14 h-1/2">
    <div class="ml-1 md:ml-16">最新发布
      <button>
        <el-icon style="position:relative;top:2px;left:5px;color: grey;" size="18px">
          <RefreshLeft/>
        </el-icon>
      </button>
    </div>
    <ul class="flex mx-10 md:mx-24 justify-center">
      <general-item class="mr-5 ml-5" v-for="(questionNaire, index) in tasks" :key="questionNaire.taskId"
                    :theList="questionNaire" :index="index" :enable="true"></general-item>
    </ul>
    <!-- <el-button class="scroll">></el-button> -->
  </div>
</template>

<script setup name="latest">
import GeneralItem from './generalItem.vue';
import {list} from "@/api/question/help-center";
import {onActivated, ref} from "vue";

let tasks = ref()

onActivated(() => {
  getList()
  console.log(tasks)
})

const getList = () => {
  list().then(res => {
    tasks.value = res.data.splice(0, 4)
  })
}
</script>

<style lang="scss" scoped>
.latest {
  position: relative;
  left: 4%;
  margin-top: 40px;
  height: 208px;
  font-family: YouSheBiaoTiHei;
  font-size: 20px;
  font-weight: normal;
  letter-spacing: 0em;
  color: #3D3D3D;
}

// .scroll{
//     position: absolute;
//     display: block;
//     top: 29px;
//     right: -2%;
//     width: 56px;
//     height: 250px;
//     border-radius: 10px;
//     background: #E7E7E7;
//     font-family: KaiTi;
//     font-size: 200%;
//     font-weight: bold;
//     color: #979595;
// }
ul {
  list-style-type: none;
}

</style>
