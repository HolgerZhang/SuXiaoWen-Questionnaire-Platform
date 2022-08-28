<template>
  <div :class="index % 2 === 0 ? 'naire1' : 'naire2'" class="relative h-72 miniwidth">
    <span :class="index % 2 === 0 ? 'title1' : 'title2'"
          class="relative top-4 text-sm md:text-base md:top-6">{{ theList.questionnaire.title }}</span>
    <span
        class="introduction relative h-32 w-3/4 text-xs overflow-hidden top-6 md:h-32 md:top-8 md:text-sm">{{ theList.questionnaire.introduction.substring(0, 10) + '...' }}</span>
    <hr>
    <span class="current text-xs md:text-sm">
        <el-icon style="position:relative;top:1px"><Position/></el-icon>
        <span>&nbsp;{{ theList.amount }}人填过</span>
      </span>
    <button class="smentrance absolute w-7 h-7 float-right right-3 md:hidden justify-center">
      <el-icon style="position:relative;top:-2px">
        <EditPen/>
      </el-icon>
    </button>
    <button class="entrance hidden float-right right-3 md:inline-flex justify-center"><span
        style="position:relative;top:-1px">填写</span></button>
  </div>
</template>

<script>
export default {
  name: 'synopsis',
  props: ['theList', 'index'],
}
</script>

<style lang="scss" scoped>
.naire1 {
  border-radius: 10px;
  background: #F1FEFF;
  box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
  color: #3D3D3D;
  width: 18%;
  margin: auto;
}

.naire2 {
  border-radius: 10px;
  background: #373737;
  box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
  color: #FFFFFF;
  width: 18%;
  margin: auto;
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
  display: block;
  margin-left: 10%;
  margin-right: 8%;
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
  top: 250px;
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
  top: 247px;
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
  top: 247px;
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
  top: 234px;
  width: 100%;
  transform: rotate(0deg);
  border: 1px solid #D8D8D8;
}

.miniwidth {
  min-width: 140px;
}
</style>
