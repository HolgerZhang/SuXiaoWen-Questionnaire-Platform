<template>
  <div class="flex m-1">
    <div :class="index % 2 === 0 ? 'naire1' : 'naire2'" class="miniwidth">
    <span :class="index % 2 === 0 ? 'title1' : 'title2'"
          class="relative top-4 text-sm md:text-base md:top-5">{{ theList.questionnaire.title }}</span>
      <span class="introduction h-32 overflow-hidden w-3/4 text-xs top-6 md:h-32 md:top-8 md:text-sm">
      {{ theList.questionnaire.introduction.substring(0, 10) + '...' }}
    </span>

      <span class="current text-xs md:text-sm">
        <el-icon style="position:relative;top:1px"><Position/></el-icon>
        <span>&nbsp;{{ theList.amount }}人填过</span>
    </span>
      <button class="smentrance absolute w-7 h-7 float-right right-3 md:hidden justify-center">
        <el-icon style="position:relative;top:-2px">
          <EditPen/>
        </el-icon>
      </button>
      <button class="entrance hidden md:inline-flex justify-center float-right right-1"><span
          style="position:relative;top:-1px" @click="gotoFill(theList.questionnaireId, enable)">填写</span></button>
    </div>
  </div>
</template>

<script>
import router from "@/router";

export default {
  name: 'synopsis',
  props: ['theList', 'index', 'enable'],
  methods: {
    gotoFill: (id, enable) => {
      if (enable) {
        router.push({
          path: '/fill/' + id
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.naire1 {
  position: relative;
  height: 200px;
  width: 30%;
  border-radius: 10px;
  background: #F1FEFF;
  box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
  color: #3D3D3D;
  margin-left: auto;
}

.naire2 {
  position: relative;
  height: 200px;
  width: 30%;
  border-radius: 10px;
  background: #373737;
  box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
  color: #FFFFFF;
  margin-left: auto;
}

.title1 {
  display: block;
  margin-left: 9%;
  margin-right: 9%;
  font-family: MicrosoftYaHeiUI;
  font-weight: bold;
  letter-spacing: 0em;
  line-height: 24px;
  color: #3D3D3D;
}

.title2 {
  display: block;
  margin-left: 9%;
  margin-right: 9%;
  font-family: MicrosoftYaHeiUI;
  font-weight: bold;
  letter-spacing: 0em;
  line-height: 24px;
  color: #FFFFFF;
}

.introduction {
  position: relative;
  display: block;
  margin-left: 10%;
  margin-right: 10%;
  top: 20px;
  font-family: MicrosoftYaHei;
  font-weight: normal;
  letter-spacing: 0em;
  line-height: 22px;
}

.current {
  position: absolute;
  display: block;
  margin-left: 10%;
  margin-right: 10%;
  top: 130px;
  font-family: SourceHanSansCN-Regular;
  font-weight: normal;
  letter-spacing: 0em;
  color: #B6B6B6;
}

.entrance {
  position: absolute;
  float: right;
  width: 55px;
  height: 25px;
  margin-right: 10px;
  top: 160px;
  border-radius: 20px;
  background: #57CFFF;
  box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
  font-family: MicrosoftYaHei-Bold;
  font-size: 16px;
  font-weight: bold;
  text-align: center;
  vertical-align: middle;
  letter-spacing: 0em;
  color: #FFFFFF;
}

.smentrance {
  top: 150px;
  border-radius: 20px;
  background: #57CFFF;
  box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
  font-family: MicrosoftYaHei-Bold;
  font-size: 16px;
  font-weight: bold;
  text-align: center;
  vertical-align: middle;
  letter-spacing: 0em;
  color: #FFFFFF;
}

hr {
  position: absolute;
  top: 206px;
  width: 100%;
  transform: rotate(0deg);
  border: 1px solid #D8D8D8;
}

li {
  margin-bottom: 15px;
  font-family: SourceHanSansCN-Regular;
  font-size: 14px;
  font-weight: normal;
  line-height: 34px;
  color: #3D3D3D;
  float: left;
}

.miniwidth {
  min-width: 140px;
}
</style>
