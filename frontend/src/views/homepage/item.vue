<template>
  <li>
    <div class="absolute w-full h-9 naire overflow-hidden rounded-xl">
      <span class="relative left-4 w-1/3 text-xs lg:text-sm lg:w-3/5 lg:left-8 ">{{ theList.title }}</span>
      <span :class="theList.condition.classObj"
            class="hidden relative float-right justify-center top-1 text-xs right-5 text-center md:inline-flex">{{ theList.condition.text }}</span>
    </div>
    <br>
  </li>
</template>

<script>
export default {
  name: 'item',
  props: ['theList'],
}
</script>

<style lang="scss" scoped>
.naire {
  background: #E8EBED;
  box-sizing: border-box;
  border: 1px solid #CEB3B3;
}

.unpublished {
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
  color: #5E5E5E;
  width: 50px;
  height: 25px;
  border-radius: 5px;
  background: #D0D0D0;
  border: 2px solid #313131;
  text-align: center;
  line-height: 20px;
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
