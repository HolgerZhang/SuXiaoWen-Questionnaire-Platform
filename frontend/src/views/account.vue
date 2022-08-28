<template>
  <div class="absolute h-full w-full account-background">
    <div class="flex items-center justify-center h-full w-full">
      <div class="hidden md:flex bg-amber-100 h-3/5 w-4/5 rounded-2xl shadow-xl backdrop-blur-2xl p-20"
           style="background: rgba(253, 253, 253, 0.8);">
        <div class="relative h-full w-full blur-md"
             style="background: url(//124.223.170.133:9000/account-bg.png) no-repeat center; background-size: 100%; ">
        </div>
        <div class="hidden lg:flex justify-between w-1/2 h-1/2 lg:absolute z-0 left-position-2 pr-10 p-20 flex-col font"
             v-if="state.action === 'register'">
          <div class="font-semibold text-2xl">欢迎注册</div>
          <div class="mt-5 text-5xl xl:text-6xl welcome-title">
            <router-link to="/welcome">苏小问校园问卷</router-link>
          </div>
          <div class="mt-5 text-xl font-semibold">
            已经有账号？
            去<a class="underline text-cyan-500" @click="gotoLogin">登录</a>。
          </div>
        </div>
        <div
            class="hidden lg:flex justify-between  w-1/2 h-1/2 lg:absolute z-0 right-position-2 pl-10 p-20 flex-col font-sans"
            v-else>
          <div class="font-semibold text-2xl ">欢迎使用</div>
          <div class="mt-5 text-5xl xl:text-6xl welcome-title">
            <router-link to="/welcome">苏小问校园问卷</router-link>
          </div>
          <div class="mt-5 text-xl font-semibold">
            还没有账号？
            去<a class="underline text-cyan-500" @click="gotoRegister">注册</a>。
          </div>
        </div>
      </div>

      <div id="register" class="fixed w-full sm:w-3/5 lg:w-1/3 h-2/3 lg:absolute z-20 right-position"
           v-if="state.action === 'register'">
        <div class="h-full w-full flex items-center justify-center flex-col rounded-3xl register-card">
          <div class="card-title">注 册</div>
          <!-- 注册表单 -->
          <el-form ref="registerRef" :model="registerForm" :rules="registerRules"
                   class="w-full p-3 mt-2 mb-2 font-sans">
            <el-form-item prop="username">
              <el-input v-model="registerForm.username" type="text" size="large" auto-complete="off" placeholder="账号">
                <template #prefix>
                  <svg-icon icon-class="user" class="el-input__icon input-icon"/>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input
                  v-model="registerForm.password" type="password" size="large" auto-complete="off" placeholder="密码"
                  @keyup.enter="handleRegister">
                <template #prefix>
                  <svg-icon icon-class="password" class="el-input__icon input-icon"/>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="confirmPassword">
              <el-input v-model="registerForm.confirmPassword" type="password" size="large" auto-complete="off"
                        placeholder="确认密码" @keyup.enter="handleRegister">
                <template #prefix>
                  <svg-icon icon-class="password" class="el-input__icon input-icon"/>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="code" v-if="captchaOnOff">
              <el-input
                  size="large" v-model="registerForm.code" auto-complete="off" placeholder="验证码" style="width: 63%"
                  @keyup.enter="handleRegister">
                <template #prefix>
                  <svg-icon icon-class="validCode" class="el-input__icon input-icon"/>
                </template>
              </el-input>
              <div class="register-code">
                <img :src="codeUrl" @click="getCode" class="login-code-img"/>
              </div>
            </el-form-item>
            <el-form-item style="width:100%;">
              <el-button :loading="loading" size="large" round style="width:100%;"
                         @click.prevent="handleRegister">
                <span v-if="!loading">注 册</span>
                <span v-else>注 册 中...</span>
              </el-button>
            </el-form-item>
            <div class="flex text-cyan-100 font-sans font-light font-medium">
              或者，使用<a class="underline text-yellow-200" @click="sudaUniAccount">统一身份认证</a>注册。
            </div>
          </el-form>
        </div>
      </div>

      <div id="login" class="fixed w-full sm:w-3/5 lg:w-1/3 h-2/3 lg:absolute z-20 left-position " v-else>
        <div class="h-full w-full flex items-center justify-center flex-col rounded-3xl login-card">
          <div class="card-title">登 录</div>
          <!-- 登录表单 -->
          <el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="w-full p-3 mt-2 mb-2 font-sans">
            <el-form-item prop="username">
              <el-input v-model="loginForm.username" type="text" size="large" auto-complete="off"
                        placeholder="账号">
                <template #prefix>
                  <svg-icon icon-class="user" class="el-input__icon input-icon"/>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="loginForm.password" type="password" size="large" auto-complete="off" placeholder="密码"
                        @keyup.enter="handleLogin">
                <template #prefix>
                  <svg-icon icon-class="password" class="el-input__icon input-icon"/>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="code" v-if="captchaOnOff">
              <el-input v-model="loginForm.code" size="large" auto-complete="off" placeholder="验证码" style="width: 63%"
                        @keyup.enter="handleLogin">
                <template #prefix>
                  <svg-icon icon-class="validCode" class="el-input__icon input-icon"/>
                </template>
              </el-input>
              <div class="login-code">
                <img :src="codeUrl" @click="getCode" class="login-code-img"/>
              </div>
            </el-form-item>
            <div class="flex justify-items-start items-center h-auto mt-2 mb-2">
              <el-switch active-color="#13ce66" v-model="loginForm.rememberMe" style=""></el-switch>
              <span class="ml-5 text-white text-base">记住密码</span>
            </div>
            <el-form-item style="width:100%;">
              <el-button :loading="loading" size="large" round style="width:100%;"
                         @click.prevent="handleLogin">
                <span v-if="!loading">登 录</span>
                <span v-else>登 录 中...</span>
              </el-button>
            </el-form-item>
            <div class="flex text-cyan-100 font-sans font-light font-medium">
              或者，使用<a class="underline text-yellow-200" @click="sudaUniAccount">统一身份认证</a>登录。
            </div>
          </el-form>
        </div>
      </div>
      <div class="flex flex-col justify-center items-center fixed font-light text-base" style="top: 90%">
        <div class="lg:hidden">
          <span v-if="state.action === 'register'">
            已经有账号？
            去<a class="underline text-cyan-500" @click="gotoLogin">登录</a>。
          </span>
          <span v-else>
            还没有账号？
            去<a class="underline text-cyan-500" @click="gotoRegister">注册</a>。
          </span>
        </div>
        <div class="truncate">Copyright &copy; 2022 苏小问. All Rights Reserved.
          <el-link @click="showAgreement">用户协议</el-link>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>

