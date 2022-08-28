<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6" :xs="24">
        <el-card class="box-card">
          <template v-slot:header>
            <div class="clearfix">
              <span>个人信息</span>
            </div>
          </template>
          <div>
            <div class="text-center">
              <userAvatar :user="state.user"/>
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <svg-icon icon-class="user"/>
                用户名称
                <div class="pull-right">{{
                    (state.user.userName === state.user.nickName) ?
                        state.user.userName :
                        state.user.userName + ' (' + state.user.nickName + ')'
                  }}
                </div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="phone"/>
                手机号码
                <div class="pull-right">{{ state.user.phonenumber || '未知' }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="email"/>
                用户邮箱
                <div class="pull-right">{{ state.user.email || '未知' }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="tree"/>
                所属部门
                <div class="pull-right" v-if="state.user.dept">
                  {{ state.user.dept.deptName }}
                </div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="peoples"/>
                所属角色
                <div class="pull-right">{{ state.roleGroup }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="date"/>
                注册日期
                <div class="pull-right">{{ state.user.createTime }}</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18" :xs="24">
        <el-card>
          <template v-slot:header>
            <div class="clearfix">
              <span>信息维护</span>
            </div>
          </template>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userinfo">
              <user-info :user="state.user"/>
            </el-tab-pane>
            <el-tab-pane v-if="state.roleGroup === '未验证用户'" label="账户验证" name="accountVerify">
              <account-verify :user="state.user"/>
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <reset-pwd :user="state.user"/>
            </el-tab-pane>
            <el-tab-pane label="增值服务" name="accountGift">
              <account-gift :user="state.user"/>
            </el-tab-pane>
            <el-tab-pane label="统一身份认证" name="uniAccount">
              <uni-account :user="state.user"/>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Profile">
import {getUserProfile} from "@/api/system/user";
import {reactive, ref} from "vue";
import UniAccount from "@/views/system/user/profile/uniAccount";
import AccountVerify from "@/views/system/user/profile/accountVerify";
import UserInfo from "@/views/system/user/profile/userInfo";
import ResetPwd from "@/views/system/user/profile/resetPwd";
import AccountGift from "@/views/system/user/profile/accountGift";

const activeTab = ref("userinfo");
const state = reactive({
  user: {},
  roleGroup: {}
});

function getUser() {
  getUserProfile().then(response => {
    state.user = response.data;
    state.roleGroup = response.roleGroup || '未验证用户';
  });
}

getUser();
</script>
