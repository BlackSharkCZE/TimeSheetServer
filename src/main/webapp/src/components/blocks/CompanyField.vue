<template>

  <div class="field" :class="props.class">
    <label :for="props.id">{{props.label}} <span v-if="vualidate.required">*</span></label>
    <Dropdown :id="props.id"
              :class="{'p-invalid':vualidate.$invalid && submitted}"
              v-model="mv"
              :options="companies"
              @change="processChange"
              optionLabel="name" :filter="true"
              placeholder="Select a company" :showClear="true">
      <template #value="slotProps">
        <div v-if="slotProps.value">
          <div>{{ slotProps.value.companyName }}</div>
        </div>
        <span v-else>
                    {{ slotProps.placeholder }}
                </span>
      </template>
      <template #option="slotProps">
        <div>
          <div>{{ slotProps.option.companyName }} ({{ slotProps.option.email }})</div>
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
import InputText from 'primevue/inputtext'
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
  submitted: false,
  class: ''
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
  axios?.get("/company/all?primary=true").then((response) => {
    if (response.status >= 200 && response.status <= 299) {
      companies.value = response.data
    } else {
      // message.error('Ulozeni spolecnosti se nezdarilo!')
      console.error(response)
    }
  })
}

// Define hooks
watch(() => props.modelValue, (current, prev) => {
  mv.value = current
})

onMounted(()=>{
  loadCompanies()
})






</script>

<style scoped>

</style>