import {useRoute, useRouter} from "vue-router"
import {onMounted, reactive, watch} from "vue";
import {ElMessageBox, ElNotification} from "element-plus";
import {getCodeImg} from "@/api/login";
import {register} from "@/api/login" ;
import Cookies from "js-cookie";
import {encrypt, decrypt} from "@/utils/jsencrypt";
import {useStore} from "vuex";
import {getCurrentInstance, ref} from "vue";
import {onBeforeRouteUpdate} from 'vue-router';

const route = useRoute();
const router = useRouter();
const state = reactive({
  action: 'login',
  redirect: '/index'
})


const store = useStore();
const {proxy} = getCurrentInstance();

const loginForm = ref({
  username: "admin",
  password: "suxiaowen2022_admin",
  rememberMe: false,
  code: "",
  uuid: ""
});

const loginRules = {
  username: [{required: true, trigger: "blur", message: "请输入您的账号"}],
  password: [{required: true, trigger: "blur", message: "请输入您的密码"}],
  code: [{required: true, trigger: "change", message: "请输入验证码"}]
};

const codeUrl = ref("");
const loading = ref(false);
// 验证码开关
const captchaOnOff = ref(true);

function handleLogin() {
  proxy.$refs.loginRef.validate(valid => {
    if (valid) {
      loading.value = true;
      // 勾选了需要记住密码设置在cookie中设置记住用户名和密码
      if (loginForm.value.rememberMe) {
        Cookies.set("username", loginForm.value.username, {expires: 30});
        Cookies.set("password", encrypt(loginForm.value.password), {expires: 30});
        Cookies.set("rememberMe", loginForm.value.rememberMe, {expires: 30});
      } else {
        // 否则移除
        Cookies.remove("username");
        Cookies.remove("password");
        Cookies.remove("rememberMe");
      }
      // 调用action的登录方法
      store.dispatch("Login", loginForm.value).then(() => {
        router.push({path: state.redirect || "/index"});
      }).catch((e) => {
        loading.value = false;
        // 重新获取验证码
        if (captchaOnOff.value) {
          getCode();
        }
      });
    }
  });
}

function getCode() {
  getCodeImg().then(res => {
    captchaOnOff.value = res.captchaOnOff === undefined ? true : res.captchaOnOff;
    if (captchaOnOff.value) {
      codeUrl.value = "data:image/gif;base64," + res.img;
      loginForm.value.uuid = res.uuid;
      registerForm.value.uuid = res.uuid;
    }
  });
}

function getCookie() {
  const username = Cookies.get("username");
  const password = Cookies.get("password");
  const rememberMe = Cookies.get("rememberMe");
  loginForm.value = {
    username: username === undefined ? loginForm.value.username : username,
    password: password === undefined ? loginForm.value.password : decrypt(password),
    rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
  };
}


