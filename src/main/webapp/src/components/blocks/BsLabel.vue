<template>
  <div class="text-900 font-medium text-xl mb-2">{{ text }}</div>
  <div class="text-600">{{label}}</div>
</template>

<script lang="ts" setup>
import {defineProps} from 'vue'

interface Properties {
  label: string,
  text: string
}
const props = defineProps<Properties>()
</script>

<style scoped>

</style>
