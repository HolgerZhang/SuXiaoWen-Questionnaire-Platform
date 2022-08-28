<template>
  <div>
    <div class="help">
      <search-box/>
      <latest/>
      <topPicks/>
    </div>
    <button class="create right-2 w-14 h-14 text-4xl md:w-20 md:h-20 md:text-5xl md:right-32 top-3/4"
            @click="gotoNew">+
    </button>
  </div>
</template>

<script setup name="Help">
import {useRouter} from "vue-router";
import searchBox from './searchBox.vue';
import latest from './latest.vue';
import topPicks from './topPicks.vue';
import {ref} from "vue";

const router = useRouter();

function gotoNew() {
  router.push({
    path: '/questionnaire/new'
  });
}
</script>

<style scoped lang="scss">
.help {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }

  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }

  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans",
  "Helvetica Neue",
  Helvetica,
  Arial,
  sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }

  height: 629px;
  background: #F6F6F6;
}

.create {
  position: absolute;
  opacity: 0.8;
  background: #8BA9FF;
  border-radius: 45px;
  box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
  font-family: KaiTi;
  font-weight: bold;
  vertical-align: middle;
  text-align: center;
  color: rgb(255, 255, 255);
}
</style>
