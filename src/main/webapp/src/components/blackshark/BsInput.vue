<template>
  <div class="bs-input" :class="{'with-button': props.buttonCallback !== undefined, 'error': statusClass==='error'}">
    <label :for="id">{{ label }}</label>

    <div class="layout">
      <input
          :ref="inputRef"
          :id='id'
          autocomplete="off"
          :type="type"
          :value="value"
          :placeholder="label"
          :required="required"
          @blur="validateItem(value)"
          @focus="focused=true;validateItem(value)"
          @input="updateValue($event.target.value)"/>
      <div class="append-button" v-if="buttonCallback">
        <button @click="processButtonClick">
          <slot>OK</slot>
        </button>
      </div>
    </div>
    <ul v-if="errors.length>0">
      <li v-for="(item, index) in errors" :key="index">{{ item }}</li>
    </ul>

  </div>

</template>

<script lang="ts" setup>

import {defineEmits, defineExpose, defineProps, onMounted, ref, withDefaults} from 'vue'

type ButtonCallBackFunction = (value: any) => void

interface BsInputProps {
  focusOnUpdate?: boolean,
  id: string,
  label: string,
  value: string | number | null,
  required?: boolean,
  type?: string,
  buttonCallback?: ButtonCallBackFunction
}

// Define DOM references
const inputRef = ref(null)

// Define component properties
const props = withDefaults(defineProps<BsInputProps>(), {
  required: true,
  type: 'text',
  buttonCallback: undefined
})

// Define emits
const emits = defineEmits(['update:value', 'validationResult'])

// Define reactive properties
const focused = ref<boolean>(false)
const statusClass = ref<string>('')
const errors = ref<string[]>([])

// Define lifecycles hooks
onMounted(() => {
  const a=1
})

// Define methods
function processButtonClick() {
  if (props.buttonCallback) {
    props.buttonCallback(props.value)
  }
}

function validateItem(item: string | number | null) {
  errors.value = []
  if (props.required === true && (item === null || item === '' || item === undefined)) {
    errors.value.push(`Value for field ${props.id} is required.`)
  }

  switch (props.type.toUpperCase()) {
    case 'NUMBER':
      if (props.value !== null) {
        if (isNaN(props.value as number)) {
          errors.value.push(`Value for field ${props.id} is not valid number.`)
        }
      }
      break;
  }

  if (errors.value.length > 0) {
    statusClass.value = 'error'
  } else {
    statusClass.value = ''
  }

  emits('validationResult', {errors: errors})

}

function updateValue(item: any) {
  emits('update:value', item)
  validateItem(item)
}

// Define methods used by parent component
const startValidateProcess = ():Array<string> => {
  validateItem(props.value)

  return errors.value
}

defineExpose({ startValidateProcess })

</script>

<style scoped lang="scss">
@import "./styles/components";

</style>
