<template>

  <div class="field" :class="props.class">
    <label :for="props.id">{{ props.label }} <span v-if="vualidate.required">*</span></label>
    <Dropdown :id="props.id"
              :class="{'p-invalid':vualidate.$invalid && submitted}"
              v-model="mv"
              @change="processChange"
              :options="companies"
              optionLabel="name" :filter="true"
              :placeholder="'Select a ' + props.label" :showClear="true">
      <template #value="slotProps">
        <div v-if="slotProps.value">
          <div>{{ slotProps.value.name }}</div>
        </div>
        <span v-else>
                    {{ slotProps.placeholder }}
                </span>
      </template>
      <template #option="slotProps">
        <div>
          <div>{{ slotProps.option.name }}</div>
        </div>
      </template>
    </Dropdown>
    <small v-if="vualidate.$invalid && submitted"
           class="p-error">{{ vualidate.required.$message.replace('Value', props.label) }}</small>
  </div>
</template>

<script lang="ts" setup>

import {defineEmits, defineProps, inject, onMounted, ref, watch, withDefaults} from 'vue'
import Dropdown, {DropdownChangeEvent} from 'primevue/dropdown'
import {AxiosStatic} from "axios";

// Define inject
const axios = inject<AxiosStatic>('axios')

// Define types
type InputFieldProps = {
  id: string
  label: string
  modelValue: any
  submitted: boolean,
  vualidate: any,
  class: string
}

// Define props
const props = withDefaults(defineProps<InputFieldProps>(), {
  submitted: false
})

// Define emits
const emits = defineEmits(['update:modelValue'])

// Define ref/reactive properties
const mv = ref<any | null>(null)
const companies = ref([])

// Define functions

function processChange(event: DropdownChangeEvent) {
  emits('update:modelValue', mv.value)
}

function loadCompanies() {
  axios?.get("/project/all").then((response) => {
    if (response.status >= 200 && response.status <= 299) {
      companies.value = response.data
    } else {
      console.error('Error loading projects', response)
    }
  })
}

// Define hooks
watch(() => props.modelValue, (current, prev) => {
  console.log('ModelValue for ProjectField changed to ', current)
  mv.value = current
})

onMounted(() => {
  loadCompanies()
  mv.value = props.modelValue
})


</script>

<style scoped>

</style>
