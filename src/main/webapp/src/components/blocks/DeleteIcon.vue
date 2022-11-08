<template>
    
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
const emits = defineEmits(['confirmed', 'canceled'])

// Define functions
function processIconClick() {
	console.log('Here we are!')
}

</script>

<style scoped lang="scss">

.i-button {
  &:HOVER {
    color: var(--red-300) !important;
  }
}
</style>
