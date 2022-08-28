<template>
  <section class="app-main">
    <router-view v-slot="{ Component, route }">
      <transition name="fade-transform" mode="out-in">
        <keep-alive>
          <component :is="Component" :key="route.path"/>
        </keep-alive>
      </transition>
    </router-view>
  </section>
</template>

<script setup name="DesignerLayout">

</script>

<style scoped>

</style>
