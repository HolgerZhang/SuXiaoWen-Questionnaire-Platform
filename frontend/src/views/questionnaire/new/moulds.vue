<template>
  <div class="text-center title">使用问卷模板</div>
  <div style="text-align:center">
    <el-button>公众调查</el-button>
    <el-button>大学生调查</el-button>
    <el-button>市场调查</el-button>
    <el-button>防疫调查</el-button>
    <el-button>我的收藏</el-button>
  </div>
  <div class="flex flex-wrap w-full justify-center">
    <el-button class="mould w-1/5" v-for="mould in mymoulds.slice(0,8)" :key="mould.id">
      <div class="mouldname text-xs md:text-base w-5/6">{{ mould.title }}</div>
      <div class="message text-xs md:text-sm w-3/5">共{{ mould.questions }}题/被引{{ mould.frequency }}次</div>
    </el-button>
  </div>

</template>

<script>
export default {
  name: 'modules',
  setup() {
    let mymoulds = [
      {id: '001', title: '环保调查问卷', questions: 17, frequency: 2869},
      {id: '002', title: '青少年视力调查问卷', questions: 12, frequency: 2829},
      {id: '003', title: '停课不停学线上教学调查', questions: 18, frequency: 1116},
      {id: '004', title: '关于电信网络诈骗了解及防范情况调查问卷', questions: 2, frequency: 1277},
      {id: '005', title: '关于开展“课后服务”家长问卷调查', questions: 17, frequency: 2869},
      {id: '006', title: '饭圈文化对青少年心理影响调查及反思', questions: 12, frequency: 2829},
      {id: '007', title: '2022世界睡眠日问卷调查', questions: 18, frequency: 1116},
      {id: '008', title: '中小学生“五项管理”调查问卷', questions: 2, frequency: 1277}
    ];
    return {
      mymoulds,
    }
  }
}
</script>

<style lang="scss" scoped>
.title {
  position: relative;
  margin-top: 5px;
  margin-bottom: 5px;
  font-family: SourceHanSansCN-Bold;
  font-size: 22px;
  font-weight: bold;
  letter-spacing: 0em;
  color: #3D3D3D;
}

.mould {
  position: relative;
  height: 130px;
  border-radius: 6px;
  background: #FFFFFF;
  box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
  margin: 10px;
}

.mouldname {
  position: absolute;
  text-align: left;
  white-space: normal;
  left: 10%;
  top: 20px;
  font-family: SourceHanSansCN-Regular;
  font-weight: normal;
  color: #3D3D3D;
}

.message {
  position: absolute;
  top: 90px;
  left: 10%;
  font-family: SourceHanSansCN-Regular;
  font-weight: normal;
  color: #A29F9F;
}
</style>
