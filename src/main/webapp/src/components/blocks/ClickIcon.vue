<template>
  <i class="i-button pi text-red-500 cursor-pointer"
     :class="props.icon || 'pi-trash'"
     @click="processClick()">
  </i>
</template>

<script lang="ts" setup>
import {defineProps, withDefaults, defineEmits} from 'vue'


// Define component properties
interface Properties {
    icon?: string
}

const props = withDefaults(defineProps<Properties>(), {
    icon: 'pi-trash'
})

// Define emits
const emits = defineEmits(['click'])

// Define functions
function processClick() {
	emits('click')
}

</script>

<style scoped lang="scss">

.i-button {
  &:HOVER {
    color: var(--red-300) !important;
  }
}
</style>
