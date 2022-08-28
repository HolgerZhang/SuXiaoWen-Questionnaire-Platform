<template>
  <div class="flex justify-items-center items-center align-middle">
    <div class="flex  justify-items-center items-center align-middle" id="qrcodeImg"></div>
  </div>
</template>

<script setup name="QrCode">

import QRCode from 'qrcodejs2-fix'

import {onMounted} from "vue";

const props = defineProps({
  url: {
    type: String
  }
})

onMounted(() => {
  createQrcode("http://124.223.170.133/#/fill/" + props.url)
})

function createQrcode(text) {
  let qrcodeImgEl = document.getElementById('qrcodeImg')
  qrcodeImgEl.innerHTML = ''
  let qrcode = new QRCode(qrcodeImgEl, {
    width: 100,
    height: 100,
    colorDark: '#000000',
    colorLight: '#ffffff',
    correctLevel: QRCode.CorrectLevel.H
  })
  qrcode.makeCode(text)
}

</script>

<style scoped>


</style>
