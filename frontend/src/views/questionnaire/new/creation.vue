<script setup>
import {reactive, ref} from 'vue'
import {newQuestionnaire} from '@/api/question/question';
import {ElMessage} from 'element-plus';
import {useRouter} from "vue-router";

const router = useRouter()
const createQuestionVisible = ref(false)
const formRef = ref()
const ruleForm = reactive({
  title: "",
  introduction: ""
})
const rules = {
  title: [{required: true, message: '请填写问卷标题', trigger: 'blur'}],
  introduction: [{required: true, message: "请填写问卷标题", trigger: 'blur'}]
}

const handleClose = () => {
  ruleForm.title = ""
  ruleForm.introduction = ""
  createQuestionVisible.value = false
}

const submitForm = async (formEl) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
    if (valid) {
      newQuestionnaire(ruleForm).then(res => {
        if (res.code == 200) {
          router.push("/designer/index?id=" + res.data.questionnaireId)
        } else {
          ElMessage.error("创建失败")
        }
      })
    }
  })
}
// e295ff2d-4a7d-469f-9801-ae3de2832e6b
</script>
<template>
  <div class="title text-center">选择创建方式</div>
  <div class="btns">
    <el-button class="category" @click="createQuestionVisible = true">
      <div>
        <div class="square" id="blank">+</div>
        <div class="subtitle leading-none md:leading-none text-base md:text-lg">空白创建</div>
        <div class="message leading-none md:leading-none text-xs md:text-sm">从0搭建问卷</div>
      </div>
    </el-button>
    <el-button class="category" @click="createQuestionVisible = true">
      <div>
        <div class="square" id="textin">
          <div class="littlebox">T</div>
        </div>
        <div class="subtitle leading-none md:leading-none text-base md:text-lg">文本导入</div>
        <div class="message leading-none md:leading-none text-xs md:text-sm">支持TXT格式</div>
      </div>
    </el-button>
    <el-button class="category" @click="createQuestionVisible = true">
      <div>
        <div class="square" id="copy">
          <el-icon>
            <DocumentCopy/>
          </el-icon>
        </div>
        <div class="subtitle leading-none md:leading-none text-base md:text-lg">复制项目</div>
        <div class="message leading-none md:leading-none text-xs md:text-sm">复制已创建项目</div>
      </div>
    </el-button>
    <el-dialog v-model="createQuestionVisible" title="创建问卷" width="30%" :before-close="handleClose">
      <el-form :model="ruleForm" :rules="rules" ref="formRef">
        <el-form-item label="问卷标题" prop="title">
          <el-input v-model="ruleForm.title"/>
        </el-form-item>
        <el-form-item label="问卷简介" prop="introduction">
          <el-input v-model="ruleForm.introduction" type="textarea"/>
        </el-form-item>
      </el-form>
      <template #footer>
                <span class="dialog-footer">
                    <el-button @click="createQuestionVisible = false">关闭</el-button>
                    <el-button type="primary" @click="submitForm(formRef)">创建</el-button>
                </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'creation'
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

.btns {
  display: flex;
  align-items: center;
  justify-items: center;
  margin-left: 15%;
  margin-right: 15%;
}

.category {
  position: relative;
  width: 300px;
  height: 150px;
  border-radius: 6px;
  background: #FFFFFF;
  box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
  margin: 10px;
}

.subtitle {
  position: relative;
  top: 7px;
  font-family: SourceHanSansCN-Bold;
  // font-size: 19px;
  font-weight: bold;
  text-align: center;
  letter-spacing: 0em;
  color: #3D3D3D;
}

.message {
  position: relative;
  top: 12px;
  font-family: SourceHanSansCN-Regular;
  // font-size: 15px;
  font-weight: normal;
  text-align: center;
  letter-spacing: 0em;
  color: #3D3D3D;
}

.square {
  position: relative;
  width: 60px;
  height: 60px;
  margin: auto;
  border-radius: 6px;
  box-shadow: 0px 4px 10px 0px rgba(36, 164, 255, 0.3);
}

#blank {
  font-family: SourceHanSansCN-Bold;
  font-size: 40px;
  font-weight: bold;
  color: #2EA8FF;
  text-align: center;
  line-height: 50px;
  background: #FFFFFF;
}

#textin {
  font-family: SourceHanSansCN-Bold;
  font-size: 18px;
  font-weight: normal;
  background: #36BFFF;
  display: flex;
  align-items: center;
}

.littlebox {
  position: relative;
  width: 30px;
  height: 30px;
  top: 0;
  margin: auto;
  border-radius: 6px;
  color: #09A9FF;
  text-align: center;
  line-height: 31px;
  background: #FFFFFF;
}

#copy {
  font-size: 30px;
  font-weight: bold;
  color: #FFFFFF;
  text-align: center;
  line-height: 65px;
  background: #00E25A;
}
</style>
