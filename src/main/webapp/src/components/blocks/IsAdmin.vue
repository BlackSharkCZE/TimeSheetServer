<template>
  <WithRole :roles="['admin', 'superadmin']"><slot></slot></WithRole>
</template>

<script lang="ts" setup>
import WithRole from "@/components/blocks/WithRole.vue";
</script>

<style scoped>

</style>
