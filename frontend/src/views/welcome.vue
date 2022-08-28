<template>
  <header class="fixed z-10 w-full" style="height: 10vh;">
    <nav class="h-full flex items-center justify-around m-3 p-5 rounded-2xl backdrop-blur-xl shadow-xl"
         style="background: rgba(253, 253, 253, 0.4);">
      <a class="flex m-2 text-center text-3xl xl:text-4xl welcome-title" href="#">苏小问·校园问卷</a>
      <div class="hidden xl:flex font-sans">
        <el-link type="primary" href="#design" icon="Edit" style="margin-right: 20px; font-size: 1rem">
          &nbsp;设计
        </el-link>
        <el-link type="primary" href="#publish" icon="Document" style="margin-right: 20px; font-size: 1rem">
          &nbsp;投放
        </el-link>
        <el-link type="primary" href="#analysis" icon="DataAnalysis" style="margin-right: 20px; font-size: 1rem">
          &nbsp;分析
        </el-link>
        <el-link type="primary" href="#help" icon="Connection" style="margin-right: 20px; font-size: 1rem">
          &nbsp;互助
        </el-link>
      </div>
      <div class="hidden md:flex items-center">
        <button class="w-20 h-10 rounded-xl m-4 font-medium flex items-center justify-center shadow-lg box-border"
                style="font-size: 16px; background: rgba(255, 255, 255, 0.5); border: 2px solid #1C8EF7;"
                @click="gotoLogin">登 录
        </button>
        <button class="w-20 h-10 rounded-xl m-4 font-medium flex items-center justify-center shadow-lg"
                style="font-size: 16px; background: rgba(28, 142, 247, 0.8); color: white;" @click="gotoRegister">注 册
        </button>
      </div>
    </nav>
  </header>
  <div class="relative" style="background-color: #F9FCFF">
    <div class="flex items-center justify-center mb-9 banner" style="height: 55vh;">
      <div class="flex h-full w-full pl-10 pr-10 xl:pl-32 xl:pr-32 bg-no-repeat"
           style="background-position: 80%; background-image: url(//124.223.170.133:9000/welcome-banner-image.png); background-size: 30%;">
        <div class="flex justify-center flex-col welcome-title-text mt-6">
          <div class="text-line mt-5 m-2 float-left text-base font-medium md:text-2xl">面向校园师生的</div>
          <div class="text-line m-2 float-left text-xl font-bold md:text-4xl">集设计/投放/分析/互助的</div>
          <div class="text-line-end mt-4 mb-4 m-2 float-left text-3xl font-bold md:text-6xl">一站式问卷服务平台</div>
        </div>
      </div>
    </div>
    <div class="flex flex-col items-center w-full h-full">
      <div class="flex md:hidden" id="account-buttons">
        <button class="w-20 h-10 rounded-xl m-4 font-medium flex items-center justify-center shadow-lg box-border"
                style="font-size: 16px; background: rgba(255, 255, 255, 0.5); border: 2px solid #1C8EF7;"
                @click="gotoLogin">登 录
        </button>
        <button class="w-20 h-10 rounded-xl m-4 font-medium flex items-center justify-center shadow-lg"
                style="font-size: 16px; background: rgba(28, 142, 247, 0.8); color: white;" @click="gotoRegister">注 册
        </button>
      </div>
      <div class="flex items-center justify-center h-full w-full" v-bind:key="index"
           v-for="(item, index) in welcomeInformation">
        <div :id="item.id" class="info-card" style="background: white;" v-if="index % 2 === 0">
          <div class="card-bg" :style="`background-position: 80%; background-image: url(${item.img});`">
            <div class="card-text">
              <div class="card-text-title text-violet-900">{{ item.subtitle }}</div>
              <div v-bind:key="i" class="card-text-line" v-for="(line, i) in item.desc"
                   :style="`color: rgb(${150 - i * 50} 130 250)`">
                <el-icon>
                  <promotion/>
                </el-icon> &nbsp; {{ line }}
              </div>
            </div>
          </div>
        </div>
        <div :id="item.id" class="info-card" style="background: #2F2F2F;" v-else>
          <div class="card-bg" :style="`background-position: 20%; background-image: url(${item.img});`">
            <div class="card-text items-end">
              <div class="card-text-title text-cyan-100">{{ item.subtitle }}</div>
              <div v-bind:key="i" class="card-text-line" v-for="(line, i) in item.desc"
                   :style="`color: rgb(${250 - i * 50} 254 255)`">
                <el-icon>
                  <promotion/>
                </el-icon> &nbsp; {{ line }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <footer class="flex flex-col items-center justify-center mt-9 text-zinc-400 text-sm">
      <div>
       <span class="flex items-center justify-center">
         <el-link href="mailto:sxw-suda@outlook.com">联系我们</el-link>&nbsp;|&nbsp;
         <el-link @click="showAgreement">用户协议</el-link>
        </span>
        <span class="flex items-center justify-center">
          Copyright &copy; 2022 苏小问. All Rights Reserved.
        </span>
      </div>
    </footer>
  </div>
</template>

<script setup>

import {useRouter} from "vue-router";
import {ElMessageBox, ElNotification} from 'element-plus'
import {Promotion} from "@element-plus/icons-vue";

const router = useRouter();

function gotoLogin() {
  router.push({
    path: '/account',
    query: {
      action: 'login'
    }
  });
}

function gotoRegister() {
  router.push({
    path: '/account',
    query: {
      action: 'register'
    }
  });
}

const welcomeInformation = [{
  id: 'design',
  subtitle: '高效设计 便捷填写',
  img: '//124.223.170.133:9000/welcome-info-img1.png',
  desc: ['极简设计，快速上手', '更多模版，更多灵感', '一键分享，立刻填写']
}, {
  id: 'publish',
  subtitle: '优质样本 轻松获取',
  img: '//124.223.170.133:9000/welcome-info-img2.png',
  desc: ['源于校园，更懂校园', '定向推送，省心省力', '权限分级，保障安全']
}, {
  id: 'analysis',
  subtitle: '智能统计 高效回收',
  img: '//124.223.170.133:9000/welcome-info-img3.png',
  desc: ['多种图表，多维展示', '一键导出，省时省力', '数据审核，精准筛选']
}, {
  id: 'help',
  subtitle: '互助平台 广阔渠道',
  img: '//124.223.170.133:9000/welcome-info-img4.png',
  desc: ['校园互助，高效推广', '回答问卷，赚取奖励', '渠道广阔，官方认证']
}];

function showAgreement() {
  ElNotification({
    title: '用户协议',
    message: '用户协议尚未开放！',
    type: 'info',
  })
}

</script>

<style lang='scss' scoped>

.welcome-title {
  font-family: YouSheBiaoTiHei, serif;
  color: #2462AF;
}

.banner {
  border-radius: 0px 0px 40px 40px;
  background: linear-gradient(249deg, #F5E9E9 0%, #F5E9E9 0%, rgba(232, 225, 225, 0.57) 31%, rgba(216, 216, 216, 0.00) 69%, rgba(216, 216, 216, 0.00) 100%), #E2EEFC;
}

.welcome-title-text {
  text-shadow: 0px 2px 10px 1px rgba(0, 0, 0, 0.3);
  @apply font-sans
}

.text-line {
  color: #3D3D3D;
}

.text-line-end {
  color: transparent;
  -webkit-background-clip: text;
  background-image: linear-gradient(90deg, #2e1f7c, #6652e1);
}

.info-card {
  @apply flex items-center justify-center rounded-3xl shadow-xl hover:shadow-2xl h-full w-full m-5 md:m-10 font-sans
}

.card-text {
  @apply flex flex-col pl-1 md:pl-20 xl:pl-32 pr-1 md:pr-20 xl:pr-32 m-6 md:pt-10 md:pb-10 xl:p-16 antialiased font-sans
}

.card-bg {
  background-size: 20%;
  @apply bg-no-repeat w-full h-full
}

.card-text-title {
  @apply mb-5 text-2xl md:text-3xl xl:text-4xl items-center subpixel-antialiased font-sans font-extrabold
}

.card-text-line {
  @apply flex items-center md:text-xl md:leading-relaxed subpixel-antialiased hover:italic font-sans font-semibold
}

footer {
  height: 15vh;
  border-radius: 40px 40px 0px 0px;
  background: linear-gradient(98deg, #F5E9E9 0%, #F5E9E9 0%, rgba(216, 216, 216, 0.00) 99%, rgba(216, 216, 216, 0.00) 100%), #E2EEFC;
  @apply font-sans font-thin
}
</style>
