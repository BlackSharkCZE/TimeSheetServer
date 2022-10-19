<template>

  <div class="field">
    <label :for="id">{{ props.label }} <span v-if="vualidate.required">*</span></label>
    <InputText
        :id="id"
        :class="{'p-invalid':vualidate.$invalid && props.submitted}"
        v-on:change="processValueChange"
        v-on:keyup="processValueChange"
        class="p-inputtext-sm" v-model="mv"/>
    <small v-if="vualidate.$invalid && props.submitted"
           class="p-error">{{ vualidate.required.$message.replace('Value', props.label) }}</small>
  </div>

</template>

<script lang="ts" setup>

import {defineEmits, defineProps, ref, watch, withDefaults} from 'vue'
import InputText from 'primevue/inputtext'

// Define types
type InputFieldProps = {
  id: string
  label: string
  modelValue: any
  submitted: boolean,
  vualidate: any
}

// Define props
const props = withDefaults(defineProps<InputFieldProps>(), {
  submitted: false
})

// Define emits
const emits = defineEmits(['update:modelValue'])

// Define ref/reactive properties
const mv = ref<string | null>(null)

// Define functions
function processValueChange(event: Event) {
  emits('update:modelValue', mv.value)
}

// Define hooks
watch(() => props.modelValue, (current, prev) => {
  mv.value = current
})


</script>

<style scoped>

</style>
