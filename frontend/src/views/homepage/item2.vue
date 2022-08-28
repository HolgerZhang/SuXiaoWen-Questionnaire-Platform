<template>
  <li>
    <div class="w-full h-16 md:h-20 naire overflow-hidden rounded-xl naire">
      <div style="position:relative" class="top-1 md:top-2">
        <span class="relative left-4 w-1/3  text-xs lg:text-sm lg:w-3/5 lg:left-8">{{
            theList.questionnaire.title
          }}</span>
        <span :class="theList.condition.classObj"
              class="hidden relative float-right justify-center top-1 text-xs right-5 text-center md:inline-flex">{{
            theList.condition.text
          }}</span><br>
      </div>
      <div class="additional w-full flex relative bottom-2 md:-bottom-1 left-3 md:left-7">
            <span class="relative w-1/2 mx-3 flex-initial md:w-1/4">
              <el-icon style="position:relative;top:1px"><Position/></el-icon>
              来自uid=<span style="text-decoration: underline">{{ theList.questionnaire.userId }}</span>
            </span>
        <span v-if="theList.questionnaire.dueTime !== undefined && theList.questionnaire.dueTime !== ''"
              class="relative mx-3 flex-initial hidden md:w-1/4 md:inline-flex justify-center">
              <el-icon style="position:relative;top:11px"><Calendar/></el-icon>
              <span style="text-decoration: underline">{{ theList.questionnaire.dueTime }}</span>
            </span>
        <!--        <span class="relative 2-1/2 md:w-1/4 mx-3 flex-initial"><span-->
        <!--            style="text-decoration: underline">{{ theList.amount }}</span>人填过</span>-->
      </div>
    </div>
    <br>
  </li>
</template>

<script>
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

export default {
  name: 'item2',
  props: ['theList']
}
</script>

<style lang="scss" scoped>
.naire {
  background: #E8EBED;
  box-sizing: border-box;
  border: 1px solid #CEB3B3;
}

.unpublished {
  font-size: 10px;
  color: #F7A500;
  width: 50px;
  height: 25px;
  border-radius: 5px;
  background: #F9EDC6;
  border: 2px solid #A17704;
  text-align: center;
  line-height: 20px;
}

.ongoing {
  font-size: 10px;
  color: #169E4C;
  width: 50px;
  height: 25px;
  border-radius: 5px;
  background: #DCF9C6;
  border: 2px solid #0F7026;
  text-align: center;
  line-height: 20px;
}

.done {
  font-size: 10px;
  color: #5E5E5E;
  width: 50px;
  height: 25px;
  border-radius: 5px;
  background: #D0D0D0;
  border: 2px solid #313131;
  text-align: center;
  line-height: 20px;
}

.additional {
  font-family: SourceHanSansCN-Regular;
  font-size: 12px;
  font-weight: normal;
  letter-spacing: 0em;
  color: #898989;
}

li {
  margin-bottom: 15px;
  font-family: SourceHanSansCN-Regular;
  font-size: 14px;
  font-weight: normal;
  line-height: 34px;
  color: #3D3D3D;
}
</style>
