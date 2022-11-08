<template>

  <div :class="props.class">
    <label :for="id">{{ props.label }} <span v-if="vualidate.required">*</span></label>
    <InputNumber
        mode="decimal"
        :suffix="props.suffix"
        :minFractionDigits="2"
        :id="id"
        :class="{'p-invalid':vualidate.$invalid && props.submitted}"
        v-on:input="processValueChange"
        v-model="mv"/>
    <small v-if="vualidate.$invalid && props.submitted"
           class="p-error">{{ vualidate.required.$message.replace('Value', props.label) }}</small>
  </div>

</template>

<script lang="ts" setup>

import {defineEmits, defineProps, onMounted, ref, watch, withDefaults} from 'vue'
import InputNumber, {InputNumberInputEvent} from 'primevue/inputnumber'

// Define types
type InputFieldProps = {
  id: string
  label: string
  modelValue: any
  submitted: boolean,
  vualidate: any,
  class?: string,
  suffix?: string
}

// Define props
const props = withDefaults(defineProps<InputFieldProps>(), {
  submitted: false,
  class: ''

})

// Define lifecycle hooks
onMounted(() => {
  mv.value = props.modelValue
})

// Define emits
const emits = defineEmits(['update:modelValue'])

// Define ref/reactive properties
const mv = ref<number | null>(null)

// Define functions
function processValueChange(event: InputNumberInputEvent) {
  emits('update:modelValue', event.value)
}

// Define hooks
watch(() => props.modelValue, (current, prev) => {
  mv.value = current
})


</script>

<style scoped>

</style>