onMounted(() => {
  console.log("获取到的参数: ", route.query.action, route.query.redirect);
  if (route.query.action === undefined || route.query.action === '') {
    state.action = "login";
  } else {
    state.action = route.query.action;
  }
  state.redirect = route.query.redirect;
  if (state.action !== 'login' && state.action !== 'register') {
    ElNotification({
      title: '参数不正确',
      message: '页面参数(action)只能是登录(login)或注册(register)的一种，将转跳登录页面！',
      type: 'warning',
    })
  }
  getCode();
  if (state.action === 'login') {
    getCookie();
  }
});

onBeforeRouteUpdate((to) => {
  if (to.path === '/account') {
    state.action = to.query.action;
    state.redirect = to.query.redirect
    console.log('修改：', state)
    getCode();
    if (state.action === 'login') {
      getCookie();
    }
  }
})

function sudaUniAccount() {
  ElNotification({
    title: '统一身份认证',
    message: '统一身份认证系统尚未接入，请使用账号密码登录/注册！',
    type: 'info',
  })
}

function showAgreement() {
  ElNotification({
    title: '用户协议',
    message: '用户协议尚未开放！',
    type: 'info',
  })
}

const registerForm = ref({
  username: "",
  password: "",
  confirmPassword: "",
  code: "",
  uuid: ""
});

const equalToPassword = (rule, value, callback) => {
  if (registerForm.value.password !== value) {
    callback(new Error("两次输入的密码不一致"));
  } else {
    callback();
  }
};

const registerRules = {
  username: [
    {required: true, trigger: "blur", message: "请输入您的账号"},
    {min: 2, max: 20, message: "用户账号长度必须介于 2 和 20 之间", trigger: "blur"}
  ],
  password: [
    {required: true, trigger: "blur", message: "请输入您的密码"},
    {min: 5, max: 20, message: "用户密码长度必须介于 5 和 20 之间", trigger: "blur"}
  ],
  confirmPassword: [
    {required: true, trigger: "blur", message: "请再次输入您的密码"},
    {required: true, validator: equalToPassword, trigger: "blur"}
  ],
  code: [{required: true, trigger: "change", message: "请输入验证码"}]
};

function handleRegister() {
  proxy.$refs.registerRef.validate(valid => {
    if (valid) {
      loading.value = true;
      register(registerForm.value).then(res => {
        const username = registerForm.value.username;
        ElMessageBox.alert("<font color='red'>恭喜你，您的账号 " + username + " 注册成功！</font>", "系统提示", {
          dangerouslyUseHTMLString: true,
          type: "success",
        }).then(() => {
          loading.value = false;
          router.push({
            path: "/account",
            query: {
              action: "login",
              redirect: state.redirect
            }
          });
        }).catch(() => {
        });
      }).catch(() => {
        loading.value = false;
        if (captchaOnOff) {
          getCode();
        }
      });
    }
  });
}

function gotoLogin() {
  router.push({
    path: '/account',
    query: {
      action: 'login',
      redirect: state.redirect
    }
  });
}

function gotoRegister() {
  router.push({
    path: '/account',
    query: {
      action: 'register',
      redirect: state.redirect
    }
  });
}

</script>

<style lang="scss" scoped>
.account-background {
  background: linear-gradient(299deg, #F5E9E9 0%, #F5E9E9 0%, rgba(249, 241, 241, 0.40) 46%, rgba(216, 216, 216, 0.00) 100%, rgba(216, 216, 216, 0.00) 100%), #E2EEFC;
}

.center-position {
  left: 50%;
}

@media (min-width: 1024px) { // lg
  .left-position {
    left: 15%;
  }
  .right-position {
    right: 15%;
  }
  .left-position-2 {
    left: 5%;
  }
  .right-position-2 {
    right: 0;
  }
}

.login-card {
  box-shadow: 0px 2px 25px 5px rgba(10, 1, 73, 0.6);
  background: linear-gradient(217deg, #9FDFFC 1%, #9FDFFC 1%, rgba(216, 216, 216, 0.00) 100%, rgba(216, 216, 216, 0.00) 100%), #2D87DB;
}

.register-card {
  box-shadow: 0px 2px 25px 5px rgba(10, 1, 73, 0.6);
  background: linear-gradient(143deg, #9FDFFC -1%, #9FDFFC -1%, rgba(216, 216, 216, 0.00) 99%, rgba(216, 216, 216, 0.00) 99%), #2D87DB;
}

.card-title {
  font-family: YouSheBiaoTiHei, serif;
  text-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
  @apply text-4xl text-white text-center mb-2
}

.input-icon {
  height: 39px;
  width: 14px;
  margin-left: 1px;
}

.login-code-img {
  height: 40px;
  padding-left: 10px;
}

.welcome-title {
  font-family: YouSheBiaoTiHei, serif;
  color: #5090DF;
  text-shadow: 0px 4px 20px 10px rgba(0, 0, 0, 0.3);
}


</style>
