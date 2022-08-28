<template>
  <div class="bar"></div>
  <div class="title">问卷推荐</div>
  <input class="search" value="输入关键词搜索问卷">
  <button>
    <el-icon class="btn">
      <Search/>
    </el-icon>
  </button>
</template>

<script>
export default {
  name: 'searchBox',
}
</script>

<style>
.bar {
  position: absolute;
  width: 100%;
  height: 40px;
  opacity: 0.5;
  background: #E9F0FF;
  border-radius: 15px;
  box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
}

.title {
  position: absolute;
  left: 5%;
  top: 2px;
  font-family: SourceHanSansCN-Bold;
  font-size: 22px;
  font-weight: bold;
  letter-spacing: 0em;
  color: #3D3D3D;
}

.search {
  position: absolute;
  right: 4%;
  top: 8px;
  width: 23%;
  height: 26px;
  border-radius: 30px;
  opacity: 0.5;
  background: #DFDFDF;
  text-align: center;
}

.btn {
  position: absolute;
  right: 6%;
  top: 13px;
  color: #888888;
}
</style>
