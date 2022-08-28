<template>
  <div class="topPicks w-10/12 left-14 h-1/2 top-16">
    <div class="ml-1 md:ml-16">热门精选
      <button>
        <el-icon style="position:relative;top:2px;left:5px;color: grey;" size="18px">
          <RefreshLeft/>
        </el-icon>
      </button>
    </div>
    <ul class="flex mx-10 md:mx-24 justify-center">
      <general-item v-for="(questionNaire, index) in myNaires.slice(0,4)" :key="questionNaire.id"
                    :theList="questionNaire" :index="index" :enable="false"></general-item>
    </ul>
    <!-- <el-button class="scroll">></el-button> -->
  </div>
</template>

<script>
import GeneralItem from './generalItem.vue';

export default {
  components: {GeneralItem},
  name: 'topPicks',
  setup() {
    let myNaires = [

      {
        "amount": 64,
        "questionnaire": {
          "title": "诚信资助教育知识问卷",
          "introduction": "积极宣传推广各项国家资助政策，普及银行征信、金融等相关知识，增强学生的诚信意识，引导学生诚实守信，学会感恩，励志前行",
        },
        "questionnaireId": "001",
        "taskId": "",
      }, {
        "amount": 64,
        "questionnaire": {
          "title": "诚信资助教育知识问卷",
          "introduction": "积极宣传推广各项国家资助政策，普及银行征信、金融等相关知识，增强学生的诚信意识，引导学生诚实守信，学会感恩，励志前行",
        },
        "questionnaireId": "002",
        "taskId": "",
      }, {
        "amount": 64,
        "questionnaire": {
          "title": "诚信资助教育知识问卷",
          "introduction": "积极宣传推广各项国家资助政策，普及银行征信、金融等相关知识，增强学生的诚信意识，引导学生诚实守信，学会感恩，励志前行",
        },
        "questionnaireId": "003",
        "taskId": "",
      }, {
        "amount": 64,
        "questionnaire": {
          "title": "诚信资助教育知识问卷",
          "introduction": "积极宣传推广各项国家资助政策，普及银行征信、金融等相关知识，增强学生的诚信意识，引导学生诚实守信，学会感恩，励志前行",
        },
        "questionnaireId": "004",
        "taskId": "",
      }
    ];
    return {
      myNaires
    }
  }
}
</script>

<style lang="scss" scoped>
.topPicks {
  position: relative;
  left: 4%;
  margin-top: 20px;
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
//     right: 30px;
//     width: 56px;
//     height: 250px;
//     border-radius: 10px;
//     background: #E7E7E7;
//     font-family: KaiTi;
//     font-size: 60px;
//     font-weight: bold;
//     color: #979595;
// }
ul {
  // margin-left: 7%;
  // width: 2000px;
  list-style-type: none;
}

</style>
